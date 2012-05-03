package dk.itu.grp11.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.TrafficDirection;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.route.Network;
import dk.itu.grp11.util.DimensionalTree;

/**
 * Parses information about a road network.
 * 
 * @author Group 11
 * 
 */
public class Parser {
  private static Parser ps = null;
  
  private static File pointFile;
  private static File roadFile;
  private static File postalCodesFile;
  private static File coastFile;
  
  private static EnumMap<MapBound, Double> mapBounds;
  private static HashMap<Integer, Point> points;
  private static DimensionalTree<Double, RoadType, Road> roads;
  private static HashMap<Integer, String> postalCodes;
  private static SortedMap<String, Road> nameToRoad;
  private static Network graph;

  /**
   * 
   * @param pointFile A java.File object referencing the file containing nodes.
   * @param roadFile A java.File object referencing the file containing connections.
   */
  public Parser(File pointFile, File roadFile, File postalCodesFile, File coastFile) {
    Parser.pointFile = pointFile;
    Parser.roadFile = roadFile;
    Parser.postalCodesFile = postalCodesFile;
    Parser.coastFile = coastFile;
    
    mapBounds = new EnumMap<MapBound, Double>(MapBound.class);
    mapBounds.put(MapBound.MINX, 1000000.0);
    mapBounds.put(MapBound.MAXX, 0.0);
    mapBounds.put(MapBound.MINY, 100000000.0);
    mapBounds.put(MapBound.MAXY, 0.0);
  }

  public static Parser getParser() {
    if (ps == null) {
      File points = new File("src\\dk\\itu\\grp11\\files\\kdv_node_unload.txt");
      File roads = new File("src\\dk\\itu\\grp11\\files\\kdv_unload.txt");
      File zip = new File("src\\dk\\itu\\grp11\\files\\postNR.csv");
      File coast = new File("src\\dk\\itu\\grp11\\files\\coastLine.osm"); // To be used...
      
      ps = new Parser(points, roads, zip, coast);
      ps.parsePoints();
      ps.parseRoads(ps.points());
      ps.parsePostalCodes();
    }
    return ps;
  }

  /**
   * For test purposes only
   * 
   * @param nodeFile
   * @param connectionFile
   * @return
   */
  public static Parser getTestParser(File points, File roads, File zip, File coast) {
    if (ps == null) {
      if(points == null) points = new File("src\\dk\\itu\\grp11\\files\\kdv_node_unload.txt");
      if(roads == null) roads = new File("src\\dk\\itu\\grp11\\files\\kdv_unload.txt");
      if(zip == null) zip = new File("src\\dk\\itu\\grp11\\files\\postNR.csv");
      if(coast == null) coast = new File("src\\dk\\itu\\grp11\\files\\coastLine.osm");
      
      ps = new Parser(points, roads, zip, coast);
      ps.parsePoints();
      ps.parseRoads(ps.points());
      ps.parsePostalCodes();
    }
    return ps;
  }

  /**
   * Parses all points in the network and puts it in a HashMap with point-ID as key,
   * and the point as value.
   */
  private void parsePoints() {
    System.out.println("- Parsing points");
    points = new HashMap<Integer, Point>();
    try(BufferedReader input = new BufferedReader(new FileReader(pointFile))) {  
      String line = null;
      /*
       * readLine is a bit quirky : it returns the content of a line MINUS the
       * newline. it returns null only for the END of the stream. it returns
       * an empty String if two newlines appear in a row.
       */
      input.readLine(); //Skip first line
      while ((line = input.readLine()) != null) {
        Point p = createPoint(line);
        points.put(p.getID(), p);

        // Finding maximum and minimum x and y coordinates
        if (p.getX() > mapBounds.get(MapBound.MAXX)){
          mapBounds.put(MapBound.MAXX, p.getX());
        }
        if (p.getX() < mapBounds.get(MapBound.MINX)){
          mapBounds.put(MapBound.MINX, p.getX());
        }
        if (p.getY() > mapBounds.get(MapBound.MAXY)){
          mapBounds.put(MapBound.MAXY, p.getY());
        }
        if (p.getY() < mapBounds.get(MapBound.MINY)){
          mapBounds.put(MapBound.MINY, p.getY());
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Parses all roads in the network and puts it in a DimensionalTree sorted by
   * the x and y coordinate of the road and road type.
   */
  private void parseRoads(HashMap<Integer, Point> points) {
    System.out.println("- Parsing roads");
    HashSet<Road> roadsForGraph = new HashSet<>(); //Temporary set for building the Graph
    nameToRoad = new TreeMap<>();
    roads = new DimensionalTree<Double, RoadType, Road>();
    try(BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(roadFile), "ISO8859_1"))) {
      String line = null;
      /*
       * readLine is a bit quirky : it returns the content of a line MINUS the
       * newline. it returns null only for the END of the stream. it returns
       * an empty String if two newlines appear in a row.
       */
      input.readLine();
      while ((line = input.readLine()) != null) {
        Road r = createRoad(line);
        Double xS = points.get(r.getFrom()).getX();
        Double yS = points.get(r.getFrom()).getY();
        Double xE = points.get(r.getTo()).getX();
        Double yE = points.get(r.getTo()).getY();
        roads.insert(xS, yS, r.getType(), r);
        roads.insert(xE, yE, r.getType(), r);
        roadsForGraph.add(r);
      }
      System.out.println("- Creating network graph");
      graph = new Network(points.size(), roadsForGraph);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    roadsForGraph = null; //Does not need the HashSet anymore
  }

  /**
   * Splits a line from the kdv_unload.txt document and then
   * creates a Road object from the information in the string.
   * 
   * @param input
   *          A line from the kdv_unload.txt document.
   * @return A Road object containing the information from the line.
   */
  private static Road createRoad(String input) {
    String[] inputSplit = input.split(",");
    Road r = new Road(
        Integer.parseInt(inputSplit[0]),                                    // 0 = id of from point
        Integer.parseInt(inputSplit[1]),                                    // 1 = id of to point
        inputSplit[6].substring(1, inputSplit[6].length()-1),               // 6 = name
        RoadType.getById(Integer.parseInt(inputSplit[5])),                  // 5 = road type
        TrafficDirection.getDirectionById(inputSplit[27].replace("'", "")), // 27 = traffic direction
        Double.parseDouble(inputSplit[2]),                                  // 2 = length
        Double.parseDouble(inputSplit[26]));                                // 26 = time
    nameToRoad.put(r.getName().toLowerCase(new Locale("ISO8859_1")), r);
    return r;
  }

  /**
   * Splits a line from the kdv_node_unload.txt document and then
   * creates a Point object from the information in the string.
   * 
   * @param input
   *          A line from the kdv_node_unload.txt document.
   * @return A Point object containing the information from the line.
   */
  private static Point createPoint(String input) {
    String[] inputSplit = input.split(",");
    return new Point(
        Integer.parseInt(inputSplit[2]),    //2 = id of the point
        Double.parseDouble(inputSplit[3]),  //3 = x coordinate
        Double.parseDouble(inputSplit[4])); //4 = y coordinate
  }

  private void parsePostalCodes(){
    System.out.println("- Parsing postal codes");
    postalCodes = new HashMap<Integer, String>();
    try(BufferedReader input = new BufferedReader(new FileReader(postalCodesFile))) {  
      String line = null;
      /*
       * readLine is a bit quirky : it returns the content of a line MINUS the
       * newline. it returns null only for the END of the stream. it returns
       * an empty String if two newlines appear in a row.
       */
      input.readLine();
      input.readLine(); //Skip first 2 lines
      while ((line = input.readLine()) != null) {
        String[] inputSplit = line.split(";");
        if(Integer.parseInt(inputSplit[5]) == 1) {
          postalCodes.put(Integer.parseInt(inputSplit[0]), inputSplit[1]);
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  
  // TODO ... 
  private void parseCoastLine(){
	  System.out.println("Parsing coast lines");
	  
	  // Split by " " (whitespaces) the ones we need are Node, lat and lon. return as string arrays or so.
	  
	  
	  
	  // Get each nodes lat/long convert to utm with "LatLonToUML"'s convert()
	  // and return a datastructure/send to browser, in same manner as with previous nodes/roads
  }

  public HashMap<Integer, Point> points() {
    return points;
  }
  
  public DimensionalTree<Double, RoadType, Road> roads() {
    return roads;
  }
  
  public Network network() {
    return graph;
  }
  
  public HashMap<Integer, String> postalCodes() {
    return postalCodes;
  }
  
  public SortedMap<String, Road> filterPrefix(String prefix) {
    if(prefix.length() > 0) {
        prefix.toLowerCase(Locale.getDefault());
        char nextLetter = (char) (prefix.charAt(prefix.length() - 1) + 1);
        String end = prefix.substring(0, prefix.length()-1) + nextLetter;
        return nameToRoad.subMap(prefix, end);
    }
    return nameToRoad;
  }
  
  public double mapBound(MapBound mb) {
    return mapBounds.get(mb);
  }

  public int numPoints() {
    return points.size();
  }

  public int numRoads() {
    return roads.count();
  }
}