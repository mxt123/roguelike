package model.world;

import java.util.ArrayList;
import java.util.List;

import util.Distances;

import model.Message;
import model.Point;

public class Map {
	
	private String name;
	private Tile[][] level;
	private boolean[][] visited;
	private List<Thing> things;
	private List<Message> messages = new ArrayList<Message>();
	private List<Message> permanentMessages = new ArrayList<Message>();
	private List<PolyRoom> rooms = new ArrayList<PolyRoom>();
	private List<Point> lights = new ArrayList<Point>();
	
	public Tile getTargetPoint(final int x, final int y) {
		Tile result = null;
		if (0 >= x && x <= this.getWidth() && y >=0 && y <= this.getHeight())  {
			 result =  level[y][x];
		} 
		return result;
	}
	
	public List<Point> getRoomPoints() {
		List <Point> result = new ArrayList<Point>();
		for (PolyRoom room: rooms) {
			for (Point p : room.getPoints()) {
				result.add(p);
			}
		}
		return result;
	}
	
	public Point getNearestUnVisitedCellToActor(Actor a) {
		Point nearest = null;
		final Point apos = a.getLocation();
		
		for (Point p: this.getRoomPoints()) {
			if ( apos != p && !visited[p.getX()][p.getY()]) {
				if (nearest==null || Distances.manhattanDistance(apos,p) <  Distances.manhattanDistance(nearest, p)) {
					nearest = p;
				}
			}
		}
		return nearest;
	}
	
	public List<Point> getNonRoomPoints() {
		final List<Point> roomPoints = getRoomPoints();
		List <Point> result = new ArrayList<Point>();
		for (int i = 0; i < level.length; ++i) {
	        for (int j = 0; j < level[0].length; ++j) {
	        	if (!roomPoints.contains(level[i][j])) {
	        		result.add(new Point(i,j));
	        	}
	        }
	    }
		return result;
	}
	
	public List<Thing> getThingsAt(Point p){
		List<Thing> things = new ArrayList<Thing>();
		for (Thing t : this.things) {
			if (t.getLocation().equals(p)){
				things.add(t);
			}
		}
		return things;
	}
	
	public List<Actor> getActorsAt(Point p){
		List<Actor> things = new ArrayList<Actor>();
		for (Thing t : this.things) {
			if (t instanceof Actor && t.getLocation().equals(p)){
				things.add((Actor) t);
			}
		}
		return things;
	}
	
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

	public synchronized List<Thing> getThings() {
		return things;
	}

	public synchronized void setThings(List<Thing> things) {
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

	public List<Message> getPermanentMessages() {
		return permanentMessages;
	}

	public void setPermanentMessages(List<Message> permanentMessages) {
		this.permanentMessages = permanentMessages;
	}

	public List<PolyRoom> getRooms() {
		return rooms;
	}

	public void setRooms(List<PolyRoom> places) {
		this.rooms = places;
	}

	public List<Point> getLights() {
		return lights;
	}

	public void setLights(List<Point> lights) {
		this.lights = lights;
	}

}
