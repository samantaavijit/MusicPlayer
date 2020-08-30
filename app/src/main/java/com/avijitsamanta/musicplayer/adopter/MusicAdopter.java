package com.avijitsamanta.musicplayer.adopter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.avijitsamanta.musicplayer.PlayerActivity;
import com.avijitsamanta.musicplayer.R;
import com.avijitsamanta.musicplayer.modal.MusicFiles;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.List;

public class MusicAdopter extends RecyclerView.Adapter<MusicAdopter.SongViewHolder> {

    private List<MusicFiles> musicFiles;
    private Context context;
    public static final String POSITION = "position";

    public MusicAdopter(List<MusicFiles> musicFiles, Context context) {
        this.musicFiles = musicFiles;
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
        MusicFiles audio = musicFiles.get(position);
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
            intent.putExtra(POSITION, position);
            context.startActivity(intent);
        });

        holder.menuMore.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, view);
            popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.delete:
                        Toast.makeText(context, "Delete clicked!", Toast.LENGTH_SHORT).show();
                        deleteFile(position, view);
                        break;
                }
                return false;
            });
        });
    }

    private void deleteFile(int position, View view) {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(musicFiles.get(position).getId()));
        File file = new File(musicFiles.get(position).getPath());
        boolean delete = file.delete();
        if (delete) {
            context.getContentResolver().delete(contentUri, null, null);
            musicFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, musicFiles.size());

            Snackbar.make(view, "File Deleted ", Snackbar.LENGTH_LONG)
                    .show();
        } else {
            Snackbar.make(view, "File can't be Deleted ", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public int getItemCount() {
        return musicFiles.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        private TextView fileName;
        private ImageView albumArt, menuMore;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            fileName = itemView.findViewById(R.id.musicFileName);
            albumArt = itemView.findViewById(R.id.musicImg);
            menuMore = itemView.findViewById(R.id.menu_more);
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
