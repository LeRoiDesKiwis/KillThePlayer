package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.ktp.ExecutionData;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Obstacle;
import fr.leroideskiwis.mapgame.managers.TextureManager;
import fr.leroideskiwis.utils.Interval;
import fr.leroideskiwis.utils.Utils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SpecialObjects {

    public static Supplier<SpecialObject> CLEAR_ENNEMIES, HORIZONTAL_OPEN_PATH, INVINCIBLE_PLAYER, RAYON_ENNEMY_KILLER,
    REPARATOR, RESPAWN, TRIGGER_ALL, VERTICAL_OPEN_PATH;
    public static TextureManager<String> textureManager = new TextureManager<>();

    //ALL but with builders
    public static List<Supplier<SpecialObject>> ALL = new ArrayList<>(Arrays.asList(
            CLEAR_ENNEMIES = () -> new SpecialObject.SpecialObjectBuilder()
                    .setName("clearennemies")
                    .setChance(0.001f)
                    .setExecute(SpecialObjects::clearEnnemies)
                    .build(),
            HORIZONTAL_OPEN_PATH = () -> new SpecialObject.SpecialObjectBuilder()
                    .setName("openpathH")
                    .setChance(0.2f)
                    .setExecute(SpecialObjects::horizontalOpenPath)
                    .build(),
            INVINCIBLE_PLAYER = () -> new SpecialObject.SpecialObjectBuilder()
                    .setName("invincible")
                    .setChance(0.06f)
                    .setExecute(SpecialObjects::invincible)
                    .build(),
            RAYON_ENNEMY_KILLER = () -> new SpecialObject.SpecialObjectBuilder()
                    .setName("rayonkiller")
                    .setChance(0.5f)
                    .setExecute(SpecialObjects::rayonEnnemyKiller)
                    .build(),
            REPARATOR = () -> new SpecialObject.SpecialObjectBuilder()
                    .setName("reparator")
                    .setChance(0.06f)
                    .setExecute(SpecialObjects::reparator)
                    .setCanSpawn(data -> data.map.getEntitiesByType(Obstacle.class).stream().anyMatch(Obstacle::wasObject))
                    .build(),
            RESPAWN = () -> new SpecialObject.SpecialObjectBuilder()
                    .setName("respawn")
                    .setChance(0.06f)
                    .setExecute((data, specialObject) -> data.map.getEntitiesByType(SpecialObject.class).forEach(specialObj -> specialObj.setLocation(specialObj.spawn(data))))
                    .build(),
            TRIGGER_ALL = () -> new SpecialObject.SpecialObjectBuilder()
                    .setName("trigger")
                    .setChance(0.004f)
                    .setExecute(SpecialObjects::trigger)
                    .build(),
            VERTICAL_OPEN_PATH = () -> new SpecialObject.SpecialObjectBuilder()
                    .setName("openpathV")
                    .setChance(0.1f)
                    .setExecute(SpecialObjects::verticalOpenPath)
                    .build()
    ));

    private static void clearEnnemies(ExecutionData executionData, SpecialObject specialObject){
        executionData.map.streamEntities()
                .filter(entity -> entity instanceof Enemy)
                .forEach(executionData.map::deleteEntity);

        executionData.game.sendMessage(Utils.getText("objects.clearennemies.cleared"));
    }

    private static void horizontalOpenPath(ExecutionData executionData, SpecialObject specialObject){
        int rayon = executionData.game.randomInt(3, 4);

        executionData.map.streamEntities()
                .filter(entity -> entity instanceof Enemy && Interval.of(specialObject.getLocation().y-rayon, specialObject.getLocation().y+rayon).contains(entity.getLocation().y))
                .forEach(executionData.map::deleteEntity);
    }

    private static void verticalOpenPath(ExecutionData executionData, SpecialObject specialObject) {
        int rayon = executionData.game.randomInt(3, 4);

        executionData.map.streamEntities()
                .filter(entity -> entity instanceof Enemy && Interval.of(specialObject.getLocation().x - rayon, specialObject.getLocation().x + rayon).contains(entity.getLocation().x))
                .forEach(executionData.map::deleteEntity);
    }

    private static void invincible(ExecutionData executionData, SpecialObject specialObject){
        int invincibleTour = executionData.game.randomInt(5, 6);
        executionData.player.addInvincility(invincibleTour);
        executionData.game.sendMessage(Utils.format("objects.invincible.got", invincibleTour));
    }

    private static void rayonEnnemyKiller(ExecutionData executionData, SpecialObject specialObject){
        Location location = specialObject.getLocation();

        int rayon = executionData.game.randomInt(3, 4);

        executionData.game.sendMessage(Utils.format("objects.rayonennemykiller.killed", rayon));
        int minX = location.x-rayon;
        int maxX = location.x+rayon;

        int minY = location.y-rayon;
        int maxY = location.y+rayon;

        executionData.map.streamEntities()
                .filter(entity -> entity instanceof Enemy && Interval.of(minX, maxX).contains(entity.getLocation().x) && Interval.of(minY, maxY).contains(entity.getLocation().y)).forEach(executionData.map::deleteEntity);
    }

    private static void reparator(ExecutionData executionData, SpecialObject specialObject){
        List<Obstacle> lostObstacle = executionData.map.getEntitiesByType(Obstacle.class).stream().filter(Obstacle::wasObject).collect(Collectors.toList());

        if(lostObstacle.isEmpty()) return;

        for(int i = 0, rand = executionData.game.randomInt(1, 2); i < rand; i++) {

            Obstacle obstacle = lostObstacle.get(0);
            obstacle.revive(executionData);
            i++;

        }
    }

    private static void trigger(ExecutionData executionData, SpecialObject specialObject){
        executionData.game.sendMessage(Utils.getText("objects.trigger.message"));
        executionData.map.getEntitiesByType(SpecialObject.class).stream().filter(SpecialObject::isTriggerable).forEach(entity -> entity.onCollide(executionData));
    }

}
