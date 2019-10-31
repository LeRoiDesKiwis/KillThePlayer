package fr.leroideskiwis.mapgame.logs;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.plugins.EventHandler;
import fr.leroideskiwis.plugins.Listener;
import fr.leroideskiwis.plugins.events.OnEnemyDeath;
import fr.leroideskiwis.plugins.events.OnMove;
import fr.leroideskiwis.plugins.events.OnObjectDeath;
import fr.leroideskiwis.plugins.events.OnObjectSpawn;
import fr.leroideskiwis.plugins.events.OnPlayerTakeObject;
import fr.leroideskiwis.plugins.events.OnTakeCoin;

public class FileLogsListener implements Listener {

    private Game game;
    private FileLogs fileLogs;

    public FileLogsListener(FileLogs logs, Game game) {
        this.game = game;
        this.fileLogs = logs;
    }

    @EventHandler
    public void onCoin(OnTakeCoin event){

        fileLogs.print("player took coin on location "+event.getLocation());

    }

    @EventHandler
    public void onMove(OnMove event){
        fileLogs.print("Player moved ");
    }

    @EventHandler
    public void onObjectSpawn(OnObjectSpawn event){
        fileLogs.print("A "+event.getSpecialObj().name()+" spawned at "+event.getLocation());
    }

    @EventHandler
    public void onEnemyDeath(OnEnemyDeath event){
        fileLogs.print("A enemy is die at "+event.getLocation());
    }

    @EventHandler
    public void onObjectDeath(OnObjectDeath event){
        fileLogs.print("A object is die at "+event.getLocation());
    }

    @EventHandler
    public void onPlayerTakeObject(OnPlayerTakeObject event){
        fileLogs.print("A player has take object at"+ event.getLocation() + " : "+event.getSpecialObj().name());
    }
}
