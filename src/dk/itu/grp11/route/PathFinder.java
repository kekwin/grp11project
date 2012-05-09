package dk.itu.grp11.route;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Queue;

import dk.itu.grp11.data.Parser;
import dk.itu.grp11.data.Point;
import dk.itu.grp11.data.Road;
import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.enums.TrafficDirection;
import dk.itu.grp11.enums.TransportationType;
import dk.itu.grp11.util.IndexMinPQ;

public class PathFinder {
  private Road[] roadTo;
  private double[] primaryWeight; // The weight being relaxed
  private double[] secondaryWeight; // The corresponding weight. Time if time =
                                    // true. Length if time = false.
  private IndexMinPQ<Double> pq;
  private TransportationType transType;
  private EnumMap<MapBound, Double> bounds;
  private boolean time;
  private boolean ferries;
  private boolean highways;

  /**
   * Finds the shortest path in a Network graph from a source point. The path is
   * weighted by either time or length. Obey traffic rules of the specified
   * transportation type.
   * 
   * @param G
   *          Road network to find the shortest path in
   * @param source
   *          The source point
   * @param time
   *          If true, the total weight is based upon the shortest time. If
   *          false - upon the shortest distance.
   * @param transType
   *          The type of transportation
   */
  public PathFinder(Network G, int source, boolean time,
      TransportationType transType, boolean ferries, boolean highways) {
    roadTo = new Road[G.numPoints()];
    primaryWeight = new double[G.numPoints() + 1];
    secondaryWeight = new double[G.numPoints() + 1];
    pq = new IndexMinPQ<Double>(G.numPoints());
    this.time = time;
    this.transType = transType;
    this.ferries = ferries;
    this.highways = highways;
    bounds = new EnumMap<MapBound, Double>(MapBound.class);
    bounds.put(MapBound.MINX, Double.POSITIVE_INFINITY);
    bounds.put(MapBound.MAXX, Double.NEGATIVE_INFINITY);
    bounds.put(MapBound.MINY, Double.POSITIVE_INFINITY);
    bounds.put(MapBound.MAXY, Double.NEGATIVE_INFINITY);

    for (int p = 0; p < G.numPoints(); p++) {
      primaryWeight[p] = Double.POSITIVE_INFINITY;
    }
    primaryWeight[source] = 0.0;

    pq.insert(source, 0.0);
    while (!pq.isEmpty()) {
      int d = pq.delMin();
      relax(G, d);
    }
  }

  // TODO javadoc
  /**
   * Relaxes a graph
   * 
   * @param G
   *          The graph
   * @param p
   *          the point
   */
  private void relax(Network G, int p) {
    for (Road r : G.adj(p)) {
      // Only relax if the road is allowed to be driven/walked on by the
      // transportation type
      if (((r.getDirection() == TrafficDirection.BOTH_WAYS || r.getDirection() == TrafficDirection.FROM_TO) || transType == TransportationType.WALK)
          && r.getType().isAllowed(transType)) {
          //TODO calculate for highways and ferries
          int w = r.getTo();
          double weight, sWeight;
          if (time) {
            weight = r.getTime();
            sWeight = r.getLength();
          } else {
            weight = r.getLength();
            sWeight = r.getTime();
          }
          if (primaryWeight[w] > primaryWeight[p] + weight) {
            primaryWeight[w] = primaryWeight[p] + weight;
            secondaryWeight[w] = secondaryWeight[p] + sWeight;
            roadTo[w] = r;
            if (pq.contains(w))
              pq.change(w, primaryWeight[w]);
            else
              pq.insert(w, primaryWeight[w]);
          }
      }
    }
  }

  /**
   * Returns the distance to a given point. If the time boolean was set to false
   * in the constructor, the distance will be the shortest possible. Otherwise
   * it will be the distance of the route of shortest time.
   * 
   * @param p
   *          The point
   * @return The distance
   */
  public double distTo(int p) {
    if (time == false)
      return primaryWeight[p];
    else
      return secondaryWeight[p];
  }

  /**
   * Returns the shortest time to a given point. If the time boolean was set to
   * true in the constructor, the time will be the shortest possible. Otherwise
   * it will be the time of the route of shortest distance.
   * 
   * @param p
   *          The point
   * @return The time
   */
  public double timeTo(int p) {
    if (!time)
      return secondaryWeight[p];
    else
      return primaryWeight[p];
  }

  /**
   * Checks if a point can be reached (is not connected the rest of the road
   * network graph).
   * 
   * @param p
   *          The point to be reached
   * @return True if such path exist, otherwise false.
   */
  public boolean hasPathTo(int p) {
    return primaryWeight[p] < Double.POSITIVE_INFINITY;
  }

  /**
   * Returns the route to a point as an Iterable. In order from the given point
   * to the source point.
   * 
   * @param p
   *          The point
   * @return The route. Returns null if no such path exist.
   */
  public Iterable<Road> pathTo(int p) {
    if (!hasPathTo(p))
      return null;
    Queue<Road> path = new LinkedList<Road>();
    for (Road r = roadTo[p]; r != null; r = roadTo[r.getFrom()]) {
      path.add(r);
      Point p1 = Parser.getParser().points().get(r.getFrom());
      Point p2 = Parser.getParser().points().get(r.getTo());
      updatePathBounds(p1.getX(), p1.getY());
      updatePathBounds(p2.getX(), p2.getY());
    }
    return path;
  }

  private void updatePathBounds(double x, double y) {
    if (x > bounds.get(MapBound.MAXX) - Parser.mapBoundOffset)
      bounds.put(MapBound.MAXX, x + Parser.mapBoundOffset);
    if (x < bounds.get(MapBound.MINX) + Parser.mapBoundOffset)
      bounds.put(MapBound.MINX, x - Parser.mapBoundOffset);
    if (y > bounds.get(MapBound.MAXY) - Parser.mapBoundOffset)
      bounds.put(MapBound.MAXY, y + Parser.mapBoundOffset);
    if (y < bounds.get(MapBound.MINY) + Parser.mapBoundOffset)
      bounds.put(MapBound.MINY, y - Parser.mapBoundOffset);
  }

  public double pathBound(MapBound mb) {
    return bounds.get(mb);
  }
}