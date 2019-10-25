package fr.leroideskiwis.mapgame.specialobjects;

import com.badlogic.gdx.graphics.Texture;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

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

        game.sendMessage("MAP CLEARED");

    }

    @Override
    public String name() {
        return "clear map !";
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
