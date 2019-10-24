package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;

public class Obstacle extends Entity {

    private final SpecialObj lostObject;

    public Obstacle(){
        this(null);
    }

    public Obstacle(SpecialObj obj){
         super("obstacle.png");
         this.lostObject = obj;
    }

    public boolean wasObject(){
        return lostObject != null;
    }

    public SpecialObj getLostObject() {
        return lostObject;
    }

    public String toString(){
        return "O";
    }

    @Override
    public boolean isInvulnerable() {
        return lostObject == null;
    }

    @Override
    public boolean onCollide(Game game, Map map, Player player) {
        if(!isInvulnerable()) return lostObject.onCollide(game, map, player);
        return false;
    }
}
