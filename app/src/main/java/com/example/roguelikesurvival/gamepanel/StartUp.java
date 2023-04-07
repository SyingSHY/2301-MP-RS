package com.example.roguelikesurvival.gamepanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roguelikesurvival.MainActivity;
import com.example.roguelikesurvival.R;

public class StartUp extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
    }

    public void startGame(View view){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
