package model.world;

import java.awt.Color;

import util.Randoms;

import model.Direction;
import model.Point;

public class Actor extends Thing {
	
	private Stats stats;

	private String message = this.getTile().getMessage();
	
	public Actor(Stats stats, Map map, Point location, Tile tile, Color color, String name,
			String description) {
		super(map, location, tile, color, name, description);
		this.setStats(stats);
		// TODO Auto-generated constructor stub
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message ; 
	}
	
	public void  clearMessages(){
		this.message = null;
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
		if (target.isPassable() && !this.getMap().isImpassibleThingAt(new Point(x,y))) {	
			this.setLocation(new Point(x,y));
			return true;
		} else {
			return false;
		}
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public void act() {
		Player p = this.getMap().getPlayer();
	     if (this.getDistanceTo(p) >= 2) {
	    	  this.moveTowards(p.getLocation());
	    	  //this.setMessage("!");
	     } else {
	    	 //this.getTile().setMessage("X");
	     }
	}

	public void moveTowards(Point point) {
		// adapted from python method on http://www.roguebasin.com/index.php?title=Complete_Roguelike_Tutorial,_using_python%2Blibtcod,_part_6
		// get a vector from this object to the target, and distance
		int dx = point.getX() - this.getLocation().getX();
		int dy = point.getY() - this.getLocation().getY();
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
        } else 

        { // equal, flip a coin
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
	
	private boolean moveVertical(int dy) {
		if (dy >0) {
			return this.move(Direction.SOUTH, 1 ); // TODO actors should have a speed
		} else {
			return this.move(Direction.NORTH, 1 );
		}
	}

	private boolean moveHorizontal(int dx) {
		if (dx >0) {
			return this.move(Direction.EAST, 1 ); 
		} else {
			return this.move(Direction.WEST, 1 ); 
		}
	}
}
