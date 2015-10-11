package util;


import java.util.ArrayList;
import java.util.List;

import model.Point;
import model.world.Map;
import model.world.PolyRoom;
import model.world.Tile;

// queue rather than recursive, adapted from pseudocode on http://stackoverflow.com/questions/10667497/algorithm-find-groups-of-connected-tiles
public class FloodFill {

	static final int rowNbr[] = {  0, 1, 0,-1};
	static final int colNbr[] = { -1, 0, 1, 0};
	
	public static List<PolyRoom> getRooms(final Map map, final Tile roomCell) {
		
		final List<PolyRoom> result = new ArrayList<PolyRoom>();
		final boolean visited[][] = new boolean[ map.getHeight()][ map.getWidth()];
		
		
		for (int row = 0; row < map.getHeight(); row ++) {
			for (int col = 0; col < map.getWidth(); col++) {
 				if (map.getLevel()[col][row] == roomCell && !visited[col][row]) {
					final List<Point> connectedPoints = floodFill(map,row,col,roomCell,visited);
					final PolyRoom room = new PolyRoom("Room"+result.size()+1,connectedPoints);
					result.add(room);
				}
			}
		}
		return result;
	}
	
	private static boolean isSafeMatch(Map map, Tile tileType, int row, int col, boolean [][] visited)
	{
	    return (row >= 0) && (row < map.getHeight()) &&    
	           (col >= 0) && (col < map.getWidth()) &&    
	           (map.getLevel()[col][row] == tileType && !visited[col][row]); 
	}

	public static List<Point> floodFill(Map map, int row, int col, Tile roomCell, boolean [][] visited) {
		
		final List<Point> region = new ArrayList<Point>();
		final List<Point> queue = new ArrayList<Point>();
		
		queue.add(new Point(row,col));
		visited[col][row] = true;
		
		while (queue.size()>0) {
			Point p = queue.get(0);
			queue.remove(p);
			region.add(p);
			
			for (int k = 0; k < 4; k++) {
			final int rowCheck = p.getX() + rowNbr[k];
			final int colCheck = p.getY() + colNbr[k];
		       if (isSafeMatch(map, roomCell, rowCheck, colCheck, visited) ) { 
		    	   Point newPoint = new Point( rowCheck,colCheck);
		    	   region.add(newPoint);
		    	   queue.add(newPoint);
		    	   visited[colCheck][rowCheck] = true;
		       }
		      
			}
		}
		
		return region;		
	}

}
