package worldgen;

import java.awt.Color;

import model.Point;
import model.world.Map;
import model.world.Player;
import model.world.Tile;

public class MapGenCaves {
	
	private static final double CHANCE_TO_START_WALL = 0.36;

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
		      else if ((grid[checkY][checkX]) == Tile.WALL) {
		         count += 1;
		      } 
		    }
		}
		return count;
	}
	
	public static Map evolveGrid(Map map) {
		
		Tile[][] grid = map.getLevel();
		Tile[][] newgrid = grid;
		
		  for (int y = 0; y < grid.length ; y++) {
		    for (int x = 0; x < grid[0].length ; x++) {    
		      int count = countAliveNeighbours(map,x,y);
			      boolean isAlive = (grid[y][x]) == Tile.WALL;
			      if (isAlive) {
			        if ( count < 3) {
			          newgrid[y][x] = Tile.SPACE;
			        } 
			        else {	
			          newgrid[y][x] = Tile.WALL;
			        } 
			      }
			       else if (!isAlive) {
			        if (count > 4 ){
			          newgrid[y][x] = Tile.WALL;
			        } else {
			          newgrid[y][x] = Tile.SPACE;
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
		        if (  Math.random() < CHANCE_TO_START_WALL) {
		          grid[y][x] = Tile.WALL;
		        }
		        else {
		          grid[y][x] = Tile.SPACE;  
		        }
		      }
		    }
		map.setLevel(grid);
		return map;
	}
	
	public static Map newWorld(String name, int height, int width, int generations) {
		Map map =  Map.newFilledMap(name, Tile.WALL, height, width);
		MapGenCaves.generateGrid(map);
		for (int i = 0; i < generations;i++) {
			MapGenCaves.evolveGrid(map);
		} 
		boolean [][] visited = new boolean [map.getHeight()][map.getWidth()];
		
		for (int i =0;i < map.getHeight(); i++){
    		for (int j =0; j < map.getWidth(); j ++) {
    			visited[i][j] = false;
    		}
    	}
		
		//TODO edges are always walls
		//TODO carve tunnels linking any isolated caves	
		
		map.getThings().add(new Player(
				map,
				new Point(0,0),
				Tile.PERSON,
				Color.YELLOW,
				"Player",
				"this is you :)"
				));
		
		map.setVisited(visited);
		
		return map;
	}
}