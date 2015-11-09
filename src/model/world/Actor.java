package model.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import util.Randoms;

import model.Point;
import model.world.interfaces.Fights;
import model.world.interfaces.Stats;

public class Actor extends Thing implements Stats, Fights {

	private String message = this.getTile().getMessage();
	
	private String profileName;
	private int maxHp; 
	private int hp;
	private int defence;
	private int attack;
	private List<Point> onPath;
	private List<Thing> inventory = new ArrayList<Thing>();
	
	public void destroy() {
		
		final Map map = this.getMap();
		for (final Thing t : inventory) {
			map.getThings().add(new Thing(map,this.getLocation(),t.getTile(),t.getColor(),t.getName(),t.getDescription(),0,false,false));			
		}
		
	}
	
	@Override
	public int attack(Actor target) {
		int damage = attack - target.getDefence() + Randoms.d6();
		target.takeDamage(damage);
		return damage;
	}
	
	public Actor(Stats stats, Map map, Point location, Tile tile, Color color, String name, boolean swims, boolean walks,
			String description, int speed) {
		super(map, location, tile, color, name, description, speed, swims , walks);
		this.setStats(stats);
	}
	
	public void setStats(Stats s) {
		this.profileName = s.getProfileName();
		this.maxHp = s.getMaxHp();
		this.hp = s.getHp();
		this.attack = s.getAttack();
		this.defence = s.getDefence();
	}
	
	@Override
	public boolean takeDamage(int damage) {
		this.hp -= damage;
		return true;
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
	
	public void act() {
		Player p = this.getMap().getPlayer();
   	 	this.clearMessages();
	     if (this.getDistanceTo(p) >= 2) {
	    	 for (int i = 0 ; i < getSpeed(); i ++) {
	    		 this.moveTowards(p.getLocation());
	    	 }
	    	  //this.setMessage("!");
	     } else {
	    	int damage = attack(p);
	    	p.setMessage(String.valueOf(damage));
	     }
	}

	@Override
	public String getProfileName() {
		return this.profileName;
	}

	@Override
	public int getMaxHp() {
		return this.maxHp;
	}

	@Override
	public int getHp() {
		return this.hp;
	}
	

	@Override
	public int getDefence() {
		return this.defence;
	}

	@Override
	public int getAttack() {
		return this.attack;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public List<Point> getOnPath() {
		return onPath;
	}

	public void setOnPath(List<Point> onPath) {
		this.onPath = onPath;
	}

	public List<Thing> getInventory() {
		return inventory;
	}

	public void setInventory(List<Thing> inventory) {
		this.inventory = inventory;
	}
	
	public void addItems(Map map, List<Thing> things) {
		for (final Thing t: things) {
			if (!(t instanceof Actor)) { 
				this.inventory.add(t);
				map.getThings().remove(t);
			}
		}
	}
	
	public void addItem(Thing thing) {
		this.inventory.add(thing);
	}

}
