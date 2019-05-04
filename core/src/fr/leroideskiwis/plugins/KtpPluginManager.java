package fr.leroideskiwis.plugins;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.plugins.events.Event;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;

public class KtpPluginManager {

    private final File racine = new File("./plugins/");
    private final List<KtpPlugin> plugins = new ArrayList<>();
    private final Game game;

    public KtpPluginManager(Game game) {
        this.game = game;
    }

    public void unloadPlugins(){
        plugins.forEach(plugin -> {
            plugin.onDisable(game);
            game.debug("Plugin "+plugin.getName()+"v"+plugin.getVersion()+" de "+plugin.getAuthor()+" à été déchargé");
        });

        plugins.clear();

    }

    public void callEvent(Event event){

        plugins.forEach(plugin -> plugin.listeners.forEach(listener -> {

            for(Method method : listener.getClass().getMethods()){
                if(method.isAnnotationPresent(EventHandler.class)){
                    if(method.getParameters()[0].getType() == event.getClass()){
                        try {
                            method.invoke(listener, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }));

    }

    public void loadPlugins(){

        if(!racine.exists() && !racine.mkdir()) return;

        for(File file : racine.listFiles()){

            try {
                KtpPlugin plugin = loadPlugin(file);
                if(plugin != null) {
                    plugins.add(plugin);
                    plugin.onEnable(game);
                    game.debug("Plugin "+plugin.getName()+"v"+plugin.getVersion()+" de "+plugin.getAuthor()+" à été chargé");

                }
            } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }

        }

    }

    public KtpPlugin loadPlugin(File file) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        if(file.getName().endsWith(".jar")) {
            KtpPlugin plugin = null;
            JarFile jarFile = new JarFile(file);
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});

            Enumeration enumeration = jarFile.entries();

            while(enumeration.hasMoreElements()){
                String tmp = enumeration.nextElement().toString();

                if(tmp.endsWith(".class")){
                    tmp = tmp.substring(0, tmp.length()-6);
                    tmp = tmp.replaceAll("/", ".");

                    Class clazz = Class.forName(tmp, true, urlClassLoader);

                    if(clazz.getSuperclass() == KtpPlugin.class)
                        plugin = (KtpPlugin)clazz.newInstance();
                }
            }
            return plugin;

        } else return null;

    }

}
