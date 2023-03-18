package fr.leroideskiwis.mapgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class Game {

    private boolean debugMode;

    private List<String> bufferSysout = new ArrayList<>();
    private int score;
    private Map map;
    private Player player;
    private final int size;
    //private JSONConfiguration configuration;
    private boolean lock = false;
    private TextureManager textureManager;

    public boolean movePlayer(int x, int y){
        return player.move(x, y);
    }

    public int getScore(){
        return score;
    }

    public List<String> getBuffer() {
        return new ArrayList<>(bufferSysout);
    }

    public void addScore(int score){
        this.score += score;
        sendMessage(Utils.format("score.win", score));
    }

    public <T> Optional<T> getRandomList(List<T> list){

        if(list.isEmpty()) return Optional.empty();

        return Optional.of(list.get(randomInt(list.size()-1)));

    }


    public Game(TextureManager textureManager) throws IllegalAccessException, InstantiationException, InvocationTargetException {
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

        //TODO round it System.out.println("% of empty cases : "+(int)((double)map.getEmptyCases().size()/(double)map.getTotalSize()*100.0)+"%");
    }

    public boolean update() {

        if (map.getEntitiesByType(Coin.class).size() == 0) map.generateRandom(new Coin(randomInt(5, 10)));

        for (int i = 0, rand = 1; i < rand; i++) {
            spawnEnnemy(map);
        }

        if (Math.random() < 0.05) {

            SpecialObject special = RandomerSpecialObject.randomItem(SpecialObjects.ALL.stream().map(Supplier::get).filter(specialObject -> specialObject.canSpawn(new ExecutionData(player, map, this))).collect(Collectors.toList()));

            Location location = special.spawn(new ExecutionData(player, map, this));
        }

        if(Math.random() < 0.001){
            getRandomList(map.getEntitiesByType(SpecialObject.class)
                        .stream()
                        .filter(specialObj -> map.hasFullSurrounding(specialObj))
                        .collect(Collectors.toList()))
                    .ifPresent(specialObject -> specialObject.kill(map));

        }


        if (player.hasLose() || map.getEmptyCases().size() == 0){
            sendMessage(Utils.getText("game.finish"));
            return false;
        }
        score++;
        this.lock = false;
        return true;
    }

    public SpecialObject newObject(Class<? extends SpecialObject> specialObj) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return (SpecialObject)specialObj.getConstructors()[0].newInstance(this);
    }

    public int randomInt(int min, int max){
        return (int)(Math.random()*(max+1-min))+min;
    }


    private int randomInt(int max) {
        return randomInt(0, max);
    }

    /**
     * @see Map#setEntity(int, int, Entity)
     */

    public boolean spawn(int x, int y, Entity entity){
        return map.setEntity(x, y, entity);
    }

    public void debug(String s){
        Gdx.app.log("[LOG] ", s);
    }

    public Location getLocationNearEnemy(){
        List<Enemy> enemyList = map.getEntitiesByType(Enemy.class);
        /*Location pos;
        Enemy enemy;
        do {
            enemy = getRandomList(enemyList);

            pos = map.getRandomPositionSurrounding(enemy.getLocation());

        } while (pos.isOutOfMap(map) || !map.isEmpty(pos));
*/
        return getRandomList(enemyList.stream()
                .filter(enemy1 -> !map.hasFullSurrounding(enemy1))
                .flatMap(enemy -> enemy.getSurroundingWithoutCorners().stream())
                .filter(location -> !location.isOutOfMap(map) && map.isEmpty(location)).collect(Collectors.toList())).orElse(new Location(1, 1));

    }

    private void spawnEnnemy(Map map) {
        if(debugMode) return;
        if(Math.random() < 0.001){

            Enemy enemy = new Enemy();
            map.generateRandom(enemy);

        }

        if(map.getLocationsByType(Enemy.class).isEmpty()){
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

    public Player getPlayer(){
        return player;
    }

    public List<Entity> getEntities(){
        return map.getEntities();
    }

    public int getSize() {
        return size;
    }
    public void sendMessage(String message) {

        if (!lock) {
            bufferSysout.clear();
            lock = true;
        }
        bufferSysout.add(message);
    }

    /*public JSONConfiguration getConfig() {
        return configuration;
    }*/

    public void drawMap(TextureManager manager, SpriteBatch batch, float multiplicatorX, float multiplicatorY, Texture emptyCase){
        map.draw(manager, batch, multiplicatorX, multiplicatorY, emptyCase);

    }

}
