package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.Position;

public class OnEnnemyDeath extends Event {

    private Position location;
    private Enemy enemy;

    public OnEnnemyDeath(Position location, Enemy enemy) {
        this.location = location;
        this.enemy = enemy;
    }

    public Position getLocation() {
        return location;
    }

    public Enemy getEnemy() {
        return enemy;
    }

}
