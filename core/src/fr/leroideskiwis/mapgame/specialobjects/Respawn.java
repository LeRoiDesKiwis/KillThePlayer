package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

public class Respawn extends SpecialObj {
    public Respawn(Game game) {
        super(game, "respawnobject.png");
    }

    @Override
    public void execute(Game game, Map map, Player player) {
        map.getEntitiesByType(SpecialObj.class).forEach(specialObj -> specialObj.setLocation(specialObj.spawn(game, map, player)));
    }

    @Override
    public String name() {
        return "Respawn objects";
    }

    @Override
    public float chance() {
        return 0.06f;
    }
}
