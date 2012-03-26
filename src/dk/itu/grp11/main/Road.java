package dk.itu.grp11.main;

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
  
}
