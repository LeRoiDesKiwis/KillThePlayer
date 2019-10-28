package fr.leroideskiwis.mapgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.managers.TextureManager;
import fr.leroideskiwis.mapgame.specialobjects.InvinciblePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Entity {

    private List<Location> locations;
    private String path;

    public Entity(String path){
        this(0, 0, path);
    }

    public Entity(int x, int y, String path) {
        this.path = path;
        this.locations = new ArrayList<>();
        setLocation(x, y);
    }

    public Entity setLocation(int x, int y){
        if(getClass() == InvinciblePlayer.class){
            System.out.println("t");
        }

        locations.clear();

        for(int x1 = 0; x1 < size(); x1++){

            for(int y1 = 0; y1 < size(); y1++){

                Location currentLocation = new Location(x + x1, y + y1);
                locations.add(currentLocation);

            }

        }
        return this;
    }

    public void delete(Map map){
        Location location = locations.get(0);
        map.deleteEntity(location);
    }

    public Entity setLocation(Location location) {
        return setLocation(location.x, location.y);
    }

    public boolean isInvulnerable(){
        return false;
    }

    public boolean onCollide(Game game, Map map, Player player){
        return false;
    }

    public void draw(TextureManager manager, SpriteBatch batch, Rectangle rectangle) {
        Texture texture = texture(manager);
        batch.draw(texture, rectangle.x, rectangle.y, rectangle.width*size(), rectangle.height*size());
    }

    private Texture texture(TextureManager manager) {
        if(manager.has(this)){
            return manager.getTexture(this);
        } else {
            manager.register(this, path);
            return texture(manager);
        }
    }

    public int size(){
        return 1;
    }

    public boolean isLocatedAt(int x, int y) {
        return locations.stream()
                .anyMatch(location -> location.equals(x, y));
    }

    public List<Location> getSurroundingLocations(){

        List<Location> surroundingLocations = new ArrayList<>();
        Location location = locations.get(0);

        for(int x = location.x - 1; x <= location.x+1; x++){
            for(int y = location.y - 1; y <= location.y+1; y++){
                Location currentLocation = new Location(x, y);
                surroundingLocations.add(currentLocation);
            }
        }

        return surroundingLocations;
    }

    public List<Location> getLocations(){
        return locations;
    }

    public List<Location> getSurroundingWithoutCorners(){
        Location location = locations.get(0);
        return getSurroundingLocations().stream().filter(location1 -> Math.abs(location1.x-location.x) != Math.abs(location.x-location1.y)).collect(Collectors.toList());
    }

    public Location getFirstLocation(){
        return locations.get(0);
    }
}
