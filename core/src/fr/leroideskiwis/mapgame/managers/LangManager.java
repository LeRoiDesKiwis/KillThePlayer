package fr.leroideskiwis.mapgame.managers;

import java.util.Locale;
import java.util.ResourceBundle;

public class LangManager {

    private ResourceBundle resourceBundle;

    public LangManager(String language){

        Locale locale = new Locale(language);
        this.resourceBundle = ResourceBundle.getBundle("bundle", locale);

    }

    public String getText(String key){
        return resourceBundle.getString(key);
    }

}
