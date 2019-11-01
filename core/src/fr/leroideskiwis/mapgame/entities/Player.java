package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Invincibility;
import fr.leroideskiwis.mapgame.Location;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.plugins.events.OnMove;
import fr.leroideskiwis.utils.Direction;

import java.util.Optional;

public class Player extends Entity {

    private Map map;
    private Game game;
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

    public boolean move(Location location){
        return setPosition(location.x, location.y);
    }

    /**
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return <b>false</b> if there already an object in the coordinate x and y
     */
    private boolean setPosition(int x, int y){
        Location before = getFirstLocation();
        Optional<Entity> entityOpt = map.getEntity(x, y);

        if(entityOpt.isPresent()) {
            Entity entity = entityOpt.get();
            if(entity.onCollide(game, map, this)) map.deleteEntity(entity);
        }

        if(invincibility.isInvincible()) {
            if(!entityOpt.isPresent() || !entityOpt.get().isInvulnerable()) {
                map.replaceEntity(x, y, this);
                invincibility.removeOne();
                invincibility.display(game);
            }
        } else if(map.setEntity(x, y, this)){
            Direction direction = Direction.getDirection(x-before.x, y-before.y);
            OnMove event = new OnMove(direction,before, new Location(x,y));
            game.getPluginManager().callEvent(event);
            if(event.isCancelled()) return false;

        } else return false;

        if(!getFirstLocation().equals(before)) {
            map.deleteEntity(before);
            return true;
        }

        return false;
    }

    /**
     *
     * @param x Add x to the position
     * @param y Add y to the position
     * @return <b>false</b> if there already an object in the coordinate x and y
     */

    public boolean move(int x, int y){
        if(x == 0 && y == 0) return true;
        Location location = getFirstLocation();
        return setPosition(location.x+x, location.y+y);
    }

    /**
     *
     * @return true if the player has lose
     */

    public boolean hasLose(){

        return (!map.isEmpty(getFirstLocation().add(1, 0)) &&
                !map.isEmpty(getFirstLocation().add(0, 1)) &&
                !map.isEmpty(getFirstLocation().add(-1, 0)) &&
                !map.isEmpty(getFirstLocation().add(0, -1)));

    }

}
