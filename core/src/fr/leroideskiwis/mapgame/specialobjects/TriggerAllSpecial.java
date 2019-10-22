package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

public class TriggerAllSpecial extends SpecialObj {
    public TriggerAllSpecial(Game game) {
        super(game, "trigger.png");
    }

    @Override
    public void execute(Game game, Map map, Player player) {

        game.sendMessage("All special objects are been triggered");
        map.getEntitiesByType(SpecialObj.class).stream().filter(entity -> !(entity instanceof ClearEnnemies) && !(entity instanceof TriggerAllSpecial) && !(entity instanceof Respawn)).forEach(entity -> player.executeSpecialObj(entity, false));
    }
    
    @Override
    public String name() {
        return "special trigger";
    }

    @Override
    public double chance() {
        return 0.01;
    }

}
