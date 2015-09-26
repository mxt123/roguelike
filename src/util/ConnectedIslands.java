package util;
import java.util.ArrayList;
import java.util.List;

import model.Point;
import model.world.Tile;


// lifted from c version of this algorithm  on connected graph theory
// http://www.geeksforgeeks.org/find-number-of-islands/
public class ConnectedIslands {

	// A function to check if a given cell (row, col) can be included in DFS
	private static boolean isSafe(Tile M[][], Tile tileType, int row, int col, boolean [][] visited)
	{
	    return (row >= 0) && (row < M.length) &&     // row number is in range
	           (col >= 0) && (col < M[0].length) &&     // column number is in range
	           (M[row][col] == tileType && !visited[row][col]); // value is 1 and not yet visited
	}
	
	// A utility function to do DFS for a 2D boolean matrix. It only considers
	// the 8 neighbors as adjacent vertices
	static void DFS(Tile M[][], Tile tileType, int row, int col, boolean visited[][])
	{
	    // These arrays are used to get row and column numbers of 8 neighbors 
	    // of a given cell
	    int rowNbr[] = {-1, -1, -1,  0, 0,  1, 1, 1};
	    int colNbr[] = {-1,  0,  1, -1, 1, -1, 0, 1};
	 
	    // Mark this cell as visited
	    visited[row][col] = true;
	 
	    // Recur for all connected neighbours
	    for (int k = 0; k < 8; ++k)
	        if (isSafe(M, tileType, row + rowNbr[k], col + colNbr[k], visited) )
	            DFS(M, tileType, row + rowNbr[k], col + colNbr[k], visited);
	}

	public static List<Point> getIslands(Tile M[][], Tile tileType)
	{
		List<Point> result = new ArrayList<Point>();
	    // Make a bool array to mark visited cells.
	    // Initially all cells are unvisited
	    boolean visited[][] = new boolean[M.length][M[0].length];
	   
	 
	    // Initialize count as 0 and traverse through the all cells of
	    // given matrix
	   // int count = 0;
	    for (int i = 0; i < M.length; ++i)
	        for (int j = 0; j < M[0].length; ++j)
	            if (M[i][j] == tileType && !visited[i][j]) // If a cell with value 1 is not
	            {                              // visited yet, then new island found
	                DFS(M, tileType,  i, j, visited);     // Visit all cells in this island.
	                //++count;                   // and increment island count
	                result.add(new Point(j,i));
	            }
	 
	    return result;
	}
	
}
