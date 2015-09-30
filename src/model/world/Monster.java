package model.world;

public enum Monster implements Stats {
	//				maxHp,hp,defence,attack,speed
	PLAYER("Player",50,50,1,1,1),
	GOBLIN("Goblin",1,1,1,1,1);
	
	private String profileName;
	private int maxHp; 
	private int hp;
	private int defence;
	private int attack;
	private int speed;

	private Monster(String profileName,int maxHp, int hp, int defence, int attack, int speed) {
		this.profileName = profileName;
		this.maxHp = maxHp;
		this.hp = hp;
		this.defence = defence;
		this.attack = attack;
		this.speed = speed;
	}

	@Override
	public String getProfileName() {
		return profileName;
	}

	@Override
	public int getMaxHp() {
		return maxHp;
	}

	@Override
	public int getHp() {
		return hp;
	}

	@Override
	public int getDefence() {
		return defence;
	}

	@Override
	public int getAttack() {
		return attack;
	}

	@Override
	public int getSpeed() {
		return speed;
	} 

}
