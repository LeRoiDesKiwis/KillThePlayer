package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.*;
import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

import java.util.List;
import java.util.stream.Collectors;

public class OpenPath extends SpecialObj {
    public OpenPath(Game game) {
        super(game, "openpath");
    }

    @Override
    public void execute(Game main, Map map, Player player) {

        int rayon = 2;

        Position position = map.getPositionByObject(this);

        for(int x = 0; x < map.getSize(); x++){
            for(int y = position.getY()-rayon; y < position.getY()+rayon; y++){

                if(new Position(x, y).isOutOfMap(map)) continue;

                Object object = map.getObject(x, y);

                if(object instanceof Enemy)
                    map.deleteObject(x, y);
            }
        }

    }

    @Override
    public Position spawn(Game main, Map map, Player player) {

        for(int x = 0; x < map.getSize(); x++){
            for(int y = 0; y < map.getSize(); y++){

                Position position1 = new Position(x ,y);
                if(position1.isOutOfMap(map)) continue;

                if(position1.getSurroundingsObjects(map).stream().anyMatch(o -> o instanceof Enemy)) {

                    List<Position> positionList = map.getEmptyCases().stream().filter(pos -> pos.getY() == position1.getY()).collect(Collectors.toList());

                    if(!positionList.isEmpty()) return main.getRandomList(positionList);

                }
            }
        }

        return null;
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
