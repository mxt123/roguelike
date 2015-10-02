package worldgen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import util.ConnectedIslands;

import model.Message;
import model.Point;
import model.world.Actor;
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
		
		// label the island and add some things
		int count = 0;
		List<ArrayList<Point>> islands = ConnectedIslands.getIslands(map.getLevel(), Tile.LAND);
		for (List<Point> island : islands) {
			Point p = island.get(0); // get the first point change to get first one
			map.getPermanentMessages().add(new Message(p,"Island"));
			if (count == 0) {
				map.getThings().add(new Player(
						Monster.PLAYER,
						map,
						island.get(0),
						Tile.PERSON,
						Color.YELLOW,
						"Player",
						"this is you :)"
						));
			} else {
				map.getThings().add(new Actor(
						Monster.GOBLIN,
						map,
						island.get(0),
						Tile.GOBLIN,
						Color.GREEN,
						"goblin",
						"this is a goblin"
						));	
			}
			
			count++;
		}
				
		map.setVisited(visited);
		
		return map;
	}
	
}