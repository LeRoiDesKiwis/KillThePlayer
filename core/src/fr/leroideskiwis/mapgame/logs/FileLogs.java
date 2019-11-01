package fr.leroideskiwis.mapgame.logs;

import fr.leroideskiwis.plugins.KtpPlugin;
import fr.leroideskiwis.utils.Utils;
import fr.leroideskiwis.mapgame.Game;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileLogs extends KtpPlugin {

    private PrintWriter writer;

    @Override
    public void onEnable(Game game) {
        try {
            print("File loaded");
            listeners.add(new FileLogsListener(this));

            File folder = new File(System.getProperty("user.home") + "/.ktp/");
            if(!folder.exists() && !folder.mkdirs()) throw new IOException();

            File file = new File(folder + "/"+Utils.formatDate("hh_mm_ss-d_MM_yyyy")+".log");
            System.out.println(folder + "\\"+Utils.formatDate("hh_mm_ss-d_MM_yyyy"));
            if(!file.exists() && !file.createNewFile()) throw new IOException();

            this.writer = new PrintWriter(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable(Game game) {
        if(writer != null) {
            print("File unloaded");
            writer.flush();
            writer.close();
        }
    }

    public void print(String line){
        if(writer != null) writer.println(Utils.formatDate("hh::mm:ss")+" > "+line);
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