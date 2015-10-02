package util;
import java.util.ArrayList;
import java.util.List;

import model.Point;
import model.world.Tile;

//http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html


// lifted from c version of this algorithm  on connected graph theory adapted to java 
// and to calculate islands points and return as data structers rather than just count them.
// http://www.geeksforgeeks.org/find-number-of-islands/
public class ConnectedIslands {

	// A function to check if a given cell (row, col) can be included in DFS
	private static boolean isSafeMatch(Tile M[][], Tile tileType, int row, int col, boolean [][] visited)
	{
	    return (row >= 0) && (row < M.length) &&     // row number is in range
	           (col >= 0) && (col < M[0].length) &&     // column number is in range
	           (M[row][col] == tileType && !visited[row][col]); // value is 1 and not yet visited
	}
	
	// TODO return the point not just a boolean
	
	// A utility function to do DFS for a 2D boolean matrix. It only considers
	// the 8 neighbors as adjacent vertices
	static List<Point> DFS(Tile M[][], Tile tileType, int row, int col, boolean visited[][], List<Point> island)
	{
	    // These arrays are used to get row and column numbers of 8 neighbors 
	    // of a given cell
	    int rowNbr[] = {-1, -1, -1,  0, 0,  1, 1, 1};
	    int colNbr[] = {-1,  0,  1, -1, 1, -1, 0, 1};
	 
	    // Mark this cell as visited
	    visited[row][col] = true;
	 
	    // Recur for all <diagonally> connected neighbours
	    for (int k = 0; k < 8; ++k)
	        if (isSafeMatch(M, tileType, row + rowNbr[k], col + colNbr[k], visited) ) { 
	        	island.add(new Point(col,row));
	            DFS(M, tileType, row + rowNbr[k], col + colNbr[k], visited, island);
	        }
	    return island;
	}

	public static List<ArrayList<Point>> getIslands(Tile M[][], Tile tileType)
	{
		List<ArrayList<Point>> islands = new ArrayList<ArrayList<Point>>();
		//List<Point> result = new ArrayList<Point>();
	    // Make a bool array to mark visited cells.
	    // Initially all cells are unvisited
	    boolean visited[][] = new boolean[M.length][M[0].length];
	 
	    // Initialize count as 0 and traverse through all cells
	    for (int i = 0; i < M.length; ++i) {
	        for (int j = 0; j < M[0].length; ++j) {
	        	List<Point> island = new ArrayList<Point>();
	            if (M[i][j] == tileType && !visited[i][j]) // If a cell with value (tile) is not visited yet, then new island found
	            {      
	            	List<Point> newIsland = (ArrayList<Point>) DFS(M, tileType,  i, j, visited, island);     // Visit all cells in this island.
	               // result.add(new Point(j,i));
	            	if (newIsland.size() > 0) {
	            		islands.add((ArrayList<Point>) newIsland);
	            	}
	            }
	        }
	    }
	    
	    return islands;
	}
	
}
