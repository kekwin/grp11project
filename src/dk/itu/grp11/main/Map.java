package dk.itu.grp11.main;

import java.util.HashMap;

import dk.itu.grp11.contrib.DimensionalTree;
import dk.itu.grp11.contrib.Interval;
import dk.itu.grp11.contrib.Interval2D;

/**
 * Represents a map with roads
 * 
 * @author Group 11
 *
 */
public class Map {
  private DimensionalTree<Double, Integer, Road> roads;
  private HashMap<Integer, Point> points;
  private double[] minMaxValues;

  
  //TODO Needs major rewrite!
/*	public static void main(String[] args) {
		Point[] points = new Point[10];
		points[0] = new Point(1, 300, 356);
		points[1] = new Point(2, 390, 377);
		points[2] = new Point(3, 800, 700);
		points[3] = new Point(4, 430, 431);
		
		Road[] roads = new Road[10];
		roads[0] = new Road(1, 2, "Niceness street", 1);
		roads[1] = new Road(2, 3, "Long street", 1);
		roads[2] = new Road(3, 4, "Fail street", 1);
		
		Map map = new Map(points, roads, new double[] {}); //Empty double array
		
		System.out.println("Getting part = [320, 330, 150, 100]");
		System.out.println(map.getPart(320, 330, 150, 100));
		
		System.out.println("Getting part = [200, 210, 1000, 750]");
		System.out.println(map.getPart(200, 210, 1000, 750));
	}*/
	
	/**
	* Loads a map
	* 
	* @param points Top left x-coordinate of viewbox
	* @param roads Top left y-coordinate of viewbox
	*/
	public Map(HashMap<Integer, Point> points, DimensionalTree<Double, Integer, Road> roads, double[] minMaxValues) {
	  this.points = points;
		this.roads = roads;
		this.minMaxValues = minMaxValues;
	}
	
	public double getMinMax(int index) {
	  return minMaxValues[index];
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
		
		int[] roadTypes = {1, 2};
		
		for (int roadType : roadTypes) {
  		Interval<Double, Integer> i1 = new Interval<Double, Integer>(x, y, roadType);
  		Interval<Double, Integer> i2 = new Interval<Double, Integer>(x+w, y+h, roadType);
  		Interval2D<Double, Integer> i2D = new Interval2D<Double, Integer>(i1, i2);
  		
  		long startTime = System.nanoTime(); 
  		Road[] roadsFound = roads.query2D(i2D);
  		System.out.println("Found " + roadsFound.length + " roads in " + ((System.nanoTime() - startTime)/1000000000.0) + "s");
  		for (Road roadFound : roadsFound) {
        output += "        <line id=\"line\" x1=\""+points.get(roadFound.getP1()).getX()+"\" y1=\""+((h-points.get(roadFound.getP1()).getY())+y)+"\" x2=\""+points.get(roadFound.getP2()).getX()+"\" y2=\""+((h-points.get(roadFound.getP2()).getY())+y)+"\" style=\"stroke:rgb(0,0,0); stroke-width:100;\"></line>\n";
      }
		}
		
		
		
		
		return output;
	}
}
