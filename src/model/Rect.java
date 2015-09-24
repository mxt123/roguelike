package model;


// converted from python
// http://www.roguebasin.com/index.php?title=Complete_Roguelike_Tutorial,_using_python%2Blibtcod,_part_3
public class Rect {

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

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	private int x;
	private int y;
	private int w;
	private int h;
	
	public Rect(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
	}
	
	public Point center() {
		int centerX = (x + x + w-1) /2;
		int centerY = (y + y + h-1) /2;
		return new Point(centerX,centerY);
	}
	
	public  boolean intersect(Rect rect2){
		 return (x <= rect2.x + rect2.w && 
				 x+w >= rect2.x &&
	             y <= rect2.y+rect2.h && 
	             y+h  >= rect2.y);
	}	
	
}