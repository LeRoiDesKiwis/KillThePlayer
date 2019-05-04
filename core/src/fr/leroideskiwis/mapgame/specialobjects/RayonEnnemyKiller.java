package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.*;
import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

import java.util.stream.Collectors;

public class RayonEnnemyKiller extends SpecialObj {
    public RayonEnnemyKiller(Game game) {
        super(game, "rayonkiller");
    }

    @Override
    public void execute(Game game, Map map, Player player) {
        Position position = map.getPositionByObject(this);

        int rayon = game.randomInt(3, 4);

        game.sendMessage("All ennemies in a radius of " + rayon + " has been killed");

        for (int x = position.getX() - rayon; x <= position.getX() + rayon; x++) {
            for (int y = position.getY() - rayon; y <= position.getY() + rayon; y++) {
                Position dest = new Position(x, y);
                if (dest.isOutOfMap(map)) continue;
                Object object = map.getObject(x, y);
                if (object == null)
                    continue;
                if (!(object instanceof Enemy))
                    continue;
                map.deleteObject(dest);
            }
        }
    }


    @Override
    public String name() {
        return "ennemy rayon killer";
    }

    @Override
    public double chance() {
        return 0.806;
    }

    /*
       tableau de boolean

       isNull   surroundingContains   continue
       true     true                  true
       false    true                  true
       true     false                 true
       false    false                 false
     */

    public Position spawn(Game game, Map map, Player player) {
        return game.getRandomList(map.getEmptyCases().stream().filter(pos -> pos.getSurroundingsObjects(map).stream().anyMatch(o -> o instanceof Enemy)).collect(Collectors.toList()));
    }
}