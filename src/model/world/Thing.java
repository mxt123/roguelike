package model.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import util.Distances;
import util.Randoms;

import model.Direction;
import model.Point;

public class Thing {
	private Map map;
	private Point location;	
	private Tile tile;
	private Color color;
	private String name;
	private String description;
	private boolean active;
	private int speed;
	private boolean swims;
	private boolean walks;
	private List<Point> movementHistory = new ArrayList<Point>();
	
	public Thing(Map map, Point location,Tile tile,Color color, String name, String description,int speed, boolean swims, boolean walks) {
		this.location = location;
		this.tile = tile;
		this.color = color;
		this.name = name;
		this.description = description;
		this.map = map;
		this.speed = speed;
		this.setSwims(swims);
		this.setWalks(walks);
	}
	
	public void act(){
		// nothing for most things :)
	}
	
	public boolean isActive() {
		return active;
	}
	
	// proivide an override for tile message here later
	public String getMessage() {
		return this.getTile().getMessage();
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Actor getNearestActor(Thing toThing) {
		
		Actor nearestThing = null;
		
		for (Thing t: this.getMap().getThings()) {
			if (t instanceof Actor && ! t.equals(toThing)) {
				if (nearestThing == null || t.getDistanceTo(toThing) < nearestThing.getDistanceTo(toThing)){
					nearestThing = (Actor) t;
				}
			}
		}
		return (Actor) nearestThing;
	}
	
	public Actor getNearestActiveActor(Thing toThing) {
		
		Actor nearestThing = null;
		
		for (Thing t: this.getMap().getThings()) {
			if (t instanceof Actor && ! t.equals(toThing) && t.isActive()) {
				if (nearestThing == null || t.getDistanceTo(toThing) < nearestThing.getDistanceTo(toThing)){
					nearestThing = (Actor) t;
				}
			}
		}
		return (Actor) nearestThing;
	}
	
	public int getDistanceTo(Thing t) {
		int distance = Distances.manhattanDistance(this.location,t.location);
		// TODO this is relevant if no diagonal movement check for diagonal 
		if (distance == 1 && this.getLocation().getX() != t.getLocation().getX()  && this.getLocation().getY() != t.getLocation().getY()) {
			distance +=1;
		}
		return distance;
	}
	
	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	public boolean move(Direction direction, int distance){
		int x = this.getLocation().getX();
		int y = this.getLocation().getY();
		switch (direction) {
		case NORTH:
			y -= 1;
			break;
		case SOUTH:
			y += 1;
			break;
		case EAST:
			x += 1;
			break;
		case WEST:
			x -= 1;
			break;
		default:
			break;
		}
		Tile target = this.getMap().getLevel()[y][x];		
		if (((target.isPassable() && this.walks ) || (target.isSwimable() && this.swims)) && !this.getMap().isImpassibleThingAt(new Point(x,y))) {	
			this.setLocation(new Point(x,y));
			return true;
		} else {
			return false;
		}
	}

	public void moveTowards(Point point) {
		// adapted from python method on http://www.roguebasin.com/index.php?title=Complete_Roguelike_Tutorial,_using_python%2Blibtcod,_part_6
		// get a vector from this object to the target, and distance
		final int oldX = this.getLocation().getX();
		final int oldY = this.getLocation().getY();
        this.movementHistory.add(new Point(oldX,oldY));
        
		int dx = point.getX() - oldX;
		int dy = point.getY() - oldY;
		
		int distance =  (int) Math.sqrt(Math.pow(dx,2) + Math.pow(dy, 2));
		
		//#normalize it to length 1 (preserving direction), then round it and
	    //#convert to integer so the movement is restricted to the map grid
        dx = (int) Math.ceil( (double)dx / (double)distance);
        dy = (int) Math.ceil( (double)dy / (double)distance);
        
        // no diagonal movement yet or at all :)  so changed this bit
        if ( Math.abs(dx) > Math.abs(dy)) { 
        	if (!moveHorizontal(dx)) {
    			moveVertical(dy);
    		}
        } else if (Math.abs(dx) < Math.abs(dy) ) {
        	if (!moveVertical(dy)) {
    			moveHorizontal(dx);	
    		}
        } else   { // equal, flip a coin
        	if (Randoms.getRandom(0, 1) < 1) {
        		if (!moveHorizontal(dx)) {
        			moveVertical(dy);
        		}
        	} else {
        		if (!moveVertical(dy)) {
        			moveHorizontal(dx);
        		}
        	}
        	
        }
    
	}
	
	protected boolean moveVertical(int dy) {
		if (dy >0) {
			return this.move(Direction.SOUTH, speed ); 
		} else {
			return this.move(Direction.NORTH, speed );
		}
	}

	protected boolean moveHorizontal(int dx) {
		if (dx >0) {
			return this.move(Direction.EAST, speed ); 
		} else {
			return this.move(Direction.WEST, speed ); 
		}
	}

	public boolean isSwims() {
		return swims;
	}

	public void setSwims(boolean swims) {
		this.swims = swims;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isWalks() {
		return walks;
	}

	public void setWalks(boolean walks) {
		this.walks = walks;
	}

	public List<Point> getMovementHistory() {
		return movementHistory;
	}

	public void setMovementHistory(List<Point> movementHistory) {
		this.movementHistory = movementHistory;
	}
	
	public void  destroy() {
	}
}
