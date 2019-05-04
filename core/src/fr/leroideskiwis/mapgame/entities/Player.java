package fr.leroideskiwis.mapgame.entities;

import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.Position;
import fr.leroideskiwis.mapgame.specialobjects.InvinciblePlayer;
import fr.leroideskiwis.plugins.events.OnMove;
import fr.leroideskiwis.plugins.events.OnPlayerTakeObject;
import fr.leroideskiwis.plugins.events.OnTakeCoin;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {

    private Map map;
    private Game game;
    private int invincibleTour;

    public Player(Game game, Map map){
        super("player");
        this.map = map;
        this.game = game;
    }

    public void setInvincible(int invincible){
        this.invincibleTour = invincible;
    }

    public int getInvincible(){
        return invincibleTour;
    }

    public boolean setPosition(Position position){
        return setPosition(position.getX(), position.getY());
    }

    private void passOneInvincibleMove(){
        invincibleTour--;
        if(invincibleTour > 0)
            game.sendMessage("You're invincible mode will be disabled in "+invincibleTour+" moves");
        else game.sendMessage("You're invincible mode was been disabled");
    }

    /**
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return <b>false</b> if there already an object in the coordinate x and y
     */
    public boolean setPosition(int x, int y){
        Position before = getPosition();
        Entity object = map.getObject(x, y);
        if(object instanceof SpecialObj) {
            SpecialObj special = (SpecialObj)object;
            executeSpecialObj(special);

        } else if(object instanceof Coin){

            game.addScore(((Coin)object).getAmount());
            OnTakeCoin event = new OnTakeCoin(new Position(x, y), (Coin) object);
            game.getPluginManager().callEvent(event);
            if(event.isCancelled()) return false;
            map.deleteObject(x, y);
        }

        if(invincibleTour > 0 && !(object instanceof InvinciblePlayer)) {
            if(object != null) {
                if (object instanceof Enemy) {
                    map.replaceObject(x, y, this);
                    game.sendMessage("You are in invicible mode : you killed an " + object.getClass().getSimpleName().toLowerCase() + ".");
                    passOneInvincibleMove();
                }
            } else {
                game.sendMessage("You kill nothing.");
                passOneInvincibleMove();
            }

        }

        OnMove event = new OnMove(before, new Position(x,y));
        game.getPluginManager().callEvent(event);
        if(event.isCancelled()) return false;

        if(!map.setObject(x, y, this) && !map.getObject(x,y).equals(this)) return false;
        map.replaceObject(before.getX(), before.getY(), null);

        return true;
    }

    public void executeSpecialObj(SpecialObj special){
        executeSpecialObj(special, true);
    }

    public void executeSpecialObj(SpecialObj special, boolean message){
        OnPlayerTakeObject event = new OnPlayerTakeObject(game.getMap().getPositionByObject(special), special);
        game.getPluginManager().callEvent(event);
        if(event.isCancelled()) return;
        if(message) game.sendMessage("You have found the special object \"" + special.name() + "\" ");
        List<String> tmpBuffer = new ArrayList<>(game.getBuffer());
        special.execute(game, map, this);
        game.getBuffer().clear();
        tmpBuffer.forEach(s -> game.getBuffer().add(s));
        map.deleteObject(map.getPositionByObject(special));
    }

    /**
     *
     * @param x Add x to the position
     * @param y Add y to the position
     * @return <b>false</b> if there already an object in the coordinate x and y
     */

    public boolean move(int x, int y){
        if(x == 0 && y == 0) return true;
        return setPosition(getPosition().getX()+x, getPosition().getY()+y);
    }

    /**
     *
     * @return an <b>Position</b> object for get the position of the player
     */

    public Position getPosition(){
        return map.getPositionByObject(this);
    }

    /**
     *
     * @return true if the player has lose
     */

    public boolean hasLose(){

        return !map.isNull(getPosition().add(1, 0)) &&
                !map.isNull(getPosition().add(0, 1)) &&
                !map.isNull(getPosition().add(-1, 0)) &&
                !map.isNull(getPosition().add(0, -1));

    }

}
