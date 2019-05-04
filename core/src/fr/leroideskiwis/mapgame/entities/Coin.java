package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.mapgame.Entity;

public class Coin extends Entity {

    private int amount;

    public Coin(int amount){
        super("coin");
        this.amount = amount;
    }

    public int getAmount(){
        return amount;
    }

    public String toString(){
        return "*";
    }

}
