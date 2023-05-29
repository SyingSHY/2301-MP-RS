package com.example.roguelikesurvival;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.roguelikesurvival.gamepanel.ExpBar;
import com.example.roguelikesurvival.gamepanel.InfiniteBackground;
import com.example.roguelikesurvival.gamepanel.Joystick;
import com.example.roguelikesurvival.gamepanel.Performance;
import com.example.roguelikesurvival.gamepanel.ReStart;
import com.example.roguelikesurvival.gamepanel.GameTimer;
import com.example.roguelikesurvival.gamepanel.SkillButton;
import com.example.roguelikesurvival.object.Enemy;
import com.example.roguelikesurvival.object.Knight;
import com.example.roguelikesurvival.object.Player;
import com.example.roguelikesurvival.object.Spell;
import com.example.roguelikesurvival.object.Wizzard;
import com.example.roguelikesurvival.object.item.BasicAttack;

import java.util.ArrayList;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private final Player player;
    private final Joystick joystick;

    private final SkillButton skillButton;
    public static List<Enemy> enemyList = new ArrayList<Enemy>();
    public List<Spell> spellList = new ArrayList<Spell>();
    private int joystckPointerId = 0;

    private int skillButtonPointerId = -1;
    public int numberOfSpellsToCast = 0;
    private Performance performance;
    private Camera camera;
    private InfiniteBackground background;
    private EnemySpawn enemySpawn;
    private SpellSpawn spellSpawn;
    private GameTimer gameTimer;
    private ExpBar expBar;
    private BasicAttack basicAttack;

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    public void onSkillButtonPressed() {
        // Add logic for handling skill button press here
    }

    public Game(Context context, int jobs) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        //시간 설정
        gameTimer = new GameTimer(context);

        //게임 패널 초기화
        performance = new Performance(context, gameLoop);
        joystick = new Joystick(170, 800, 100, 60);
        skillButton = new SkillButton(1500, 800, 50, this);

        //오브젝트 초기설정
        if (jobs == 0)
            player = new Knight(getContext(), joystick, 500, 500, 30);
        else
            player = new Wizzard(getContext(), joystick, 500, 500, 30, enemyList);

        //기본공격 설정
        basicAttack = new BasicAttack(context, player, jobs, 20);

        //배경 설정
        background = new InfiniteBackground(context, player);

        //경험치바 설정
        expBar = new ExpBar(context, player);

        //스포너 설정
        enemySpawn = new EnemySpawn(this, player, camera, gameTimer);
        spellSpawn = new SpellSpawn(this, player, camera, gameTimer, jobs, basicAttack);

        //카메라 시점을 플레이어가 중앙에 오게 설정
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics((displayMetrics));
        camera = new Camera(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.getIsPressed()) {
                    numberOfSpellsToCast++;
                } else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    joystckPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else if (skillButton.isPressed((double) event.getX(), (double) event.getY())) {
                    skillButtonPointerId = event.getPointerId(event.getActionIndex());
                    skillButton.setIsPressed(true);
                    player.useSkill();
                } else {
                    numberOfSpellsToCast++;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()) {
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystckPointerId == event.getPointerId(event.getActionIndex())) {
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                } else if (skillButtonPointerId == event.getPointerId(event.getActionIndex())) {
                    skillButton.setIsPressed(false);
                    onSkillButtonPressed();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //배경 그리기
        background.draw(canvas, camera);

        //오브젝트 그리기
        player.draw(canvas, camera);

        for (Enemy enemy : enemyList) {
            enemy.draw(canvas, camera);
        }

        for (Spell spell : spellList) {
            spell.draw(canvas, camera);
        }

        // 게임패널 그리기
        joystick.draw(canvas);

        // 스킬버튼 그리기
        skillButton.draw(canvas);

        //시간 출력
        gameTimer.draw(canvas);

        //경험치바 그리기
        expBar.draw(canvas);

        //기본공격
        if(basicAttack.getAnimationState() == true)
            basicAttack.draw(canvas,camera);

    }

    public void update() {

        joystick.update();
        player.update();

        enemySpawn.update(camera, expBar);
        spellSpawn.update(camera, expBar);

        camera.update();

        if (player.getHealthPoint() <= 0) {
            checkHP();
        }

        background.update(camera);

        expBar.update();
    }

    public void checkHP() {
        if (player.getHealthPoint() <= 0) {
            enemyList.clear(); // enemy 초기화 하도록 변경
            Intent intent = new Intent(getContext(), ReStart.class);
            getContext().startActivity(intent);
            ((Activity) getContext()).finish();
        }
    }

    public void pause() {
        gameLoop.stopLoop();
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }
}

