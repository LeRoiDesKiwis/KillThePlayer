package fr.leroideskiwis.utils;

import fr.leroideskiwis.mapgame.managers.LangManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private static LangManager langManager = new LangManager("en");

    public static String formatDate(String pattern){

        return new SimpleDateFormat(pattern).format(new Date());

    }

    public static String getText(String key){
        return langManager.getText(key);
    }

}
