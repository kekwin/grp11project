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
    FileServer fs = new FileServer(80, map);
    fs.run();
  }
}
