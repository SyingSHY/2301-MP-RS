package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roguelikesurvival.MainActivity;
import com.example.roguelikesurvival.R;

import org.w3c.dom.Text;

import java.io.FileOutputStream;

public class ReStart extends AppCompatActivity{
    Context context;
    private int playTime_minute_round = 0;
    private int playTime_second_round = 0;
    private int killCount_round = 0;
    private int playerLevel = 0;
    private int playTime_minute_total = 0;
    private int playTime_second_total = 0;
    private int killCount_total = 0;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView((R.layout.restart));

        context = getApplicationContext();

        Intent intent = getIntent();
        playTime_minute_round = intent.getIntExtra("playtime_minute", 0);
        playTime_second_round = intent.getIntExtra("playtime_second", 0);
        killCount_round = intent.getIntExtra("play_killCount", 0);
        playerLevel = intent.getIntExtra("play_levelCount", 0);

        TextView aliveTimeView = (TextView) findViewById(R.id.textSetTime);
        TextView levelView = (TextView) findViewById(R.id.textSetLevel);
        TextView killCountView = (TextView) findViewById(R.id.textSetKill);

        aliveTimeView.setText(playTime_minute_round + "분 " + playTime_second_round + "초");
        levelView.setText("Level " + playerLevel);
        killCountView.setText(killCount_round + " Kills");

        overridePendingTransition(R.anim.fade_in, R.anim.no_anim);
    }

    public void restartGame(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}
