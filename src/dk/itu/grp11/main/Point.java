package dk.itu.grp11.main;

import dk.itu.grp11.exceptions.NegativeValueException;

/**
 * A point representing a location in (x,y) coordinate space, specified in double precision.
 * The point also have a id.
 * 
 * @param ID
 *          The ID of the point
 * @param x
 *          The x-coordinate of the Point.
 * @param y
 *          The y-coordinate of the Point.
 *          
 * @throws NegativeValueException if x or y value is negative
 * 
 */
public class Point {

  private int ID;
  private double x;
  private double y;

  public Point(int id, double x, double y) throws NegativeValueException {
    ID = id;
    if(Math.abs(x) != x && Math.abs(y) != y) throw new NegativeValueException();
    this.x = x;
    this.y = y;
  }

  public int getID() {
    return ID;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public String toString() {
    return "Point id: " + ID + " x:" + x + " y:" + y;

  }
}
