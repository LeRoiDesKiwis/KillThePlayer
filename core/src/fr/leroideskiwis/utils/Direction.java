package fr.leroideskiwis.utils;

import fr.leroideskiwis.mapgame.Location;

import java.util.Arrays;

public enum Direction {

    LEFT(-1, 0), RIGHT(1, 0), TOP(0, 1), BOTTOM(0, -1), UNKNOWN(0, 0);

    private int x, y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Direction getDirection(int directionX, int directionY){
        return Arrays.stream(values())
                .filter(direction -> direction.x == directionX && direction.y == directionY)
                .findAny()
                .orElse(Direction.UNKNOWN);
    }
}
