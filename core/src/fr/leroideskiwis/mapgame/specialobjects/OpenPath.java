package fr.leroideskiwis.mapgame.specialobjects;

import com.badlogic.gdx.graphics.Color;
import fr.leroideskiwis.mapgame.*;

import java.util.List;
import java.util.stream.Collectors;

public class OpenPath extends SpecialObj{
    public OpenPath(Game game) {
        super(game, "openpath");
    }

    @Override
    public void execute(Game main, Map map, Player player) {

        int rayon = 2;

        Position position = map.getPositionByObject(this);

        for(int x = 0; x < map.getSize()[1]; x++){
            for(int y = position.getY()-rayon; y < position.getY()+rayon; y++){

                if(new Position(x, y).isOutOfMap(map)) continue;

                Object object = map.getObject(x, y);

                if(object instanceof Ennemy)
                    map.deleteObject(x, y);
            }
        }

    }

    @Override
    public Position spawn(Game main, Map map, Player player) {

        for(int x = 0; x < map.getSize()[0]; x++){
            for(int y = 0; y < map.getSize()[1]; y++){

                Position position1 = new Position(x ,y);
                if(position1.isOutOfMap(map)) continue;

                if(position1.getSurroundingsObjects(map).stream().anyMatch(o -> o instanceof Ennemy)) {

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
