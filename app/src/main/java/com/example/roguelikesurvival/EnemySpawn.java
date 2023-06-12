package com.example.roguelikesurvival;

import com.example.roguelikesurvival.gamepanel.ExpBar;
import com.example.roguelikesurvival.gamepanel.GameTimer;
import com.example.roguelikesurvival.object.BigZombie;
import com.example.roguelikesurvival.object.Chort;
import com.example.roguelikesurvival.object.Circle;
import com.example.roguelikesurvival.object.Enemy;
import com.example.roguelikesurvival.object.Goblin;
import com.example.roguelikesurvival.object.Imp;
import com.example.roguelikesurvival.object.Muddy;
import com.example.roguelikesurvival.object.Ogre;
import com.example.roguelikesurvival.object.Orc;
import com.example.roguelikesurvival.object.Player;
import com.example.roguelikesurvival.object.Spell;
import com.example.roguelikesurvival.object.item.BasicAttack;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class EnemySpawn {

    private Game game;
    private GameTimer gameTimer;
    private Player player;
    private Goblin goblin;
    private Orc orc;
    private Imp imp;
    private Muddy muddy;
    private BigZombie bigZombie;
    private Chort chort;
    private Ogre ogre;
    private double spawnPositionX;
    private double spawnPositionY;
    private boolean isDamage = false;
    private boolean damageDelay = false;
    private int jobs;

    public EnemySpawn(Game game, Player player, Camera camera, GameTimer gameTimer, int jobs) {
        this.game = game;
        this.gameTimer = gameTimer;
        this.player = player;
        this.jobs = jobs;

        goblin = new Goblin(game.getContext(), player, camera, 0, 0, 30);
        orc = new Orc((game.getContext()), player, camera, 0, 0, 30);
        imp = new Imp((game.getContext()), player, camera, 0, 0, 30);
        muddy = new Muddy((game.getContext()), player, camera, 0, 0, 30);
        bigZombie = new BigZombie((game.getContext()), player, camera, 0, 0, 45);
        chort = new Chort((game.getContext()), player, camera, 0, 0, 35);
        ogre = new Ogre((game.getContext()), player, camera, 0, 0, 35);
    }

    public void positionUpOrDown() {
        spawnPositionX = (ThreadLocalRandom.current().nextInt(2) * 2 - 1) * (Math.random() * 1000);
        spawnPositionY = (ThreadLocalRandom.current().nextInt(2) * 2 - 1) * ((Math.random() * 100) + 600);
    }

    public void positionLeftOrRight() {
        spawnPositionX = (ThreadLocalRandom.current().nextInt(2) * 2 - 1) * ((Math.random() * 100) + 1000);
        spawnPositionY = (ThreadLocalRandom.current().nextInt(2) * 2 - 1) * (Math.random() * 550);
    }

    public void setRandomPosition() {
        int randomNum = ThreadLocalRandom.current().nextInt(2);
        if (randomNum == 0)
            positionUpOrDown();
        else if (randomNum == 1)
            positionLeftOrRight();
    }

    public void update(Camera camera, ExpBar expBar) {
        if (goblin.readyToSpawn()) {
            setRandomPosition();
            game.enemyList.add(new Goblin(game.getContext(), player, camera, spawnPositionX, spawnPositionY, 30));
        }
        //오크 스폰
        if (gameTimer.getMinute() >= 1 && orc.readyToSpawn()) {
            setRandomPosition();
            game.enemyList.add(new Orc(game.getContext(), player, camera, spawnPositionX, spawnPositionY, 30));
        }
        //임프 스폰
        if (gameTimer.getMinute() >= 3 && imp.readyToSpawn()) {
            setRandomPosition();
            game.enemyList.add(new Imp(game.getContext(), player, camera, spawnPositionX, spawnPositionY, 30));
        }
        //진흙 스폰
        if (gameTimer.getMinute() >= 2 && muddy.readyToSpawn()) {
            setRandomPosition();
            game.enemyList.add(new Muddy(game.getContext(), player, camera, spawnPositionX, spawnPositionY, 30));
        }
        //좀비 스폰
        if (gameTimer.getMinute() >= 4 && bigZombie.readyToSpawn()) {
            setRandomPosition();
            game.enemyList.add(new BigZombie(game.getContext(), player, camera, spawnPositionX, spawnPositionY, 45));
        }
        //chort 스폰
        if (gameTimer.getMinute() >= 7 && chort.readyToSpawn()) {
            setRandomPosition();
            game.enemyList.add(new Chort(game.getContext(), player, camera, spawnPositionX, spawnPositionY, 35));
        }
        //오거 스폰
        if (gameTimer.getMinute() >= 5 && ogre.readyToSpawn()) {
            setRandomPosition();
            game.enemyList.add(new Ogre(game.getContext(), player, camera, spawnPositionX, spawnPositionY, 35));
        }

        for (Enemy enemy : game.enemyList) {
            enemy.update();
        }

        //플레이어 끔살 방지를 위해 데미지 받았을 시 무적시간 부여
        if (damageDelay) {
            if (player.isDamage() == true)
                damageDelay = false;
        }

        //enemy와 player간의 충돌 체크
        Iterator<Enemy> iteratorEnemy = game.enemyList.iterator();
        while (iteratorEnemy.hasNext()) {
            Enemy enemy = iteratorEnemy.next();
            if (Circle.isColliding(enemy, player)) {
                if(jobs == 0) {
                    if (!player.isUsingSkill() && damageDelay == false) {
                        isDamage = true;
                    }
                }
                else if(jobs == 1){
                    if (damageDelay == false) {
                        isDamage = true;
                    }
                }
                if (isDamage) {
                    player.setHealthPoint(player.getHealthPoint() - 1);
                    damageDelay = true;
                    isDamage = false;
                }
            }

//            //enemy와 spell간의 충돌 체크
//            Iterator<Spell> iteratorSpell = game.spellList.iterator();
//            while (iteratorSpell.hasNext()) {
//                Circle spell = iteratorSpell.next();
//
//                if (Circle.isColliding(spell, enemy)) {
//                    iteratorSpell.remove();
//                    enemy.setHealthPoint(enemy.getHealthPoint() - 1);
//                    enemy.setHitImage(true);
//                    break;
//                }
//            }
            //체력 0이하면 몬스터 제거
            if (enemy.getHealthPoint() <= 0) {
                iteratorEnemy.remove();
                expBar.plusExpPoint(1);
            }
        }
    }
}
