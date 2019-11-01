package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.plugins.events.OnPlayerTakeObject;
import fr.leroideskiwis.utils.Utils;

public abstract class SpecialObj extends Entity{

    protected final Game game;

    protected abstract void execute(Game game, Map map, Player player);

    public SpecialObj(Game game, String path) {
        super(path);
        this.game = game;
    }

    public SpecialObj(Game game){
        this(game, null);
    }

    public String toString(){
        return name();
    }

    public abstract String name();
    public abstract float chance();

    public Location spawn(Game main, Map map, Player player){

        return map.getRandomLocationWithSize(size());

    }

    public void kill(){
        delete(game.getMap());
        game.getMap().setEntity(getLocation(), new Obstacle(this));
    }

    @Override
    public boolean onCollide(Game game, Map map, Player player) {
        OnPlayerTakeObject event = new OnPlayerTakeObject(getLocation(), this);
        game.getPluginManager().callEvent(event);
        if(event.isCancelled()) return false;
        game.sendMessage(Utils.format("objects.found", event.getSpecialObj().name()));
        event.getSpecialObj().execute(game, map, player);
        return true;
    }
}