package dk.itu.grp11.data;

import java.util.HashSet;
import java.util.Set;

import dk.itu.grp11.enums.TrafficDirection;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.enums.TransportationType;
//TODO javadoc
public class Network {
  private final int P; //number of vertices/Points
  private int R; //number of edges/Roads
  private HashSet<Road>[] adj;
  
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
