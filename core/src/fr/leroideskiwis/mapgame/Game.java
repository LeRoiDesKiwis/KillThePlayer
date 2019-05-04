package fr.leroideskiwis.mapgame;

import com.badlogic.gdx.Gdx;

import fr.leroideskiwis.mapgame.entities.*;
import fr.leroideskiwis.mapgame.specialobjects.*;
import fr.leroideskiwis.plugins.KtpPluginManager;
import fr.leroideskiwis.plugins.events.OnObjectDeath;
import fr.leroideskiwis.plugins.events.OnObjectSpawn;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Game {

    private boolean noClearingScreen;
    private boolean debugMode;
    private List<Class<? extends SpecialObj>> specialObjs = new ArrayList<>();
    private List<String> bufferSysout = new ArrayList<>();
    private int score;
    private Map map;
    private Player player;
    private int size = 30;
    //private JSONConfiguration configuration;
    public final String version = "v1.1.0";
    private KtpPluginManager pluginManager = new KtpPluginManager(this);

    public void setScore(int score){
        this.score = score;
    }
    
    public int getScore(){
        return score;
    }

    public void addScore(int score){
        this.score+= score;
        sendMessage("You win "+score+"pt"+(score == 1 ? "" : "s"));
    }

    private Entity parseObject(String s) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        switch(s){
            case "!":
                return getRandomObj();
            case "SP":
                return new ClearEnnemies(this);
            case "P":
                return new Player(this, map);
            case "E":
                return new Enemy(map);
            case "O":
                return new Obstacle();
            case ".":
                return null;
            default:
                return null;

        }

    }

    private SpecialObj getRandomObj() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
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

        return list.get(new Random().nextInt(list.size()));

    }

    private void tryLoadFile(File file, boolean create) throws Exception {
        if(!file.exists()) {
            if(create) file.createNewFile();
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));

        Entity[][] content = new Entity[size][size];
        int x = 0;

        for(String line = reader.readLine(); line != null; line = reader.readLine()){
            String[] lineMap = line.split(" ");

            for (int y = 0; y < lineMap.length; y++) {
                String s = lineMap[y];
                content[x][y] = parseObject(s);
            }

            x++;
        }

        map = new Map(this, content);

        if(map.getObjectsByType(Player.class).isEmpty())
            map.generateRandom(new Player(this, map));
        else player = map.getObjectsByType(Player.class).get(0);
        if(map.getObjectsByType(Enemy.class).isEmpty())
            map.generateRandom(new Enemy(map));
    }

    public void setMap(Map map){
        if(map != null)
            this.map = map;
    }

    public void sendMessage(String s){
        bufferSysout.add(s);
    }

    public Game() throws Exception {
        //this.size = configuration.getInt("size", 30);
        Gdx.app.log("INFO", "new instance of game");
        debugMode = false;
        this.pluginManager = new KtpPluginManager(this);

        //tryLoadFile(new File("./map.ktp"), true);

        specialObjs.add(RayonEnnemyKiller.class);
        specialObjs.add(InvinciblePlayer.class);
        specialObjs.add(TriggerAllSpecial.class);
        specialObjs.add(ClearEnnemies.class);
        specialObjs.add(Reparator.class);
        specialObjs.add(OpenPath.class);
        //Not added because chiant specialObjs.add(new Teleporter());

        map = new Map(this, size, size);

        player = new Player(this, map);
        map.generateRandom(player);

        Gdx.app.log("INFO", "Generate defaults ennemies");

        for(int i = 0, rand = randomInt(1, 3); i < rand; i++){
            map.generateRandom(new Enemy(map));
        }
        Gdx.app.log("INFO", "Generate defaults obstacles");

        for(int i = 0, rand = randomInt(3, 7); i < rand; i++){
            map.generateRandom(new Obstacle());
        }

        //TODO round it System.out.println("% of empty cases : "+(int)((double)map.getEmptyCases().size()/(double)map.getTotalSize()*100.0)+"%");

        pluginManager.loadPlugins();
    }

    public boolean update() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        if (map.getObjectsByType(Coin.class).size() == 0) map.generateRandom(new Coin(randomInt(5, 10)));

        for (int i = 0, rand = randomInt(1, 2); i < rand; i++) {
            spawnEnnemy(map);
        }

        if (Math.random() < 0.05) {

            SpecialObj special = getRandomObj();
            Position position = special.spawn(this, map, player);
            OnObjectSpawn event = new OnObjectSpawn(position, special);
            getPluginManager().callEvent(event);
            if(!event.isCancelled())
                map.setObject(position, event.getSpecialObj());
        }

        for (Position position : map.getPositionsByType(SpecialObj.class)) {

            if (Math.random() < 0.001) {

                if (position.getSurroundingsObjects(map).stream().allMatch(o -> o instanceof Enemy)) {
                    OnObjectDeath event = new OnObjectDeath(position, (SpecialObj) map.getObject(position));
                    getPluginManager().callEvent(event);
                    if(!event.isCancelled())
                        map.replaceObject(position, new Obstacle((SpecialObj) map.getObject(position)));
                }
            }

        }

        if (player.hasLose() || map.getEmptyCases().size() == 0) return false;
        score++;
        return true;
    }

    public int randomInt(int min, int max){
        if(min < 0) min = 1;
        if(max < 0) max = 1;
        return new Random().nextInt(max-min)+min;
    }

    public KtpPluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * @see Map#setObject(int, int, Entity)
     */

    public boolean spawn(int x, int y, Entity entity){
        return map.setObject(x, y, entity);
    }

    public void debug(String s){
        Gdx.app.log("[LOG] ", s);
    }

    private void spawnEnnemy(Map map) {
        if(debugMode) return;
        if(Math.random() < 0.05){

            Enemy enemy = new Enemy(map);
            map.generateRandom(enemy);

        }
        List<Position> positionList = map.getPositionsByType(Enemy.class);
        if(map.getPositionsByType(Enemy.class).isEmpty()){
            Enemy enemy = new Enemy(map);
            map.generateRandom(enemy);
        }
		
		if(map.getEmptyCases().size() == 0) return;

		Position pos;
		Enemy enemy;
		do {
		    enemy = (Enemy) map.getObject(positionList.get(new Random().nextInt(positionList.size())));
		    pos = map.getRandomPositionSurrounding(map.getPositionByObject(enemy));

		} while (pos.isOutOfMap(map) || !map.setObject(pos, new Enemy(map)));


        //System.out.println("A new enemy has spawned in "+pos);
    }

    public Map getMap() {
        return map;
    }

    public Player getPlayer(){
        return player;
    }

    public List<String> getBuffer() {
        return bufferSysout;
    }

    /*public JSONConfiguration getConfig() {
        return configuration;
    }*/
}
