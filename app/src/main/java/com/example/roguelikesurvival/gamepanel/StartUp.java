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
}
