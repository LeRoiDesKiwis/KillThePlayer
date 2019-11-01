package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;
import fr.leroideskiwis.utils.Utils;

public class TriggerAllSpecial extends SpecialObj {
    public TriggerAllSpecial(Game game) {
        super(game, "trigger.png");
    }

    @Override
    public void execute(Game game, Map map, Player player) {

        game.sendMessage(Utils.getText("objects.trigger.message"));
        map.getEntitiesByType(SpecialObj.class).stream().filter(entity -> !(entity instanceof ClearEnnemies) && !(entity instanceof TriggerAllSpecial) && !(entity instanceof Respawn)).forEach(entity -> entity.onCollide(game, map, player));
    }
    
    @Override
    public String name() {
        return Utils.getText("objects.trigger.name");
    }

    @Override
    public float chance() {
        return 0.01f;
    }

}
