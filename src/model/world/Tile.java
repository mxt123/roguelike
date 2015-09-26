package model.world;

import java.awt.Color;

public enum Tile {	
	
	LAND(',',false,true,Color.BLACK, Color.GREEN,"land"),
	WALL('▨',false,false,Color.BLACK,Color.GRAY,"wall"),
	SPACE('_',true,true,Color.BLACK,Color.ORANGE,"open space"),
	SEA('~',true,true,Color.BLACK,Color.BLUE,"water"),
	TREE('T',false,true,Color.BLACK,Color.GREEN,"tree"),
	PERSON('@',false,true,Color.YELLOW,Color.YELLOW,"A person"),
	GOBLIN('g',false,true,Color.BLACK,Color.green,"A goblin"),
	COIN('o',true,true,Color.BLACK,Color.YELLOW,"A coin");
	
	//old wall #
	//old space _
	// http://www.alanwood.net/unicode/unicode_samples.html 
	//:)
	/*
	∙
	▨
	□
	⟰
	ȣ
	Ȣ
	Ѧ
	о҉
	*/
	
	private char character;
	private boolean passable;
	private boolean transparent;
	private Color colorDark;
	private Color colorLight;
	private String description;
	
	Tile( char character, boolean passable, boolean transparent, Color colorDark, Color colorLight, String description) {
		this.setCharacter(character);
		this.setPassable(passable);
		this.setTransparent(transparent);
		this.setDescription(description);
		this.setColorDark(colorDark);
		this.setColorLight(colorLight);
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
		for (Tile t:  Tile.values()) {
			if (t.getCharacter()== tile) {
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

	public Color getColorDark() {
		return colorDark;
	}

	public void setColorDark(Color colorDark) {
		this.colorDark = colorDark;
	}
}
