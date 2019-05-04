package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.Position;

import java.util.Random;

public class Enemy extends Entity {

    private Map map;

    public Enemy(Map map){
        super("ennemy");
        this.map = map;
    }


    public boolean hasFullObjectInSurrounding(){

        return map.getObject(getPosition().add(1, 0)) != null &&
                map.getObject(getPosition().add(-1, 0)) != null &&
                map.getObject(getPosition().add(0, 1)) != null &&
                map.getObject(getPosition().add(1, -1)) != null;

    }

    public Position getPosition(){
        return map.getPositionByObject(this);
    }

    public String toString(){return "X";}
}