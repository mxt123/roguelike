package model.world;

import java.awt.Color;
import java.util.List;

import model.Direction;
import model.Point;
import model.world.interfaces.Stats;

public class Player extends Actor {
	
	public Player(Stats stats, Map map, Point location, Tile tile, Color color, String name,
			String description, int speed) {
		super(stats, map, location, tile, color, name,true, true, description,speed);
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
		final Tile target = this.getMap().getLevel()[y][x];	
		final Point here = new Point(x,y);
		List<Actor> actors = this.getMap().getActorsAt(here);
		// its a monster attack it
		if (actors.size() > 0) {
			Actor a = actors.get(0);
			int damage = attack(a);
			a.setMessage(String.valueOf(damage));
			return false;
		} 
		else if ((target.isPassable() || (target.isSwimable() && this.isSwims()))  && !this.getMap().isImpassibleThingAt(new Point(x,y))) {	
			this.setLocation(new Point(x,y));
			this.getMap().getVisited()[x][y] = true;
			
			// pick up any things TODO have an autopickup
			final List<Thing> things= this.getMap().getThings(here);
			addItems(this.getMap(), things);
			
			return true;
		} else {
			return false;
		}
	}
	
	
}
