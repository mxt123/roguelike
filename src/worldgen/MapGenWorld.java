package worldgen;

import java.awt.Color;

import model.Point;
import model.world.Map;
import model.world.Player;
import model.world.Tile;

public class MapGenWorld {
	
	private static final double CHANCE_TO_START_SEA = 0.43;

	public static int countAliveNeighbours(Map map ,int x, int y)
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
		      else if ((grid[checkY][checkX]) == Tile.SEA) {
		         count += 1;
		      } 
		    }
		}
		return count;
	}
	
	public static Map evolveGrid(Map map) {
		
		Tile[][] grid = map.getLevel();
		Tile[][] newgrid = grid;
		
		  for (int y = 0; y < grid.length-1 ; y++) {
		    for (int x = 0; x < grid[0].length-1 ; x++) {    
		      int count = countAliveNeighbours(map,x,y);
			      boolean isAlive = (grid[y][x]) == Tile.SEA;
			      if (isAlive) {
			        if ( count < 3) {
			          newgrid[y][x] = Tile.LAND;
			        } 
			        else {	
			          newgrid[y][x] = Tile.SEA;
			        } 
			      }
			       else if (!isAlive) {
			        if (count > 4 ){
			          newgrid[y][x] = Tile.SEA;
			        } else {
			          newgrid[y][x] = Tile.LAND;
			        }
			      }
		      	}
		  }
		  map.setLevel(grid);
		  return map;
	}

	public static Map generateGrid(Map map){  
		Tile[][] grid = map.getLevel();
		  for (int y = 0; y < grid.length ; y++) {
		    for (int x = 0; x < grid[0].length ; x++) {    
		        if (  Math.random() < CHANCE_TO_START_SEA) {
		          grid[y][x] = Tile.SEA;
		        }
		        else {
		          grid[y][x] = Tile.LAND;  
		        }
		      }
		    }
		map.setLevel(grid);
		return map;
	}
	
	public static Map newWorld(String name, int height, int width, int generations) {
		Map map =  Map.newFilledMap(name, Tile.SEA, height, width);
		MapGenWorld.generateGrid(map);
		for (int i = 0; i < generations;i++) {
			MapGenWorld.evolveGrid(map);
		} 
		
		map.getThings().add(new Player(
				map,
				new Point(0,0),
				Tile.PERSON,
				Color.YELLOW,
				"Player",
				"this is you :)"
				));
		
		return map;
	}
	
}