package dk.itu.grp11.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
	* @param coastline Our coasline data
	*/
	private Map(HashMap<Integer, Point> points, DimensionalTree<Double, RoadType, Road> roads, HashSet<LinkedList<Integer>> coastline) {
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
	* @param session the session of the user who is requesting a new part.
	* 
	* @return String of SVG elements in the specified viewbox (with linebreaks)
	*/
	public String getPart(double x, double y, double w, double h, int zoomlevel, Session session) {
	  StringBuffer outputBuilder = new StringBuffer();

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
  		
  		Set<Road> roadsFound = roads.query2D(i2D);
  		
  		if (roadsFound.size() > 0) {
  		  outputBuilder.append("var svg = document.getElementById('map');\n");
        StringBuffer id = new StringBuffer();
        StringBuffer path = new StringBuffer();
  	    for (Road roadFound : roadsFound) {
	        if (roadFound != null) {
	          if (session != null) {
	            synchronized(session) {
	              if (!session.isRoadDrawn(roadFound.getId())) {
	                id.append(roadFound.getId()+",");
	                session.addRoadID(roadFound.getId());
	                path.append("M"+points.get(roadFound.getFrom()).getX()+","+points.get(roadFound.getFrom()).getY()+"L"+points.get(roadFound.getTo()).getX()+","+points.get(roadFound.getTo()).getY()+"");
	              }
	            }
	          }
	        }
	      }
  	    if (id.length() > 0) {
  	      outputBuilder.append("var path = document.createElementNS('http://www.w3.org/2000/svg', 'path');\n");
	        outputBuilder.append("path.setAttributeNS(null, 'fill-opacity', '0');\n");
	        outputBuilder.append("path.setAttributeNS(null, 'stroke', 'rgb("+roadType.getColorAsString()+")');\n");
	        outputBuilder.append("path.setAttributeNS(null, 'stroke-width', '"+roadType.getStroke()+"%');\n");
    	    outputBuilder.append("path.setAttributeNS(null, 'd', '"+path.toString()+"');");
    	    outputBuilder.append("path.setAttributeNS(null, 'id', '"+id.toString()+"');");
    	    outputBuilder.append("path.setAttributeNS(null, 'class', 'zoom"+roadType.getZoomLevel()+"');");
    	    outputBuilder.append("svg.appendChild(path);");
  	    }
  		}
		}
		
		return outputBuilder.toString();
	}
	/**
	 * 
	 * @return Javascript commands to draw coastline data.
	 */
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
	
	/**
	 * 
	 * @param point1 ID of the starting point
	 * @param point2 ID of the ending point
	 * @param transportation Transportation type desired
	 * @param fastestroute whether the route should be the fastest or the shortest, true for fastest, false for shortest.
	 * @param ferries whether or not the route should include ferries
	 * @param highways whether or not the route should include highways
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
      double actualTime = pf.timeTo(point2);
      
      //Converting distance
      String distUnit = "m";
      int distance = (int) Math.round(pf.distTo(point2));
      if(distance < 1) distance = 1;
      if(distance > 999) { distance /= 1000; distUnit = "km"; }
      
      //If the transportation type is not a car, we calculate the time
      int bicylceSpeed = 20;
      int walkSpeed = 5;
      if(transportation == TransportationType.BICYCLE) actualTime = ((pf.distTo(point2)/1000) / bicylceSpeed)*60;
      else if(transportation == TransportationType.WALK) actualTime = ((pf.distTo(point2)/1000) / walkSpeed)*60;
      
      //Converting time
      int time = (int) Math.round(actualTime);
      if(time < 1) time = 1;
      String hours = (time / 60)+"";
      String minutes = (time % 60)+"";
      

      outputBuilder.append("$('#dist').text('"+distance+" "+distUnit+"');\n");
      outputBuilder.append("$('#time').text('"+hours+"h "+minutes+"m');\n");
      outputBuilder.append("$('#routeinfo').animate({opacity: 1}, 1500);\n");
      outputBuilder.append("var svg = document.getElementById('map');\n");
      outputBuilder.append("var path = document.createElementNS('http://www.w3.org/2000/svg', 'path');\n");
      outputBuilder.append("path.setAttributeNS(null, 'fill-opacity', '0');\n");
      outputBuilder.append("path.setAttributeNS(null, 'stroke', 'rgb("+RoadType.ROUTE.getColorAsString()+")');\n");
      outputBuilder.append("path.setAttributeNS(null, 'stroke-width', '"+RoadType.ROUTE.getStroke()+"%');\n");
      outputBuilder.append("path.setAttributeNS(null, 'class', 'ROUTE');");
      outputBuilder.append("path.setAttributeNS(null, 'id', 'ROUTE');");
      String command = "M";
      double lastX = points.get(point2).getX();
      double lastY = points.get(point2).getY();
      double[] currX = new double[2], currY = new double[2];
      StringBuffer dataString = new StringBuffer();
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
          dataString.append(""+command+""+currX[i]+", "+currY[i]+"");
          if (command.equals("M")) command = "L";
        }
        lastX = currX[1];
        lastY = currY[1];
      }
      outputBuilder.append("path.setAttributeNS(null, 'd', '"+dataString.toString()+"');");
      dataString = null;
      outputBuilder.append("svg.appendChild(path);");
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
