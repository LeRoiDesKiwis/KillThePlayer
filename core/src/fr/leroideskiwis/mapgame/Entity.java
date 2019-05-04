package fr.leroideskiwis.mapgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import fr.leroideskiwis.ktp.Main;

public class Entity {

    private Texture texture;

    public Entity(String path) {
        this.texture = Main.getTexture(path+".png");
    }

    public Texture texture(){
        return texture == null ? new Texture(Gdx.files.internal("defaultobj.png")) : texture;
    }

}
