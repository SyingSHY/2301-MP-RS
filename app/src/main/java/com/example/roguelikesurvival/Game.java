package com.example.roguelikesurvival;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.object.Circle;
import com.example.roguelikesurvival.object.Enemy;
import com.example.roguelikesurvival.object.Player;
import com.example.roguelikesurvival.object.Spell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private final Player player;
    private final Joystick joystick;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Spell> spellList = new ArrayList<Spell>();
    private int joystckPointerId = 0;
    private int numberOfSpellsToCast = 0;

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        //game objects 초기설정
        joystick = new Joystick(170, 800, 100, 60);
        player = new Player(getContext(), joystick, 500, 500, 30);

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
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);

        joystick.draw(canvas);
        player.draw(canvas);
        for (Enemy enemy : enemyList) {
            enemy.draw(canvas);
        }
        for (Spell spell : spellList) {
            spell.draw(canvas);
        }
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }

    public void update() {
        joystick.update();
        player.update();

        if (Enemy.readyToSpawn()) {
            enemyList.add(new Enemy(getContext(), player));
        }

        while (numberOfSpellsToCast > 0) {
            spellList.add(new Spell(getContext(), player));
            numberOfSpellsToCast--;
        }
        for (Enemy enemy : enemyList) {
            enemy.update();
        }

        for (Spell spell : spellList) {
            spell.update();
        }

        //enemy와 player간의 충돌 체크
        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while (iteratorEnemy.hasNext()) {
            Circle enemy = iteratorEnemy.next();
            if (Circle.isColliding(enemy, player)) {
                iteratorEnemy.remove();

                player.setHealthPoint(player.getHealthPoint() - 1);
                continue;
            }

            //enemy와 spell간의 충돌 체크
            Iterator<Spell> iteratorSpell = spellList.iterator();
            while (iteratorSpell.hasNext()) {
                Circle spell = iteratorSpell.next();

                if (Circle.isColliding(spell, enemy)) {
                    iteratorSpell.remove();
                    iteratorEnemy.remove();
                    break;
                }
            }
        }
    }
}

