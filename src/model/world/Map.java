package model.world;

import java.util.ArrayList;
import java.util.List;

import model.Message;
import model.Point;

public class Map {
	
	private String name;
	private Tile[][] level;
	private boolean[][] visited;
	private List<Thing> things;
	private List<Message> messages = new ArrayList<Message>();
	
	public List<Thing> getThings(Point point) {
		ArrayList<Thing> ts = new ArrayList<Thing>();
		for (Thing t:this.things ){
			if (t.getLocation().equals(point)) {
				ts.add(t);
			}
		}
		return ts;
	}
	
	public boolean isImpassibleThingAt(Point p){
		boolean result = false;
		List<Thing> things = getThings(p);
		for (Thing t: things) {
			if(!t.getTile().isPassable()) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	public Player getPlayer() {
		for (Thing t: things) {
			if (t.getTile().equals(Tile.PERSON)) {
				return (Player) t;
			}
		}
		return null;
	}
	
	public Map (String name, Tile[][] level, List<Thing> things, List<Message> messages) {
		this.name = name;
		this.level = level;
		this.setThings(things);
		this.setMessages(messages);
	}
	
	public static Map newFilledMap(String name, Tile tile, int height, int width) {
		Tile[][] tiles = new Tile[height][width];
		for (int i =0;i < height; i++){
    		for (int j =0; j < width; j ++) {
    			tiles[i][j] = tile;
    		} 
    	}
		Map map = new Map(name,tiles,new ArrayList<Thing>(),new ArrayList<Message>());
		return map;
	}
	
	public int getHeight() {
		return this.level.length;
	}
	
	public int getWidth() {
		return this.level[0].length;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Tile[][] getLevel() {
		return level;
	}
	public void setLevel(Tile[][] level) {
		this.level = level;
	}

	public List<Thing> getThings() {
		return things;
	}

	public void setThings(List<Thing> things) {
		this.things = things;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public boolean[][] getVisited() {
		return visited;
	}

	public void setVisited(boolean[][] visited) {
		this.visited = visited;
	}

}
