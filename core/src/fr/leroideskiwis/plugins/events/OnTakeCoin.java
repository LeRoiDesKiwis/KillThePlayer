package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.entities.Coin;

public class OnTakeCoin extends Event {

    private Location location;
    private Coin coin;

    public OnTakeCoin(Location location, Coin coin) {
        this.location = location;
        this.coin = coin;
    }

    public Location getLocation() {
        return location;
    }

    public Coin getCoin() {
        return coin;
    }
}
