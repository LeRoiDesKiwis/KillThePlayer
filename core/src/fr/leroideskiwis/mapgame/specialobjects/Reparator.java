package fr.leroideskiwis.mapgame.specialobjects;

import com.badlogic.gdx.graphics.Color;
import fr.leroideskiwis.mapgame.*;

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
                main.addInBuffer("You restore a " + obstacle.getLostObject().getClass().getSimpleName());
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
