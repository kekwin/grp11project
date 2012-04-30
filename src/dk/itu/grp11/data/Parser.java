package dk.itu.grp11.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;

import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.TrafficDirection;
import dk.itu.grp11.enums.RoadType;

/**
 * Parses information about a road network.
 * 
 * @author Group 11
 * 
 */
public class Parser {
  private static Parser ps = null;
  private static File nodes;
  private static File connections;
  private static EnumMap<MapBound, Double> mapBounds;
  private static HashMap<Integer, Point> points = null;
  private static DimensionalTree<Double, RoadType, Road> roads = null;
  private static Network graph = null;
  
  /**
   * 
   * @param nodeFile A java.File object referencing the file containing nodes.
   * @param connectionFile A java.File object referencing the file containing connections.
   */
  public Parser(File nodeFile, File connectionFile) {
    nodes = nodeFile;
    connections = connectionFile;
    mapBounds = new EnumMap<MapBound, Double>(MapBound.class);
    mapBounds.put(MapBound.MINX, 1000000.0);
    mapBounds.put(MapBound.MAXX, 0.0);
    mapBounds.put(MapBound.MINY, 100000000.0);
    mapBounds.put(MapBound.MAXY, 0.0);
  }
  
  public static Parser getParser() {
    if (ps == null) {
      File node = new File("src\\dk\\itu\\grp11\\files\\kdv_node_unload.txt");
      File road = new File("src\\dk\\itu\\grp11\\files\\kdv_unload.txt");
      ps = new Parser(node, road);
      ps.parsePoints();
      ps.parseRoads(ps.points());
    }
    return ps;
  }

  /**
   * Parses all points in the network and puts it in a HashMap with point-ID as key,
   * and the point as value.
   */
  private void parsePoints() {
    System.out.println("- Parsing points");
    if(points == null) {
      points = new HashMap<Integer, Point>();
      try(BufferedReader input = new BufferedReader(new FileReader(nodes))) {  
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
  }
  
  /**
   * @return HashMap containing all nodes
   */
  public HashMap<Integer, Point> points() {
    return points;
  }

  /**
   * Parses all roads in the network and puts it in a DimensionalTree sorted by
   * the x and y coordinate of the road and road type.
   */
  private void parseRoads(HashMap<Integer, Point> points) {
    System.out.println("- Parsing roads");
    if(roads == null) {
      HashSet<Road> roadsForGraph = new HashSet<>(); //Temporary set for building the Graph
      roads = new DimensionalTree<Double, RoadType, Road>();
        try(BufferedReader input = new BufferedReader(new FileReader(connections))) {
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
  }
  
  /**
   * 
   * @return DimensionalTree containing all roads
   */
  public DimensionalTree<Double, RoadType, Road> roads() {
    return roads;
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
    return new Road(
        Integer.parseInt(inputSplit[0]),                  // 0 = id of from point
        Integer.parseInt(inputSplit[1]),                  // 1 = id of to point
        inputSplit[6],                                    // 6 = name
        RoadType.getById(Integer.parseInt(inputSplit[5])),// 5 = road type
        TrafficDirection.getDirectionById(inputSplit[27]),// 27 = traffic direction
        Double.parseDouble(inputSplit[2]),                // 2 = length
        Double.parseDouble(inputSplit[26]));              // 26 = time
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
  
  public double mapBound(MapBound mb) {
    return mapBounds.get(mb);
  }
  
  public Network network() {
    return graph;
  }
  
  public int numPoints() {
    return points.size();
  }

  public int numRoads() {
    return roads.count();
  }
}
