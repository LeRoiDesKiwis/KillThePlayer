package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

public class OnObjectDeath extends Event {

    private Location location;
    private SpecialObj object;

    public OnObjectDeath(Location location, SpecialObj object) {
        this.location = location;
        this.object = object;
    }

    public Location getLocation() {
        return location;
    }

    public SpecialObj getObject() {
        return object;
    }
}
