package model.world.interfaces;

import java.util.List;

import model.world.Actor;
import model.world.Thing;

public interface Fights {
	public List<Thing> destroy();
	public boolean takeDamage(int damage);
	public int attack(Actor a);
}
