package fr.leroideskiwis.mapgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import fr.leroideskiwis.ktp.Main;
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
    public abstract double chance();

    public Location spawn(Game main, Map map, Player player){

        return map.getRandomLocation();

    }

    public void kill(){
        game.getMap().deleteEntity(getLocation());
        game.getMap().setEntity(getLocation(), new Obstacle(this));
    }
}