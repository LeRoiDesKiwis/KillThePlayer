package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.ktp.ExecutionData;
import fr.leroideskiwis.mapgame.Entity;
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
    private static Random random = new Random();
    public static TextureManager<String> textureManager = new TextureManager<>();

    public static List<Supplier<SpecialObject>> ALL = new ArrayList<>(Arrays.asList(
            CLEAR_ENNEMIES = () -> new SpecialObject("clearennemies", 0.004f, SpecialObjects::clearEnnemies, data -> data.getGame().getLocationNearEnemy(), null, null),
            HORIZONTAL_OPEN_PATH = () -> new SpecialObject("openpathH", 0.123f, SpecialObjects::horizontalOpenPath, data -> data.getGame().getLocationNearEnemy(), null, null),
            INVINCIBLE_PLAYER = () -> new SpecialObject("invincible", 0.06f, SpecialObjects::invincible, data -> data.getGame().getLocationNearEnemy(), null, null),
            RAYON_ENNEMY_KILLER = () -> new SpecialObject("rayonkiller", 0.69f, SpecialObjects::rayonEnnemyKiller, data -> data.getGame().getLocationNearEnemy(), null, null),
            REPARATOR = () -> new SpecialObject("reparator", 0.06f, SpecialObjects::reparator, null, null, data -> data.getGame().getMap().getEntitiesByType(Obstacle.class).stream().noneMatch(Obstacle::wasObject)),
            RESPAWN = () -> new SpecialObject("respawn", 0.06f, (data, specialObject) -> data.getMap().getEntitiesByType(SpecialObject.class).forEach(specialObj -> specialObj.setLocation(specialObj.spawn(data))), null, null, null),
            TRIGGER_ALL = () -> new SpecialObject("trigger", 0.004f, SpecialObjects::trigger),
            VERTICAL_OPEN_PATH = () -> new SpecialObject("openpathV", 0.123f, SpecialObjects::verticalOpenPath, data -> data.getGame().getLocationNearEnemy(), null, null)
    ));

    private static void clearEnnemies(ExecutionData executionData, SpecialObject specialObject){
        executionData.getMap().getEntities()
                .stream()
                .filter(entity -> entity instanceof Enemy)
                .forEach(executionData.getMap()::deleteEntity);

        executionData.getGame().sendMessage(Utils.getText("objects.clearennemies.cleared"));
    }

    private static void horizontalOpenPath(ExecutionData executionData, SpecialObject specialObject){
        int rayon = executionData.getGame().randomInt(3, 4);

        executionData.getGame().getEntities()
                .stream()
                .filter(entity -> entity instanceof Enemy && Interval.of(specialObject.getLocation().y-rayon, specialObject.getLocation().y+rayon).contains(entity.getLocation().y))
                .forEach(executionData.getMap()::deleteEntity);
    }

    private static void verticalOpenPath(ExecutionData executionData, SpecialObject specialObject) {
        int rayon = executionData.getGame().randomInt(3, 4);

        executionData.getMap().getEntities()
                .stream()
                .filter(entity -> entity instanceof Enemy && Interval.of(specialObject.getLocation().x - rayon, specialObject.getLocation().x + rayon).contains(entity.getLocation().x))
                .forEach(executionData.getMap()::deleteEntity);
    }

    private static void invincible(ExecutionData executionData, SpecialObject specialObject){
        int invincibleTour = executionData.getGame().randomInt(5, 6);
        executionData.getPlayer().addInvincility(invincibleTour);
        executionData.getGame().sendMessage(Utils.format("objects.invincible.got", invincibleTour));
    }

    private static void rayonEnnemyKiller(ExecutionData executionData, SpecialObject specialObject){
        Location location = specialObject.getLocation();

        int rayon = executionData.getGame().randomInt(3, 4);

        executionData.getGame().sendMessage(Utils.format("objects.rayonennemykiller.killed", rayon));
        int minX = location.x-rayon;
        int maxX = location.x+rayon;

        int minY = location.y-rayon;
        int maxY = location.y+rayon;

        executionData.getGame().getEntities()
                .stream()
                .filter(entity -> entity instanceof Enemy && Interval.of(minX, maxX).contains(entity.getLocation().x) && Interval.of(minY, maxY).contains(entity.getLocation().y)).forEach(entity -> executionData.getMap().deleteEntity(entity));
    }

    private static void reparator(ExecutionData executionData, SpecialObject specialObject){
        List<Obstacle> lostObstacle = executionData.getMap().getEntitiesByType(Obstacle.class).stream().filter(Obstacle::wasObject).collect(Collectors.toList());

        if(lostObstacle.isEmpty()) return;

        for(int i = 0, rand = executionData.getGame().randomInt(1, 2); i < rand; i++) {

            Obstacle obstacle = lostObstacle.get(0);
            executionData.getMap().replaceEntity(obstacle.getLocation(), obstacle.getLostObject());
            executionData.getGame().sendMessage(Utils.format("objects.reparator.restore", obstacle.getLostObject().getName()));
            i++;

        }
    }

    private static void trigger(ExecutionData executionData, SpecialObject specialObject){
        executionData.getGame().sendMessage(Utils.getText("objects.trigger.message"));
        executionData.getMap().getEntitiesByType(SpecialObject.class).stream().filter(entity -> !entity.isName("clearennemies") && !entity.isName("trigger") && !entity.isName("respawn")).forEach(entity -> entity.onCollide(executionData));
    }

    public static Supplier<SpecialObject> randomObject(){
        return ALL.get(random.nextInt(ALL.size()));
    }

}
