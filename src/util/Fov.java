package util;

import model.Point;
import model.world.Map;

// adapted from looking at example at http://www.roguebasin.com/index.php?title=Eligloscode
public class Fov {
	
	private static final int ANGLE_INCREMENT = 15;

	// update for multipl sources and set light level decreasing from centre
	public static int[][] getFov(Map map, Point source, int lightRadius)
	{
	  double x;
	  double y;
	  int[][] lightMap = new int[map.getWidth()][map.getHeight()];//Initially set all tiles to not visible.
	  for(int i=0;i<360;i+=ANGLE_INCREMENT) // increasing i increment to send out less than 360 rays depending on radius
	  {
	    x=Math.cos(i*0.01745f);
	    y=Math.sin(i*0.01745f);
	    Fov.doFov(map, lightMap, source,  x, y, lightRadius);
	  };
	//  lightMap[source.getX()][source.getY()] = 1;
	  return lightMap;
	};

	private static void doFov(Map map, int[][] lightMap,Point source, double x,double y, int radius)
	{
	  int i;
	  float ox = (float)source.getX()+0.5f;
	  float oy = (float)source.getY()+0.5f;
	  for(i=0;i<radius;i++)
	  {
		 if (ox >= 0 && oy >= 0 && ox <= map.getWidth() && oy <= map.getHeight()) {
			  lightMap[(int)ox][(int)oy]=1;//Set the tile to visible.
		  }	
	 //   if(!map.getLevel()[(int)ox][(int)oy].isTransparent())
	   //   return;
	    ox+=x;
	    oy+=y;
	  };
	};

}
