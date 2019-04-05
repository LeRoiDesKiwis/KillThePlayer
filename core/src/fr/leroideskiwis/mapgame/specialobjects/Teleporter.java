package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.Player;
import fr.leroideskiwis.mapgame.Position;

public class Teleporter extends SpecialObj {
    public Teleporter(Game game) {
        super(game, "teleporter");
    }

    @Override
    public void execute(Game game, Map map, Player player) {

        Position pos;
        do
            pos = Position.getRandomPosition(map);
        while(!player.setPosition(pos));

        System.out.println("You have been teleported in "+pos);    }

    @Override
    public String name() {
        return "teleporter";
    }

    @Override
    public double chance() {
        return 0.01;
    }

}
