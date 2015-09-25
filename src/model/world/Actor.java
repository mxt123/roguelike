package model.world;

import java.awt.Color;

import model.Direction;
import model.Point;

public class Actor extends Thing {

	public Actor(Map map, Point location, Tile tile, Color color, String name,
			String description) {
		super(map, location, tile, color, name, description);
		// TODO Auto-generated constructor stub
	}
	
	public void move(Direction direction, int distance){
		Point oldLocation = this.getLocation();
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
		if (target.isPassable() && !this.getMap().isImpassibleThingAt(new Point(x,y))) {	
			this.setLocation(new Point(x,y));
		}
	}
	
}