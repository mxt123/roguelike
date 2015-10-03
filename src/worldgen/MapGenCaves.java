package worldgen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import util.ConnectedIslands;

import model.Message;
import model.Point;
import model.world.Actor;
import model.world.Map;
import model.world.Monster;
import model.world.Player;
import model.world.PolyRoom;
import model.world.Tile;

public class MapGenCaves extends MapGenBase {
	
	private static final double CHANCE_TO_START_WALL = 0.36;
	private static final int MAX_THINGS = 100;
	
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
		
		List<ArrayList<Point>> islands = ConnectedIslands.getIslands(map.getLevel(), Tile.SPACE);
		List<PolyRoom> places = new ArrayList<PolyRoom>();
		
		for (List<Point> island : islands) {
			places.add( new PolyRoom ("island",island));
		}
		
		// only add the largest room delete the rest
		Collections.sort(places);
		for (int i =0 ; i < places.size(); i++){
			if (i == 0) {
				map.setRooms(Arrays.asList(places.get(i)));
			} else {
				PolyRoom p = places.get(0);
				for (Point pnt : p.getPoints()) {
					map.getLevel()[pnt.getY()][pnt.getX()] = Tile.WALL; 
				}
			}
		}
		places = null;
		
		int count = 0;
		while  (count <= MAX_THINGS) {	
			PolyRoom room = map.getRooms().get(0);
			Point p = room.getPoints().get(0); 
			map.getPermanentMessages().add(new Message(p,"A Cave"));
			if (count == 0) {
				map.getThings().add(new Player(
						Monster.PLAYER,
						map,
						room.getRandomPoint(),
						Tile.PERSON,
						Color.YELLOW,
						"Player",
						"this is you :)"
						));
			} else {
				map.getThings().add(new Actor(
						Monster.GOBLIN,
						map,
						room.getRandomPoint(),
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