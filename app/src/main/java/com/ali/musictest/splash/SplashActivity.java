package com.ali.musictest.splash;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.musictest.R;

public class SplashActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        videoView = findViewById(R.id.videoView);
        playVideo();

    }
    private void playVideo() {
        Uri videoPath = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.calm);
        videoView.setVideoURI(videoPath);
        videoView.setOnCompletionListener(mp -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        });
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoView.stopPlayback();
    }
}