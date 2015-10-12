package worldgen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import util.Randoms;

import model.Point;
import model.Rect;
import model.world.Actor;
import model.world.Map;
import model.world.Monster;
import model.world.Player;
import model.world.Thing;
import model.world.Tile;


// adapted from http://www.roguebasin.com/index.php?title=Complete_Roguelike_Tutorial,_using_python%2Blibtcod,_part_3
public class MapGenDungeon {
	
	static int ROOM_MAX_SIZE = 15;
	static int ROOM_MIN_SIZE = 3;
	static int MAX_ROOMS = 300;

	public static void createRoom(Map map, Rect room) {
	    for (int x = room.getX();x < room.getX() + room.getW() -1; x++) {
	        for (int y = room.getY(); y < room.getY() + room.getH()  -1; y++) {
	            map.getLevel()[y][x] = Tile.SPACE;
	        }
	    }
	}
	
	public static void createHTunnel(Map map, int x1, int x2, int y)
	{
		for (int x = Math.min(x1,x2); x < Math.max(x1, x2) +1; x++){
			 map.getLevel()[y][x] = Tile.SPACE;
		}
	}
	
	public static void createVTunnel(Map map, int y1, int y2, int x)
	{
		for (int y = Math.min(y1,y2); y < Math.max(y1,y2); y++){
			map.getLevel()[y][x] = Tile.SPACE;
		}
	}
 
	public static Map newFullMap(Map map) {
		
		boolean [][] visited = new boolean [map.getHeight()][map.getWidth()];
		
		for (int i =0;i < map.getHeight(); i++){
    		for (int j =0; j < map.getWidth(); j ++) {
    			map.getLevel()[i][j] = Tile.WALL;
    			visited[i][j] = false;
    		}
    	}
	
		List<Rect> rooms = new ArrayList<Rect>();
		
		for (int r = 0; r< MAX_ROOMS;r++){
			int w = Randoms.getRandom(ROOM_MIN_SIZE,ROOM_MAX_SIZE);
			int h = Randoms.getRandom(ROOM_MIN_SIZE,ROOM_MAX_SIZE);
			int x = Randoms.getRandom(1, map.getWidth() - w);
			int y = Randoms.getRandom(1, map.getHeight() - h);
			Rect room = new Rect(x,y,w,h);
			boolean fail = false;
			
			Iterator<Rect> it = rooms.iterator();
			while (it.hasNext() && !fail){
				if (room.intersect(it.next())) {
					fail = true;
					break;
				}
			}
			
			if (!fail) {
				rooms.add(room);
				createRoom(map,room);
			}
		}
		
		int roomNum = 0;
		for (Rect r : rooms){
			Point center = r.center();
			if (roomNum == 0) {
				//nothing for now
			} else {
				Point centerPrevious = rooms.get(roomNum-1).center();
				if (Randoms.getRandom(1, 2) < 2) {
					createHTunnel(map, centerPrevious.getX(), center.getX(), centerPrevious.getY());
					createVTunnel(map, centerPrevious.getY(), center.getY(), center.getX());
				} else {
					createVTunnel(map, centerPrevious.getY(), center.getY(), centerPrevious.getX());
					createHTunnel(map, centerPrevious.getX(), center.getX(), center.getY());
				}
			}
			roomNum++;
			
		}
		
		int labelCount = 0;
		for (Rect room:rooms){
			Point center = room.center();
			if (labelCount == 0) {
				map.getThings().add(new Player(
					Monster.PLAYER,
					map,
					new Point(center.getX(),center.getY()),
					Tile.PERSON,
					Color.YELLOW,
					"Player",
					"this is you :)",
					1
					));
			} else if( labelCount % 2== 1){
				map.getThings().add(new Actor(
					Monster.GOBLIN,
					map,
					new Point(center.getX(),center.getY()),
					Tile.GOBLIN,
					Color.GREEN,
					"goblin",
					false,
					true,
					"this is a goblin",
					1
					));	
			} else if( labelCount % 3== 0){
				map.getThings().add(new Thing(
						map,
						new Point(center.getX(),center.getY()),
						Tile.COIN,
						Color.YELLOW,
						"coin",
						"this is gold!",
						0,
						false,
						false
						));
				}
			else {
				map.getThings().add(new Actor(
						Monster.GOBLIN,
						map,
						new Point(center.getX(),center.getY()),
						Tile.OGRE,
						Color.RED,
						"ogre",
						false,
						true,
						"this is an ogre",
						1
						));	
				
		}
			labelCount++;
		}
	
		map.setVisited(visited);
				   
		return map;
	}
	
	            
}
