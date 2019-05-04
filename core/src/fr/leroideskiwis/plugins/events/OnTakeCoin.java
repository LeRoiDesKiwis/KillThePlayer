package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.entities.Coin;
import fr.leroideskiwis.mapgame.Position;

public class OnTakeCoin extends Event {

    private Position position;
    private Coin coin;

    public OnTakeCoin(Position position, Coin coin) {
        this.position = position;
        this.coin = coin;
    }

    public Position getPosition() {
        return position;
    }

    public Coin getCoin() {
        return coin;
    }
}
