package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.ktp.ExecutionData;
import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.specialobjects.SpecialObject;

public class Obstacle extends Entity {

    private final SpecialObject lostObject;

    public Obstacle(){
        this(null);
    }

    public Obstacle(SpecialObject obj){
         super("obstacle.png");
         this.lostObject = obj;
    }

    public boolean wasObject(){
        return lostObject != null;
    }

    public SpecialObject getLostObject() {
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
    public boolean onCollide(ExecutionData executionData) {
        if(!isInvulnerable()) return lostObject.onCollide(executionData);
        return false;
    }
}
