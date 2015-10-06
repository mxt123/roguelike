package worldgen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import util.ConnectedIslands;
import util.Randoms;

import model.Message;
import model.Point;
import model.world.Actor;
import model.world.Map;
import model.world.Monster;
import model.world.Place;
import model.world.Player;
import model.world.PolyRoom;
import model.world.Tile;

public class MapGenWorld extends MapGenBase {
	
	private static final double CHANCE_TO_START_SEA = 0.43;
	private static final int MAX_THINGS = 20;

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

		List<ArrayList<Point>> islands = ConnectedIslands.getIslands(map.getLevel(), Tile.LAND);
		List<PolyRoom> places = new ArrayList<PolyRoom>();
		
		for (List<Point> island : islands) {
			places.add( new PolyRoom ("island",island));
			/*
			for (Point p : island) {
				map.getLevel()[p.getY()][p.getX()] = Tile.COIN;
			} //mark islands for test
			*/
		}
			
		map.setRooms(places);
		int count = 0;
		while  (count <= MAX_THINGS) {			
			Place place = places.get(Randoms.getRandom(0,places.size()-1));
			Point p = place.getPoints().get(0); 
			map.getPermanentMessages().add(new Message(p,"Island"));
			if (count == 0) {
				map.getThings().add(new Player(
						Monster.PLAYER,
						map,
						place.getRandomPoint(),
						Tile.PERSON,
						Color.YELLOW,
						"Player",			
						"this is you :)"
						));
			} else if (count % 2 == 0){
				map.getThings().add(new Actor(
						Monster.GOBLIN,
						map,
						place.getRandomPoint(),
						Tile.GOBLIN,
						Color.GREEN,
						"goblin",
						false,
						"this is a goblin"
						));	
			} else {
				map.getThings().add(new Actor(
						Monster.SHARK,
						map,
						place.getRandomPoint(),
						Tile.SHARK,
						Color.WHITE,
						"shark",
						true,
						"this is a shark"
						));	
			}
			count++;
		}
				
		map.setVisited(visited);
		
		return map;
	}
	
}