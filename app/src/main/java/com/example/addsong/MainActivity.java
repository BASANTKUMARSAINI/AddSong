package com.example.addsong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String phone_id="abc";
    List<Song>songs=new ArrayList<>();
    FirebaseFirestore db;
    SongAdapter adapter;
    TextView tvMusic;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }
        else{
            phone_id=getPhoneId();
        }
        dialog=new ProgressDialog(this);
        dialog.setProgress(0);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Fetching...");
        dialog.show();

        tvMusic=findViewById(R.id.tv_music);
        db=FirebaseFirestore.getInstance();
        adapter=new SongAdapter(this,songs);
        recyclerView.setAdapter(adapter);
        getSongsList();





    }

    private void getSongsList() {
        db.collection("songs").document(phone_id).collection("songs").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots!=null&&(!queryDocumentSnapshots.isEmpty()))
                {
                    songs=new ArrayList<>();
                    for(DocumentSnapshot snapshot:queryDocumentSnapshots)
                    {
                        if(snapshot!=null)
                        {
                            songs.add(snapshot.toObject(Song.class));
                        }
                    }
                    tvMusic.setVisibility(View.GONE);
                    adapter=new SongAdapter(MainActivity.this,songs);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    private String getPhoneId() {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if(telephonyManager!=null)
           return telephonyManager.getDeviceId();
        return "abc";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                phone_id=getPhoneId();
                getSongsList();

            }
        }

    }

    public void  addNewSong(View view)
    {
            startActivity(new Intent(MainActivity.this,AddNewSongActivity.class));
    }
}