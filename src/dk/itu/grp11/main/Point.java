package dk.itu.grp11.main;

import dk.itu.grp11.exceptions.NegativeValueException;

/**
 * Objects of the class Point are single "nodes" from the kdv_node_unload.txt.
 * @param ID The ID of the point, used to reference points from within roads.
 * @param x The x-coordinate of the Point.
 * @param y The y-coordinate of the Point.
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
  
  public String toString(){
    return "Point id: " + ID + "x: " + x + "y: " + y;
    
  }
}
