package com.avijitsamanta.musicplayer.adopter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avijitsamanta.musicplayer.PlayerActivity;
import com.avijitsamanta.musicplayer.R;
import com.avijitsamanta.musicplayer.modal.MusicFiles;
import com.bumptech.glide.Glide;

import java.util.List;

public class MusicAdopter extends RecyclerView.Adapter<MusicAdopter.SongViewHolder> {

    private List<MusicFiles> musicFilesList;
    private Context context;
    public static final String POSITION = "position";

    public MusicAdopter(List<MusicFiles> musicFilesList, Context context) {
        this.musicFilesList = musicFilesList;
        this.context = context;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_items, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        MusicFiles audio = musicFilesList.get(position);
        holder.fileName.setText(audio.getTitle());
        byte[] image = getAlbumArt(audio.getPath());
        if (image != null)
            Glide.with(context)
                    .asBitmap()
                    .load(image)
                    .into(holder.albumArt);
        else
            Glide.with(context)
                    .asBitmap()
                    .load(R.drawable.song)
                    .into(holder.albumArt);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra(POSITION,position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return musicFilesList.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        private TextView fileName;
        private ImageView albumArt;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.musicFileName);
            albumArt = itemView.findViewById(R.id.musicImg);
        }
    }

    private byte[] getAlbumArt(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
