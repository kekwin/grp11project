package dk.itu.grp11.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import dk.itu.grp11.data.Parser;
import dk.itu.grp11.data.Road;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.enums.TrafficDirection;
import dk.itu.grp11.enums.TransportationType;
import dk.itu.grp11.route.Network;
import dk.itu.grp11.route.PathFinder;

public class PathFinderTest {
  Parser p = Parser.getParser();
  Network g = p.network();

  @Test
  public void test0() {
    System.out.println("\nTEST 0");
    Set<Road> roads = new HashSet<>();
    
    roads.add(new Road(1, 2, "Vej 1", 5000, 5000, RoadType.MOTORVEJ, TrafficDirection.BOTH_WAYS, 10, 0.5));
    roads.add(new Road(2, 3, "Vej 2", 5000, 5000, RoadType.MOTORVEJ, TrafficDirection.BOTH_WAYS, 30, 0.2));
    roads.add(new Road(1, 4, "Vej 3", 5000, 5000, RoadType.MOTORVEJ, TrafficDirection.TO_FROM, 7, 0.7));
    roads.add(new Road(4, 3, "Vej 4", 5000, 5000, RoadType.MOTORVEJ, TrafficDirection.BOTH_WAYS, 22, 0.8));
    roads.add(new Road(1, 5, "Vej 5", 5000, 5000, RoadType.MOTORVEJ, TrafficDirection.FROM_TO, 3, 0.33));
    //roads.add(new Road(1, 5, "Vej 5", 5000, 5000, RoadType.MOTORVEJ, TrafficDirection.DRIVING_NOT_ALLOWED, 3, 0.33)); will go through point 6 to get to point 5
    roads.add(new Road(4, 5, "Vej 6", 5000, 5000, RoadType.VEJ_3_TIL_6M, TrafficDirection.DRIVING_NOT_ALLOWED, 2, 0.12));
    roads.add(new Road(4, 5, "Vej 10", 5000, 5000, RoadType.STI, TrafficDirection.BOTH_WAYS, 4, 0.10));
    roads.add(new Road(3, 5, "Vej 7", 5000, 5000, RoadType.MOTORVEJ, TrafficDirection.FROM_TO, 33, 0.54));
    roads.add(new Road(1, 6, "Vej 8", 5000, 5000, RoadType.MOTORVEJ, TrafficDirection.BOTH_WAYS, 2, 0.5));
    roads.add(new Road(6, 5, "Vej 9", 5000, 5000, RoadType.MOTORVEJ, TrafficDirection.FROM_TO, 1, 0.5));
    
    Network g = new Network(7, roads);
    
    int[][] routes = new int[8][2];
    routes[0][0] = 1; routes[0][1] = 4; //62
    routes[1][0] = 4; routes[1][1] = 1; //7
    
    routes[2][0] = 2; routes[2][1] = 3; //30
    routes[3][0] = 3; routes[3][1] = 2; //30
    
    routes[4][0] = 5; routes[4][1] = 2; //Infinity
    routes[5][0] = 2; routes[5][1] = 5; //13
    
    routes[6][0] = 4; routes[6][1] = 5; //(CAR = 10) (BICYCLE = 4) (WALK = 2)
    routes[7][0] = 5; routes[7][1] = 4; //Infinity

    for(int i = 0; i < routes.length; i++) {
      int from = routes[i][0];
      int to = routes[i][1];
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
      System.out.println();
      double expected;
      switch(i) {
        case 0: expected = 62; break;
        case 1: expected = 7; break;
        case 2: expected = 30; break;
        case 3: expected = 30; break;
        case 4: expected = Double.POSITIVE_INFINITY; break;
        case 5: expected = 13; break;
        case 6: expected = 10; break;
        case 7: expected = Double.POSITIVE_INFINITY; break;
        default: expected = Double.NEGATIVE_INFINITY; //Not really expected - will therefore make the test fail
      }
      assertEquals(expected, pf.distTo(to), 0);
    }
  }
  
  @Test
  public void test1() {
    System.out.println("\nTEST 1");
    PathFinder pf = new PathFinder(g, 599909, false, TransportationType.CAR);
    System.out.println(pf.distTo(599840));
    /*
     * 599909 -> 599836 = 70.70321
     * 599909 -> 599840 = 74.15599
     * 
     */
    //TODO Not finished
  }
  
  @Test
  public void test2() {
    System.out.println("\nTEST 2");
    PathFinder pf = new PathFinder(g, 406585, true, TransportationType.CAR);
    System.out.println(pf.distTo(406550));
    System.out.println(pf.timeTo(406550));
  }
}
