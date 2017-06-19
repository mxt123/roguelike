package model.world;

import java.awt.Color;

public enum Tile {
	FIRE('m', false, false,true, Color.WHITE, "A fire", "","fire3.png"),
	LAND(',', true, false, true, Color.GREEN, "land", "","land.png"),
	WALL('#', false, false, false, Color.GRAY, "wall", "","wall.png"),
	SPACE('_', true, false, true, Color.ORANGE, "open space", "","floor.png"),
	SEA('~', false, true, true, Color.BLUE, "water", "","water2.png"), 
	TREE('T', true, false, false, Color.GREEN, "tree", "","tree.png"), 
	PERSON('@', false, false, true, Color.YELLOW, "A person","","player.png"), 
	GOBLIN('g', false, false, true, Color.green, "A goblin",	"grrr!", "goblin.png"), //elf.png"), 
	OGRE('H', false, false, true, Color.green,"An ogre", "RAAAR!","troll.png"), 
	COIN('o', true, false, true, Color.YELLOW, "A coin", "","gold.png"), 
	//ARROW('▨', true, true, true, Color.WHITE, "An arrow", "",""), 
	SHARK('^', false, false,true, Color.WHITE, "A shark", "","shark.png");

	private char character;
	private boolean passable;
	private boolean transparent;
	private Color colorLight;
	private String description;
	private String message;
	private boolean swimable;
	private String fileName;
	
	Tile(char character, boolean passable, boolean swimable,
			boolean transparent, Color colorLight,
			String description, String message, String fileName) {
		
		this.setCharacter(character);
		this.setPassable(passable);
		this.setTransparent(transparent);
		this.setDescription(description);
		this.setColorLight(colorLight);
		this.setMessage(message);
		this.setSwimable(swimable);
		this.setFileName(fileName);
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
