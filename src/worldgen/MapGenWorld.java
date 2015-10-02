package worldgen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import util.ConnectedIslands;

import model.Message;
import model.Point;
import model.world.Map;
import model.world.Monster;
import model.world.Player;
import model.world.Tile;

public class MapGenWorld extends MapGenBase {
	
	private static final double CHANCE_TO_START_SEA = 0.43;

	public static Map newWorld(String name, int height, int width, int generations) {
		Map map =  Map.newFilledMap(name, Tile.SEA, height, width);
		generateGrid(map, Tile.SEA, Tile.LAND, CHANCE_TO_START_SEA);
		for (int i = 0; i < generations;i++) {
			MapGenWorld.evolveGrid(map,Tile.SEA,Tile.LAND);
		} 
		
		boolean [][] visited = new boolean [map.getHeight()][map.getWidth()];
		
		for (int i =0;i < map.getHeight(); i++){
    		for (int j =0; j < map.getWidth(); j ++) {
    			visited[i][j] = false;
    		}
    	}
		
		// label the island
		List<ArrayList<Point>> islands = ConnectedIslands.getIslands(map.getLevel(), Tile.LAND);
		for (List<Point> island : islands) {
			Point p = island.get(0); // get the first point change to get first one
			map.getPermanentMessages().add(new Message(p,"Island"));
		}
		
		// get islands should probably return the whole island not just start of graph
		
		
		// find a suitable place to put the player
		// put them in the middle of an island
		
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