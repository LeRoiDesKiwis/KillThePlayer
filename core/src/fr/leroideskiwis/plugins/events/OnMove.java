package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Position;

public class OnMove extends Event {

    private Position oldPosition;
    private Position newPosition;

    public OnMove(Position oldPosition, Position newPosition) {
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }

    public Position getOldPosition() {
        return oldPosition;
    }

    public Position getNewPosition() {
        return newPosition;
    }
}
