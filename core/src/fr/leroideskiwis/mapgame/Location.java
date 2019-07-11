package fr.leroideskiwis.mapgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Location {

    private int x;
    private int y;

    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static Location getRandomPosition(Map map){
        List<Location> locations = map.getLocations().stream().filter(pos -> !pos.isOutOfMap(map)).collect(Collectors.toList());
        return locations.get(new Random().nextInt(locations.size()));
    }

    /**
     *
     * @param x x to add to this location
     * @param y y to add to this location
     * @return a new instance of Location who contains the coordinates of this location + the parameters
     */

    public Location add(int x, int y){
        return new Location(this.x+x, this.y+y);
    }

    public List<Object> getSurroundingsObjects(Map map){

        List<Object> objs = new ArrayList<>();

        for(int x = getX()-1; x <= getX()+1; x++){

            for(int y = getY()-1 ; y <= getY()+1 ;y++){

                if(new Location(x, y).isOutOfMap(map)) continue;
                if(y == getY() && x == getX())
                    continue;
                Object obj = map.getEntity(x, y);
                objs.add(obj);

            }

        }

        return objs;

    }

    /**
     *
     * @return the x coordinate
     */

    public int getX(){
        return x;
    }

    /**
     *
     * @return the y coordinate
     */

    public int getY(){
        return y;
    }

    public String toString(){
        return "x:"+x+" and y: "+y;
    }

    public boolean isOutOfMap(Map map){
        return x < 0 || y < 0 || x >= map.getWidth() || y >= map.getHeight();
    }

    public boolean equals(int x, int y){
        return x == this.x && y == this.y;
    }

    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof Location)) return false;
        Location pos = (Location)obj;

        return equals(pos.getY(), pos.getX());

    }
}
