package model.world;

import java.util.List;

import model.Point;

/*
TODO island and room and rect should all implement this. Maps should contain lists of places
set these up with the connected islands code for messy maps or the dungeongenerator
*/
public interface Place {
	
	String getName();
	void setName(String name);
	List<Point> getPoints(); 
	void setPoints(List<Point> points);
	Point getRandomPoint();
	boolean isThingInHere(Thing thing);

}
