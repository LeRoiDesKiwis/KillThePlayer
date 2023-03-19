package fr.leroideskiwis.utils;

import com.badlogic.gdx.utils.I18NBundle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Utils {

    public static I18NBundle resourceBundle;
    public static Random random = new Random();

    public static String formatDate(String pattern){

        return new SimpleDateFormat(pattern).format(new Date());

    }

    public static String getText(String key) {
        return resourceBundle.get(key);
    }

    public static String format(String key, Object... args){
        return resourceBundle.format(key, args);
    }

    public static <T> Optional<T> getRandomElement(List<T> list){

        if(list.isEmpty()) return Optional.empty();

        return Optional.of(list.get(random.nextInt(list.size()-1)));

    }
}
