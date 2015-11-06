package util;

import model.Point;

public class Distances {
	
	// manhattan distance while cant move diagonal
	public static int manhattanDistance(Point start, Point goal) {
		final int d = 1;
		final int dx = Math.abs(start.getX() - goal.getX());
		final int dy = Math.abs(start.getY() - goal.getY());
		return d * (dx + dy);
	}

}
