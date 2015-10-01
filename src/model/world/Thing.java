package model.world;

import java.awt.Color;

import model.Point;

public class Thing {
	private Map map;
	private Point location;	
	private Tile tile;
	private Color color;
	private String name;
	private String description;
	private boolean active;
	
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
	
	public int getDistanceTo(Thing t) {
		int dx = t.getLocation().getX() - this.getLocation().getX();
		int dy = t.getLocation().getY() - this.getLocation().getY();
		int distance = (int) Math.sqrt(Math.pow(dx,2) + Math.pow(dy, 2));
		// TODO this is relevant if no diagonal movement check for diagonal 
		if (distance == 1 && this.getLocation().getX() != t.getLocation().getX()  && this.getLocation().getY() != t.getLocation().getY()) {
			distance +=1;
		}
		return distance;
	}
	
	public Thing(Map map, Point location,Tile tile,Color color, String name, String description) {
		this.location = location;
		this.tile = tile;
		this.color = color;
		this.name = name;
		this.description = description;
		this.map = map;
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
}
