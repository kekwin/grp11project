package dk.itu.grp11.data;

import java.util.HashMap;
import java.util.HashSet;

import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.main.FileServer;

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
	public String getPart(double x, double y, double w, double h, int zoomlevel, FileServer fs) {
		String output = "var svg = $('#map-container').svg('get');\n";

		DynArray<RoadType> roadTypes = new DynArray<RoadType>(RoadType[].class);
	  for(RoadType rt : RoadType.values()) {
      if(rt.getZoomLevel() <= zoomlevel) {
        roadTypes.add(rt);
      }
    }
		
	  // Adding all calculated road types within the viewbox
		for (RoadType roadType : roadTypes) {
  		Interval<Double, RoadType> i1 = new Interval<Double, RoadType>(x, x+w, roadType);
  		Interval<Double, RoadType> i2 = new Interval<Double, RoadType>(y, y+h, roadType);
  		Interval2D<Double, RoadType> i2D = new Interval2D<Double, RoadType>(i1, i2);
  		
  		HashSet<Road> roadsFound = roads.query2D(i2D);
  		double yOff = fs.getYStart(); //Y-axis offset
  		double cH = fs.getYDiff(); //Canvas height
  		for (Road roadFound : roadsFound) {
  		  if (roadFound != null)
  		    output += "svg.line("+points.get(roadFound.getP1()).getX()+", "+(yOff+(cH-points.get(roadFound.getP1()).getY()))+", "+points.get(roadFound.getP2()).getX()+", "+(yOff+(cH-points.get(roadFound.getP2()).getY()))+", {stroke: 'rgb("+roadType.getColorAsString()+")', strokeWidth: '"+roadType.getStroke()+"%'});\n";
      }
		}	
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
	  int zoomlevel = (int)((Math.ceil(Parser.getMapBound(MapBound.MAXX))-Math.floor(Parser.getMapBound(MapBound.MINX)))/w);
	  return (zoomlevel < 1 ? 1 : zoomlevel);
	}
	private int zoomLevelY(double h) {
    int zoomlevel = (int)((Math.ceil(Parser.getMapBound(MapBound.MAXY))-Math.floor(Parser.getMapBound(MapBound.MINY)))/h);
    return (zoomlevel < 1 ? 1 : zoomlevel);
  }
}
