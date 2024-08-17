package com.example.sensortester;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class InitialActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    MediaPlayer mediaPlayer,intro_song;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        surfaceView=findViewById(R.id.surface_anim);
        surfaceView.setKeepScreenOn(true);
        intro_song=MediaPlayer.create(this,R.raw.intro_sound_2);
        intro_song.start();
        mediaPlayer=MediaPlayer.create(this,R.raw.blender_intro_animation);
        SurfaceHolder surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                mediaPlayer.setDisplay(surfaceHolder);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });
        mediaPlayer.start();
        SharedPreferences user_authentication_flag=getSharedPreferences("authentication",MODE_PRIVATE);
        boolean flag_value=user_authentication_flag.getBoolean("authentication_flag",false);
        Intent intent;
        if (flag_value){
            intent=new Intent(this, HomePage.class);
            intent.putExtra("Username","from_initial_activity");
        }
        else
            intent=new Intent(this, MainActivity.class);
        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        },3000);
    }

    @Override
    protected void onPause() {
        if (intro_song.isPlaying())
            intro_song.pause();
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        intro_song.start();
        super.onPostResume();
    }
}