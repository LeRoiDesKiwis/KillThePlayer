package fr.leroideskiwis.mapgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import fr.leroideskiwis.mapgame.entities.Coin;
import fr.leroideskiwis.mapgame.entities.Enemy;
import fr.leroideskiwis.mapgame.entities.Obstacle;
import fr.leroideskiwis.mapgame.entities.Player;
import fr.leroideskiwis.mapgame.entities.SpecialObj;
import fr.leroideskiwis.mapgame.managers.TextureManager;
import fr.leroideskiwis.mapgame.specialobjects.ClearEnnemies;
import fr.leroideskiwis.mapgame.specialobjects.InvinciblePlayer;
import fr.leroideskiwis.mapgame.specialobjects.OpenPath;
import fr.leroideskiwis.mapgame.specialobjects.RayonEnnemyKiller;
import fr.leroideskiwis.mapgame.specialobjects.Reparator;
import fr.leroideskiwis.mapgame.specialobjects.Respawn;
import fr.leroideskiwis.mapgame.specialobjects.TriggerAllSpecial;
import fr.leroideskiwis.plugins.KtpPluginManager;
import fr.leroideskiwis.plugins.events.OnObjectDeath;
import fr.leroideskiwis.plugins.events.OnObjectSpawn;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public final class Game {

    private boolean noClearingScreen;
    private boolean debugMode;
    private List<Class<? extends SpecialObj>> specialObjs = new ArrayList<>();
    private List<String> bufferSysout = new ArrayList<>();
    private int score;
    private Map map;
    private Player player;
    private final int size;
    //private JSONConfiguration configuration;
    private KtpPluginManager pluginManager = new KtpPluginManager(this);
    private boolean lock = false;
    private TextureManager textureManager;

    public boolean movePlayer(int x, int y){
        return player.move(x, y);
    }

    public void setScore(int score){
        this.score = score;
    }
    
    public int getScore(){
        return score;
    }

    public List<String> getBuffer() {
        return new ArrayList<>(bufferSysout);
    }

    public void addScore(int score){
        this.score+= score;
        sendMessage("You win "+score+"pt"+(score == 1 ? "" : "s"));
    }

    private SpecialObj getRandomObj() throws IllegalAccessException, InstantiationException,  InvocationTargetException {
        SpecialObj special;
        do {
            special = (SpecialObj) specialObjs.get(new Random().nextInt(specialObjs.size())).getConstructors()[0].newInstance(this);
        }while(Math.random() > special.chance());
        return special;
    }

    public void registerObject(Class<? extends SpecialObj> specialObj){
        specialObjs.add(specialObj);
    }

    public <T> T getRandomList(List<T> list){

        if(list.isEmpty()) return null;

        return list.get(randomInt(list.size()-1));

    }


    public void setMap(Map map){
        if(map != null)
            this.map = map;
    }

    public Game(TextureManager textureManager) {
        this.textureManager = textureManager;
        this.size = randomInt(28, 32);
        Gdx.app.log("INFO", "new instance of game");
        debugMode = false;
        this.pluginManager = new KtpPluginManager(this);


        specialObjs.add(RayonEnnemyKiller.class);
        //specialObjs.add(InvinciblePlayer.class);
        specialObjs.add(TriggerAllSpecial.class);
        specialObjs.add(ClearEnnemies.class);
        specialObjs.add(Reparator.class);
        specialObjs.add(OpenPath.class);
        specialObjs.add(Respawn.class);

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

        pluginManager.loadPlugins();
    }

    public boolean update() throws IllegalAccessException, InstantiationException, InvocationTargetException {

        if (map.getEntitiesByType(Coin.class).size() == 0) map.generateRandom(new Coin(randomInt(5, 10)));

        for (int i = 0, rand = 1; i < rand; i++) {
            spawnEnnemy(map);
        }

        if (Math.random() < 0.05) {

            SpecialObj special = getRandomObj();
            Location location = special.spawn(this, map, player);
            OnObjectSpawn event = new OnObjectSpawn(location, special);
            getPluginManager().callEvent(event);
            if(!event.isCancelled())
                map.setEntity(location, event.getSpecialObj());
        }

        if(Math.random() < 0.01){

            SpecialObj obj = getRandomList(map.getEntitiesByType(SpecialObj.class)
                    .stream()
                    .filter(specialObj -> map.hasFullSurrounding(specialObj, Enemy.class))
                    .collect(Collectors.toList()));
            if(obj != null) obj.kill();

        }

        for (Entity entity : map.getEntitiesByType(SpecialObj.class)) {

            if (Math.random() < 0.001) {

                if (entity.getLocation().getSurroundingsObjects(map).stream().allMatch(o -> o instanceof Enemy)) {
                    OnObjectDeath event = new OnObjectDeath(entity.getLocation(), (SpecialObj) entity);
                    getPluginManager().callEvent(event);
                    if(!event.isCancelled())
                        map.replaceEntity(entity.getLocation(), new Obstacle((SpecialObj) entity));
                }
            }

        }

        if (player.hasLose() || map.getEmptyCases().size() == 0){
            sendMessage("Game is finish. Please press enter.");
            return false;
        }
        score++;
        this.lock = false;
        return true;
    }

    public int randomInt(int min, int max){
        return (int)(Math.random()*(max+1-min))+min;
    }


    private int randomInt(int max) {
        return randomInt(0, max);
    }

    public KtpPluginManager getPluginManager() {
        return pluginManager;
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
        Location pos;
        Enemy enemy;
        do {
            enemy = getRandomList(enemyList);
            pos = map.getRandomPositionSurrounding(enemy.getLocation());

        } while (pos.isOutOfMap(map) || !map.isEmpty(pos));

        return pos;

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
