package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.entities.Enemy;

public class OnEnemyDeath extends Event {

    private Location location;
    private Enemy enemy;

    public OnEnemyDeath(Location location, Enemy enemy) {
        this.location = location;
        this.enemy = enemy;
    }

    public Location getLocation() {
        return location;
    }

    public Enemy getEnemy() {
        return enemy;
    }

}
