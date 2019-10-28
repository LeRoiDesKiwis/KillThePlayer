package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Obstacle;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

import java.util.List;
import java.util.stream.Collectors;

public class Reparator extends SpecialObj {

    public Reparator(Game game) {
        super(game, "reparator.png");
    }

    @Override
    public void execute(Game game, Map map, Player player) {

        List<Obstacle> lostObstacle = map.getEntitiesByType(Obstacle.class).stream().filter(Obstacle::wasObject).collect(Collectors.toList());

        if(lostObstacle.isEmpty()) return;

        for(int i = 0, rand = game.randomInt(1, 2); i < rand; i++) {

            Obstacle obstacle = lostObstacle.get(0);
            map.replaceEntity(obstacle.getFirstLocation(), obstacle.getLostObject());
            game.sendMessage("You restore a " + obstacle.getLostObject().getClass().getSimpleName());
            i++;

        }

    }

    @Override
    public String name() {
        return "obstacle reparator";
    }

    @Override
    public float chance() {
        if(game.getMap().getEntitiesByType(Obstacle.class).stream().anyMatch(Obstacle::wasObject))
            return 0.06f;
        return 0f;
    }

}
