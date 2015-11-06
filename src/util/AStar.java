package util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Message;
import model.Point;
import model.world.Thing;
import model.world.Tile;

// written from pseudocode at https://en.wikipedia.org/wiki/A*_search_algorithm
public class AStar {

	// need a util map for default values of infinity for map of all viable points
	private static final int MAX = 999999;

	// straight lines only
	private static int rowNbr[] = { 0, 1, 0, -1 };
	private static int colNbr[] = { -1, 0, 1, 0 };

	private static Map<Point, Integer> initScores(List<Point> points,Integer initValue) {
		Map<Point, Integer> result = new HashMap<Point, Integer>();
		for (Point p : points) {
			result.put(p, initValue);
		}
		return result;
	}

	private static Point getLowScore(Map<Point, Integer> scores,List<Point> openSet) {
		Point result = null;
		Integer lowScore = MAX + 1;
		for (Point p : openSet) {
			Integer score = scores.get(p);
			if (score < lowScore) {
				lowScore = score;
				result = p;
			}
		}
		
		return result;
	}

	private static boolean isSafe(Tile M[][], Point p, List<Tile> roomTiles) {
		return (p.getX() >= 0) && (p.getX() < M.length) && // is in range
				(p.getY() >= 0) && (p.getY() < M[0].length) && // is a passable cell;
					roomTiles.contains(M[p.getY()][p.getX()]);
	}
	
	private static List<Point> reconstructPath(Point start, Map<Point,Point> cameFrom, Point current) {
	    List<Point> result = new ArrayList<Point>();
	    result.add(current);
	    while ( cameFrom.containsKey(current)) {
	        current = cameFrom.get(current);
	        result.add(current);
	    };
	    result.remove(start);
	    return result;
	}

	public static List<Point> aStar(Point start, Point goal, model.world.Map map, List<Tile> roomTiles) {
		final List<Point> closedSet = new ArrayList<Point>(); // The set of evaluated nodes
		final List<Point> openSet = new ArrayList<Point>(); // The set of nodes to evaluate
		final Map<Point,Point> cameFrom = new HashMap<Point,Point>(); // The map of navigated nodes

		//openSet.addAll(map.getRoomPoints());
		
		openSet.add(start);
		//openSet.add(goal);
		
		// cost of pointswith default value of Infinity
		Map<Point, Integer> g_score = initScores(openSet, MAX);
		g_score.put(start, 0); // Cost from start along best known path.

		// Estimated total cost from start to goal through y.
		Map<Point, Integer> f_score = initScores(openSet, MAX);
		f_score.put(start, g_score.get(start) + Distances.manhattanDistance(start, goal));

		// while OpenSet is not empty
		while (openSet.size() > 0 ) {
			// current := the node in OpenSet having the lowest f_score[] value
			Point current = getLowScore(f_score,openSet);
			openSet.remove(current);
			closedSet.add(current);
			
			// if current = goal return reconstruct_path(Came_From, goal)
			if (current.equals(goal)) {
				return reconstructPath(start,cameFrom,current);
			}
			
			// for each neighbor of current
			for (int i = 0; i < 4; i++) {
				
				Point neighbour = new Point(current.getX() - colNbr[i],	current.getY() - rowNbr[i]);
				// if neighbor in ClosedSet
				if (!isSafe(map.getLevel(), neighbour, roomTiles) || closedSet.contains(neighbour)) {
					continue; // Ignore the neighbor which is already evaluated or not safe
				}
				
				Integer tentative_g_score = g_score.get(current)+ Distances.manhattanDistance(current, neighbour); // length of this path.
				
				if (!openSet.contains(neighbour) && !closedSet.contains(neighbour)) { // Discover a new node
					openSet.add(neighbour);
 				} else if (!roomTiles.contains(neighbour) || g_score.get(neighbour)== null || tentative_g_score >= g_score.get(neighbour)) {
					continue; // This is not a better path.
				}
				// This path is the best until now. Record it!
				cameFrom.put(neighbour,current);

				g_score.put(neighbour, tentative_g_score);
				f_score.put(neighbour,g_score.get(neighbour) + Distances.manhattanDistance(neighbour, goal));
			}

		}
		
		return null;
		
	}
	
}
