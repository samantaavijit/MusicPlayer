package com.avijitsamanta.musicplayer.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avijitsamanta.musicplayer.R;
import com.avijitsamanta.musicplayer.adopter.MusicAdopter;

import static com.avijitsamanta.musicplayer.MainActivity.musicFilesList;

public class SongsFragment extends Fragment {
    private RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    public static MusicAdopter musicAdopter;

    public SongsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewSongs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (musicFilesList.size() > 0) {
            musicAdopter = new MusicAdopter(musicFilesList, getActivity());
            recyclerView.setAdapter(musicAdopter);
        }
    }
}