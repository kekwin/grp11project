package dk.itu.grp11.main;

import java.util.HashMap;

/**
 * Represents a map with roads
 * 
 * @author Group 11
 *
 */
public class Map {
	private HashMap<Integer, Point> pointMap = new HashMap<Integer, Point>();
	private Road[] roads;

	public static void main(String[] args) {
		Point[] points = new Point[10];
		points[0] = new Point(1, 300, 356);
		points[1] = new Point(2, 390, 377);
		points[2] = new Point(3, 800, 700);
		points[3] = new Point(4, 430, 431);
		
		Road[] roads = new Road[10];
		roads[0] = new Road(1, 2, "Niceness street", 1);
		roads[1] = new Road(2, 3, "Long street", 1);
		roads[2] = new Road(3, 4, "Fail street", 1);
		
		Map map = new Map(points, roads);
		
		System.out.println("Getting part = [320, 330, 150, 100]");
		System.out.println(map.getPart(320, 330, 150, 100));
		
		System.out.println("Getting part = [200, 210, 1000, 750]");
		System.out.println(map.getPart(200, 210, 1000, 750));
	}
	
	/**
	* Loads a map
	* 
	* @param points Top left x-coordinate of viewbox
	* @param roads Top left y-coordinate of viewbox
	*/
	public Map(Point[] points, Road[] roads) {
		this.roads = roads;
		
		//Creating a map for the points
		for(int i = 0; i < points.length && points[i] != null; i++) {
			pointMap.put(points[i].getID(), points[i]);
		}
	}
	
	/**
	* Get a part of the loaded map
	* 
	* @param x Top left x-coordinate of viewbox
	* @param y Top left y-coordinate of viewbox
	* @param w Width of viewbox
	* @param h Height of viewbox
	* 
	* @return String of SVG elements in the specified viewbox (with linebreaks)
	*/
	public String getPart(double x, double y, double w, double h) {
		String output = "";
		for(int i = 0; i < roads.length && roads[i] != null; i++) {
			double x1 = pointMap.get(roads[i].getP1()).getX();
			double y1 = pointMap.get(roads[i].getP1()).getY();
			double x2 = pointMap.get(roads[i].getP2()).getX();
			double y2 = pointMap.get(roads[i].getP2()).getY();
			
			// Checks to see if the road is in the viewbox
			if((x1 <= x+w && x1 >= x) && (y1 <= y+h && y1 >= y) || (x2 <= x+w && x2 >= x) && (y2 <= y+h && y2 >= y))
				output += "<line id=\"line\" x1=\""+x1+"\" y1=\""+y1+"\" x2=\""+x2+"\" y2=\""+y2+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"/>\n";
		}
		
		return output;
	}
}