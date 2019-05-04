package fr.leroideskiwis.mapgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Position {

    private int x;
    private int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static Position getRandomPosition(Map map){
        List<Position> positions = map.getPositions().stream().filter(pos -> !pos.isOutOfMap(map)).collect(Collectors.toList());
        return positions.get(new Random().nextInt(positions.size()));
    }

    public Position add(int x, int y){
        this.x+=x;
        this.y+=y;
        return this;
    }

    public List<Object> getSurroundingsObjects(Map map){

        List<Object> objs = new ArrayList<>();
        for(int x = getX()-1; x <= getX()+1; x++){
            for(int y = getY()-1 ; y <= getY()+1 ;y++){
                if(new Position(x, y).isOutOfMap(map)) continue;
                if(y == getY() && x == getX())
                    continue;
                Object obj = map.getObject(x, y);
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
        return x < 0 || y < 0 || x >= map.getSize() || y >= map.getSize();
    }

    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof Position)) return false;
        if(obj == null) return false;
        Position pos = (Position)obj;

        return pos.getY() == y && pos.getX() == x;

    }
}
