package com.example.roguelikesurvival;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    AnimationDrawable selectAnimation;

    private enum Jobs {
        KNIGHT,
        WIZZARD
    }

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView selectImage = (ImageView) findViewById(R.id.select_image);
        selectImage.setBackgroundResource(R.drawable.select_background);
        selectAnimation = (AnimationDrawable) selectImage.getBackground();

        selectAnimation.start();
    }

    public void onClickKnight(View target) {
        game = new Game(this, 0);
        setContentView(game);
    }

    public void onClickWizzard(View target) {
        game = new Game(this, 1);
        setContentView(game);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        game.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}