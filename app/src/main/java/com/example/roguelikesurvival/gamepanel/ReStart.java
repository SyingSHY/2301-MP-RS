package com.example.roguelikesurvival.gamepanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roguelikesurvival.MainActivity;
import com.example.roguelikesurvival.R;

public class ReStart extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView((R.layout.restart));

        overridePendingTransition(R.anim.fade_in, R.anim.no_anim);
    }

    public void restartGame(View view){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        finish();
    }
}
