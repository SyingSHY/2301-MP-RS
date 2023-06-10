package com.example.roguelikesurvival.gamepanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.roguelikesurvival.MainActivity;
import com.example.roguelikesurvival.R;

public class ReStart extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restart);
        overridePendingTransition(R.anim.fade_in, R.anim.no_anim);

        ConstraintLayout layout = findViewById(R.id.main_layout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    restartGame();
                }
                return true;
            }
        });
    }

    public void restartGame() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
