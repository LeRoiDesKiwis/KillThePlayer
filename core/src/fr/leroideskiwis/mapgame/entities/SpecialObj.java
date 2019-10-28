package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;

public abstract class SpecialObj extends Entity{

    protected final Game game;

    public abstract void execute(Game game, Map map, Player player);

    public SpecialObj(Game game, String path) {
        super(path);
        this.game = game;
    }

    public SpecialObj(Game game){
        this(game, null);
    }

    public String toString(){
        return "!";
    }

    public abstract String name();
    public abstract float chance();

    public Location spawn(Game main, Map map, Player player){

        return map.getRandomLocationWithSize(size());

    }

    public void kill(){
        delete(game.getMap());
        game.getMap().setEntity(getFirstLocation(), new Obstacle(this));
    }

    @Override
    public boolean onCollide(Game game, Map map, Player player) {
        execute(game, map, player);
        return true;
    }
}