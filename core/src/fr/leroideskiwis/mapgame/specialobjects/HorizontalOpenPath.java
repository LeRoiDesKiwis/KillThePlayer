package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;
import fr.leroideskiwis.utils.Interval;
import fr.leroideskiwis.utils.Utils;

public class HorizontalOpenPath extends SpecialObj {
    public HorizontalOpenPath(Game game) {
        super(game, "openpathH.png");
    }

    @Override
    public void execute(Game game, Map map, Player player) {

        int rayon = game.randomInt(3, 4);

        game.getEntities()
                .stream()
                .filter(entity -> entity instanceof Enemy && Interval.of(getLocation().y-rayon, getLocation().y+rayon).contains(entity.getLocation().y))
                .forEach(map::deleteEntity);

    }

    @Override
    public Location spawn(Game main, Map map, Player player) {

        return game.getLocationNearEnemy();
    }

    @Override
    public String name() {
        return Utils.getText("objects.openpath.name");
    }

    @Override
    public float chance() {
        return 0.123f;
    }
}
