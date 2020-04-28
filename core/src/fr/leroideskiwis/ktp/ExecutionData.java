package fr.leroideskiwis.ktp;

import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.Player;

public class ExecutionData {

    private Player player;
    private Map map;
    private Game game;

    public ExecutionData(Player player, Map map, Game game) {
        this.player = player;
        this.map = map;
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public Map getMap() {
        return map;
    }

    public Game getGame() {
        return game;
    }
}
