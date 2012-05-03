package dk.itu.grp11.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.main.Session;
import dk.itu.grp11.util.DimensionalTree;
import dk.itu.grp11.util.DynArray;
import dk.itu.grp11.util.Interval;
import dk.itu.grp11.util.Interval2D;
import dk.itu.grp11.enums.TransportationType;

/**
 * Represents a map with roads
 * 
 * @author Group 11
 *
 */
public class Map {
  
  private static Map map = null;
  
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
	
	public static Map getMap() {
	  if (map == null) {
	    HashMap<Integer, Point> points = Parser.getParser().points();
	    DimensionalTree<Double, RoadType, Road> roads = Parser.getParser().roads();
	    map = new Map(points, roads);
	  }
	  return map;
	}
	
	/**
	* Get a part of the loaded map
	* 
	* @param x Top left x-coordinate of viewbox
	* @param y Top left y-coordinate of viewbox
	* @param w Width of viewbox
	* @param h Height of viewbox
	* @param zoomLevel the zoomlevel of this part
	* @param fs
	* 
	* @return String of SVG elements in the specified viewbox (with linebreaks)
	*/
	public String getPart(double x, double y, double w, double h, int zoomlevel, Session session) {
	  StringBuffer outputBuilder = new StringBuffer();
		outputBuilder.append("var svg = $('#map-container').svg('get');\n");

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
  		
  		if (roadsFound.size() > 0) {
    		
    		int csLimit = 1000; //JavaScript CallStack limit
    		String id = "";
    		Iterator<Road> i = roadsFound.iterator();
    		while (i.hasNext()) {
    		  outputBuilder.append("var path = svg.createPath();\nsvg.path(path");
      		for (int j = 0; j < csLimit/2 && i.hasNext(); j++) {
      		  Road roadFound = i.next();
      		  if (roadFound != null) {
      		    synchronized(session) {
        		    if (!session.isRoadDrawn(roadFound.getId())) {
          		    outputBuilder.append(".move("+points.get(roadFound.getFrom()).getX()+", "+points.get(roadFound.getFrom()).getY()+").line("+points.get(roadFound.getTo()).getX()+", "+points.get(roadFound.getTo()).getY()+")");
          		    id += roadFound.getId()+",";
          		    session.addRoadID(roadFound.getId());
        		    }
      		    }
      		  }
          }
      		outputBuilder.append(",{stroke: 'rgb("+roadType.getColorAsString()+")', strokeWidth: '"+roadType.getStroke()+"%', fillOpacity: 0, class: 'zoom"+roadType.getZoomLevel()+"', id: '"+id+"'});\n");
    		}
  		}
		}	
		return outputBuilder.toString();
	}
	
	public String getRoute(int point1, int point2, TransportationType transportation, boolean fastestroute) {
	  StringBuffer outputBuilder = new StringBuffer();
    outputBuilder.append("var svg = $('#map-container').svg('get');\n");
	  Parser p = Parser.getParser();
    Network g = p.network();
    PathFinder pf = new PathFinder(g, point1, fastestroute, transportation);
    Iterable<Road> roads = pf.pathTo(point2);
    outputBuilder.append("var path = svg.createPath();\nsvg.path(path");
    String command = "move";
    double lastX = points.get(point2).getX();
    double lastY = points.get(point2).getY();
    double[] currX = new double[2], currY = new double[2];
    for (Road road : roads) { 
      if (lastX == points.get(road.getFrom()).getX() && lastY == points.get(road.getFrom()).getY()) {
        currX[0] = points.get(road.getFrom()).getX();
        currX[1] = points.get(road.getTo()).getX();
        currY[0] = points.get(road.getFrom()).getY();
        currY[1] = points.get(road.getTo()).getY();
      } else if (lastX == points.get(road.getTo()).getX() && lastY == points.get(road.getTo()).getY()) {
        currX[0] = points.get(road.getTo()).getX();
        currX[1] = points.get(road.getFrom()).getX();
        currY[0] = points.get(road.getTo()).getY();
        currY[1] = points.get(road.getFrom()).getY();
      }
      for (int i = 0; i < currX.length && i < currY.length; i++) {
        outputBuilder.append("."+command+"("+currX[i]+", "+currY[i]+")");
        if (command.equals("move")) command = "line";
      }
      lastX = currX[1];
      lastY = currY[1];
    }
    outputBuilder.append(",{stroke: 'rgb("+RoadType.ROUTE.getColorAsString()+")', strokeWidth: '"+RoadType.ROUTE.getStroke()+"%', fillOpacity: 0, class: 'ROUTE', id: 'ROUTE'});\n");
    return outputBuilder.toString();
	}
	
	/**
	 * Calculates a zoomlevel based on the viewbox width
	 * 
	 * (total map width)/(viewbox width)
	 * 
	 * @param w width of the viewbox
	 * @return zoom level as int. 1 if w = (total map width). 2 if w = (total map width)/2.
	 *         w if w = (total map width)/(total map width).
	 */
	private static int zoomLevelX(double w) {
	  int zoomlevel = (int)((Math.ceil(Parser.getParser().mapBound(MapBound.MAXX))-Math.floor(Parser.getParser().mapBound(MapBound.MINX)))/w);
	  return (zoomlevel < 1 ? 1 : zoomlevel);
	}
	
  public static int getZoomLevelX(double w) {
    return zoomLevelX(w);
  }
	
	 /**
   * Calculates a zoomlevel based on the viewbox height
   * 
   * (total map height)/(viewbox height)
   * 
   * @param h height of the viewbox
   * @return zoom level as int. 1 if h = (total map height). 2 if h = (total map height)/2.
   *         h if h = (total map height)/(total map height).
   */
	private static int zoomLevelY(double h) {
    int zoomlevel = (int)((Math.ceil(Parser.getParser().mapBound(MapBound.MAXY))-Math.floor(Parser.getParser().mapBound(MapBound.MINY)))/h);
    return (zoomlevel < 1 ? 1 : zoomlevel);
  }
	
  public static int getZoomLevelY(double h) {
    return zoomLevelY(h);
  }
}
