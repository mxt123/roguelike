package model.world;

import java.awt.Color;

public enum Tile {
	LAND(',', true, false, true, Color.GREEN, "land", "",new String[]{"land.png"}),
	WALL('▨', false, false, false, Color.GRAY, "wall", "",new String[]{"wall.png"}),
	SPACE('_', true, false, true, Color.ORANGE, "open space", "",new String[]{"floor.png"}),
	SEA('~', false, true, true, Color.BLUE, "water", "",new String[]{"water.png"}), 
	TREE('T', true, false, false, Color.GREEN, "tree", "",new String[]{"tree.png"}), 
	PERSON('@', false, false, true, Color.YELLOW, "A person","",new String[]{"player.png"}), 
	GOBLIN('g', false, false, true, Color.green, "A goblin",	"grrr!",new String[]{"goblin.png"}), 
	OGRE('H', false, false, true, Color.green,"An ogre", "RAAAR!",new String[]{"troll.png"}), 
	COIN('o', true, false, true, Color.YELLOW, "A coin", "",new String[]{"gold.png"}), 
	ARROW('▨', true, true, true, Color.WHITE, "An arrow", "",new String[]{""}), 
	SHARK('^', false, false,true, Color.WHITE, "A shark", "",new String[]{"shark.png"}),
	FIRE('m', false, false,true, Color.WHITE, "A fire", "",new String[]{"fire.png","fire2.png"});

	private char character;
	private boolean passable;
	private boolean transparent;
	private Color colorLight;
	private String description;
	private String message;
	private boolean swimable;
	private String [] fileNames;
	
	Tile(char character, boolean passable, boolean swimable,
			boolean transparent, Color colorLight,
			String description, String message, String[] fileNames) {
		
		this.setCharacter(character);
		this.setPassable(passable);
		this.setTransparent(transparent);
		this.setDescription(description);
		this.setColorLight(colorLight);
		this.setMessage(message);
		this.setSwimable(swimable);
		this.setFileNames(fileNames);
	}
	
	private void setSwimable(boolean swimable) {
		this.swimable = swimable;

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public boolean isPassable() {
		return passable;
	}

	public void setPassable(boolean passable) {
		this.passable = passable;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public Tile[] getAll() {
		return Tile.values();
	}

	public static Tile getByChar(char tile) {
		Tile result = null;
		for (Tile t : Tile.values()) {
			if (t.getCharacter() == tile) {
				result = t;
				break;
			}
		}
		return result;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Color getColorLight() {
		return colorLight;
	}

	public void setColorLight(Color colorLight) {
		this.colorLight = colorLight;
	}

	public boolean isSwimable() {
		return swimable;
	}

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}
}
