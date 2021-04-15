package com.example.addsong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class AddNewSongActivity extends AppCompatActivity {
    EditText etTitle,etDes;
    String phone_id="abc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_song);
        etDes=findViewById(R.id.et_des);
        etTitle=findViewById(R.id.et_title);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }
        else{
            phone_id=getPhoneId();
        }

    }
    public void submitSong(View view)
    {
        String title=etTitle.getText().toString().trim();
        String des=etDes.getText().toString().trim();
        if(TextUtils.isEmpty(title))
        {
            Toast.makeText(this,"Please Enter Title",Toast.LENGTH_LONG).show();
        }
        else  if(TextUtils.isEmpty(des))
        {
            Toast.makeText(this,"Please Enter Title",Toast.LENGTH_LONG).show();
        }
        else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
            }
            else{
                Song song=new Song(title,des);
                ProgressDialog dialog=new ProgressDialog(this);
                dialog.setProgress(0);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setTitle("Uploading...");
                dialog.show();
                FirebaseFirestore.getInstance().collection("songs").document(phone_id).collection("songs").add(song).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        dialog.dismiss();
                         startActivity(new Intent(AddNewSongActivity.this,MainActivity.class));
                         finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(AddNewSongActivity.this,"Try Again",Toast.LENGTH_LONG).show();
                    }
                });
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                phone_id=getPhoneId();

            }
            else{
                Toast.makeText(this,"For uploading you have to give permission",Toast.LENGTH_LONG).show();
            }
        }

    }
    private String getPhoneId() {
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if(telephonyManager!=null)
            return telephonyManager.getDeviceId();
        return "abc";
    }
}