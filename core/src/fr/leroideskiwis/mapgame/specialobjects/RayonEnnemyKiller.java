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
        super(game, "rayonkiller.png");
    }

    @Override
    public void execute(Game game, Map map, Player player) {
        Location location = getLocation();

        int rayon = game.randomInt(3, 4);

        game.sendMessage("All ennemies in a radius of " + rayon + " has been killed");
        int minX = location.x-rayon;
        int maxX = location.x+rayon;

        int minY = location.y-rayon;
        int maxY = location.y+rayon;

        game.getEntities()
                .stream()
                .filter(entity -> entity instanceof Enemy && Interval.of(minX, maxX).contains(entity.getLocation().x) && Interval.of(minY, maxY).contains(entity.getLocation().y)).forEach(entity -> game.getMap().deleteEntity(entity));
    }


    @Override
    public String name() {
        return "ennemy rayon killer";
    }

    @Override
    public float chance() {
        return 0.69f;
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