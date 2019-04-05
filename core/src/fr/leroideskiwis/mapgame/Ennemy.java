package fr.leroideskiwis.mapgame;

import java.util.Random;

public class Ennemy {

    private Map map;

    public Ennemy(Map map){
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