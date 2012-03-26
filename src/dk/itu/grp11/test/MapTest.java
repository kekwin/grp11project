package dk.itu.grp11.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import dk.itu.grp11.main.Map;
import dk.itu.grp11.main.Point;
import dk.itu.grp11.main.Road;

public class MapTest {
  
  // Testing single road in viewbox
  @Test
  public void test0() {
    Point[] points = new Point[10];
    points[0] = new Point(1, 300, 356);
    points[1] = new Point(2, 390, 377);
    
    Road[] roads = new Road[10];
    roads[0] = new Road(1, 2, "Niceness street", 1);
    
    Map map = new Map(points, roads);
    
    assertEquals(map.getPart(320, 330, 150, 100), "<line id=\"line\" x1=\""+300.0+"\" y1=\""+356.0+"\" x2=\""+390.0+"\" y2=\""+377.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"/>\n");
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
    
    Map map = new Map(points, roads);
    
    assertEquals(map.getPart(320, 330, 150, 100), "<line id=\"line\" x1=\""+300.0+"\" y1=\""+356.0+"\" x2=\""+390.0+"\" y2=\""+377.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"/>\n"+
                                                  "<line id=\"line\" x1=\""+390.0+"\" y1=\""+377.0+"\" x2=\""+800.0+"\" y2=\""+700.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"/>\n");
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
    
    Map map = new Map(points, roads);
    
    assertFalse(map.getPart(320, 331, 150, 100).equals("<line id=\"line\" x1=\""+300.0+"\" y1=\""+356.0+"\" x2=\""+390.0+"\" y2=\""+377.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"/>\n"+
                                                  "<line id=\"line\" x1=\""+390.0+"\" y1=\""+377.0+"\" x2=\""+800.0+"\" y2=\""+700.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"/>\n"+
                                                  "<line id=\"line\" x1=\""+800.0+"\" y1=\""+700.0+"\" x2=\""+430.0+"\" y2=\""+431.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"/>\n"));
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
    
    Map map = new Map(points, roads);
    
    assertEquals(map.getPart(320, 331, 150, 100), "<line id=\"line\" x1=\""+300.0+"\" y1=\""+356.0+"\" x2=\""+390.0+"\" y2=\""+377.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"/>\n"+
                                                  "<line id=\"line\" x1=\""+390.0+"\" y1=\""+377.0+"\" x2=\""+800.0+"\" y2=\""+700.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"/>\n"+
                                                  "<line id=\"line\" x1=\""+800.0+"\" y1=\""+700.0+"\" x2=\""+430.0+"\" y2=\""+431.0+"\" style=\"stroke:rgb(0,0,0); stroke-width:2;\"/>\n");
  }
}
