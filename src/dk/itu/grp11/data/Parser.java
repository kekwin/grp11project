package dk.itu.grp11.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;

import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.exceptions.DataNotInitializedException;

/**
 * Parses information about a road network.
 * 
 * @author Group 11
 * 
 */
public class Parser {
  
  private static Parser ps = null;
  private static boolean pointsInit = false;
  private static boolean roadsInit = false;
  private static File nodes;
  private static File connections;
  private static EnumMap<MapBound, Double> mapBounds;
  private static HashMap<Integer, Point> points = null;
  private static DimensionalTree<Double, RoadType, Road> roads = null;
  
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
    }
    return ps;
  }

  /**
   * Parses all points in the network and puts it in a HashMap with point-ID as key,
   * and the point as value.
   * 
   * @return HashMap containing all nodes
   */
  public HashMap<Integer, Point> parsePoints() {
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
          pointsInit = true; //Points have been initialized (and getMapBound() can be called)
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return points;
  }

  /**
   * Parses all roads in the network and puts it in a DimensionalTree sorted by
   * the x and y coordinate of the road and road type.
   * 
   * @return DimensionalTree containing all roads
   */
  public DimensionalTree<Double, RoadType, Road> parseRoads(HashMap<Integer, Point> points) {
    if(roads == null) {
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
          }
          roadsInit = true;
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
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
    /*
     * 0 = id of P1
     * 1 = id of P2
     * 6 = name
     * 5 = RoadType
     * 2 = length
     * 26 = time
     */
    return new Road(Integer.parseInt(inputSplit[0]), Integer.parseInt(inputSplit[1]),
        inputSplit[6], RoadType.getById(Integer.parseInt(inputSplit[5])),
        Double.parseDouble(inputSplit[2]), Double.parseDouble(inputSplit[26]));
  }

  // Creates a point, to be put in the array and parsed.
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
    return new Point(Integer.parseInt(inputSplit[2]),
        Double.parseDouble(inputSplit[3]), Double.parseDouble(inputSplit[4]));
  }
  
  /**
   * Returns an array containing 4 values, representing the smallest and largest x and y coordinates of all Points.
   * [0]=minX.
   * [1]=maxX.
   * [2]=minY.
   * [3]=maxY.
   * 
   * Values can be referenced using the MinMax enum.
   * 
   * CAN ONLY BE CALLED IF THE PARSEPOINTS() FUNCTION HAS ALREADY BEEN CALLED!
   * @return Maximum/minimum x- and y-coordinates.
   * @throws DataNotInitializedException if points has not been initialized.
   */
  public static double getMapBound(MapBound mb) throws DataNotInitializedException {
    if(!pointsInit) throw new DataNotInitializedException(); // Checks if data has been initialized (parsed)
    return mapBounds.get(mb);
  }
  
  public static int numPoints() {
    if(!pointsInit) throw new DataNotInitializedException();
    return points.size();
  }

  public static int numRoads() {
    if(!roadsInit) throw new DataNotInitializedException();
    return roads.count();
  }

}
