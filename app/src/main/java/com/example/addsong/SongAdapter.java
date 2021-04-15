package com.example.addsong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    Context context;
    List<Song>songs;
    public SongAdapter(Context context, List<Song>songs) {
        this.context=context;
        this.songs=songs;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.song_layout,parent,false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.tvTitle.setText(songs.get(position).getTitle());
        holder.tvDes.setText(songs.get(position).getDes());


    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class  SongViewHolder extends RecyclerView.ViewHolder{
        public TextView tvTitle,tvDes;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDes=itemView.findViewById(R.id.tv_des);
            tvTitle=itemView.findViewById(R.id.tv_title);
        }
    }
}
