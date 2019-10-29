package fr.leroideskiwis.utils;

import fr.leroideskiwis.mapgame.entities.SpecialObj;

import java.util.List;
import java.util.Random;

public class SpecialObjects {

    private static final Random random = new Random();

    public static SpecialObj randomItem(List<SpecialObj> items) {
        float total = 0;

        for (SpecialObj specialObject : items)
            total += specialObject.chance();

        float rf = random.nextFloat();
        rf *= total;

        float cur = 0;
        for(SpecialObj item : items) {
            float proba = item.chance();

            if(rf >= cur && rf < cur + proba)
                return item;

            cur += proba;
        }

        return items.stream().findFirst().orElse(null);
    }

}