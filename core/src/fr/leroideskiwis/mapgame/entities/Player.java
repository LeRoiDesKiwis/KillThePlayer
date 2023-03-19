package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.ktp.ExecutionData;
import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Invincibility;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;

import java.util.Optional;

public class Player extends Entity {

    private final Map map;
    private final Game game;
    private final Invincibility invincibility;

    public Player(Game game, Map map){
        super("player.png");
        this.map = map;
        this.game = game;
        this.invincibility = new Invincibility();
    }

    public void addInvincility(int invincibleTour){
        invincibility.addInvincility(invincibleTour);
    }

    /**
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return <b>false</b> if there already an object in the coordinate x and y
     */
    private boolean moveTo(int x, int y){
        Optional<Entity> entityOpt = map.getEntity(x, y);

        entityOpt.filter(entity -> entity.onCollide(new ExecutionData(this, map, game))).ifPresent(map::deleteEntity);

        if(invincibility.isInvincible()) {
            entityOpt.filter(Entity::isRemovable).ifPresent(entity -> {
                map.replaceEntity(x, y, this);
                invincibility.removeOne();
                invincibility.display(game);
            });
        } else return map.setEntity(x, y, this);

        return true;
    }

    /**
     *
     * @param x Add x to the position
     * @param y Add y to the position
     * @return <b>false</b> if there already an object in the coordinate x and y
     */

    public boolean move(int x, int y){
        if(x == 0 && y == 0) return true;
        Location location = getLocation();
        return moveTo(location.x+x, location.y+y);
    }

    /**
     *
     * @return true if the player has lose
     */

    public boolean hasLose(){

        return (!map.isEmpty(getLocation().add(1, 0)) &&
                !map.isEmpty(getLocation().add(0, 1)) &&
                !map.isEmpty(getLocation().add(-1, 0)) &&
                !map.isEmpty(getLocation().add(0, -1)));

    }

    @Override
    public boolean isRemovable() {
        return false;
    }
}
