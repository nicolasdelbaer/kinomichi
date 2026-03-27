package be.nidel.utils;

public class RandomUtils {

    public static int getRandomInt(int from, int to){
        double rd = Math.random();
        return (int) (rd * (to - from) + from);
    }
}
