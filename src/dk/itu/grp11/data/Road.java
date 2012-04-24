package dk.itu.grp11.data;

import dk.itu.grp11.enums.RoadType;

/**
 * Defines a road by a start point and endpoint including a name and roadtype.
 * 
 * @author Group 11
 */

public class Road {
  private int id;
  private int P1;
  private int P2;
  private String name;
  private RoadType type;
  private double length;
  private double time;
  private static int nxtID = 0;
  
  
  /**
   * 
   * @param p1 ID of the first Point
   * @param p2 ID of the second Point
   * @param name name of the road
   * @param type type of road
   */
  public Road(int p1, int p2, String name, RoadType type, double length, double time) {
    this.P1 = p1;
    this.P2 = p2;
    this.name = name;
    this.type = type;
    this.length = length;
    this.time = time;
    this.id = nxtID++;
  }

  /**
   * Start point
   * 
   * @return id of the start point
   */
  public int getFrom() {
    return P1;
  }
  
  /**
   * End point
   * 
   * @return id of the end point
   */
  public int getTo() {
    return P2;
  }

  public String getName() {
    return name;
  }

  public RoadType getType() {
    return type;
  }
  
  public double getLength() {
    return length;
  }
  
  public double getTime() {
    return time;
  }
  
  public int getId() {
    return id;
  }

  public String toString() {
    return "["+id+"] name='" + name + "' type=" + type + " P1=" + P1 + " P2=" + P2;
  }

}
