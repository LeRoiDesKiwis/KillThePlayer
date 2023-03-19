package fr.leroideskiwis.mapgame;

import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Obstacle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    private Map map;

    @BeforeEach
    void setUp() {
        this.map = new Map(10, 10);
    }

    @Test
    void getEntity() {
        assertFalse(map.getEntity(1, 1).isPresent(), "getEntity location empty");
        assertTrue(map.getEntity(0, 0).isPresent(), "getEntity obstacle present");
        assertInstanceOf(Obstacle.class, map.getEntity(0, 0).get(), "0, 0 instanceof Obstace");
    }

    @Test
    void testSetEntity() {
        Obstacle obstacle = new Obstacle(1, 1);
        assertTrue(map.setEntity(1, 1, obstacle), "setEntity 1,1 : true");
        assertFalse(map.setEntity(1, 1, new Obstacle(1, 1)), "setEntity replace forbidden");
        assertTrue(map.getEntity(1, 1).isPresent(), "setEntity isPresent");
    }

    @Test
    void testDeleteEntity() {
        Enemy enemy = new Enemy();
        map.setEntity(1, 1, enemy);
        map.deleteEntity(enemy);
        assertFalse(map.getEntity(1, 1).isPresent(), "deleteEntity : not present");
    }

    @Test
    void testreplaceEntity() {
        Enemy enemy = new Enemy();
        Enemy enemy1 = new Enemy();
        map.setEntity(1, 1, enemy);
        map.replaceEntity(1, 1, enemy1);
        assertTrue(map.getEntity(1, 1).isPresent(), "replaceEntity: isPresent");
        assertSame(map.getEntity(1, 1).get(), enemy1, "replaceEntity: replaced");
    }

    @Test
    void generateRandom() {
        Obstacle obstacle = new Obstacle();
        map.generateRandom(obstacle);

        List<Obstacle> obstacles = map.getEntitiesByType(Obstacle.class);
        assertTrue(obstacles.contains(obstacle));
    }

    @Test
    void getRandomLocationWithSize() {
    }

    @Test
    void getEmptyCases() {
        List<Location> emptyCases = map.getEmptyCases();
        assertFalse(emptyCases.stream().anyMatch(location -> map.getEntity(location.x, location.y).isPresent()), "getEmptyCases : anyMatch");
    }

    @Test
    void isEmpty() {
        assertFalse(map.getEntity(1, 1).isPresent(), "isEmpty : 1, 1 not present");
    }

    @Test
    void getLocationsByType() {
        List<Location> locations = map.getLocationsByType(Obstacle.class);
        assertTrue(locations.contains(new Location(0, 0)), "getLocationsByType : 0, 0");
        assertTrue(locations.contains(new Location(9, 9)), "getLocationsByType : 9, 9");
        assertFalse(locations.contains(new Location(1, 5)), "getLocationsByType : 1, 5");
    }

    @Test
    void getEntitiesByType() {
        List<Obstacle> obstacles = map.getEntitiesByType(Obstacle.class);
        assertTrue(obstacles.get(0).isLocatedAt(0, 0), "getLocationsByType : 0, 0");
    }

    @Test
    void locationsToEntities() {
        List<Location> locations = Arrays.asList(new Location(0, 0), new Location(1, 1));
        List<Entity> entities = map.locationsToEntities(locations);
        assertEquals(entities.size(), 1, "locationsToEntities : size = 1");
        assertInstanceOf(Obstacle.class, entities.get(0), "locationsToEntities : instanceof");
    }

    @Test
    void hasFullSurrounding() {

        Enemy enemy = new Enemy();

        map.setEntity(4, 4, enemy);

        for (int x = 3; x <= 5; x++) {
            for (int y = 3; y <= 5; y++) {
                if (x == 4 && y == 4) {
                    continue;
                }
                map.setEntity(x, y, new Enemy());
            }
        }

        assertTrue(map.hasFullSurrounding(enemy));

        map.deleteEntity(new Location(3, 3));
        assertFalse(map.hasFullSurrounding(enemy));
    }
}