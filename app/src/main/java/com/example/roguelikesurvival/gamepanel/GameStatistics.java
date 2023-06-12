package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.MainActivity;
import com.example.roguelikesurvival.R;

public class GameStatistics extends AppCompatActivity {

    private Context context;

//    AnimationDrawable titleAnimation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamestatistics);

        ImageView titleImage = (ImageView) findViewById(R.id.title_image);
        titleImage.setBackgroundResource(R.drawable.statistics_background);
//        titleAnimation = (AnimationDrawable) titleImage.getBackground();
//
//        titleAnimation.start();

        // SharedPreferences 기록 읽어 들이기
        SharedPreferences sharedPref = getSharedPreferences("UserRecord", MODE_PRIVATE);
        int total_playedGame = sharedPref.getInt("playedGame", 0);
        int total_playedWin = sharedPref.getInt("playedWin", 0);
        int total_playedLose = sharedPref.getInt("playedLose", 0);
        int playTime_minute_best = sharedPref.getInt("bestMinute", 0);
        int playTime_second_best = sharedPref.getInt("bestSecond", 0);
        int killCount_best = sharedPref.getInt("bestKillCount", 0);
        int playerLevel_best = sharedPref.getInt("bestLevel", 0);
        int playTime_minute_total = sharedPref.getInt("totalMinute", 0);
        int playTime_second_total = sharedPref.getInt("totalSecond", 0);
        int killCount_total = sharedPref.getInt("totalKillCount", 0);
        int playerLevel_total = sharedPref.getInt("totalLevel", 0);

        TextView bestTime = (TextView) findViewById(R.id.textStatBestTimeValue);
        TextView bestLevel = (TextView) findViewById(R.id.textStatBestLevelValue);
        TextView bestKills = (TextView) findViewById(R.id.textStatBestKillValue);
        TextView totalTime = (TextView) findViewById(R.id.textStatTotalTimeValue);
        TextView totalLevel = (TextView) findViewById(R.id.textStatTotalLevelValue);
        TextView totalKills = (TextView) findViewById(R.id.textStatTotalKillValue);
        TextView playGame = (TextView) findViewById(R.id.textStatTotalPlayValue);
        TextView playWin = (TextView) findViewById(R.id.textStatTotalGameValue);
        TextView playLose = (TextView) findViewById(R.id.textStatTotalLoseValue);

        bestTime.setText(playTime_minute_best + "분 " + playTime_second_best + "초");
        bestLevel.setText("Level " + playerLevel_best);
        bestKills.setText(killCount_best + " Kills");
        totalTime.setText(playTime_minute_total + "분 " + playTime_second_total + "초");
        totalLevel.setText("Level " + playerLevel_total);
        totalKills.setText(killCount_total + " Kills");
        playGame.setText(total_playedGame + " Games");
        playWin.setText(total_playedWin + " Games");
        playLose.setText(total_playedLose + " Games");
    }

    public void onClickReturn(View v) {
        Intent intent = new Intent(this, StartUp.class);
        startActivity(intent);

        finish();
    }
}
