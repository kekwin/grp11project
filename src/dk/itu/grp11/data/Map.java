package dk.itu.grp11.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.enums.TransportationType;
import dk.itu.grp11.main.Session;
import dk.itu.grp11.route.Network;
import dk.itu.grp11.route.PathFinder;
import dk.itu.grp11.util.DimensionalTree;
import dk.itu.grp11.util.DynArray;
import dk.itu.grp11.util.Interval;
import dk.itu.grp11.util.Interval2D;

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
  private HashSet<LinkedList<Integer>> coastline;
  private int routeOffset = 10000;
	
	/**
	* Loads a map by points and roads
	* 
	* @param points All points in the network
	* @param roads All roads in the network
	*/
	public Map(HashMap<Integer, Point> points, DimensionalTree<Double, RoadType, Road> roads, HashSet<LinkedList<Integer>> coastline) {
	  this.points = points;
		this.roads = roads;
		this.coastline = coastline;
	}
	
	public static Map getMap() {
	  if (map == null) {
      Parser p = Parser.getParser();
	    HashMap<Integer, Point> points = p.points();
	    DimensionalTree<Double, RoadType, Road> roads = p.roads();
	    HashSet<LinkedList<Integer>> coastline = p.coastline();
	    map = new Map(points, roads, coastline);
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
	  int startLen = outputBuilder.length();
		for (RoadType roadType : roadTypes) {
  		Interval<Double, RoadType> i1 = new Interval<Double, RoadType>(x, x+w, roadType);
  		Interval<Double, RoadType> i2 = new Interval<Double, RoadType>(y, y+h, roadType);
  		Interval2D<Double, RoadType> i2D = new Interval2D<Double, RoadType>(i1, i2);
  		
  		HashSet<Road> roadsFound = roads.query2D(i2D);
  		
  		if (roadsFound.size() > 0) {
  		  drawRoadSet(outputBuilder, roadsFound, session, roadType);
  		}
		}
		
		if (startLen < outputBuilder.length()) return outputBuilder.toString();
		else return "";
	}
	
	public String getCoastLine() {
	  StringBuffer outputBuilder = new StringBuffer();
	  outputBuilder.append("var svg = document.getElementById('map');\n");
	  outputBuilder.append("var group = document.createElementNS('http://www.w3.org/2000/svg', 'g');\n");
	  outputBuilder.append("group.setAttributeNS(null, 'fill-opacity', '0');\n");
	  outputBuilder.append("group.setAttributeNS(null, 'stroke', 'rgb("+RoadType.COASTLINE.getColorAsString()+")');\n");
    outputBuilder.append("group.setAttributeNS(null, 'stroke-width', '"+RoadType.COASTLINE.getStroke()+"%');\n");
	  outputBuilder.append("svg.appendChild(group);\n");
	  for (LinkedList<Integer> outline : coastline) {
	    String command = "M";
	    outputBuilder.append("var path = document.createElementNS('http://www.w3.org/2000/svg', 'path');\n");
	    outputBuilder.append("path.setAttributeNS(null, 'class', 'COASTLINE');\n");
	    StringBuffer data = new StringBuffer();
	    if (outline.size() > 0) {
    	  for (Integer point : outline) {
  	  	    data.append(command+""+points.get(point+Parser.getPointsOffset()).getX()+","+points.get(point+Parser.getPointsOffset()).getY()+"");
    	    command = "L";
    	  }
    	  //data.append("Z");
    	  outputBuilder.append("path.setAttributeNS(null, 'd', '"+data.toString()+"');\n");
	    }
  	  outputBuilder.append("group.appendChild(path);\n");
	  }
	  return outputBuilder.toString();
  }
	
	private StringBuffer drawRoadSet(StringBuffer outputBuilder, HashSet<Road> roadsFound, Session session, RoadType roadType) {
	  int csLimit = 1000; //JavaScript CallStack limit
    Iterator<Road> i = roadsFound.iterator();
    while (i.hasNext()) {
      StringBuffer id = new StringBuffer();
      StringBuffer path = new StringBuffer();
      path.append("var path = svg.createPath();\nsvg.path(path");
      for (int j = 0; j < csLimit/2 && i.hasNext(); j++) {
        Road roadFound = i.next();
        if (roadFound != null) {
          if (session != null) {
            synchronized(session) {
              if (!session.isRoadDrawn(roadFound.getId())) {
                id.append(roadFound.getId()+",");
                session.addRoadID(roadFound.getId());
                path.append(".move("+points.get(roadFound.getFrom()).getX()+", "+points.get(roadFound.getFrom()).getY()+").line("+points.get(roadFound.getTo()).getX()+", "+points.get(roadFound.getTo()).getY()+")");
              }
            }
          }
        }
      }
      path.append(",{stroke: 'rgb("+roadType.getColorAsString()+")', strokeWidth: '"+roadType.getStroke()+"%', fillOpacity: 0, class: 'zoom"+roadType.getZoomLevel()+"', id: '"+id.toString()+"'});\n");
      if (id.length() > 0) outputBuilder.append(path);
    }
    return outputBuilder;
	}
	/**
	 * 
	 * @param point1
	 * @param point2
	 * @param transportation
	 * @param fastestroute
	 * @param ferries
	 * @param highways
	 * @return JavaScript commands to draw the route (and zoom in to it). If no such route exist an empty string will be returned.
	 */
	public String getRoute(int point1, int point2, TransportationType transportation, boolean fastestroute, boolean ferries, boolean highways) {
	  StringBuffer outputBuilder = new StringBuffer();
    outputBuilder.append("var svg = $('#map-container').svg('get');\n");
	  Parser p = Parser.getParser();
    Network g = p.network();
    PathFinder pf = new PathFinder(g, point1, fastestroute, transportation, ferries, highways);
    if(pf.hasPathTo(point2)) {
      Iterable<Road> roads = pf.pathTo(point2);
      
      //Converting time
      String timeUnit = "min. ";
      int time = (int) Math.round(pf.timeTo(point2));
      if(time < 1) time = 1;
      if(time > 59) { timeUnit = "t."; time /= 60; }
      
      //Converting distance
      String distUnit = "m ";
      int distance = (int) Math.round(pf.distTo(point2));
      if(distance < 1) distance = 1;
      if(distance > 999) { distance /= 1000; distUnit = "km"; }
      
      outputBuilder.append("$('#dist').text('"+distance+distUnit+"');\n");
      if(transportation == TransportationType.CAR) outputBuilder.append("$('#time').text('"+time+timeUnit+"');\n");
      else outputBuilder.append("$('#time').text('n/a');\n");
      
      outputBuilder.append("$('#routeinfo').animate({opacity: 1}, 1500);\n");
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
      outputBuilder.append("zoomSVGCoords("+(pf.pathBound(MapBound.MINX)-routeOffset)+", "+(pf.pathBound(MapBound.MINY)-routeOffset)+", "+((pf.pathBound(MapBound.MAXX)-pf.pathBound(MapBound.MINX))+routeOffset*2)+", "+((pf.pathBound(MapBound.MAXY)-pf.pathBound(MapBound.MINY))+routeOffset*2)+", 2000, true);");
      //outputBuilder.append("svg.rect("+(pf.pathBound(MapBound.MINX)-routeOffset)+", "+(pf.pathBound(MapBound.MINY)-routeOffset)+", "+((pf.pathBound(MapBound.MAXX)-pf.pathBound(MapBound.MINX))+routeOffset*2)+", "+((pf.pathBound(MapBound.MAXY)-pf.pathBound(MapBound.MINY))+routeOffset*2)+", {stroke: 'rgb(0,0,0)', strokeWidth: '0.2%', fill: 'none'});");
      return outputBuilder.toString();
    }
    else return "";
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
