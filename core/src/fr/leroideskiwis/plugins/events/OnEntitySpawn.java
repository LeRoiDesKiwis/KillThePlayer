package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Position;

public class OnEntitySpawn extends Event {

    private Entity entity;
    private Position position;

    public OnEntitySpawn(Entity entity, Position position) {
        this.entity = entity;
        this.position = position;
    }

    public Entity getEntity() {
        return entity;
    }

    public Position getPosition() {
        return position;
    }
}
