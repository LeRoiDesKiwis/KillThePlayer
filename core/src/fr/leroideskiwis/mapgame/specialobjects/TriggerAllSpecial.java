package fr.leroideskiwis.mapgame.specialobjects;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.Position;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

public class TriggerAllSpecial extends SpecialObj {
    public TriggerAllSpecial(Game game) {
        super(game, "trigger");
    }

    @Override
    public void execute(Game game, Map map, Player player) {

        game.sendMessage("All special objects are been triggered");
        for(Position pos : map.getPositionsByType(SpecialObj.class)){
            Object o = map.getObject(pos);
            if(o instanceof TriggerAllSpecial) continue;
            if(o instanceof ClearEnnemies) continue;
            SpecialObj special = (SpecialObj)o;
            player.executeSpecialObj(special, false);
        }
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
