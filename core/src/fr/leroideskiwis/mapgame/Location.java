package fr.leroideskiwis.mapgame;

public class Location {

    public final int x;
    public final int y;

    public Location(int x, int y){
        this.x = x;
        this.y = y;
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

        return equals(pos.x, pos.y);

    }
}
