package com.example.roguelikesurvival.gamepanel;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roguelikesurvival.MainActivity;
import com.example.roguelikesurvival.R;

public class StartUp extends AppCompatActivity {
    AnimationDrawable titleAnimation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);

        ImageView titleImage = (ImageView) findViewById(R.id.title_image);
        titleImage.setBackgroundResource(R.drawable.title_background);
        titleAnimation = (AnimationDrawable) titleImage.getBackground();

        titleAnimation.start();

    }

    public void startGame(View view){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void statisticsGame(View view) {
        startActivity(new Intent(this, GameStatistics.class));
        finish();
    }

    public void exitApp(View view){
        // 종료 버튼
        System.exit(0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
