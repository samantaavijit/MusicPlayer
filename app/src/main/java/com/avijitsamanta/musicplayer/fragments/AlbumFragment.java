package com.avijitsamanta.musicplayer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avijitsamanta.musicplayer.R;
import com.avijitsamanta.musicplayer.adopter.AlbumAdopter;


import static com.avijitsamanta.musicplayer.MainActivity.albumLists;


public class AlbumFragment extends Fragment {
    public AlbumFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAlbum);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        if (albumLists != null) {
            AlbumAdopter adopter = new AlbumAdopter(getActivity(), albumLists);
            recyclerView.setAdapter(adopter);
        }
        return view;
    }

}