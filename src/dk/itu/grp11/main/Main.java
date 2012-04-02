package dk.itu.grp11.main;

import java.io.File;
import java.util.HashMap;

import dk.itu.grp11.contrib.DimensionalTree;
import dk.itu.grp11.enums.RoadType;

public class Main {
  public static void main(String[] args) {
    System.out.println("Program started");
    File node = new File("kdv_node_unload.txt");
    File road = new File("kdv_unload.txt");
    Parser p = new Parser(node, road);
    
    long startTime = System.nanoTime();
    HashMap<Integer, Point> points = p.parsePoints();
    DimensionalTree<Double, RoadType, Road> roads = p.parseRoads(points);
    System.out.println("Loaded " + roads.count()/2 + " roads from file in " + ((System.nanoTime() - startTime)/1000000000.0) + "s");
    
    Map map = new Map(points, roads, p.getMinMaxValues());
    double[] d = p.getMinMaxValues();
    //getMinMaxValues i funktion
    System.out.println("minX=" + d[0]);
    System.out.println("maxX=" + d[1]);
    System.out.println("minY=" + d[2]);
    System.out.println("maxY=" + d[3]);
    FileServer fs = new FileServer(80, map);
    fs.run(); 
    System.out.println("");
    //TEST
  }
}
