package dk.itu.grp11.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;

import org.junit.Test;

import dk.itu.grp11.data.DimensionalTree;
import dk.itu.grp11.data.Map;
import dk.itu.grp11.data.Point;
import dk.itu.grp11.data.Road;
import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.RoadType;

public class MapTest {
  @Test
  public void test0() {
    Point p1 = new Point(1, 300, 356);
    Point p2 = new Point(2, 390, 377);
    HashMap<Integer, Point> points = new HashMap<>();
    points.put(1, p1);
    points.put(2, p2);
    
    Road r = new Road(p1.getID(), p2.getID(), "Niceness street", RoadType.MOTORVEJ);
    
    DimensionalTree<Double, RoadType, Road> roads = new DimensionalTree<Double, RoadType, Road>(Road[].class);
    roads.insert(points.get(r.getP1()).getX(), points.get(r.getP1()).getY(), RoadType.MOTORVEJ, r);
    roads.insert(points.get(r.getP2()).getX(), points.get(r.getP2()).getY(), RoadType.MOTORVEJ, r);
    
    Map map = new Map(points, roads);
    
    System.out.println("Getting part: " + map.getPart(320, 330, 150, 100, 0, 0, 1));
  }
  
  
  
  /*
  
  // Testing single road in viewbox
  @Test
  public void test0() {
    Point[] points = new Point[10];
    points[0] = new Point(1, 300, 356);
    points[1] = new Point(2, 390, 377);
    
    Road[] roads = new Road[10];
    roads[0] = new Road(1, 2, "Niceness street", 1);
    
    Map map = new Map(points, roads, new double[] {}); //Empty double array
    
    assertEquals(map.getPart(320, 330, 150, 100), "<line id=\"line\" x1=\""+300.0+"\" y1=\""+356.0+"\" x2=\""+390.0+"\" y2=\""+377.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"></line>\n");
  }
  
  //Testing two roads in viewbox and one not in viewbox
  @Test
  public void test1() {
    Point[] points = new Point[10];
    points[0] = new Point(1, 300, 356);
    points[1] = new Point(2, 390, 377);
    points[2] = new Point(3, 800, 700);
    points[3] = new Point(4, 430, 431);
    
    Road[] roads = new Road[10];
    roads[0] = new Road(1, 2, "Niceness street", 1);
    roads[1] = new Road(2, 3, "Long street", 1);
    roads[2] = new Road(3, 4, "Fail street", 1);
    
    Map map = new Map(points, roads, new double[] {});
    
    assertEquals(map.getPart(320, 330, 150, 100), "<line id=\"line\" x1=\""+300.0+"\" y1=\""+356.0+"\" x2=\""+390.0+"\" y2=\""+377.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"></line>\n"+
                                                  "<line id=\"line\" x1=\""+390.0+"\" y1=\""+377.0+"\" x2=\""+800.0+"\" y2=\""+700.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"></line>\n");
  }
  
  //Testing that a road outside the viewbox should not be returned
  @Test
  public void test2() {
    Point[] points = new Point[10];
    points[0] = new Point(1, 300, 356);
    points[1] = new Point(2, 390, 377);
    points[2] = new Point(3, 800, 700);
    points[3] = new Point(4, 430, 430);
    
    Road[] roads = new Road[10];
    roads[0] = new Road(1, 2, "Niceness street", 1);
    roads[1] = new Road(2, 3, "Long street", 1);
    roads[2] = new Road(3, 4, "Fail street", 1);
    
    Map map = new Map(points, roads, new double[] {});
    
    assertFalse(map.getPart(320, 331, 150, 100).equals("<line id=\"line\" x1=\""+300.0+"\" y1=\""+356.0+"\" x2=\""+390.0+"\" y2=\""+377.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"></line>\n"+
                                                  "<line id=\"line\" x1=\""+390.0+"\" y1=\""+377.0+"\" x2=\""+800.0+"\" y2=\""+700.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"></line>\n"+
                                                  "<line id=\"line\" x1=\""+800.0+"\" y1=\""+700.0+"\" x2=\""+430.0+"\" y2=\""+431.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"></line>\n"));
    }
  
  //Testing two roads in viewbox and one partly in viewbox
  @Test
  public void test3() {
    Point[] points = new Point[10];
    points[0] = new Point(1, 300, 356);
    points[1] = new Point(2, 390, 377);
    points[2] = new Point(3, 800, 700);
    points[3] = new Point(4, 430, 431);
    
    Road[] roads = new Road[10];
    roads[0] = new Road(1, 2, "Niceness street", 1);
    roads[1] = new Road(2, 3, "Long street", 1);
    roads[2] = new Road(3, 4, "Fail street", 1);
    
    Map map = new Map(points, roads, new double[] {});
    
    assertEquals(map.getPart(320, 331, 150, 100), "<line id=\"line\" x1=\""+300.0+"\" y1=\""+356.0+"\" x2=\""+390.0+"\" y2=\""+377.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"></line>\n"+
                                                  "<line id=\"line\" x1=\""+390.0+"\" y1=\""+377.0+"\" x2=\""+800.0+"\" y2=\""+700.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"></line>\n"+
                                                  "<line id=\"line\" x1=\""+800.0+"\" y1=\""+700.0+"\" x2=\""+430.0+"\" y2=\""+431.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"></line>\n");
  }*/
}
