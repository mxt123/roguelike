package model.world;

import java.awt.Color;
import java.util.List;

import model.Direction;
import model.Point;

public class Player extends Actor {
	
	public Player(Stats stats, Map map, Point location, Tile tile, Color color, String name,
			String description) {
		super(stats, map, location, tile, color, name, description);
	}
	
	@Override
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
		List<Actor> actors = this.getMap().getActorsAt(new Point(x,y));
		if (actors.size() > 0) {
			this.setMessage("!"); // TODO do attack here
			// there can only be one blocking mob on a tile at the mo
			Actor a = actors.get(0);
			int damage = attack(a);
			a.setMessage(String.valueOf(damage));
			return false;
		} else if (target.isPassable() && !this.getMap().isImpassibleThingAt(new Point(x,y))) {	
			this.setLocation(new Point(x,y));
			return true;
		} else {
			return false;
		}
	}
	
	
}
