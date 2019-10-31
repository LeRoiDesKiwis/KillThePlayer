package fr.leroideskiwis.mapgame.logs;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.plugins.KtpPlugin;
import fr.leroideskiwis.plugins.KtpPluginManager;

public class FileLogs extends KtpPlugin {

    @Override
    public void onEnable(Game game) {

        listeners.add(new FileLogsListener(this, game));

    }

    @Override
    public String getName() {
        return "logs";
    }

    @Override
    public String getAuthor() {
        return "LeRoiDesKiwis";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}