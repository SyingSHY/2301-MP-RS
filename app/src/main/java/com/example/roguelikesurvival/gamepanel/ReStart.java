package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private int playTime_minute_best = 0;
    private int playTime_second_best = 0;
    private int killCount_best = 0;
    private int playerLevel_best = 0;
    private int playTime_minute_total = 0;
    private int playTime_second_total = 0;
    private int killCount_total = 0;
    private int playerLevel_total = 0;
    private SharedPreferences sharedPref;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView((R.layout.restart));

        context = getApplicationContext();

        // SharedPreferences 기록 읽어 들이기
        sharedPref = getSharedPreferences("UserRecord", MODE_PRIVATE);
        playTime_minute_best = sharedPref.getInt("bestMinute", 0);
        playTime_second_best = sharedPref.getInt("bestSecond", 0);
        killCount_best = sharedPref.getInt("bestKillCount", 0);
        playerLevel_best = sharedPref.getInt("bestLevel", 0);

        Intent intent = getIntent();
        playTime_minute_round = intent.getIntExtra("playtime_minute", 0);
        playTime_second_round = intent.getIntExtra("playtime_second", 0);
        killCount_round = intent.getIntExtra("play_killCount", 0);
        playerLevel = intent.getIntExtra("play_levelCount", 0);

        TextView aliveTimeRecord = (TextView) findViewById(R.id.textNewrecord1);
        TextView levelRecord = (TextView) findViewById(R.id.textNewrecord2);
        TextView killCountRecord = (TextView) findViewById(R.id.textNewrecord3);

        // SharedPreferences editor 오픈, 기록 갱신 시작
        SharedPreferences.Editor editor = sharedPref.edit();
        // 기존 기록과 현재 기록을 비교, 신기록 여부 확인
        if ((playTime_minute_best * 60 + playTime_second_best < playTime_minute_round * 60 + playTime_second_round)) {
            aliveTimeRecord.setVisibility(View.VISIBLE);
            editor.putInt("bestMinute", playTime_minute_round);
            editor.putInt("bestSecond", playTime_second_round);
        }
        if (playerLevel_best < playerLevel) {
            levelRecord.setVisibility(View.VISIBLE);
            editor.putInt("bestLevel", playerLevel);
        }
        if (killCount_round > killCount_best) {
            killCountRecord.setVisibility(View.VISIBLE);
            editor.putInt("bestKillCount", killCount_round);
        }
        // 전체 통계 갱신
        int total_playedGame = sharedPref.getInt("playedGame", 0);
        int total_playedLose = sharedPref.getInt("playedLose", 0);

        playTime_minute_total = sharedPref.getInt("totalMinute", 0);
        playTime_second_total = sharedPref.getInt("totalSecond", 0);
        killCount_total = sharedPref.getInt("totalKillCount", 0);
        playerLevel_total = sharedPref.getInt("totalLevel", 0);

        int total_time = playTime_minute_total * 60 + playTime_second_total + playTime_minute_round * 60 + playTime_second_round;
        editor.putInt("totalMinute", total_time / 60);
        editor.putInt("totalSecond", total_time % 60);
        editor.putInt("totalLevel", playerLevel_total + playerLevel);
        editor.putInt("totalKillCount", killCount_total + killCount_round);

        editor.putInt("playedGame", total_playedGame + 1);
        editor.putInt("playedLose", total_playedLose + 1);

        // 전체 기록 작성 및 확인 완료, 갱신 실행
        editor.commit();

        TextView aliveTimeView = (TextView) findViewById(R.id.textSetTime);
        TextView levelView = (TextView) findViewById(R.id.textSetLevel);
        TextView killCountView = (TextView) findViewById(R.id.textSetKill);

        aliveTimeView.setText(playTime_minute_round + "분 " + playTime_second_round + "초");
        levelView.setText("Level " + playerLevel);
        killCountView.setText(killCount_round + " Kills");

        overridePendingTransition(R.anim.fade_in, R.anim.no_anim);
    }

    public void restartGame(View view){

        Intent intent = new Intent(this, StartUp.class);
        startActivity(intent);

        finish();
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
