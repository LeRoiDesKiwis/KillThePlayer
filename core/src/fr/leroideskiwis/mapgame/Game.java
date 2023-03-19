package fr.leroideskiwis.mapgame;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.leroideskiwis.ktp.ExecutionData;
import fr.leroideskiwis.mapgame.entities.Coin;
import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Obstacle;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.specialobjects.SpecialObject;
import fr.leroideskiwis.mapgame.managers.TextureManager;
import fr.leroideskiwis.mapgame.specialobjects.SpecialObjects;
import fr.leroideskiwis.utils.RandomerSpecialObject;
import fr.leroideskiwis.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class Game {

    private final boolean debugMode;
    private final List<String> bufferSysout = new ArrayList<>();
    private int score;
    private final Map map;
    private final Player player;
    private final int size;
    //private JSONConfiguration configuration;
    private boolean lock = false;
    private final TextureManager<Entity> textureManager;

    public boolean movePlayer(int x, int y){
        return player.move(x, y);
    }

    public void addScore(int score){
        this.score += score;
        sendMessage(Utils.format("score.win", score));
    }

    public <T> Optional<T> getRandomElement(List<T> list){

        if(list.isEmpty()) return Optional.empty();

        return Optional.of(list.get(randomInt(list.size()-1)));

    }

    public Game(TextureManager<Entity> textureManager) {
        this.textureManager = textureManager;
        this.size = randomInt(28, 32);
        Gdx.app.log("INFO", "new instance of game");
        debugMode = false;

        map = new Map(this, size, size);
        player = new Player(this, map);
        map.generateRandom(player);

        Gdx.app.log("INFO", "Generate defaults ennemies");

        for(int i = 0, rand = randomInt(1, 3); i < rand; i++){
            map.generateRandom(new Enemy());
        }
        Gdx.app.log("INFO", "Generate defaults obstacles");

        for(int i = 0, rand = randomInt(3, 7); i < rand; i++){
            map.generateRandom(new Obstacle());
        }

        Gdx.graphics.setTitle("KillThePlayer (created by LeRoiDesKiwis) map size : "+size);
    }

    private void spawnRandomObject(){
        SpecialObject special = RandomerSpecialObject.randomItem(SpecialObjects.ALL.stream()
                .map(Supplier::get)
                .filter(specialObject -> specialObject.canSpawn(new ExecutionData(player, map, this)))
                .collect(Collectors.toList()));

        special.spawn(new ExecutionData(player, map, this));
    }

    private void killObjectIfSurrounded(){
        getRandomElement(map.getEntitiesByType(SpecialObject.class)
                .stream()
                .filter(map::hasFullSurrounding)
                .collect(Collectors.toList()))
                .ifPresent(specialObject -> specialObject.kill(map));
    }

    public void update() {
        if (map.getEntitiesByType(Coin.class).size() == 0) map.generateRandom(new Coin(randomInt(5, 10)));

        for (int i = 0, rand = 1; i < rand; i++) {
            spawnEnnemy(map);
        }

        if (Math.random() < 0.05) spawnRandomObject();

        if(Math.random() < 0.001) killObjectIfSurrounded();

        if (player.hasLose() || map.getEmptyCases().size() == 0){
            sendMessage(Utils.getText("game.finish"));
            return;
        }
        addScore(1);
        bufferSysout.add(0, Utils.format("score.show", score));
        this.lock = false;
    }

    public int randomInt(int min, int max){
        return (int)(Math.random()*(max+1-min))+min;
    }

    private int randomInt(int max) {
        return randomInt(0, max);
    }

    public void debug(String s){
        Gdx.app.log("[LOG] ", s);
    }

    public Location getLocationNearEnemy(){
        List<Enemy> enemyList = map.getEntitiesByType(Enemy.class);

        return getRandomElement(enemyList.stream()
                .filter(enemy1 -> !map.hasFullSurrounding(enemy1))
                .flatMap(enemy -> enemy.getSurroundingWithoutCorners().stream())
                .filter(location -> !location.isOutOfMap(map) && map.isEmpty(location)).collect(Collectors.toList())).orElse(new Location(1, 1));

    }

    private void spawnEnnemy(Map map) {
        if(debugMode) return;

        if(map.getLocationsByType(Enemy.class).isEmpty() || Math.random() < 0.001){
            Enemy enemy = new Enemy();
            map.generateRandom(enemy);
            return;
        }
		
		if(map.getEmptyCases().size() == 0) return;

        map.setEntity(getLocationNearEnemy(), new Enemy());

        //System.out.println("A new enemy has spawned in "+pos);
    }

    public Map getMap() {
        return map;
    }

    public void sendMessage(String message) {

        if (!lock) {
            bufferSysout.clear();
            lock = true;
        }
        bufferSysout.add(message);
    }

    public void drawMap(TextureManager<Entity> manager, SpriteBatch batch, float multiplicatorX, float multiplicatorY, Texture emptyCase){
        map.draw(manager, batch, multiplicatorX, multiplicatorY, emptyCase);

    }

    public void drawBuffer(SpriteBatch batch, BitmapFont font){
        for(int i = 0; i < bufferSysout.size(); i++) {
            String s = bufferSysout.get(i);
            font.draw(batch, s, 650, 460-i*20);
        }
    }

    public void updatePresence(long started){
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.state = "score : " + score;
        presence.details = Utils.format("presence.sizemap", size);
        presence.startTimestamp = started;
        presence.largeImageKey = "new_high_score_";
        DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
    }

    public boolean hasLose() {
        return player.hasLose();
    }
}
