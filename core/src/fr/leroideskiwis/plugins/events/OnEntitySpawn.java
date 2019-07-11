package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Location;

public class OnEntitySpawn extends Event {

    private Entity entity;
    private Location location;

    public OnEntitySpawn(Entity entity, Location location) {
        this.entity = entity;
        this.location = location;
    }

    public Entity getEntity() {
        return entity;
    }

    public Location getLocation() {
        return location;
    }
}
