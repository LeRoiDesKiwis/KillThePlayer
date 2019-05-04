package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.mapgame.Entity;

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
}
