package fr.leroideskiwis.ktp;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Player;

public class ExecutionData {

    public final Player player;
    public final Map map;
    public final Game game;

    public ExecutionData(Player player, Map map, Game game) {
        this.player = player;
        this.map = map;
        this.game = game;
    }
}
