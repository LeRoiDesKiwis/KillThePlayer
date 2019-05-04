package fr.leroideskiwis.mapgame.multiplayer;

import fr.leroideskiwis.mapgame.Entity;
import fr.leroideskiwis.mapgame.Game;
import fr.leroideskiwis.mapgame.Map;
import fr.leroideskiwis.mapgame.entities.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class Multiplayer implements Closeable {

    private Game game;
    private Map otherMap;
    private Socket socket;

    public Multiplayer(Game game) {
        this.game = game;
    }

    public boolean isConnected(){
        return socket != null;
    }

    public void connect(String ip) throws IOException {
        this.socket = new Socket(ip, 19836);
    }

    private String readString() throws IOException {

        byte[] bytes = new byte[4096];
        StringBuilder builder = new StringBuilder();

        while((socket.getInputStream().read(bytes)) != 0){

            for(byte b : bytes){
                if(b <= 0) continue;
                builder.append((char)b);

            }

        }

        return builder.toString();

    }

    private boolean hasPacket() throws IOException {
        return socket.getInputStream().available() > 0;
    }

    public JSONObject getJsonObject() throws IOException, JSONException {

        if(hasPacket()) return new JSONObject(readString());
        return null;

    }

    public void reloadMaps() throws IOException, JSONException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        game.setMap(parseMap(getJsonObject().getJSONObject("you")));
        this.otherMap = parseMap(getJsonObject().getJSONObject("other"));
    }

    public Map parseMap(JSONObject json) throws JSONException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {

        if(json == null) return null;

        Map map = new Map(game, json.getInt("x"), json.getInt("y"));

        //instance des jsons
        JSONObject playerInfos = json.getJSONObject("player");
        JSONArray array = json.getJSONArray("objects");
        JSONObject coinInfos = json.getJSONObject("coin");
        JSONArray obstacles = json.getJSONArray("obstacles");
        JSONArray enemies = json.getJSONArray("enemies");

        //instances des objets
        Coin coin = new Coin(coinInfos.getInt("amount"));
        Player player = new Player(game, map);
        player.setInvincible(playerInfos.getInt("invincible"));

        //spawn des objets
        spawn(playerInfos, map, player);
        spawn(coinInfos, map, coin);

        //instance et spawn des objets dans les arrays
        for(int i = 0; i < array.length(); i++){

            JSONObject entityInfos = array.getJSONObject(i);
            SpecialObj obj = parseSpecialObj(entityInfos);
            map.replaceObject(entityInfos.getInt("x"), entityInfos.getInt("y"), obj);

        }

        for (int i = 0; i < obstacles.length(); i++) {
            JSONObject obstacleInfo = obstacles.getJSONObject(i);

            spawn(obstacleInfo, map, new Obstacle());

        }

        for(int i = 0; i < enemies.length(); i++) {

            JSONObject enemyInfo = obstacles.getJSONObject(i);
            spawn(enemyInfo, map, new Enemy(map));

        }

        return map;
    }

    private SpecialObj parseSpecialObj(JSONObject json) throws JSONException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        SpecialObj obj = (SpecialObj)Class.forName(json.getString("path")).getConstructors()[0].newInstance(game);
        return obj;
    }

    /*private <T> List<T> jsonArrayToArray(JSONArray array, Class<T> type) throws JSONException {

        List<T> ts = new ArrayList<>();

        for(int i = 0; i < array.length(); i++){

            if(!array.get(i).getClass().isInstance(type)) continue;

            ts.add((T) array.get(i));

        }

        return ts;
    }*/

    private void spawn(JSONObject json, Map map, Entity object) throws JSONException {
        map.replaceObject(json.getInt("x"), json.getInt("y"), object);
    }

    public Map getOtherMap() {
        return otherMap;
    }

    public boolean isClosed(){
        return socket.isClosed();
    }

    @Override
    public void close() throws IOException {
        socket.getOutputStream().write("{\"type\":\"exit\"}".getBytes());
        socket.getOutputStream().flush();
        socket.close();
    }
}
