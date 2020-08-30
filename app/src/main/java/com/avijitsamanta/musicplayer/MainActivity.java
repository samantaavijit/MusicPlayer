package com.avijitsamanta.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.avijitsamanta.musicplayer.fragments.AlbumFragment;
import com.avijitsamanta.musicplayer.fragments.SongsFragment;
import com.avijitsamanta.musicplayer.modal.MusicFiles;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 101;
    public static List<MusicFiles> musicFilesList;
    public static boolean shuffleBoolean = false, repeatBoolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();

    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            musicFilesList = getAllAudio(this);
            initViewPager();
        }
    }

    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPagerAdopter adopter = new ViewPagerAdopter(getSupportFragmentManager());
        adopter.addFragments(new SongsFragment(), "Songs");
        adopter.addFragments(new AlbumFragment(), "Albums");
        viewPager.setAdapter(adopter);
        tabLayout.setupWithViewPager(viewPager);
    }

    static class ViewPagerAdopter extends FragmentPagerAdapter {
        private List<String> titleList;
        private List<Fragment> fragmentList;

        public ViewPagerAdopter(@NonNull FragmentManager fm) {
            super(fm);
            this.titleList = new ArrayList<>();
            this.fragmentList = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragments(Fragment fragment, String title) {
            titleList.add(title);
            fragmentList.add(fragment);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

    public static List<MusicFiles> getAllAudio(Context context) {
        List<MusicFiles> audioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID
        };

        try {
            @SuppressLint("Recycle") Cursor cursor = context.getContentResolver().query(uri, projection,
                    null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String album = cursor.getString(0);
                    String title = cursor.getString(1);
                    String duration = cursor.getString(2);
                    String path = cursor.getString(3);
                    String artist = cursor.getString(4);
                    String id = cursor.getString(5);

                    MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration, id);
                    audioList.add(musicFiles);

                    Log.d("TAG", "getAllAudio: " + path);
                }
                cursor.close();
            }


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return audioList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Some Permission denied", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
                Toast.makeText(this, "All permission granted", Toast.LENGTH_SHORT).show();
                musicFilesList = getAllAudio(this);
                initViewPager();
            }
        }
    }
}