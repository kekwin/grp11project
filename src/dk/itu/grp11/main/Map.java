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
  private double[] minMaxValues; //Minimum/maximum x and y coordinates in the map
	
	/**
	* Loads a map by points and roads
	* 
	* @param points All points in the network
	* @param roads All roads in the network
	*/
	public Map(HashMap<Integer, Point> points, DimensionalTree<Double, RoadType, Road> roads, double[] minMaxValues) {
	  this.points = points;
		this.roads = roads;
		this.minMaxValues = minMaxValues;
	}
	
	public double getMinMax(int index) {
	  return minMaxValues[index];
	}
	
	public int getZoomLevelX(double w) {
	  return zoomLevelX(w);
	}
	public int getZoomLevelY(double h) {
    return zoomLevelY(h);
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
	public String getPart(double x, double y, double w, double h, double yOff, double cH, int zoomlevel) {
	  //long partTime = System.nanoTime(); 
		String output = "var svg = $('#map-container').svg('get');\n";

		DynArray<RoadType> roadTypes = new DynArray<RoadType>(RoadType[].class);
		//System.out.print("Showing roadtypes (zoom level = " + zoomlevel + "):");
	  for(RoadType rt : RoadType.values()) {
      if(rt.getZoomLevel() <= zoomlevel) {
        //System.out.print("\n  " + rt.name());
        roadTypes.add(rt);
      }
    }
	  //System.out.println();
		
	  // Adding all calculated road types within the viewbox
		for (RoadType roadType : roadTypes) {
  		Interval<Double, RoadType> i1 = new Interval<Double, RoadType>(x, x+w, roadType);
  		Interval<Double, RoadType> i2 = new Interval<Double, RoadType>(y, y+h, roadType);
  		Interval2D<Double, RoadType> i2D = new Interval2D<Double, RoadType>(i1, i2);
  		
  		//long startTime = System.nanoTime(); 
  		Road[] roadsFound = roads.query2D(i2D);
  		//System.out.println("Found " + roadsFound.length + " roads of type "+roadType.name()+" in " + ((System.nanoTime() - startTime)/1000000000.0) + "s");
  		//startTime = System.nanoTime(); 
  		for (Road roadFound : roadsFound) {
  		  output += "svg.line("+points.get(roadFound.getP1()).getX()+", "+(yOff+(cH-points.get(roadFound.getP1()).getY()))+", "+points.get(roadFound.getP2()).getX()+", "+(yOff+(cH-points.get(roadFound.getP2()).getY()))+", {stroke: 'rgb("+roadType.getColorAsString()+")', strokeWidth: "+roadType.getStroke()+"});\n";
      }
  		//System.out.println("Wrote output in " + ((System.nanoTime() - startTime)/1000000000.0) + "s");
		}	
		//System.out.println("Completed getPart in "+ ((System.nanoTime() - partTime)/1000000000.0) + "s");
		return output;
	}
	
	/**
	 * Calculates a zoomlevel.
	 * 
	 * (total map width)/(viewbox width)
	 * 
	 * @param w width of the viewbox
	 * @param h height of the viewbox
	 * @return zoom level as int. 1 if w = (total map width). 2 if w = (total map width)/2.
	 *         w if w = (total map width)/(total map width).
	 */
	private int zoomLevelX(double w) {
	  int zoomlevel = (int)((Math.ceil(minMaxValues[MinMax.MAXX.id()])-Math.floor(minMaxValues[MinMax.MINX.id()]))/w);
	  return (zoomlevel < 1 ? 1 : zoomlevel);
	}
	private int zoomLevelY(double h) {
    int zoomlevel = (int)((Math.ceil(minMaxValues[MinMax.MAXY.id()])-Math.floor(minMaxValues[MinMax.MINY.id()]))/h);
    return (zoomlevel < 1 ? 1 : zoomlevel);
  }
}
