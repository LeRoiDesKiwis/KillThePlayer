package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.*;
import fr.leroideskiwis.mapgame.entities.Obstacle;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

import java.util.List;
import java.util.stream.Collectors;

public class Reparator extends SpecialObj {

    public Reparator(Game game) {
        super(game, "reparator");
    }

    @Override
    public void execute(Game main, Map map, Player player) {

        int i = 0, rand = main.randomInt(1, 2);

        List<Obstacle> lostObstacle = map.getObjectsByType(Obstacle.class).stream().filter(Obstacle::wasObject).collect(Collectors.toList());

        while(i < rand){

            if(lostObstacle.isEmpty()) break;
            Obstacle obstacle = lostObstacle.get(0);
            if(obstacle.wasObject()) {
                map.replaceObject(map.getPositionByObject(obstacle), obstacle.getLostObject());
                main.sendMessage("You restore a " + obstacle.getLostObject().getClass().getSimpleName());
                i++;
            }
        }

    }

    @Override
    public String name() {
        return "obstacle reparator";
    }

    @Override
    public double chance() {
        if(game.getMap().getObjectsByType(Obstacle.class).stream().anyMatch(Obstacle::wasObject))
            return 0.06;
        return 0;
    }
}
