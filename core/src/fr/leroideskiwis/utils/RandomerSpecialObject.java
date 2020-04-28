package fr.leroideskiwis.utils;

import fr.leroideskiwis.mapgame.specialobjects.SpecialObject;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class RandomerSpecialObject {

    private static final Random random = new Random();

    public static SpecialObject randomItem(List<SpecialObject> items) {
        float total = 0;

        for (SpecialObject specialObject : items)
            total += specialObject.getChance();

        float rf = random.nextFloat();
        rf *= total;

        float cur = 0;
        for(SpecialObject item : items) {
            float proba = item.getChance();

            if(rf >= cur && rf < cur + proba)
                return item;

            cur += proba;
        }

        return items.stream().findFirst().orElse(null);
    }

}