package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.specialobjects.SpecialObject;

public class OnObjectSpawn extends Event {

    private Location location;
    private SpecialObject specialObject;

    public OnObjectSpawn(Location location, SpecialObject specialObject) {
        this.location = location;
        this.specialObject = specialObject;
    }

    public SpecialObject getSpecialObject() {
        return specialObject;
    }

    public void setSpecialObject(SpecialObject specialObject) {
        this.specialObject = specialObject;
    }

    public Location getLocation() {
        return location;
    }
}
