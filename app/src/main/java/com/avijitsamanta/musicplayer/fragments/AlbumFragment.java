package com.avijitsamanta.musicplayer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avijitsamanta.musicplayer.R;
import com.avijitsamanta.musicplayer.adopter.AlbumAdopter;
import com.avijitsamanta.musicplayer.adopter.MusicAdopter;

import static com.avijitsamanta.musicplayer.MainActivity.albumLists;
import static com.avijitsamanta.musicplayer.MainActivity.musicFilesList;


public class AlbumFragment extends Fragment {
    private RecyclerView recyclerView;
    private AlbumAdopter adopter;


    public AlbumFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAlbum);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        if (albumLists != null) {
            adopter = new AlbumAdopter(getActivity(), albumLists);
            recyclerView.setAdapter(adopter);
        }
        return view;
    }

}