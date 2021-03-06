package worldgen;


import java.awt.Color;
import java.util.List;

import util.FloodFill;
import util.Randoms;

import model.Message;
import model.Point;
import model.world.Actor;
import model.world.Map;
import model.world.Monster;
import model.world.Place;
import model.world.Player;
import model.world.PolyRoom;
import model.world.Thing;
import model.world.Tile;

public class MapGenWorld extends MapGenBase {
	
	private static final double CHANCE_TO_START_SEA = 0.43;
	private static final int MAX_THINGS = 40;

	public static Map newWorld(String name, int height, int width, int generations) {
		
		Map map =  Map.newFilledMap(name, Tile.SEA, height, width);
		generateGrid(map, Tile.SEA, Tile.LAND, CHANCE_TO_START_SEA);
		for (int i = 0; i < generations;i++) {
			MapGenWorld.evolveGrid(map,Tile.SEA,Tile.LAND);
		} 
		
		boolean [][] visited = new boolean [map.getWidth()][map.getHeight()];
		
		for (int i =0;i < map.getHeight(); i++){
    		for (int j =0; j < map.getWidth(); j ++) {
    			visited[i][j] = false;
    		}
    	}
		
		List<PolyRoom> places = FloodFill.getRooms(map,Tile.LAND);
		for (PolyRoom island:places) {
			Point p = island	.getPoints().get(0); 
			map.getPermanentMessages().add(new Message(p,"Island"));
		}
		
		List<PolyRoom> allsea = FloodFill.getRooms(map,Tile.SEA);
			
		map.setRooms(places);
		
		int count = 0;
		while  (count <= MAX_THINGS) {			
			Place place = places.get(Randoms.getRandom(0,places.size()-1));
			Place sea = allsea.get(Randoms.getRandom(0,allsea.size()-1));

			if (count == 0) {
				map.getThings().add(new Player(
						Monster.PLAYER,
						map,
						place.getRandomPoint(),
						Tile.PERSON,
						Color.YELLOW,
						"Player",			
						"this is you :)",
						1
						));
			}  else if (count % 3 == 0 || count % 2 ==0){
				for (int i = 0; i < Randoms.d20() ; i++) {
					Point p = place.getRandomPoint();
					map.getLevel()[p.getY()][p.getX() ] = Tile.TREE;
				}
			} else if (count % 5 == 0){
				map.getThings().add(new Actor(
						Monster.GOBLIN,
						map,
						place.getRandomPoint(),
						Tile.GOBLIN,
						Color.GREEN,
						"goblin",
						false,
						true,
						"this is a goblin",
						1
						));	
			} 
			else {
				map.getThings().add(new Actor(
						Monster.SHARK,
						map,
						sea.getRandomPoint(),
						Tile.SHARK,
						Color.WHITE,
						"shark",
						true,
						false,
						"this is a shark they still walk at the mo ",
						2
						));	
			}
			count++;
		}
				
		map.setVisited(visited);
		
		return map;
	}
	
}