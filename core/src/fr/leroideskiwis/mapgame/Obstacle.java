package fr.leroideskiwis.mapgame;

import fr.leroideskiwis.mapgame.specialobjects.SpecialObj;

public class Obstacle {

    private final SpecialObj lostObject;

    public Obstacle(){
        this.lostObject = null;
    }

    public Obstacle(SpecialObj obj){
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
}
