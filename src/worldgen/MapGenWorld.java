package worldgen;

import model.world.Tile;

public class MapGenWorld {
	
	private static final double CHANCE_TO_START_SEA = 0.43;

	public static int countAliveNeighbours(byte[][] grid,int x, int y)
	{
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
		      else if (((char)grid[checkY][checkX]) == Tile.SEA.getCharacter()) {
		         count += 1;
		      } 
		    }
		}
		return count;
	}
	
	public static byte[][] evolveGrid(byte[][] grid) {
		byte[][] newgrid = grid;
		  for (int y = 0; y < grid.length ; y++) {
		    for (int x = 0; x < grid[0].length ; x++) {    
		      int count = countAliveNeighbours(grid,x,y);
			      boolean isAlive = ((char)grid[y][x]) == Tile.SEA.getCharacter();
			      if (isAlive) {
			        if ( count < 3) {
			          newgrid[y][x] = (byte) Tile.LAND.getCharacter();
			        } 
			        else {	
			          newgrid[y][x] = (byte) Tile.SEA.getCharacter();
			        } 
			      }
			       else if (!isAlive) {
			        if (count > 4 ){
			          newgrid[y][x] = (byte) Tile.SEA.getCharacter();
			        } else {
			          newgrid[y][x] = (byte) Tile.LAND.getCharacter();
			        }
			      }
		      	}
		    //}
		  }
		  grid = newgrid;
		  newgrid=null;
		  return grid;
	}

	public static byte[][] generateGrid(byte[][] grid){  
	  for (int y = 0; y < grid.length ; y++) {
	    for (int x = 0; x < grid[0].length ; x++) {    
	        if (  Math.random() < CHANCE_TO_START_SEA) {
	          grid[y][x] = (byte) Tile.SEA.getCharacter();
	        }
	        else {
	          grid[y][x] = (byte) Tile.LAND.getCharacter();  
	        }
	      }
	    }
	return grid;
	}
	
	public static byte[][] newBlankMap(int height, int width) {
		byte[][] map = new byte[height][width];
		for (int i =0;i < height; i++){
    		for (int j =0; j < width; j ++) {
    			map[i][j] = (byte) Tile.SEA.getCharacter();
    		}
    	}
		return map;
	}
	
}