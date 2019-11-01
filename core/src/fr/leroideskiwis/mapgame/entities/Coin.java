package fr.leroideskiwis.mapgame.entities;

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
    public boolean onCollide(Game game, Map map, Player player) {
        game.addScore(amount);
        OnTakeCoin event = new OnTakeCoin(getLocation(), this);
        game.getPluginManager().callEvent(event);
        if (!event.isCancelled()) map.deleteEntity(getLocation());
        return false;
    }
}
