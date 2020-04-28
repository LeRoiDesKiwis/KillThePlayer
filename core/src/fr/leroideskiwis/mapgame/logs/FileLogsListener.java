package fr.leroideskiwis.mapgame.logs;

import fr.leroideskiwis.plugins.EventHandler;
import fr.leroideskiwis.plugins.Listener;
import fr.leroideskiwis.plugins.events.OnEnemyDeath;
import fr.leroideskiwis.plugins.events.OnMove;
import fr.leroideskiwis.plugins.events.OnObjectDeath;
import fr.leroideskiwis.plugins.events.OnObjectSpawn;
import fr.leroideskiwis.plugins.events.OnPlayerTakeObject;
import fr.leroideskiwis.plugins.events.OnTakeCoin;
import fr.leroideskiwis.utils.Utils;

public class FileLogsListener implements Listener {

    private FileLogs fileLogs;

    public FileLogsListener(FileLogs logs) {
        this.fileLogs = logs;
    }

    @EventHandler
    public void onCoin(OnTakeCoin event){

        fileLogs.print(Utils.format("logs.ontakecoin", event.getLocation()));

    }

    @EventHandler
    public void onMove(OnMove event){
        fileLogs.print(Utils.format("logs.onmove", event.direction, event.newLocation));
    }

    @EventHandler
    public void onObjectSpawn(OnObjectSpawn event){
        fileLogs.print(Utils.format("logs.onobjectspawn", event.getSpecialObject().getName(), event.getLocation()));
    }

    @EventHandler
    public void onEnemyDeath(OnEnemyDeath event){
        fileLogs.print(Utils.format("logs.onenemydeath", event.getLocation()));
    }

    @EventHandler
    public void onObjectDeath(OnObjectDeath event){
        fileLogs.print(Utils.format("logs.onobjectdeath", event.getLocation()));
    }

    @EventHandler
    public void onPlayerTakeObject(OnPlayerTakeObject event){
        fileLogs.print(Utils.format("logs.playertakeobject", event.getLocation(), event.getSpecialObject()));
    }
}
