package com.example.roguelikesurvival;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.roguelikesurvival.gamepanel.ExpBar;
import com.example.roguelikesurvival.gamepanel.GamePauseButton;
import com.example.roguelikesurvival.gamepanel.InfiniteBackground;
import com.example.roguelikesurvival.gamepanel.Joystick;
import com.example.roguelikesurvival.gamepanel.Performance;
import com.example.roguelikesurvival.gamepanel.PlayerState;
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
    private final GamePauseButton gamePauseButton;
    public static List<Enemy> enemyList = new ArrayList<Enemy>();
    public List<Spell> spellList = new ArrayList<Spell>();
    private int joystckPointerId = 0;
    private int skillButtonPointerId = -1;
    private int gamePauseButtonPointerId = -2;
    public int numberOfSpellsToCast = 0;
    private Performance performance;
    private Camera camera;
    private InfiniteBackground background;
    private EnemySpawn enemySpawn;
    private SpellSpawn spellSpawn;
    private GameTimer gameTimer;
    private ExpBar expBar;
    private SelectItem selectItem;
    private PauseMenu pauseMenu;
    private PlayerState playerState;

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

    public void onGamePauseButtonPressed() {
        pauseMenu.gamePauseMenu();
        pauseMenu.setGamePauseMenu(true);
    }

    public void onGameGiveupButtonPressed() {
        enemyList.clear(); // enemy 초기화 하도록 변경
        Intent intent = new Intent(getContext(), ReStart.class);
        getContext().startActivity(intent);
        ((Activity) getContext()).finish();
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
        joystick = new Joystick(170, 900, 100, 60);
        skillButton = new SkillButton(1700, 900, 50, this);
        gamePauseButton = new GamePauseButton(context, 1800, 60, this);

        //오브젝트 초기설정
        if (jobs == 0)
            player = new Knight(getContext(), joystick, 500, 500, 30);
        else
            player = new Wizzard(getContext(), joystick, 500, 500, 30, enemyList);

        //배경 설정
        background = new InfiniteBackground(context, player);

        // 아이템 선택창 설정
        selectItem = new SelectItem(context, player, camera);

        // 게임 일시정지 메뉴 설정
        pauseMenu = new PauseMenu(context, player, camera);

        //스포너 설정
        enemySpawn = new EnemySpawn(this, player, camera, gameTimer);
        spellSpawn = new SpellSpawn(this, player, camera, gameTimer, jobs, selectItem);

        //카메라 시점을 플레이어가 중앙에 오게 설정
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics((displayMetrics));
        camera = new Camera(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

        //경험치바 설정
        expBar = new ExpBar(context, player, selectItem);

        playerState = new PlayerState(context, player, selectItem);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if ((selectItem.isLevelUp() == false) && (pauseMenu.isGamePauseMenu() == false)) {
                    if (joystick.getIsPressed()) {
                        numberOfSpellsToCast++;
                    } else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                        joystckPointerId = event.getPointerId(event.getActionIndex());
                        joystick.setIsPressed(true);
                    } else if (skillButton.isPressed((double) event.getX(), (double) event.getY())) {
                        skillButtonPointerId = event.getPointerId(event.getActionIndex());
                        skillButton.setIsPressed(true);
                        player.useSkill();
                    } else if (playerState.isShowState((double) event.getX(), (double) event.getY())) {
                        playerState.setShowState();

                    } else if (gamePauseButton.isPressed((double) event.getX(), (double) event.getY())) {
                        gamePauseButtonPointerId = event.getPointerId(event.getActionIndex());
                        gamePauseButton.setIsPressed(true);
                    } else {
                        numberOfSpellsToCast++;
                    }
                }
                // 레벨업을 했을때
                else if (selectItem.isLevelUp() == true) {
                    if (selectItem.isFirstSelectPressed((double) event.getX(), (double) event.getY())) {
                        selectItem.setLevelUp(false);
                    } else if (selectItem.isSecondSelectPressed((double) event.getX(), (double) event.getY())) {
                        selectItem.setLevelUp(false);
                    } else if (selectItem.isThirdSelectPressed((double) event.getX(), (double) event.getY())) {
                        selectItem.setLevelUp(false);
                    }
                }
                // 게임 일시정지를 했을 때
                else if (pauseMenu.isGamePauseMenu() == true) {
                    if (pauseMenu.isFirstSelectPressed((double) event.getX(), (double) event.getY())) {
                        pauseMenu.setGamePauseMenu(false);
                    } else if (pauseMenu.isSecondSelectPressed((double) event.getX(), (double) event.getY())) {
                        pauseMenu.setGamePauseMenu(false);
                    } else if (pauseMenu.isThirdSelectPressed((double) event.getX(), (double) event.getY())) {
                        onGameGiveupButtonPressed();
                        pauseMenu.setGamePauseMenu(false);
                    }
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
                } else if (gamePauseButtonPointerId == event.getPointerId(event.getActionIndex())) {
                    gamePauseButton.setIsPressed(false);
                    onGamePauseButtonPressed();
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
            enemy.draw(canvas, camera, selectItem, pauseMenu);
        }

        for (Spell spell : spellList) {
            spell.draw(canvas, camera);
        }

        // 게임패널 그리기
        joystick.draw(canvas);

        // 스킬버튼 그리기
        skillButton.draw(canvas);

        // 게임 일시정지 메뉴 그리기
        gamePauseButton.draw(canvas);

        //시간 출력
        gameTimer.draw(canvas, selectItem, pauseMenu);

        //경험치바 그리기
        expBar.draw(canvas);

        //플레이어 스텟 그리기
        playerState.draw(canvas);

        spellSpawn.draw(canvas, camera);

        //레벨업하면 아이템창
        if (selectItem.isLevelUp())
            selectItem.draw(canvas, camera);

        if (pauseMenu.isGamePauseMenu())
            pauseMenu.draw(canvas, camera);
    }

    public void update() {

        if (!selectItem.isLevelUp() && !pauseMenu.isGamePauseMenu()) {
            joystick.update();
            player.update();
            skillButton.update();

            enemySpawn.update(camera, expBar);
            spellSpawn.update(camera, expBar);

            camera.update();

            if (player.getHealthPoint() <= 0) {
                checkHP();
            }

            background.update(camera);

            expBar.update();
        }

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

