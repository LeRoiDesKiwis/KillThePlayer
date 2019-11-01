package fr.leroideskiwis.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String formatDate(String pattern){

        return new SimpleDateFormat(pattern).format(new Date());

    }

}
