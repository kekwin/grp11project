package dk.itu.grp11.main;

import dk.itu.grp11.enums.RoadType;

/**
 * Defines a road by a start point and endpoint including a name and roadtype.

 * @param P1
 *          The ID of the first Point of the road, where the road "starts"
 * @param P2
 *          The ID of the second Point of the road, where the road "ends"
 * @param name
 *          The name of the road
 * @param type
 *          The type of the road, described as a RoadType
 * 
 */

public class Road {
  int P1;
  int P2;
  String name;
  RoadType type;

  // More fields to come

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
