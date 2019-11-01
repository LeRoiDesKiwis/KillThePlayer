package fr.leroideskiwis.mapgame;

import fr.leroideskiwis.utils.Utils;

public class Invincibility {

    private int tour;

    public boolean isInvincible(){
        return tour > 0;
    }

    public void removeOne(){
        tour--;
    }

    public void addInvincility(int invincibleTour){
        tour += invincibleTour;
    }

    public void display(Game game){
        game.sendMessage(Utils.format("objects.invincibility.moves", tour));
    }
}
