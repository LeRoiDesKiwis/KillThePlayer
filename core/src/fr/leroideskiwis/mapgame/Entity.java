package fr.leroideskiwis.mapgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import fr.leroideskiwis.ktp.Main;
import fr.leroideskiwis.mapgame.managers.TextureManager;

public class Entity {

    private Location location;
    private String path;


    public Entity(Location location, String path){
        this(location.getX(), location.getY(), path);
    }

    public Entity(String path){
        this(0, 0, path);
    }

    public Entity(int x, int y, String path) {
        this.path = path;
        this.location = new Location(x,y);
    }

    public Entity setLocation(Location location){
        this.location = location;
        return this;
    }

    public int getX(){
        return location.getX();
    }

    public int getY(){
        return location.getY();
    }

    public Texture texture(TextureManager manager){

        if(manager.has(this))
            return manager.getTexture(this);
        else {
            manager.register(this, manager.getTexture(path));
            return texture(manager);
        }
    }

    public Location getLocation(){
        return location;
    }

    public Entity setLocation(int x, int y) {
        return setLocation(new Location(x, y));
    }
}
