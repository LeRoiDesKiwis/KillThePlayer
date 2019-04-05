package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.Player;
import fr.leroideskiwis.mapgame.Position;

import java.util.Random;

public class InvinciblePlayer extends SpecialObj {
    public InvinciblePlayer(Game game) {
        super(game, "invincible");
    }

    @Override
    public void execute(Game main, Map map, Player player) {

        int invincibleTour = new Random().nextInt(3)+3;
        player.setInvincible(player.getInvincible()+invincibleTour);
        main.addInBuffer("You are now invicible for "+invincibleTour+" moves");

    }

    @Override
    public String name() {
        return "player invincible";
    }

    @Override
    public Position spawn(Game main, Map map, Player player) {
        return new RayonEnnemyKiller(game).spawn(main, map, player);
    }

    @Override
    public double chance() {
        return 0.06;
    }
}
