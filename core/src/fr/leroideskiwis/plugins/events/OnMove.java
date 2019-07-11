package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Location;

public class OnMove extends Event {

    private Location oldLocation;
    private Location newLocation;

    public OnMove(Location oldLocation, Location newLocation) {
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }

    public Location getOldLocation() {
        return oldLocation;
    }

    public Location getNewLocation() {
        return newLocation;
    }
}
