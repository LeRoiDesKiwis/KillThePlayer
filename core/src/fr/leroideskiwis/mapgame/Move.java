package fr.leroideskiwis.mapgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import fr.leroideskiwis.mapgame.entities.Player;

public class Move {

    private final int key;
    private int time;
    public final int x;
    public final int y;

    public Move(int key, int x, int y){
        this.key = key;
        this.x = x;
        this.y = y;
    }

    public boolean canMove(){

        if(Gdx.input.isKeyPressed(key)) {
            time++;
        }
        else time = 0;

        if(time >= 4){
            time = 0;
            return true;
        }

        return false;


    }

}
