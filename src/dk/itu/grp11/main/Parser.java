package dk.itu.grp11.main;

import java.io.*;
import dk.itu.grp11.contrib.DynArray;

/**
 * The Parser class is used to parse the information from the two documents
 * kdv_node_unload.txt and kdv_unload.txt into arrays of Points and Roads, which
 * are then used by our Map class.
 * 
 * @param nodes
 *          A java.File object referencing the kdv_node_unload.txt file.
 * @param connections
 *          A java.File object referencing the kdv_unload.txt file.
 * 
 * @author Asus
 * 
 */
public class Parser {

  private File nodes;
  private File connections;
  private double minX = 1000000;
  private double maxX = 0;
  private double minY = 100000000.0;
  private double maxY = 0;
  
  // the constructor takes 2 arguments, the kdv.node_unload.txt file and the
  // kdv_unload.txt file.
  public Parser(File nodeFile, File connectionFile) {
    nodes = nodeFile;
    connections = connectionFile;
  }

  /**
   * The parsePoints() function runs through the "nodes" file, and creates a new
   * Point object for each line, and puts it into the Array to be returned.
   * 
   * @return An array of all Point objects from the information given in the
   *         kdv_node_unload.txt file.
   */
  public Point[] parsePoints() {
    Point[] tmp = new Point[675902]; // NON DYNAMIC
    //DynArray<Point> tmp = new DynArray<Point>();  DYNARRAY VARIANT
    int index = 0;
    try {
      BufferedReader input = new BufferedReader(new FileReader(nodes));
      try {
        String line = null;
        /*
         * readLine is a bit quirky : it returns the content of a line MINUS the
         * newline. it returns null only for the END of the stream. it returns
         * an empty String if two newlines appear in a row.
         */
        input.readLine();
        while ((line = input.readLine()) != null) {
          Point p = createPoint(line);
          if (p.getX()>maxX){
            maxX=p.getX();
          }
          if (p.getX()<minX){
            minX=p.getX();
          }
          if (p.getY()>maxY){
            maxY=p.getY();
          }
          if (p.getY()<minY){
            minY=p.getY();
          }
          tmp[index]=p; // NON DYNAMIC
          // tmp.add(p); DYNARRAY VARIANT
          index++;
        }
      } finally {
        input.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return tmp; // NON DYNAMIC
    //return tmp.toArray();   DYNARRAY VARIANT
  }

  /**
   * The parseRoads() function runs through the "connections" file, and creates
   * a new Road object for each line, and puts it into the Array to be returned.
   * 
   * @return An array of all Road objects from the information given in the
   *         kdv_unload.txt file.
   */
  public Road[] parseRoads() {
    Road[] tmp = new Road[850000]; // NON DYNAMIC
    //DynArray<Road> tmp = new DynArray<Road>();  DYNARRAY VARIANT
    int index = 0;
    try {
      //HER ER IKKE NOGEN STRINGBUILDER
      BufferedReader input = new BufferedReader(new FileReader(connections));
      try {
        String line = null;
        /*
         * readLine is a bit quirky : it returns the content of a line MINUS the
         * newline. it returns null only for the END of the stream. it returns
         * an empty String if two newlines appear in a row.
         */
        input.readLine();
        while ((line = input.readLine()) != null) {
          tmp[index]=createRoad(line); // NON DYNAMIC
          // tmp.add(createRoad(line)); DYNARRAY VARIANT
          index++;
        }
      } finally {
        input.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return tmp; // NON DYNAMIC
    //return tmp.toArray();   DYNARRAY VARIANT
  }

  // Creates a point, to be put in the array and parsed.
  /**
   * createPoint splits a line from the kdv_node_unload.txt document and then
   * creates a Point object from the information in the string.
   * 
   * @param input
   *          A line from the kdv_node_unload.txt document.
   * @return A Point object containing the information from the line.
   */
  private static Point createPoint(String input) {
    Point tmp;
    String[] inputSplit = input.split(",");
    tmp = new Point(Integer.parseInt(inputSplit[2]),
        Double.parseDouble(inputSplit[3]), Double.parseDouble(inputSplit[4]));

    return tmp;
  }

  /**
   * createPoint splits a line from the kdv_unload.txt document and then creates
   * a Road object from the information in the string.
   * 
   * @param input
   *          A line from the kdv_unload.txt document.
   * @return A Road object containing the information from the line.
   */
  private static Road createRoad(String input) {
    Road tmp = null;

    String[] inputSplit = input.split(",");
    if (Integer.parseInt(inputSplit[0]) != Integer.parseInt(inputSplit[1])) {

      tmp = new Road(Integer.parseInt(inputSplit[0]),
          Integer.parseInt(inputSplit[1]), inputSplit[6],
          Integer.parseInt(inputSplit[5]));

    }
    return tmp;
  }
  /**
   * Returns an array containing 4 values, representing the smallest and largest x and y coordinates of all Points.
   * [0]=minX
   * [1]=maxX
   * [2]=minY
   * [3]=maxY
   * 
   * CAN ONLY BE CALLED IF THE PARSEPOINTS() FUNCTION HAS ALREADY BEEN CALLED!
   * @return A double array of size 4.
   */
  public double[] getMinMaxValues(){
    double[] tmp = new double[4];
    tmp[0]=minX;
    tmp[1]=maxX;
    tmp[2]=minY;
    tmp[3]=maxY;
    return tmp;
  }

}
