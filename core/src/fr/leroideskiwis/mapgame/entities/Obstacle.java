package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.ktp.ExecutionData;
import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.specialobjects.SpecialObject;
import fr.leroideskiwis.utils.Utils;

public class Obstacle extends Entity {

    private final SpecialObject lostObject;

    public Obstacle(){
        this(null);
    }

    public Obstacle(int x, int y){
        this(null, x, y);
    }

    public Obstacle(SpecialObject specialObject){
        this(specialObject, 0, 0);
    }

    public Obstacle(SpecialObject obj, int x, int y){
         super(x, y, "obstacle.png");
         this.lostObject = obj;
    }

    public boolean wasObject(){
        return lostObject != null;
    }

    public void revive(ExecutionData executionData){
        executionData.map.replaceEntity(getLocation(), lostObject);
        executionData.game.sendMessage(Utils.format("objects.reparator.restore", lostObject));
    }

    public String toString(){
        return "O";
    }

    @Override
    public boolean isRemovable() {
        return lostObject != null;
    }

    @Override
    public boolean onCollide(ExecutionData executionData) {
        if(isRemovable()) return lostObject.onCollide(executionData);
        return false;
    }
}
