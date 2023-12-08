package fr.leroideskiwis.mapgame.specialobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import fr.leroideskiwis.ktp.ExecutionData;
import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Obstacle;
import fr.leroideskiwis.mapgame.managers.TextureManager;
import fr.leroideskiwis.utils.Utils;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class SpecialObject extends Entity{

    private final BiConsumer<ExecutionData, SpecialObject> execute;
    private final Function<ExecutionData, Location> spawn;
    private final Predicate<ExecutionData> onCollide = data -> {
        execute(data, this);
        return true;
    };;
    private final Predicate<ExecutionData> canSpawn;
    private final String name;
    private final float chance;
    private final boolean triggerable;

    protected void execute(ExecutionData executionData, SpecialObject specialObject){
        execute.accept(executionData, specialObject);
    }

    public SpecialObject(String name, float chance, BiConsumer<ExecutionData, SpecialObject> execute, Function<ExecutionData, Location> spawn, Predicate<ExecutionData> canSpawn, boolean triggerable) {
        super(name+".png");
        this.name = Utils.getText("objects."+name+".name");
        this.chance = chance;
        this.execute = execute;
        this.spawn = spawn;
        this.canSpawn = canSpawn;
        this.triggerable = triggerable;
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

    public float getChance() {
        return chance;
    }

    public boolean isTriggerable(){
        return triggerable;
    }

    @Override
    public void draw(TextureManager<Entity> manager, SpriteBatch batch, Rectangle rectangle) {
        Texture texture = SpecialObjects.textureManager.registerIfAbsent(name, path);
        batch.draw(texture, rectangle.x, rectangle.y, rectangle.width*size(), rectangle.height*size());
    }

    public static class SpecialObjectBuilder{
        private BiConsumer<ExecutionData, SpecialObject> execute = (data, sp) -> {};
        private Function<ExecutionData, Location> spawn = data -> data.game.getLocationNearEnemy();
        private Predicate<ExecutionData> canSpawn = (data) -> true;
        private String name;
        private float chance;
        private boolean triggerable;

        public SpecialObjectBuilder setExecute(BiConsumer<ExecutionData, SpecialObject> execute) {
            this.execute = execute;
            return this;
        }

        public SpecialObjectBuilder setSpawn(Function<ExecutionData, Location> spawn) {
            this.spawn = spawn;
            return this;
        }

        public SpecialObjectBuilder setCanSpawn(Predicate<ExecutionData> canSpawn) {
            this.canSpawn = canSpawn;
            return this;
        }

        public SpecialObjectBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public SpecialObjectBuilder setChance(float chance) {
            this.chance = chance;
            return this;
        }

        public SpecialObjectBuilder setTriggerable(boolean triggerable) {
            this.triggerable = triggerable;
            return this;
        }

        public SpecialObject build() {
            return new SpecialObject(name, chance, execute, spawn, canSpawn, triggerable);
        }
    }
}