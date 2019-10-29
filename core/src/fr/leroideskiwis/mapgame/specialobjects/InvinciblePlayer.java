package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

public class InvinciblePlayer extends SpecialObj {
    public InvinciblePlayer(Game game) {
        super(game, "invincible.png");
    }

    @Override
    public void execute(Game game, Map map, Player player) {

        int invincibleTour = game.randomInt(5, 6);
        player.addInvincility(invincibleTour);
        game.sendMessage("You got "+invincibleTour+" invincible moves");

    }

    @Override
    public String name() {
        return "player invincible";
    }

    @Override
    public Location spawn(Game main, Map map, Player player) {
        return new RayonEnnemyKiller(game).spawn(main, map, player);
    }

    @Override
    public float chance() {
        return 0.06f;
    }

}
