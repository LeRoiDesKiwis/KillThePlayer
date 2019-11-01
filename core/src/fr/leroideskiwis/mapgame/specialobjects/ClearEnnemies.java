package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;
import fr.leroideskiwis.utils.Utils;

public class ClearEnnemies extends SpecialObj {


    public ClearEnnemies(Game game) {
        super(game, "clearennemis.png");
    }

    @Override
    public void execute(Game game, Map map, Player player) {

        map.getEntities()
                .stream()
                .filter(entity -> entity instanceof Enemy)
                .forEach(map::deleteEntity);

        game.sendMessage(Utils.getText("objects.clearennemies.cleared"));

    }

    @Override
    public String name() {
        return Utils.getText("objects.clearennemies.name");
    }

    @Override
    public float chance() {
        return 0.004f;
    }

    @Override
    public Location spawn(Game main, Map map, Player player) {
        if(Math.random() < 0.10)
            return super.spawn(main, map, player);
        else
            return new Location(0, 0);
    }
}
