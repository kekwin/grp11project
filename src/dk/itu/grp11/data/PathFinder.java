package dk.itu.grp11.data;

import java.util.Stack;

import dk.itu.grp11.enums.TrafficDirection;
import dk.itu.grp11.enums.TransportationType;

//TODO javadoc
public class PathFinder {
  private Road[] roadTo;
  private double[] distTo;
  private double[] secondaryWeight;
  private IndexMinPQ<Double> pq;
  private boolean time;
  private TransportationType transType;

  public PathFinder(Network G, int s, boolean time, TransportationType transType) {
    roadTo = new Road[G.numPoints()];
    distTo = new double[G.numPoints()+1];
    secondaryWeight = new double[G.numPoints()];
    pq = new IndexMinPQ<Double>(G.numPoints());
    this.time = time;
    this.transType = transType;

    for (int p = 0; p < G.numPoints(); p++) {
      distTo[p] = Double.POSITIVE_INFINITY;
    }
    distTo[s] = 0.0;

    pq.insert(s, 0.0);
    while (!pq.isEmpty()) {
      int d = pq.delMin();
      relax(G, d);
    }
  }

  private void relax(Network G, int p) {
    for (Road r : G.adj(p)) {
      // Only relax if the road is allowed to be driven/walked on by the
      // transportation type
      if (((r.getDirection() == TrafficDirection.BOTH_WAYS || r.getDirection() == TrafficDirection.FROM_TO) || transType == TransportationType.WALK)
          && r.getType().isAllowed(transType)) {
        int w = r.getTo();
        double weight, sWeight;
        if (time) {
          weight = r.getTime();
          sWeight = r.getLength();
        }
        else {
          weight = r.getLength();
          sWeight = r.getTime();
        }
        if (distTo[w] > distTo[p] + weight) {
          distTo[w] = distTo[p] + weight;
          secondaryWeight[w] = secondaryWeight[p] + sWeight;
          roadTo[w] = r;
          if (pq.contains(w))
            pq.change(w, distTo[w]);
          else
            pq.insert(w, distTo[w]);
        }
      }
    }

  }

  // shortest path based on distance (if time = false) or time (if time = true)
  public double distTo(int p) {
    return distTo[p];
  }
  
  public double secondaryWeight(int p) {
    return secondaryWeight[p];
  }

  public boolean hasPathTo(int p) {
    return distTo[p] < Double.POSITIVE_INFINITY;
  }

  // shortest path from s to v as an Iterable, null if no such path
  public Iterable<Road> pathTo(int p) {
    if (!hasPathTo(p))
      return null;
    Stack<Road> path = new Stack<Road>();
    for (Road e = roadTo[p]; e != null; e = roadTo[e.getFrom()]) {
      path.push(e);
    }
    return path;
  }
}
