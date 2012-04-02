package dk.itu.grp11.main;

import java.util.HashMap;

import dk.itu.grp11.contrib.DimensionalTree;
import dk.itu.grp11.contrib.DynArray;
import dk.itu.grp11.contrib.Interval;
import dk.itu.grp11.contrib.Interval2D;
import dk.itu.grp11.enums.MinMax;
import dk.itu.grp11.enums.RoadType;

/**
 * Represents a map with roads
 * 
 * @author Group 11
 *
 */
public class Map {
  private DimensionalTree<Double, RoadType, Road> roads;
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
	public Map(HashMap<Integer, Point> points, DimensionalTree<Double, RoadType, Road> roads, double[] minMaxValues) {
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
		/*
		//Some test calculations of zoom level
		System.out.println("All zoomed out: " + zoomLevel(w, h));
		System.out.println("200%: " + zoomLevel(w/2, h/2));
		System.out.println("1000%: " + zoomLevel(w/10, h/10));
		System.out.println("Zoomed in to 1 px: " + zoomLevel(1, 1));
		*/
		DynArray<RoadType> roadTypes = new DynArray<RoadType>(RoadType[].class);
		System.out.print("Showing roadtypes (zoom level = " + zoomLevel(w, h) + "):");
	  for(RoadType rt : RoadType.values()) {
      if(rt.getZoomLevel() <= zoomLevel(w, h)) {
        System.out.print("\n  " + rt.name());
        roadTypes.add(rt);
      }
    }
	  System.out.println();
		
		for (RoadType roadType : roadTypes) {
  		Interval<Double, RoadType> i1 = new Interval<Double, RoadType>(x, y, roadType);
  		Interval<Double, RoadType> i2 = new Interval<Double, RoadType>(x+w, y+h, roadType);
  		Interval2D<Double, RoadType> i2D = new Interval2D<Double, RoadType>(i1, i2);
  		
  		long startTime = System.nanoTime(); 
  		Road[] roadsFound = roads.query2D(i2D);
  		System.out.println("Found " + roadsFound.length + " roads of type "+roadType.name()+" in " + ((System.nanoTime() - startTime)/1000000000.0) + "s");
  		for (Road roadFound : roadsFound) {
  		  output += "      <line id=\"line\" "+
            "x1=\""+points.get(roadFound.getP1()).getX()+"\" "+
            "y1=\""+((h-points.get(roadFound.getP1()).getY()))+"\" "+
            "x2=\""+points.get(roadFound.getP2()).getX()+"\" " +
            "y2=\""+((h-points.get(roadFound.getP2()).getY()))+"\" style=\"" +
            "stroke:rgb("+roadFound.getType().getColorAsString()+"); " +
            "stroke-width:"+roadFound.getType().getStroke()+";\"></line>\n";
      }
		}	
		return output;
	}
	
	private int zoomLevel(double w, double h) {
	  return (int)((Math.ceil(minMaxValues[MinMax.MAXX.id()])-Math.floor(minMaxValues[MinMax.MINX.id()]))/w);
	}
}
