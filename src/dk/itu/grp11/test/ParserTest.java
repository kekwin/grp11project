package dk.itu.grp11.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import dk.itu.grp11.data.DimensionalTree;
import dk.itu.grp11.data.Interval;
import dk.itu.grp11.data.Interval2D;
import dk.itu.grp11.data.Parser;
import dk.itu.grp11.data.Point;
import dk.itu.grp11.data.Road;
import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.RoadType;

public class ParserTest {
  //Testing to see whether (some) points get the right id and values assigned or not
  @Test
  public void test0() throws IOException {
    dk.itu.grp11.data.Parser p = new Parser(new File("kdv_node_unload.txt"), new File("kdv_unload.txt"));
    HashMap<Integer, Point> points = p.parsePoints();
    
    //The first point
    assertEquals(1, points.get(1).getID());
    assertEquals(595527.51786, points.get(1).getX(), 0);
    assertEquals(6402050.98297, points.get(1).getY(), 0);
    
    //The last point
    assertEquals(675902, points.get(points.size()).getID());
    assertEquals(692067.66450, points.get(points.size()).getX(), 0);
    assertEquals(6049914.43018, points.get(points.size()).getY(), 0);
    
    System.out.println(countRoads(1));
  }
  
  /*@Test
  public void test1() {
    dk.itu.grp11.data.Parser p = new Parser(new File("kdv_node_unload.txt"), new File("kdv_unload.txt"));
    HashMap<Integer, Point> points = p.parsePoints();
    DimensionalTree<Double, RoadType, Road> roads = p.parseRoads(points);
    
    Interval<Double, RoadType> intervalX = new Interval<Double, RoadType>(Parser.getMapBound(MapBound.MINX), Parser.getMapBound(MapBound.MAXX), RoadType.MOTORVEJ);
    Interval<Double, RoadType> intervalY = new Interval<Double, RoadType>(Parser.getMapBound(MapBound.MINY), Parser.getMapBound(MapBound.MAXY), RoadType.MOTORVEJ);
    Interval2D<Double, RoadType> rect = new Interval2D<Double, RoadType>(intervalX, intervalY);
    Road[] roadsInViewbox = roads.query2D(rect);
    
    assertEquals(200000, roadsInViewbox.length);
  }*/
  
  public int countRoads(int zoomLevel) throws IOException {
    File f = new File("kdv_unload.txt");
    BufferedReader input = new BufferedReader(new FileReader(f));
    String line = input.readLine(); line = input.readLine();
    int count = 0;
    while(line != null) {
      String[] r = line.split(",");
      if(Integer.parseInt(r[5]) == RoadType.getById(1)) count++;
      line = input.readLine();
    }
    return count;
  }
}
