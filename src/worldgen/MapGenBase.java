package worldgen;

import model.world.Map;
import model.world.Tile;

public abstract class MapGenBase {

	public static int countAliveNeighbours(Tile tile, Map map ,int x, int y)
	{
		Tile[][] grid = map.getLevel();
		int count = 0;
		for (int i= -1;i<2;i++) {
		    for (int j= -1;j<2;j++){
		      int checkX = x+j;
		      int checkY = y+i;  
		      if (checkX == x && checkY ==y) {
		        // do nothing
		      } else if (checkY <= 0 || checkX <= 0 || checkY >= grid.length || checkX >= grid[0].length) {
		          count +=1;
		      }
		      else if ((grid[checkY][checkX]) == tile) {
		         count += 1;
		      } 
		    }
		}
		return count;
	}
	
	public static Map evolveGrid(Map map, Tile full, Tile empty) {
		
		Tile[][] grid = map.getLevel();
		Tile[][] newgrid = grid;
		
		  for (int y = 0; y < grid.length ; y++) {
		    for (int x = 0; x < grid[0].length ; x++) {    
		      int count = countAliveNeighbours(full, map,x,y);
			      boolean isAlive = (grid[y][x]) == full;
			      if (isAlive && ! isBorder(grid,x,y) ) {
			        if ( count < 3) {
			          newgrid[y][x] = empty;
			        } 
			        else {	
			          newgrid[y][x] = full;
			        } 
			      }
			       else if (!isAlive) {
			        if (count > 4 ){
			          newgrid[y][x] = full;
			        } else {
			          newgrid[y][x] = empty;
			        }
			      }
		      	}
		  }
		  map.setLevel(grid);
		  return map;
	}
	
	public static Map generateGrid(Map map, Tile full, Tile empty, double startAlive){  
		Tile[][] grid = map.getLevel();
		  for (int y = 0; y < grid.length ; y++) {
		    for (int x = 0; x < grid[0].length ; x++) {    
		        if (  Math.random() < startAlive || isBorder(grid,x,y) ) {
		          grid[y][x] = full;
		        }
		        else {
		          grid[y][x] = empty;  
		        }
		      }
		    }
		map.setLevel(grid);
		return map;
	}
	
	private static boolean isBorder(Tile[][] grid, int x, int y) {
		return x == 0  || x == grid[0].length -1 || y == 0 || y == grid.length -1;
	}

}
