package dk.itu.grp11.main;

import dk.itu.grp11.enums.RoadType;

/**
 * Defines a road by a start point and endpoint including a name and roadtype.
 * 
 * @author Group 11
 */

public class Road {
  private int P1;
  private int P2;
  private String name;
  private RoadType type;
  
  
  /**
   * 
   * @param p1 ID of the first Point
   * @param p2 ID of the second Point
   * @param name name of the road
   * @param type type of road
   */
  public Road(int p1, int p2, String name, RoadType type) {
    this.P1 = p1;
    this.P2 = p2;
    this.name = name;
    this.type = type;
  }

  /**
   * Start point
   * 
   * @return id of the start point
   */
  public int getP1() {
    return P1;
  }
  
  /**
   * End point
   * 
   * @return id of the end point
   */
  public int getP2() {
    return P2;
  }

  public String getName() {
    return name;
  }

  public RoadType getType() {
    return type;
  }

  public String toString() {
    return "Road name: " + name + " type: " + type + " P1: " + P1 + " P2: "
        + P2;
  }

}
