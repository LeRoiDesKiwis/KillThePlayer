package fr.leroideskiwis.mapgame;

import com.badlogic.gdx.Gdx;

import fr.leroideskiwis.mapgame.specialobjects.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

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

    public void setScore(int score){
        this.score = score;
    }
    
    public int getScore(){
        return score;
    }

    public void addScore(int score){
        this.score+= score;
        addInBuffer("You win "+score+"pt"+(score == 1 ? "" : "s"));
    }

    private Object parseObject(String s) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        switch(s){
            case "!":
                return getRandomObj();
            case "SP":
                return new ClearEnnemies(this);
            case "P":
                return new Player(this, map, 'P');
            case "E":
                return new Ennemy(map);
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

    public <T> T getRandomList(List<T> list){

        return list.get(new Random().nextInt(list.size()));

    }

    private void tryLoadFile(File file, boolean create) throws Exception {
        if(!file.exists()) {
            if(create) file.createNewFile();
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));

        Object[][] content = new Object[size][size];
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
            map.generateRandom(new Player(this, map, 'P'));
        else player = map.getObjectsByType(Player.class).get(0);
        if(map.getObjectsByType(Ennemy.class).isEmpty())
            map.generateRandom(new Ennemy(map));
    }

    public void addInBuffer(String s){
        bufferSysout.add(s);
    }

    public Game() throws Exception {
        //this.size = configuration.getInt("size", 30);
        Gdx.app.log("INFO", "new instance of game");
        debugMode = false;

        //tryLoadFile(new File("./map.ktp"), true);

        specialObjs.add(RayonEnnemyKiller.class);
        specialObjs.add(InvinciblePlayer.class);
        specialObjs.add(TriggerAllSpecial.class);
        specialObjs.add(ClearEnnemies.class);
        specialObjs.add(Reparator.class);
        specialObjs.add(OpenPath.class);
        //Not added because chiant specialObjs.add(new Teleporter());

        map = new Map(this, size, size);

        player = new Player(this, map, 'P');
        map.generateRandom(player);

        Gdx.app.log("INFO", "Generate defaults ennemies");

        for(int i = 0, rand = randomInt(1, 3); i < rand; i++){
            map.generateRandom(new Ennemy(map));
        }
        Gdx.app.log("INFO", "Generate defaults obstacles");

        for(int i = 0, rand = randomInt(3, 7); i < rand; i++){
            map.generateRandom(new Obstacle());
        }

        //TODO round it System.out.println("% of empty cases : "+(int)((double)map.getEmptyCases().size()/(double)map.getTotalSize()*100.0)+"%");


    }

    public boolean update() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        if (map.getObjectsByType(Coin.class).size() == 0) map.generateRandom(new Coin(randomInt(5, 10)));

        for(int i = 0, rand = randomInt(1, 2); i < rand; i++){
            spawnEnnemy(map);
        }

        if(Math.random() < 0.05){

            SpecialObj special = getRandomObj();
            map.setObject(special.spawn(this, map, player), special);
        }

        for(Position position : map.getPositionsByType(SpecialObj.class)){

            if(Math.random() < 0.001) {

                if (position.getSurroundingsObjects(map).stream().allMatch(o -> o instanceof Ennemy)) {
                    map.replaceObject(position, new Obstacle((SpecialObj)map.getObject(position)));
                }
            }

        }

        if(player.hasLose() || map.getEmptyCases().size() == 0) return false;
        score++;
        return true;
    }

    public int randomInt(int min, int max){
        if(min < 0) min = 1;
        if(max < 0) max = 1;
        return new Random().nextInt(max-min)+min;
    }

    private void spawnEnnemy(Map map) {
        if(debugMode) return;
        if(Math.random() < 0.05){

            map.generateRandom(new Ennemy(map));

        }
        List<Position> positionList = map.getPositionsByType(Ennemy.class);
        if(map.getPositionsByType(Ennemy.class).isEmpty()){
            Ennemy ennemy = new Ennemy(map);
            map.generateRandom(ennemy);
            return;
        }
		
		if(map.getEmptyCases().size() == 0) return;

        Ennemy ennemy;
        Position pos;
        do {
            ennemy = (Ennemy) map.getObject(positionList.get(new Random().nextInt(positionList.size())));
            pos = map.getRandomPositionSurrounding(map.getPositionByObject(ennemy));

        }while(pos.isOutOfMap(map) || !map.setObject(pos, new Ennemy(map)));

        //System.out.println("A new ennemy has spawned in "+pos);
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
