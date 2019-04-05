package fr.leroideskiwis.mapgame;

public class Coin {

    private int amount;

    public Coin(int amount){
        this.amount = amount;
    }

    public int getAmount(){
        return amount;
    }

    public String toString(){
        return "*";
    }

}
