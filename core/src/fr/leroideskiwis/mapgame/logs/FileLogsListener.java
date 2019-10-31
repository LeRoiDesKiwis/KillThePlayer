package fr.leroideskiwis.mapgame.logs;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.plugins.EventHandler;
import fr.leroideskiwis.plugins.Listener;

public class FileLogsListener implements Listener {

    private Game game;
    private FileLogs fileLogs;

    public FileLogsListener(FileLogs logs, Game game) {
        this.game = game;
        this.fileLogs = logs;
    }

}
