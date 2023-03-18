package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.ktp.ExecutionData;
import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;

public class Coin extends Entity {

    private int amount;

    public Coin(int amount){
        super("coin.png");
        this.amount = amount;
    }

    public int getAmount(){
        return amount;
    }

    public String toString(){
        return "*";
    }

    @Override
    public boolean onCollide(ExecutionData executionData) {
        executionData.getGame().addScore(amount);
        return false;
    }
}
