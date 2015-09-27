package model.world;

import java.awt.Color;

import model.Direction;
import model.Point;

public class Actor extends Thing {
	
	private Stats stats;

	public Actor(Stats stats, Map map, Point location, Tile tile, Color color, String name,
			String description) {
		super(map, location, tile, color, name, description);
		this.setStats(stats);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getMessage() {
		return this.getTile().getMessage() +" you are " +  this.getDistanceTo(this.getMap().getPlayer()) + " tiles away";
	}
	
	public void move(Direction direction, int distance){
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

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}
	
	/*
	 def move_towards(self, target_x, target_y):
	        #vector from this object to the target, and distance
	        dx = target_x - self.x
	        dy = target_y - self.y
	        distance = math.sqrt(dx ** 2 + dy ** 2)
	 
	        #normalize it to length 1 (preserving direction), then round it and
	        #convert to integer so the movement is restricted to the map grid
	        dx = int(round(dx / distance))
	        dy = int(round(dy / distance))
	        self.move(dx, dy)
	        
	 
	
    #move towards player if far away
            if monster.distance_to(player) >= 2:
                monster.move_towards(player.x, player.y)
 
            #close enough, attack! (if the player is still alive.)
            elif player.fighter.hp > 0:
                print 'The attack of the ' + monster.name + ' bounces off your shiny metal armor!'
	*/
}
