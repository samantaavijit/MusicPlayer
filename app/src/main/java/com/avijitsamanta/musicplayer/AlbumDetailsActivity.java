package com.avijitsamanta.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.avijitsamanta.musicplayer.adopter.AlbumDetailsAdopter;
import com.avijitsamanta.musicplayer.modal.MusicFiles;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.avijitsamanta.musicplayer.MainActivity.musicFilesList;
import static com.avijitsamanta.musicplayer.adopter.AlbumAdopter.ALBUM_NAME;

public class AlbumDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView albumPhoto;
    private String albumName;
    private List<MusicFiles> albumSongs = new ArrayList<>();
    private AlbumDetailsAdopter adopter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);


        recyclerView = findViewById(R.id.recyclerView);
        albumPhoto = findViewById(R.id.albumPhoto);
        getIntentMethod();

        int j = 0;
        for (int i = 0; i < musicFilesList.size(); i++) {
            if (albumName.equalsIgnoreCase(musicFilesList.get(i).getAlbum())) {
                albumSongs.add(j, musicFilesList.get(i));
                j++;
            }
        }
        byte[] image = getAlbumArt(albumSongs.get(0).getPath());
        if (image != null) {
            Glide.with(this)
                    .asBitmap()
                    .load(image)
                    .into(albumPhoto);
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(R.drawable.song)
                    .into(albumPhoto);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (albumSongs.size() > 0) {
            adopter = new AlbumDetailsAdopter(this, albumSongs);
            recyclerView.setAdapter(adopter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void getIntentMethod() {
        Intent im = getIntent();
        if (im != null) {
            albumName = im.getStringExtra(ALBUM_NAME);
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