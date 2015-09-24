package util;

public class Randoms {
	public static int getRandom(int min, int max) {
    	return min + (int)(Math.random() * ((max - min) + 1));	
    }
}
