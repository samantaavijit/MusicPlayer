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

import static com.avijitsamanta.musicplayer.adopter.MusicAdopter.POSITION;

public class AlbumDetailsAdopter extends RecyclerView.Adapter<AlbumDetailsAdopter.AlbumHolder> {
    private Context context;
    public static List<MusicFiles> list;
    public static final String SENDER = "sender";
    public static final String ALBUM_DETAILS = "albumDetails";

    public AlbumDetailsAdopter(Context context, List<MusicFiles> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_items, parent, false);
        return new AlbumHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
        MusicFiles audio = list.get(position);
        holder.albumName.setText(audio.getTitle());
        byte[] image = getAlbumArt(audio.getPath());
        if (image != null)
            Glide.with(context)
                    .asBitmap()
                    .load(image)
                    .into(holder.albumImage);
        else
            Glide.with(context)
                    .asBitmap()
                    .load(R.drawable.song)
                    .into(holder.albumImage);

        holder.itemView.setOnClickListener(view -> {
            Intent intent=new Intent(context, PlayerActivity.class);
            intent.putExtra(SENDER,ALBUM_DETAILS);
            intent.putExtra(POSITION,position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class AlbumHolder extends RecyclerView.ViewHolder {
        private ImageView albumImage;
        private TextView albumName;

        public AlbumHolder(@NonNull View itemView) {
            super(itemView);

            albumImage = itemView.findViewById(R.id.musicImg);
            albumName = itemView.findViewById(R.id.musicFileName);
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
