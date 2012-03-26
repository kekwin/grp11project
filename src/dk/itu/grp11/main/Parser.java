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

  File nodes;
  File connections;

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
    DynArray<Point> tmp = new DynArray<Point>();
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
          tmp.add(createPoint(line));
          index++;
        }
      } finally {
        input.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    return tmp.toArray();
  }

  /**
   * The parseRoads() function runs through the "connections" file, and creates
   * a new Road object for each line, and puts it into the Array to be returned.
   * 
   * @return An array of all Road objects from the information given in the
   *         kdv_unload.txt file.
   */
  public Road[] parseRoads() {
    DynArray<Road> tmp = new DynArray<Road>();
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
          tmp.add(createRoad(line));
          index++;
        }
      } finally {
        input.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return tmp.toArray();
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
  public Point createPoint(String input) {
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
  public Road createRoad(String input) {
    Road tmp = null;

    String[] inputSplit = input.split(",");
    if (Integer.parseInt(inputSplit[0]) != Integer.parseInt(inputSplit[1])) {

      tmp = new Road(Integer.parseInt(inputSplit[0]),
          Integer.parseInt(inputSplit[1]), inputSplit[6],
          Integer.parseInt(inputSplit[5]));

    }
    return tmp;
  }

}
