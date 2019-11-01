package fr.leroideskiwis.utils;

import com.badlogic.gdx.utils.I18NBundle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static I18NBundle resourceBundle;

    public static String formatDate(String pattern){

        return new SimpleDateFormat(pattern).format(new Date());

    }

    public static String getText(String key) {
        return resourceBundle.get(key);
    }

    public static String format(String key, Object... args){
        return resourceBundle.format(key, args);
    }
}
