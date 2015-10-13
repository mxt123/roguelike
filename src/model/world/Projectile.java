package model.world;

import java.awt.Color;
import java.util.List;

import util.Randoms;

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
	
	public void moveTowards(Point point) {
		// adapted from python method on http://www.roguebasin.com/index.php?title=Complete_Roguelike_Tutorial,_using_python%2Blibtcod,_part_6
		// get a vector from this object to the target, and distance
		final int oldX = this.getLocation().getX();
		final int oldY = this.getLocation().getY();
        this.getMovementHistory().add(new Point(oldX,oldY));
        
		int dx = point.getX() - oldX;
		int dy = point.getY() - oldY;
		
		int distance =  (int) Math.sqrt(Math.pow(dx,2) + Math.pow(dy, 2));
		
		//#normalize it to length 1 (preserving direction), then round it and
	    //#convert to integer so the movement is restricted to the map grid
        dx = (int) Math.ceil( (double)dx / (double)distance);
        dy = (int) Math.ceil( (double)dy / (double)distance);
       
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
		List<Actor> actors = this.getMap().getActorsAt(this.getLocation());
		for (Actor a : actors) {
			a.takeDamage(Randoms.d20()); // TODO should be a power or damage variable
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
