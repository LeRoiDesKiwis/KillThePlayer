package fr.leroideskiwis.utils;

public class Interval {

    private int min;
    private int max;

    private Interval(int min, int max) {
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
    }

    public static Interval of(int min, int max){
        return new Interval(min, max);
    }

    public boolean contains(int i){

        return i < max && i > min;

    }

    public boolean notContains(int i){
        return !contains(i);
    }
}
