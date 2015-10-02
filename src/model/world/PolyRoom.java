package model.world;

import java.util.List;

import util.Randoms;

import model.Point;

// centre of gravity of a polygon :
// wikipedia and http://stackoverflow.com/questions/5271583/center-of-gravity-of-a-polygon
public class PolyRoom implements Place, Comparable<PolyRoom> {
	
	private List<Point> points;
	private String name;
	
	public PolyRoom(String name, List<Point> points) {
		this.name = name;
		this.points = points;
	}

	@Override
	public String getName(){ 
		return name;
	};
	
	@Override
	public void setName(String name){
		this.name = name;
	};
	
	@Override
	public List<Point> getPoints(){
		return points;
	};
	
	@Override
	public void setPoints(List<Point> points){
		this.points = points;
	};
	
	@Override
	public Point getRandomPoint(){
		return this.points.get(Randoms.getRandom(0, points.size()-1));
	};
	
	@Override
	public boolean isThingInHere(Thing thing){
		return false;
	}

	@Override
	public int compareTo(PolyRoom o) {
		return Integer.valueOf(this.getPoints().size()).compareTo(Integer.valueOf(o.getPoints().size()));
	}

}
