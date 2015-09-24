package util;

import java.util.ArrayList;
import java.util.List;

import model.Message;
import model.Point;
import model.world.Map;
import model.world.Thing;
import model.world.Tile;

public class LevelCreator {
	
	public static Map getMap(byte[][] b) {
		Tile[][] tiles = getLevelFromByteArray(b);
		List<Thing> things = new ArrayList<Thing>();
		return new Map ("Test",tiles,things,getIslandLabels(b));
	}

	private static Tile[][] getLevelFromByteArray(byte [][] b) {
		Tile[][] tiles= new Tile[b[0].length][b.length];
		for (int i =0;i < b.length; i++){
    		for (int j =0; j < b[0].length; j ++) {
				tiles[i][j] = Tile.getByChar((char)b[i][j]);
			}
		}
		return tiles;
	}
	
	private static List<Message> getIslandLabels(byte[][] yourMap) {
		List<Point> islands = ConnectedIslands.getIslands(yourMap);
		List<Message> messages = new ArrayList<Message>();
		for (Point p: islands) {
			messages.add(new Message(p, "Island"));
		}
		return messages;
	}
	
}
