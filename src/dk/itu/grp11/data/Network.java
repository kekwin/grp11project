package dk.itu.grp11.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.TrafficDirection;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.enums.TransportationType;

public class Network {
  private final int P; //number of vertices/Points
  private int R; //number of edges/Roads
  private HashSet<Road>[] adj;
  
  /*public static void main(String[] args) {
    Parser pars = Parser.getParser();
    System.out.println(Parser.numRoads());
    pars.parseRoads(pars.parsePoints());
    Road[] points = pars.getRoads();
    System.out.println(Parser.numRoads());
    System.out.println("point[] length " + points.length);
    
    Graph g = new Graph(Parser.numPoints(), r);
  }
  
  public static void main(String[] args) {
    Parser p = Parser.getParser();
    Network g = p.network();
    PathFinder pf = new PathFinder(g, 599909, false, TransportationType.CAR);
    System.out.println(pf.distTo(599840));
    /*
     * 599909 -> 599836 = 70.70321
     * 599909 -> 599840 = 74.15599
     * 
     *//*
  }
  */
  /**/public static void main(String[] args) {
    Set<Road> roads = new HashSet<>();
    
    roads.add(new Road(1, 2, "Vej 1", RoadType.MOTORVEJ, TrafficDirection.BOTH_WAYS, 10, 0.5));
    roads.add(new Road(2, 3, "Vej 2", RoadType.MOTORVEJ, TrafficDirection.BOTH_WAYS, 30, 0.2));
    roads.add(new Road(1, 4, "Vej 3", RoadType.MOTORVEJ, TrafficDirection.TO_FROM, 7, 0.7));
    roads.add(new Road(4, 3, "Vej 4", RoadType.MOTORVEJ, TrafficDirection.BOTH_WAYS, 22, 0.8));
    roads.add(new Road(1, 5, "Vej 5", RoadType.MOTORVEJ, TrafficDirection.FROM_TO, 3, 0.33));
    //roads.add(new Road(1, 5, "Vej 5", RoadType.MOTORVEJ, TrafficDirection.DRIVING_NOT_ALLOWED, 3, 0.33)); will go through point 6 to get to point 5
    roads.add(new Road(4, 5, "Vej 6", RoadType.VEJ_3_TIL_6M, TrafficDirection.DRIVING_NOT_ALLOWED, 2, 0.12));
    roads.add(new Road(4, 5, "Vej 10", RoadType.STI, TrafficDirection.BOTH_WAYS, 4, 0.10));
    roads.add(new Road(3, 5, "Vej 7", RoadType.MOTORVEJ, TrafficDirection.FROM_TO, 33, 0.54));
    roads.add(new Road(1, 6, "Vej 8", RoadType.MOTORVEJ, TrafficDirection.BOTH_WAYS, 2, 0.5));
    roads.add(new Road(6, 5, "Vej 9", RoadType.MOTORVEJ, TrafficDirection.FROM_TO, 1, 0.5));
    
    Network g = new Network(7, roads);
    
    //int from = 1; int to = 4; // 62
    //int from = 4; int to = 1; // 7
    
    //int from = 2; int to = 3; // 30
    //int from = 3; int to = 2; // 30
    
    //int from = 5; int to = 2; // Infinity
    //int from = 2; int to = 5; // 13
    
    int from = 4; int to = 5; // (CAR = 10) (BICYCLE = 4) (WALK = 2)
    //int from = 5; int to = 4; // Infinity
    System.out.print("Shortest path from " + from + " to " + to + ": ");
    PathFinder pf = new PathFinder(g, from, false, TransportationType.CAR);
    System.out.println(pf.distTo(to) + "M");
    System.out.println("Route:");
    if(pf.hasPathTo(to)) {
      for (Road r : pf.pathTo(to)) {
        System.out.println(r);
      }
    }
    else System.out.println("No route exist");
  }
  
  /*public static void main(String[] args) {
    Parser pars = Parser.getParser();
    HashMap<Integer, Point> points = pars.parsePoints();
    DimensionalTree<Double, RoadType, Road> tree = pars.parseRoads(points);
    HashSet<Road> roads = new HashSet<>();
    
    for (RoadType roadType : RoadType.values()) {
      Interval<Double, RoadType> i1 = new Interval<Double, RoadType>(Parser.getMapBound(MapBound.MINX), Parser.getMapBound(MapBound.MAXX), roadType);
      Interval<Double, RoadType> i2 = new Interval<Double, RoadType>(Parser.getMapBound(MapBound.MINY), Parser.getMapBound(MapBound.MAXY), roadType);
      Interval2D<Double, RoadType> i2D = new Interval2D<Double, RoadType>(i1, i2);
      
      roads.addAll(tree.query2D(i2D));
    } 
    
    Network g = new Network(points.size(), roads);
    
    int from = 1; int to = 500;
    System.out.print("Shortest path from " + from + " to " + to + ": ");
    PathFinder pf = new PathFinder(g, from, false);
    System.out.println(pf.distTo(to) + "M");
    System.out.println("Route:");
    for (Road r : pf.pathTo(to)) {
      System.out.println(r);
    }
  }*/
  
  private Network(int P) {
    this.P = P;
    this.R = 0;
    adj = (HashSet<Road>[]) new HashSet[P+1]; //TODO Bag necessary ? P+1 because IDs start at 1, not 0
    for(int p = 0; p <= P; p++) {
      adj[p] = new HashSet<Road>();
    }
  }
  
  public Network(int numberOfPoints, Set<Road> roads) {
    this(numberOfPoints);
    for (Road r : roads) {
        addRoad(r);
    }
  }
  
  public int numPoints() { return P; }
  public int numRoads() { return R; }
  
  private void addRoad(Road r) {
    adj[r.getFrom()].add(r);
    
    TrafficDirection d;
    if(r.getDirection() == TrafficDirection.DRIVING_NOT_ALLOWED) d = TrafficDirection.DRIVING_NOT_ALLOWED;
    else if(r.getDirection() == TrafficDirection.FROM_TO) d = TrafficDirection.TO_FROM;
    else if(r.getDirection() == TrafficDirection.TO_FROM) d = TrafficDirection.FROM_TO;
    else d = TrafficDirection.BOTH_WAYS;
    
    Road opposite = new Road(r.getTo(), r.getFrom(), r.getName(), r.getType(), d, r.getLength(), r.getTime());
    adj[opposite.getFrom()].add(opposite);
    R+=2;
  }
  
  public Iterable<Road> adj(int p) {
    return adj[p];
  }
  
  public Iterable<Road> roads() {
    HashSet<Road> allRoads = new HashSet<>();
    for(int p = 0; p < P; p++) {
      for(Road r : adj[p]) {
        allRoads.add(r);
      }
    }
    return allRoads;
  }
}
