package dk.itu.grp11.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;

import org.junit.Test;

import dk.itu.grp11.data.DimensionalTree;
import dk.itu.grp11.data.Map;
import dk.itu.grp11.data.Parser;
import dk.itu.grp11.data.Point;
import dk.itu.grp11.data.Road;
import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.main.FileServer;

public class MapTest {
  
  //Testing getPart
  @Test
  public void test0() {
    Parser p = new Parser(new File("src/dk/itu/grp11/test/test_points.txt"), new File("src/dk/itu/grp11/test/test_roads.txt"));
    
    HashMap<Integer, Point> points = p.points();
    DimensionalTree<Double, RoadType, Road> roads = p.parseRoads(points);
    Map map = new Map(points, roads);
    
    FileServer fs = new FileServer(80, map);
    System.out.println("Getting part: " + map.getPart(320.0, 330.0, 150.0, 100.0, 1, fs));
    assertEquals("var svg = $('#map-container').svg('get');\n"+
                 "svg.line(300.0, "+(377.0-356.0)+", 390.0, 0.0, {stroke: 'rgb(255,0,0)', strokeWidth: '0.3%'});\n", map.getPart(320, 330, 150, 100, 1, fs));
  }
  
  //Testing getZoomLevelX
  @Test
  public void test1() {
    dk.itu.grp11.data.Parser p = new Parser(new File("kdv_node_unload.txt"), new File("kdv_unload.txt"));
    HashMap<Integer, Point> points = p.points();
    DimensionalTree<Double, RoadType, Road> roads2 = p.parseRoads(points);
    
    Point p1 = new Point(1, 300, 356);
    Point p2 = new Point(2, 390, 377);
    HashMap<Integer, Point> points2 = new HashMap<>();
    points2.put(1, p1);
    points2.put(2, p2);
    
    Road r = new Road(p1.getID(), p2.getID(), "Niceness street", RoadType.MOTORVEJ);
    DimensionalTree<Double, RoadType, Road> roads = new DimensionalTree<Double, RoadType, Road>();
    roads2.insert(points.get(r.getFrom()).getX(), points2.get(r.getFrom()).getY(), RoadType.MOTORVEJ, r);
    roads2.insert(points.get(r.getTo()).getX(), points2.get(r.getTo()).getY(), RoadType.MOTORVEJ, r);
    
    Map map = new Map(points, roads);
    
    assertEquals(1, map.getZoomLevelX(Parser.mapBound(MapBound.MAXX)-Parser.mapBound(MapBound.MINX)));
  }
  
  //Testing getZoomLevelY
  @Test
  public void test2() {
    dk.itu.grp11.data.Parser p = new Parser(new File("kdv_node_unload.txt"), new File("kdv_unload.txt"));
    HashMap<Integer, Point> points = p.points();
    DimensionalTree<Double, RoadType, Road> roads2 = p.parseRoads(points);
    
    Point p1 = new Point(1, 300, 356);
    Point p2 = new Point(2, 390, 377);
    HashMap<Integer, Point> points2 = new HashMap<>();
    points2.put(1, p1);
    points2.put(2, p2);
    
    Road r = new Road(p1.getID(), p2.getID(), "Niceness street", RoadType.MOTORVEJ);
    DimensionalTree<Double, RoadType, Road> roads = new DimensionalTree<Double, RoadType, Road>();
    roads2.insert(points.get(r.getFrom()).getX(), points2.get(r.getFrom()).getY(), RoadType.MOTORVEJ, r);
    roads2.insert(points.get(r.getTo()).getX(), points2.get(r.getTo()).getY(), RoadType.MOTORVEJ, r);
    
    Map map = new Map(points, roads);
    
    assertEquals(1, map.getZoomLevelY(Parser.mapBound(MapBound.MAXY)-Parser.mapBound(MapBound.MINY)));
  }
  
  //Testing zoomLevelX
  @Test
  public void test3() {
    
  }
  
  //Testing zoomLevelY
  @Test
  public void test4() {
    
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
