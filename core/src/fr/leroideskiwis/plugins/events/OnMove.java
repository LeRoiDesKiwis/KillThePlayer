package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.utils.Direction;

public class OnMove extends Event {

    public final Location oldLocation;
    public final Location newLocation;
    public final Direction direction;

    public OnMove(Direction direction, Location oldLocation, Location newLocation) {
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
        this.direction = direction;
    }
}
