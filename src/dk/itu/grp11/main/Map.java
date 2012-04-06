package dk.itu.grp11.main;

import java.util.HashMap;

import dk.itu.grp11.contrib.DimensionalTree;
import dk.itu.grp11.contrib.DynArray;
import dk.itu.grp11.contrib.Interval;
import dk.itu.grp11.contrib.Interval2D;
import dk.itu.grp11.enums.MapBound;
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
	
	/**
	* Loads a map by points and roads
	* 
	* @param points All points in the network
	* @param roads All roads in the network
	*/
	public Map(HashMap<Integer, Point> points, DimensionalTree<Double, RoadType, Road> roads) {
	  this.points = points;
		this.roads = roads;
	}
	
	public int getZoomLevel(double w, double h) {
	  return zoomLevel(w, h);
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
	public String getPart(double x, double y, double w, double h, double cW, double cH, int zoomlevel) {
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
  		  output += "svg.line("+points.get(roadFound.getP1()).getX()+", "+((cH-points.get(roadFound.getP1()).getY()))+", "+points.get(roadFound.getP2()).getX()+", "+((cH-points.get(roadFound.getP2()).getY()))+", {stroke: 'rgb("+roadType.getColorAsString()+")', strokeWidth: "+roadType.getStroke()+"});\n";
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
	private int zoomLevel(double w, double h) {
	  return (int)((Math.ceil(Parser.getMapBound(MapBound.MAXX))-Math.floor(Parser.getMapBound(MapBound.MINX)))/w);
	}
}
