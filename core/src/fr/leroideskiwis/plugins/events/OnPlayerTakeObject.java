package fr.leroideskiwis.plugins.events;

import fr.leroideskiwis.mapgame.Position;
import fr.leroideskiwis.mapgame.entities.SpecialObj;

public class OnPlayerTakeObject extends Event {

    private Position position;
    private SpecialObj specialObj;

    public OnPlayerTakeObject(Position position, SpecialObj specialObj) {
        this.position = position;
        this.specialObj = specialObj;
    }

    public SpecialObj getSpecialObj() {
        return specialObj;
    }

    public void setSpecialObj(SpecialObj specialObj) {
        this.specialObj = specialObj;
    }

    public Position getPosition() {
        return position;
    }
}
