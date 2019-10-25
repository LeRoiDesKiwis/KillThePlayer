package fr.leroideskiwis.mapgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import fr.leroideskiwis.mapgame.Entity;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {

    private Map<Class<? extends Entity>, Texture> textures = new HashMap<>();

    public void register(Entity entity, Texture texture){
        textures.put(entity.getClass(), texture);
    }

    public Texture getTexture(Entity entity){
        return textures.get(entity.getClass());
    }

    public boolean has(Entity entity){
        return textures.containsKey(entity.getClass());
    }

    public Texture getTexture(String path){
        try {
            Texture texture = new Texture(getAsset(path));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            return texture;
        }catch(Exception exception){
            return null;
        }
    }

    private FileHandle getAsset(String path){
        if(path.endsWith(".png.png")) path = path.substring(0, path.length()-4);

        path = "textures/"+path;
        FileHandle handle = Gdx.files.internal(path);
        if (handle.exists())
            return handle;
        else
            handle = Gdx.files.classpath(path);
        return handle.exists() ? handle : null;
    }

    public void dispose(){
        textures.values().forEach(Texture::dispose);
    }

}
