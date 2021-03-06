package dk.itu.grp11.route;

import java.util.Set;

import dk.itu.grp11.data.Road;
import dk.itu.grp11.enums.TrafficDirection;
import dk.itu.grp11.util.DynArray;

/**
 * Describes a road network graph
 * 
 * @author Group 11
 * 
 */
public class Network {
  private final int numPoints; // Number of vertices (Point)
  private int numRoads; // Number of edges (Road)
  private DynArray<Road>[] adj; // Roads you can get to, from a point

  //Casting, as arrays cannot be of a generic type. This is OK to suppress.
  @SuppressWarnings("unchecked")
  /**
   * Creates a Network-graph, based upon the given roads.
   * 
   * @param numberOfPoints
   *          number of vertices in the network (really the maximum ID of the
   *          points)
   * @param roads
   *          roads being added to the network
   */
  public Network(int numberOfPoints, Set<Road> roads) {
    this.numPoints = numberOfPoints;
    this.numRoads = 0;
    adj = new DynArray[numberOfPoints+1]; // +1 as IDs start at 1 not 0
    for (int p = 0; p <= numberOfPoints; p++) {
      adj[p] = new DynArray<Road>(Road[].class);
    }
    for (Road r : roads) {
      addRoad(r);
    }
  }

  /**
   * Adds a road to the network graph. As the graph is directed, the opposite
   * road is also added.
   * 
   * @param r
   *          the road to add
   */
  private void addRoad(Road r) {
    adj[r.getFrom()].add(r);

    TrafficDirection d;
    if (r.getDirection() == TrafficDirection.DRIVING_NOT_ALLOWED)
      d = TrafficDirection.DRIVING_NOT_ALLOWED;
    else if (r.getDirection() == TrafficDirection.FROM_TO)
      d = TrafficDirection.TO_FROM;
    else if (r.getDirection() == TrafficDirection.TO_FROM)
      d = TrafficDirection.FROM_TO;
    else
      d = TrafficDirection.BOTH_WAYS;

    // Roads are only represented once in the HashSet. Needs to be represented
    // twice as the graph is directed. The to and from node are switched and the
    // TrafficDirection is inverted, if it is one way.
    Road opposite = new Road(r.getTo(), r.getFrom(), r.getName(),
        r.getFromZip(), r.getToZip(), r.getType(), d, r.getLength(),
        r.getTime());
    adj[opposite.getFrom()].add(opposite);
    numRoads++; // Actually there are added 2 edges. But only 1 real-word road.
  }

  /**
   * Returns the adjacency list for a point. The adjacency list contains all
   * roads you can get to, from a given point.
   * 
   * @param p
   *          the point
   * @return adjacency list for point p
   */
  public Iterable<Road> adjacent(int p) {
    return adj[p];
  }

  public int numPoints() {
    return numPoints;
  }

  public int numRoads() {
    return numRoads;
  }
}
