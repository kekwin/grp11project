package dk.itu.grp11.main;

/**
 * Objects of the class Road are "connections" from the file kdv_unload.txt, and they are the basis of our visualization part.
 * 
 * A road is defined by two points, and is simply a line between the two points.
 * @param P1 The ID of the first Point of the road, where the road "starts"
 * @param P2 The ID of the second Point of the road, where the road "ends"
 * @param name The name of the road, if there is one
 * @param type The type of the road, described as an integer, type describes whether a road is a highway, a tunnel, etc.
 *
 */

public class Road {
  int P1;
  int P2;
  String name;
  int type;
  //More fields to come
  
  public Road(int p1, int p2, String name, int type) {
    this.P1 = p1;
    this.P2 = p2;
    this.name = name;
    this.type = type;
  }

  public int getP1() {
    return P1;
  }

  public int getP2() {
    return P2;
  }

  public String getName() {
    return name;
  }

  public int getType() {
    return type;
  }
  
  public String toString(){
    return "Road name: " + name + " type: " + type + " P1: " + P1 + " P2: " + P2;
  }
  
}
