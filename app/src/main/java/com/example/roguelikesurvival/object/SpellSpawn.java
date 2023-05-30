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

    public SpellSpawn(Game game, Player player, Camera camera, GameTimer gameTimer, int jobs) {
        this.game = game;
        this.gameTimer = gameTimer;
        this.player = player;
        this.jobs = jobs;

        basicAttack = new BasicAttack(game.getContext(), player, jobs);

    }

    public void update(Camera camera, ExpBar expBar) {
        //기사일때 기본공격
        if (jobs == 0) {
            while (game.numberOfSpellsToCast > 0) {
                game.spellList.add(new BasicAttack(game.getContext(), player, jobs));
                game.numberOfSpellsToCast--;
            }
        }

        //마법사일때 기본공격
        else {
            while (basicAttack.readyToAttack()) {
                game.spellList.add(new BasicAttack(game.getContext(), player, jobs));
            }
        }

        for (Spell spell : game.spellList) {
            spell.update();
        }

        Iterator<Spell> iteratorSpell = game.spellList.iterator();
        while (iteratorSpell.hasNext()) {
            Circle spell = iteratorSpell.next();

            if (Utils.getDistanceBetweenPoints(spell.getPositionX(), spell.getPositionY(), player.getPositionX(), player.getPositionY()) > 1500) {
                iteratorSpell.remove();
                break;
            }
        }

    }
}
