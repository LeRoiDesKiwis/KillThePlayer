package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;
import fr.leroideskiwis.utils.Interval;

public class OpenPath extends SpecialObj {
    public OpenPath(Game game) {
        super(game, "openpath");
    }

    @Override
    public void execute(Game game, Map map, Player player) {

        int rayon = 2;

        game.getEntities()
                .stream()
                .filter(entity -> entity instanceof Enemy && Interval.of(getLocation().getY()-rayon, getLocation().getY()+rayon).contains(entity.getY()))
                .forEach(map::deleteEntity);

    }

    @Override
    public Location spawn(Game main, Map map, Player player) {

        return game.getLocationNearEnemy();
    }

    @Override
    public String name() {
        return "open path";
    }

    @Override
    public double chance() {
        return 0.05;
    }
}
