package model;

public class Point { 
	private int x;
	private int y;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean result = false;
		if (o instanceof Point) {
			Point p = (Point) o;
			result = x == p.x && y == p.y;
		}
		return  result; 
	}
	
	@Override
	public String toString(){
		return "(" + this.x + " " + this.y + ")";
	}
	
	@Override
	public int hashCode(){
		return Integer.valueOf(Integer.toString(x) + Integer.toString(y) );
	}

	
}
