package model.world;

import java.awt.Color;

import model.Direction;
import model.Point;

public class Projectile extends Thing {
	
	private Point target;
	private int power;

	public Projectile(Map map, Point location, Tile tile, Color color,
			String name, String description, boolean swims, boolean walks, Point target, int speed, int power ) {
		super(map, location, tile, color, name, description, speed, swims, walks);
		
		this.setTarget(target);
		this.power = power;
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
		if ( target.isPassable() || target.isSwimable()) {	
			this.setLocation(new Point(x,y));
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void act() {
		if (this.getLocation()!= target && this.power > 0) {
			this.moveTowards(target);
		}
		this.power -=1;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public Point getTarget() {
		return target;
	}

	public void setTarget(Point target) {
		this.target = target;
	}

}
