package fr.leroideskiwis.mapgame.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureManager<T> {

    private final Map<T, Texture> textures = new HashMap<>();

    public void register(T name, String path){
        register(name, new Texture(getAsset(path)));
    }

    public Texture registerIfAbsent(T name, String path){
        if(has(name)){
            return getTexture(name);
        } else {
            register(name, path);
            return registerIfAbsent(name, path);
        }
    }

    private void register(T name, Texture texture){
        textures.put(name, texture);
    }

    public Texture getTexture(T name){
        return textures.get(name);
    }

    public boolean has(T name){
        return textures.containsKey(name);
    }

    public Texture toTexture(String path){
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
