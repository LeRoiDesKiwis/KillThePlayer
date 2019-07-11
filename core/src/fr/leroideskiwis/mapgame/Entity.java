package fr.leroideskiwis.mapgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import fr.leroideskiwis.ktp.Main;

public class Entity {

    private Texture texture;
    private Location location;

    public Entity(Location location, String path){
        this(location.getX(), location.getY(), path);
    }

    public Entity(String path){
        this(0, 0, path);
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

    public Entity(int x, int y, String path) {
        this.texture = Main.getTexture(path+".png");
        this.location = new Location(x,y);
    }

    public Texture texture(){
        return texture == null ? new Texture(Gdx.files.internal("defaultobj.png")) : texture;
    }

    public Location getLocation(){
        return location;
    }

    public Entity setLocation(int x, int y) {
        return setLocation(new Location(x, y));
    }
}
