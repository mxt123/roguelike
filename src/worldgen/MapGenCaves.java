package worldgen;

import java.awt.Color;

import model.Point;
import model.world.Map;
import model.world.Monster;
import model.world.Player;
import model.world.Tile;

public class MapGenCaves extends MapGenBase {
	
	private static final double CHANCE_TO_START_WALL = 0.36;
	
	public static Map newWorld(String name, int height, int width, int generations) {
		Map map =  Map.newFilledMap(name, Tile.WALL, height, width);
		generateGrid(map, Tile.WALL, Tile.SPACE, CHANCE_TO_START_WALL);
		for (int i = 0; i < generations;i++) {
			MapGenCaves.evolveGrid(map,Tile.WALL, Tile.SPACE);
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
				Monster.PLAYER,
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