package com.example.roguelikesurvival.object;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.Game;
import com.example.roguelikesurvival.Utils;
import com.example.roguelikesurvival.gamepanel.ExpBar;
import com.example.roguelikesurvival.gamepanel.GameTimer;
import com.example.roguelikesurvival.object.item.BasicAttack;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class SpellSpawn {
    private Game game;
    private GameTimer gameTimer;
    private BasicAttack basicAttack;
    private Player player;
    private int jobs;

    public SpellSpawn(Game game, Player player, Camera camera, GameTimer gameTimer, int jobs, BasicAttack basicAttack) {
        this.game = game;
        this.gameTimer = gameTimer;
        this.player = player;
        this.jobs = jobs;
        this.basicAttack = basicAttack;
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
                    enemy.setHealthPoint(enemy.getHealthPoint() - 1);
                    enemy.setHitImage(true);
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
                        enemy.setHealthPoint(enemy.getHealthPoint() - 1);
                        enemy.setHitImage(true);
                        break;
                    }
                }

                // 화염구가 플레이어에게서 일정거리 이상 떨어지면 삭제
                if (Utils.getDistanceBetweenPoints(spell.getPositionX(), spell.getPositionY(), player.getPositionX(), player.getPositionY()) > 1500) {
                    iteratorSpell.remove();
                    break;
                }
            }
        }
    }
}
