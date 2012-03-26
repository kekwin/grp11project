package dk.itu.grp11.main;

public class Point {

  private int ID;
  private double x;
  private double y;
  
  public Point(int id, double x, double y){
    ID = id;
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
