package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.ktp.ExecutionData;
import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.plugins.events.OnTakeCoin;

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
        OnTakeCoin event = new OnTakeCoin(getLocation(), this);
        executionData.getGame().getPluginManager().callEvent(event);
        if (!event.isCancelled()) executionData.getMap().deleteEntity(getLocation());
        return false;
    }
}
