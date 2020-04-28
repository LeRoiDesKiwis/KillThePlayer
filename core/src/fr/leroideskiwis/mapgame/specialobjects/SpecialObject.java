package fr.leroideskiwis.mapgame.specialobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import fr.leroideskiwis.ktp.ExecutionData;
import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Obstacle;
import fr.leroideskiwis.mapgame.managers.TextureManager;
import fr.leroideskiwis.plugins.events.OnPlayerTakeObject;
import fr.leroideskiwis.utils.Utils;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class SpecialObject extends Entity{

    private BiConsumer<ExecutionData, SpecialObject> execute = (data, sp) -> {};
    private Function<ExecutionData, Location> spawn = data -> data.getMap().getRandomLocationWithSize(size());
    private Predicate<ExecutionData> onCollide;
    private Predicate<ExecutionData> canSpawn = (data) -> true;
    private String name;
    private float chance;

    protected void execute(ExecutionData executionData, SpecialObject specialObject){
        execute.accept(executionData, specialObject);
    }

    public SpecialObject(String name, float chance, BiConsumer<ExecutionData, SpecialObject> execute, Function<ExecutionData, Location> spawn,
                         Predicate<ExecutionData> onCollide, Predicate<ExecutionData> canSpawn) {
        super(name+".png");
        this.name = Utils.getText("objects."+name+".name");
        this.chance = chance;

        this.onCollide = data -> {
            OnPlayerTakeObject event = new OnPlayerTakeObject(getLocation(), this);
            data.getGame().getPluginManager().callEvent(event);
            if(event.isCancelled()) return false;
            data.getGame().sendMessage(Utils.format("objects.found", event.getSpecialObject().name));
            event.getSpecialObject().execute(data, this);
            return true;
        };
        this.execute = execute != null ? execute : this.execute;
        this.onCollide = onCollide != null ? onCollide : this.onCollide;
        this.spawn = spawn != null ? spawn : this.spawn;
        this.canSpawn = canSpawn != null ? canSpawn : this.canSpawn;
    }

    public SpecialObject(String name, float chance, BiConsumer<ExecutionData, SpecialObject> execute){
        this(name, chance, execute, null, null, null);
    }

    public String toString(){
        return name;
    }

    public Location spawn(ExecutionData executionData){
        return spawn.apply(executionData);
    }

    public void kill(Map map){
        delete(map);
        map.setEntity(getLocation(), new Obstacle(this));
    }

    @Override
    public boolean onCollide(ExecutionData executionData) {
        return onCollide.test(executionData);
    }

    public boolean canSpawn(ExecutionData executionData){
        return canSpawn.test(executionData);
    }

    public boolean isName(String name){
        return this.name.equals(name);
    }

    public String getName() {
        return name;
    }

    public float getChance() {
        return chance;
    }

    @Override
    public void draw(TextureManager<Entity> manager, SpriteBatch batch, Rectangle rectangle) {
        Texture texture = SpecialObjects.textureManager.registerIfAbsent(name, path);
        batch.draw(texture, rectangle.x, rectangle.y, rectangle.width*size(), rectangle.height*size());
    }
}