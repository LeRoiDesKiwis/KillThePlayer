package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.specialobjects.SpecialObject;

public class OnObjectDeath extends Event {

    private Location location;
    private SpecialObject object;

    public OnObjectDeath(Location location, SpecialObject object) {
        this.location = location;
        this.object = object;
    }

    public Location getLocation() {
        return location;
    }

    public SpecialObject getObject() {
        return object;
    }
}
