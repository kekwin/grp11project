package dk.itu.grp11.data;

import dk.itu.grp11.enums.TrafficDirection;
import dk.itu.grp11.enums.RoadType;

/**
 * Defines a road by a start point and endpoint including a name and roadtype.
 * 
 * @author Group 11
 */

public class Road {
  private int id;
  private int from;
  private int to;
  private String name;
  private RoadType type;
  private TrafficDirection direction;
  private double length;
  private double time;
  private static int nxtID = 0;
  
  
  /**
   * 
   * @param from ID of the first Point
   * @param to ID of the second Point
   * @param name name of the road
   * @param type type of road
   */
  public Road(int from, int to, String name, RoadType type, TrafficDirection direction, double length, double time) {
    this.from = from;
    this.to = to;
    this.name = name;
    this.type = type;
    this.direction = direction;
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
    return from;
  }
  
  /**
   * End point
   * 
   * @return id of the end point
   */
  public int getTo() {
    return to;
  }

  public String getName() {
    return name;
  }

  public RoadType getType() {
    return type;
  }
  
  public TrafficDirection getDirection() {
    return direction;
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
    return "["+id+"] name='" + name + "' type=" + type + " from=" + from + " to=" + to + " length=" + length + " time=" + time;
  }

}
