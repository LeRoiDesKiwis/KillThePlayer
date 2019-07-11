package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

public class OnObjectSpawn extends Event {

    private Location location;
    private SpecialObj specialObj;

    public OnObjectSpawn(Location location, SpecialObj specialObj) {
        this.location = location;
        this.specialObj = specialObj;
    }

    public SpecialObj getSpecialObj() {
        return specialObj;
    }

    public void setSpecialObj(SpecialObj specialObj) {
        this.specialObj = specialObj;
    }

    public Location getLocation() {
        return location;
    }
}
