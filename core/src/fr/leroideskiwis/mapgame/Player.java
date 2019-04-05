package fr.leroideskiwis.mapgame;

import fr.leroideskiwis.mapgame.specialobjects.InvinciblePlayer;
import fr.leroideskiwis.mapgame.specialobjects.SpecialObj;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private Map map;
    private char token;
    private Game game;
    private int invincibleTour;

    public Player(Game game, Map map, char token){
        this.map = map;
        this.token = token;
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

    public void passOneInvincibleMove(){
        invincibleTour--;
        if(invincibleTour > 0)
            game.addInBuffer("You're invincible mode will be disabled in "+invincibleTour+" moves");
        else game.addInBuffer("You're invincible mode was been disabled");
    }

    /**
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return <b>false</b> if there already an object in the coordinate x and y
     */
    public boolean setPosition(int x, int y){
        Position before = getPosition();
        Object object = map.getObject(x, y);
        if(object instanceof SpecialObj) {
            SpecialObj special = (SpecialObj)object;
            executeSpecialObj(special);

        } else if(object instanceof Coin){

            game.addScore(((Coin)object).getAmount());
            map.deleteObject(x, y);
        }

        if(invincibleTour > 0 && !(object instanceof InvinciblePlayer)) {
            if(object != null) {
                if (object instanceof Ennemy) {
                    map.replaceObject(x, y, this);
                    game.addInBuffer("You are in invicible mode : you killed an " + object.getClass().getSimpleName().toLowerCase() + ".");
                    passOneInvincibleMove();
                }
            } else {
                game.addInBuffer("You kill nothing.");
                passOneInvincibleMove();
            }

        }

        if(!map.setObject(x, y, this) && !map.getObject(x,y).equals(this)) return false;
        map.replaceObject(before.getX(), before.getY(), null);
        return true;
    }

    public void executeSpecialObj(SpecialObj special){
        executeSpecialObj(special, true);
    }

    public void executeSpecialObj(SpecialObj special, boolean message){
        if(message) game.addInBuffer("You have found the special object \"" + special.name() + "\" ");
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

    public String toString(){
        return Character.toString(token);
    }

}
