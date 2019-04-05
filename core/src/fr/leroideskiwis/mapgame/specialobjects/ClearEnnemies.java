package fr.leroideskiwis.mapgame.specialobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import fr.leroideskiwis.ktp.Main;
import fr.leroideskiwis.mapgame.*;

public class ClearEnnemies extends SpecialObj{


    public ClearEnnemies(Game game) {
        super(game, "clearennemis");
    }

    @Override
    public void execute(Game main, Map map, Player player) {

        for(Object[] array : map.getContent()){
            for(Object o : array){
                if(o instanceof Ennemy){
                    map.deleteObject(map.getPositionByObject(o));
                }
            }
        }

        main.addInBuffer("MAP CLEARED");

    }

    @Override
    public String name() {
        return "clear map !";
    }

    @Override
    public Texture texture() {
        return super.texture();//new Texture(Gdx.files.internal("obj/clearenn.png"));
    }

    @Override
    public double chance() {
        return 0.004;
    }

    @Override
    public Position spawn(Game main, Map map, Player player) {
        if(Math.random() < 0.10)
            return super.spawn(main, map, player);
        else
            return new Position(0, 0);
    }
}
