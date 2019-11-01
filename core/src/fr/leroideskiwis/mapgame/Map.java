package fr.leroideskiwis.mapgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sun.istack.internal.NotNull;
import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Obstacle;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.managers.TextureManager;
import fr.leroideskiwis.plugins.events.OnEnemyDeath;
import fr.leroideskiwis.plugins.events.OnEntitySpawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Map implements Cloneable{

    private final List<Entity> entities = new ArrayList<>();
    private final Game game;
    private int height;
    private int width;

    public Map(Game main, int height, int width){
        this.game = main;
        this.height = height;
        this.width = width;

        for(int x = 0; x < width; x++){
            entities.add(new Obstacle().setLocation(x, 0));
            entities.add(new Obstacle().setLocation(x, height-1));
        }

        for(int y = 0; y < height; y++){
            entities.add(new Obstacle().setLocation(0, y));
            entities.add(new Obstacle().setLocation(width-1, y));
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Optional<Entity> getEntity(int x, int y){
        return entities.stream().filter(entity -> entity.isLocatedAt(x, y)).findAny();
    }

    private Optional<Entity> getEntity(Location location){
        return getEntity(location.x, location.y);
    }

    private List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){

                locations.add(new Location(x, y));

            }
        }

        return locations;

    }

    /**
     * Use this to set an object in the coordinates x y
     * <b>WARNING : this method don't replace the object, then it don't go to set the object if there already an object in. If you want to replace, use {@link #replaceEntity}</b>
     *
     * @param pos the coordinates
     * @param entity the object to replace in x y.
     * @return false if there are already an object in x y
     * @see Map#replaceEntity(int, int, Entity)
     */
    public boolean setEntity(Location pos, Entity entity){
        if(!getEntity(pos.x, pos.y).isPresent()) {
            OnEntitySpawn event = new OnEntitySpawn(entity, pos);
            game.getPluginManager().callEvent(event);
            if(event.isCancelled()) return false;
            entity.setLocation(pos);
            if(!entities.contains(entity)) entities.add(entity);
        }
        else return false;
        return true;
    }

    public boolean setEntity(int x, int y, Entity o){
        return setEntity(new Location(x, y), o);
    }

    public void deleteEntity(Location location){
        deleteEntity(location.x, location.y);
    }

    public void deleteEntity(Entity entity){
        entity.delete(this);
    }

    /**
     * Replace the object in x y by null
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    private void deleteEntity(int x, int y) {
        getEntity(x, y).ifPresent(entity -> {
            if(!(entity instanceof Player))
                entities.remove(entity);
        });
    }

    /**
     *
     * Replace the object in x y by the object <b>newObject</b> in parameters
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param newObject the object to replace with <b>don't accept null value ! Please with {@link #deleteEntity(int, int)} to delete an entity !</b>
     */

    public void replaceEntity(int x, int y, @NotNull Entity newObject){

        Optional<Entity> optionalEntity = getEntity(x, y);

        if(optionalEntity.isPresent()){
            Entity entity = optionalEntity.get();

            if(entity instanceof Enemy){
                OnEnemyDeath event = new OnEnemyDeath(new Location(x, y), (Enemy) entity);
                game.getPluginManager().callEvent(event);
                if(event.isCancelled()) return;
            }
            deleteEntity(x, y);
        }

        if(!entities.contains(newObject)) entities.add(newObject);
        newObject.setLocation(x, y);

    }

    public void replaceEntity(Location locationByObject, Entity newObject) {
        replaceEntity(locationByObject.x, locationByObject.y, newObject);
    }

    /**
     * Place an object randomly in the map
     * @param entity The object to generate
     */

    public void generateRandom(Entity entity){

        setEntity(getRandomLocationWithSize(entity.size()), entity);

    }

    public Location getRandomLocationWithSize(int size){
        return game.getRandomList(getEmptyCases().stream()
                .filter(location -> {
                    for(int x = 0; x < size; x++){

                        for(int y = 0; y < size; y++){

                            if(!isEmpty(new Location(location.x + x, location.y + y))) return false;
                        }

                    }
                    return true;
                }).collect(Collectors.toList())).orElse(new Location(1, 1));
    }

    public List<Location> getEmptyCases(){
        return getLocations().stream().filter(this::isEmpty).collect(Collectors.toList());
    }

    public List<Entity> getEntities(){
        return new ArrayList<>(entities);
    }

    public boolean isEmpty(Location location){
        return !getEntity(location).isPresent();
    }

    /**
     * Get the locations of a specific type
     *
     * @param clazz the class to locate
     * @return the locations of all objects who is type clazz
     */

    public List<Location> getLocationsByType(Class<? extends Entity> clazz){
        return getEntitiesByType(clazz).stream().map(Entity::getLocation).collect(Collectors.toList());
    }

    public <T> List<T> getEntitiesByType(Class<T> clazz) {

        return entities.stream().filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toList());

    }

    private Optional<Entity> getEntityWithoutSize(int x, int y){
        return entities.stream()
                .filter(entity -> entity.getLocation().equals(x, y)).findAny();
    }

    public void draw(TextureManager manager, SpriteBatch batch, float multiplicatorX, float multiplicatorY, Texture emptyCase) {

        for(Location location : getLocations()){
            Optional<Entity> entity = getEntityWithoutSize(location.x, location.y);

            Rectangle rectangle = new Rectangle(location.x*multiplicatorX+1, location.y*multiplicatorY+1, multiplicatorX, multiplicatorY);

            if(entity.isPresent()) {
                entity.get().draw(manager, batch, rectangle);
            }
            else if(isEmpty(location)) batch.draw(emptyCase, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }

    private List<Entity> getSurrounding(Entity entity){
        return getLocationsSurrounding(entity).stream().map(location -> getEntity(location).orElse(null)).collect(Collectors.toList());
    }

    /**
     * @deprecated Use Entity#getSurroundingLocations
     * @param entity
     * @return
     */
    private List<Location> getLocationsSurrounding(Entity entity) {
        return entity.getSurroundingLocations();
    }

    public boolean hasFullSurrounding(Entity entity){
        return getSurrounding(entity).stream().noneMatch(Objects::isNull);
    }

    /*public <T> boolean hasFullSurrounding(Entity entity, Class<T> clazz){

        List<T> all = getEntitiesByType(clazz);

        return getSurrounding(entity).stream().allMatch(entity1 -> entity1 != null && all.contains(entity1));

    }*/
}
