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
