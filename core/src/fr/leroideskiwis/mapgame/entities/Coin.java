package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.ktp.ExecutionData;
import fr.leroideskiwis.mapgame.Entity;

public class Coin extends Entity {

    private final int amount;

    public Coin(int amount){
        super("coin.png");
        this.amount = amount;
    }

    public String toString(){
        return "*";
    }

    @Override
    public boolean onCollide(ExecutionData executionData) {
        executionData.game.addScore(amount);
        return false;
    }
}
