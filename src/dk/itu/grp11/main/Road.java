package dk.itu.grp11.main;

public class Road {

 
  Point P1;
  Point P2;
  String name;
  int type;
  //More fields to come
  
  public Road(Point p1, Point p2, String name, int type) {
    P1 = p1;
    P2 = p2;
    this.name = name;
    this.type = type;
  }

  public Point getP1() {
    return P1;
  }

  public Point getP2() {
    return P2;
  }

  public String getName() {
    return name;
  }

  public int getType() {
    return type;
  }
  
}
