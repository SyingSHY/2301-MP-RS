package com.example.roguelikesurvival;

import android.graphics.Canvas;

import com.example.roguelikesurvival.gamepanel.GameTimer;
import com.example.roguelikesurvival.object.Circle;
import com.example.roguelikesurvival.object.Enemy;
import com.example.roguelikesurvival.object.Goblin;
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
    private int spawnPositionX;
    private int spawnPositionY;

    public EnemySpawn(Game game, Player player, Camera camera, GameTimer gameTimer) {
        this.game = game;
        this.gameTimer = gameTimer;
        this.player = player;

        goblin = new Goblin(game.getContext(), player, camera);
        orc = new Orc((game.getContext()), player, camera);
    }

//    public int getSpawnPositionX(Camera camera) {
//        if (player.getDirectionX() > 0) {
//            spawnPositionX = (int) camera.gameToScreenCoordinateX(player.getPositionX() + 1200);
//        }
//        return spawnPositionX;
//    }
//
//    public int getSpawnPositionY(Camera camera) {
//        return spawnPositionY;
//    }

    public void update(Camera camera) {
        //고블린 스폰
        if (goblin.readyToSpawn()) {
            game.enemyList.add(new Goblin(game.getContext(), player, camera));
        }
        //오크 스폰
        if (gameTimer.getMinute() > 1 && orc.readyToSpawn()) {
            game.enemyList.add(new Orc(game.getContext(), player, camera));
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
            Circle enemy = iteratorEnemy.next();
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
                    iteratorEnemy.remove();
                    break;
                }
            }
        }
    }
}
