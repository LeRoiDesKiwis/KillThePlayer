package fr.leroideskiwis.mapgame.logs;

import fr.leroideskiwis.plugins.KtpPlugin;
import fr.leroideskiwis.utils.Utils;
import fr.leroideskiwis.mapgame.Game;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileLogs extends KtpPlugin {

    private File file;
    private PrintWriter writer;

    @Override
    public void onEnable(Game game) {
        try {
            print("File loaded");
            listeners.add(new FileLogsListener(this, game));
            this.writer = new PrintWriter(file);
            this.file = new File(Utils.formatDate("hh:mm:ss-d/MM/yyyy"));
            if(!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable(Game game) {
        print("File unloaded");
        writer.flush();
    }

    public void print(String line){
        writer.println(Utils.formatDate("hh::mm:ss")+" > "+line);
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