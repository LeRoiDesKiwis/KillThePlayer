package fr.leroideskiwis.plugins;

import fr.leroideskiwis.mapgame.Game;

import java.util.ArrayList;
import java.util.List;

public abstract class KtpPlugin {

    public final List<Listener> listeners = new ArrayList<>();

    protected void registerListener(Listener listener){
        listeners.add(listener);
    }

    public void onEnable(Game game){}
    public void onDisable(Game game){}

    public abstract String getName();
    public abstract String getAuthor();
    public abstract String getVersion();

}