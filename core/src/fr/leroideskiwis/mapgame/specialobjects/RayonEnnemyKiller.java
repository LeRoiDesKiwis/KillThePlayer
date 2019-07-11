package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;
import fr.leroideskiwis.utils.Interval;

public class RayonEnnemyKiller extends SpecialObj {
    public RayonEnnemyKiller(Game game) {
        super(game, "rayonkiller");
    }

    @Override
    public void execute(Game game, Map map, Player player) {
        Location location = getLocation();

        int rayon = game.randomInt(3, 4);

        game.sendMessage("All ennemies in a radius of " + rayon + " has been killed");
        int minX = location.getX()-rayon;
        int maxX = location.getX()+rayon;

        int minY = location.getY()-rayon;
        int maxY = location.getY()+rayon;

        game.getEntities()
                .stream()
                .filter(entity -> entity instanceof Enemy && Interval.of(minX, maxX).contains(entity.getX()) && Interval.of(minY, maxY).contains(entity.getY())).forEach(entity -> game.getMap().deleteEntity(entity));
    }


    @Override
    public String name() {
        return "ennemy rayon killer";
    }

    @Override
    public double chance() {
        return 0.806;
    }

    /*
       tableau de boolean

       isEmpty   surroundingContains   continue
       true     true                  true
       false    true                  true
       true     false                 true
       false    false                 false
     */

    public Location spawn(Game game, Map map, Player player) {
        return game.getLocationNearEnemy();
    }
}