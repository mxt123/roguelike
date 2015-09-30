package util;

public class Randoms {
	public static int getRandom(int min, int max) {
    	return min + (int)(Math.random() * ((max - min) + 1));	
    }
	
	public static int d6() {
    	return 1 + (int)(Math.random() * ((6 - 1) + 1));	
    }
	
	public static int d20() {
    	return 1 + (int)(Math.random() * ((20 - 1) + 1));	
    }
}
