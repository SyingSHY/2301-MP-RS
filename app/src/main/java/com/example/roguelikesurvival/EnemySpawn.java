package com.example.roguelikesurvival;

import android.graphics.Canvas;

import com.example.roguelikesurvival.gamepanel.GameTimer;
import com.example.roguelikesurvival.object.Circle;
import com.example.roguelikesurvival.object.Enemy;
import com.example.roguelikesurvival.object.Goblin;
import com.example.roguelikesurvival.object.Imp;
import com.example.roguelikesurvival.object.Muddy;
import com.example.roguelikesurvival.object.Orc;
import com.example.roguelikesurvival.object.Player;
import com.example.roguelikesurvival.object.Spell;

import java.util.Iterator;

public class EnemySpawn {

    private Game game;
    private GameTimer gameTimer;
    private Player player;
    private Goblin goblin;
    private Orc orc;
    private Imp imp;
    private Muddy muddy;
    private int spawnPositionX;
    private int spawnPositionY;

    public EnemySpawn(Game game, Player player, Camera camera, GameTimer gameTimer) {
        this.game = game;
        this.gameTimer = gameTimer;
        this.player = player;

        goblin = new Goblin(game.getContext(), player, camera);
        orc = new Orc((game.getContext()), player, camera);
        imp = new Imp((game.getContext()), player, camera);
        muddy = new Muddy((game.getContext()), player, camera);
    }


    public void update(Camera camera) {
        //고블린 스폰
        if (goblin.readyToSpawn()) {
            game.enemyList.add(new Goblin(game.getContext(), player, camera));
        }
        //오크 스폰
        if (gameTimer.getMinute() > 1 && orc.readyToSpawn()) {
            game.enemyList.add(new Orc(game.getContext(), player, camera));
        }
        //임프 스폰
        if (imp.readyToSpawn()) {
            game.enemyList.add(new Imp(game.getContext(), player, camera));
        }
        //진흙 스폰
        if (muddy.readyToSpawn()) {
            game.enemyList.add(new Muddy(game.getContext(), player, camera));
        }

        while (game.numberOfSpellsToCast > 0) {
            game.spellList.add(new Spell(game.getContext(), player));
            game.numberOfSpellsToCast--;
        }
        for (Enemy enemy : game.enemyList) {
            enemy.update();
        }

        for (Spell spell : game.spellList) {
            spell.update();
        }

        //enemy와 player간의 충돌 체크
        Iterator<Enemy> iteratorEnemy = game.enemyList.iterator();
        while (iteratorEnemy.hasNext()) {
            Enemy enemy = iteratorEnemy.next();
            if (Circle.isColliding(enemy, player)) {
                iteratorEnemy.remove();

                player.setHealthPoint(player.getHealthPoint() - 1);
                continue;
            }

            //enemy와 spell간의 충돌 체크
            Iterator<Spell> iteratorSpell = game.spellList.iterator();
            while (iteratorSpell.hasNext()) {
                Circle spell = iteratorSpell.next();

                if (Circle.isColliding(spell, enemy)) {
                    iteratorSpell.remove();
                    enemy.setHealthPoint(enemy.getHealthPoint() - 1);
                    enemy.setHitImage(true);
                    break;
                }
            }
            //체력 0이하면 몬스터 제거
            if(enemy.getHealthPoint() <= 0)
                iteratorEnemy.remove();
        }
    }
}
