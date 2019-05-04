package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Position;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

public class OnObjectDeath extends Event {

    private Position position;
    private SpecialObj object;

    public OnObjectDeath(Position position, SpecialObj object) {
        this.position = position;
        this.object = object;
    }

    public Position getPosition() {
        return position;
    }

    public SpecialObj getObject() {
        return object;
    }
}
