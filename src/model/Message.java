package model;

import java.awt.Color;
import java.awt.Font;

public class Message {
	
	private Point p;
	private String message;
	private Font font;
	private Color color;
	
	public Message(Point p, String message){
		this.p = p;
		this.message = message;
	}
	
	public Message(Point p, String message, Font font, Color color){
		this.p = p;
		this.message = message;
		this.font = font;
		this.color = color;
	}
	
	public Point getP() {
		return p;
	}
	public void setP(Point p) {
		this.p = p;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
