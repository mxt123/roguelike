package model;


public class Message {
	private Point p;
	private String message;
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
	public Message(Point p, String message){
		this.p = p;
		this.message = message;
	}
}
