package model.world.interfaces;

import model.world.Actor;

public interface Fights {
	public boolean takeDamage(int damage);
	public int attack(Actor a);
}
