package com.example.roguelikesurvival;

import android.graphics.Canvas;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.Game;
import com.example.roguelikesurvival.Utils;
import com.example.roguelikesurvival.gamepanel.ExpBar;
import com.example.roguelikesurvival.gamepanel.GameTimer;
import com.example.roguelikesurvival.object.Circle;
import com.example.roguelikesurvival.object.Enemy;
import com.example.roguelikesurvival.object.Player;
import com.example.roguelikesurvival.object.Spell;
import com.example.roguelikesurvival.object.item.BasicAttack;
import com.example.roguelikesurvival.object.item.RotateAttack;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class SpellSpawn {
    private Game game;
    private GameTimer gameTimer;
    private BasicAttack basicAttack;
    private RotateAttack rotateAttack;
    private Player player;
    private SelectItem selectItem;
    private int jobs;

    public SpellSpawn(Game game, Player player, Camera camera, GameTimer gameTimer, int jobs, SelectItem selectItem) {
        this.game = game;
        this.gameTimer = gameTimer;
        this.player = player;
        this.jobs = jobs;
        this.selectItem = selectItem;

        basicAttack = new BasicAttack(game.getContext(), player, jobs, 20);
        rotateAttack = selectItem.getRotateAttack();
    }

    public void draw(Canvas canvas, Camera camera){
        //기본공격
        if (basicAttack.getAnimationState() == true)
            basicAttack.draw(canvas, camera);
        //회전공격
        if (rotateAttack.getAnimationState() == true)
            rotateAttack.draw(canvas, camera);
    }

    public void update(Camera camera, ExpBar expBar) {
        //기사일때 기본공격
        if (jobs == 0) {
            while (basicAttack.readyToAttack()) {
//                game.spellList.add(new BasicAttack(game.getContext(), player, jobs, 60));
                basicAttack.startAnimationState();
            }

            Iterator<Enemy> iteratorEnemy = game.enemyList.iterator();
                while (iteratorEnemy.hasNext()) {
                    Enemy enemy = iteratorEnemy.next();
                    if (basicAttack.getDamageState() == true && basicAttack.withinAttackDistance(camera, enemy)) {
                        enemy.setHealthPoint(enemy.getHealthPoint() - player.getAttackPower());
                        enemy.setHitImage(true);
                    }

                    //체력 0이하면 몬스터 제거
                    if (enemy.getHealthPoint() <= 0) {
                        iteratorEnemy.remove();
                        game.setMonsterKillCount();
                        expBar.plusExpPoint(1);
                    }
                }

//            //enemy와 spell간의 충돌 체크
//            Iterator<Spell> iteratorSpell = game.spellList.iterator();
//            while (iteratorSpell.hasNext()) {
//                Circle spell = iteratorSpell.next();
//
//                Iterator<Enemy> iteratorEnemy = game.enemyList.iterator();
//                while (iteratorEnemy.hasNext()) {
//                    Enemy enemy = iteratorEnemy.next();
//                    if (basicAttack.withinAttackDistance(camera, enemy)) {
//                        enemy.setHealthPoint(enemy.getHealthPoint() - 1);
//                        enemy.setHitImage(true);
//                        break;
//                    }
//                }
//                iteratorSpell.remove();
//            }
        }

        //마법사일때 기본공격
        else {
            while (basicAttack.readyToAttack()) {
                game.spellList.add(new BasicAttack(game.getContext(), player, jobs, 20));
            }

            for (Spell spell : game.spellList) {
                spell.update();
            }

            Iterator<Spell> iteratorSpell = game.spellList.iterator();
            while (iteratorSpell.hasNext()) {
                Circle spell = iteratorSpell.next();

                // enemy와 spell간의 충돌 체크
                Iterator<Enemy> iteratorEnemy = game.enemyList.iterator();
                while (iteratorEnemy.hasNext()) {
                    Enemy enemy = iteratorEnemy.next();

                    if (Circle.isColliding(spell, enemy)) {
                        iteratorSpell.remove();
                        enemy.setHealthPoint(enemy.getHealthPoint() - player.getAttackPower());
                        enemy.setHitImage(true);
                        break;
                    }

                    //체력 0이하면 몬스터 제거
                    if (enemy.getHealthPoint() <= 0) {
                        iteratorEnemy.remove();
                        game.setMonsterKillCount();
                        expBar.plusExpPoint(1);
                    }
                }

                // 화염구가 플레이어에게서 일정거리 이상 떨어지면 삭제
                if (Utils.getDistanceBetweenPoints(spell.getPositionX(), spell.getPositionY(), player.getPositionX(), player.getPositionY()) > 1500) {
                    iteratorSpell.remove();
                    break;
                }
            }
        }

        //회전공격
        if(rotateAttack.getIsSelect()) {
            while (rotateAttack.readyToAttack()) {
                rotateAttack.startAnimationState();
            }
            Iterator<Enemy> iteratorEnemy = game.enemyList.iterator();
            while (iteratorEnemy.hasNext()) {
                Enemy enemy = iteratorEnemy.next();
                if (rotateAttack.getDamageState() == true && rotateAttack.withinAttackDistance(camera, enemy)) {
                    enemy.setHealthPoint(enemy.getHealthPoint() - selectItem.getItemAttackPower());
                    enemy.setHitImage(true);
                }

                //체력 0이하면 몬스터 제거
                if (enemy.getHealthPoint() <= 0) {
                    iteratorEnemy.remove();
                    game.setMonsterKillCount();
                    expBar.plusExpPoint(1);
                }
            }
        }
    }
}
