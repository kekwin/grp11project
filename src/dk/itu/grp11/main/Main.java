package dk.itu.grp11.main;

import java.io.*;

public class Main {
  public static void main(String[] args) {
    System.out.println("Program compiles as well :D :D :D :D");
    File node = new File("kdv_node_unload.txt");
    File road = new File("kdv_unload.txt");
    Parser p = new Parser(node, road);
    Point[] points = p.parsePoints();
    Road[] roads = p.parseRoads();
    Map map = new Map(points, roads);
    double[] d = p.getMinMaxValues();
    //getMinMaxValues i funktion
    System.out.println("minX=" + d[0]);
    System.out.println("maxX=" + d[1]);
    System.out.println("minY=" + d[2]);
    System.out.println("maxY=" + d[3]);
    FileServer fs = new FileServer(80, map);
    fs.run();    
  }
}
