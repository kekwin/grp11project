package dk.itu.grp11.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import dk.itu.grp11.data.Parser;
import dk.itu.grp11.enums.TransportationType;
import dk.itu.grp11.route.Network;
import dk.itu.grp11.route.PathFinder;

public class PathFinderTest {
  private Network g;
  public PathFinderTest() {
    Parser p = Parser.getTestParser(new File("src\\dk\\itu\\grp11\\test\\test_points.txt"), new File("src\\dk\\itu\\grp11\\test\\test_roads.txt"), null, null, null);
    g = p.network();
  }
  
  @Test
  public void test1_1() {
    PathFinder pf = new PathFinder(g, 1, false, TransportationType.CAR, true, true);
    assertEquals(39, pf.distTo(5), 0);
    assertEquals(174, pf.timeTo(5), 0);
  }
  @Test
  public void test1_2() {
    PathFinder pf = new PathFinder(g, 1, true, TransportationType.CAR, true, true);
    assertEquals(82, pf.distTo(5), 0);
    assertEquals(146, pf.timeTo(5), 0);
  }
  @Test
  public void test1_3() {
    PathFinder pf = new PathFinder(g, 5, false, TransportationType.CAR, true, true);
    assertEquals(30, pf.distTo(6), 0);
    assertEquals(65, pf.timeTo(6), 0);
  }
  @Test
  public void test1_4() {
    PathFinder pf = new PathFinder(g, 7, false, TransportationType.CAR, true, true);
    assertEquals(222, pf.distTo(4), 0);
    assertEquals(125, pf.timeTo(4), 0);
  }
  @Test
  public void test1_5() {
    PathFinder pf = new PathFinder(g, 7, true, TransportationType.CAR, true, true);
    assertEquals(223, pf.distTo(4), 0);
    assertEquals(87, pf.timeTo(4), 0);
  }
  @Test
  public void test1_6() {
    PathFinder pf = new PathFinder(g, 3, false, TransportationType.CAR, true, false);
    assertEquals(1339, pf.distTo(5), 0);
    assertEquals(1338, pf.timeTo(5), 0);
  }
  @Test
  public void test1_7() {
    PathFinder pf = new PathFinder(g, 3, false, TransportationType.CAR, false, true);
    assertEquals(1365, pf.distTo(6), 0);
    assertEquals(1401, pf.timeTo(6), 0);
  }
  @Test
  public void test1_8() {
    PathFinder pf = new PathFinder(g, 3, false, TransportationType.CAR, false, false);
    assertEquals(Double.POSITIVE_INFINITY, pf.distTo(5), 0);
    assertEquals(Double.POSITIVE_INFINITY, pf.timeTo(5), 0); //18-05-12: returned 0, not expected (Double.POSITIVE_INFINITY expected)
  }
  @Test
  public void test1_9() {
    PathFinder pf = new PathFinder(g, 5, false, TransportationType.CAR, true, true);
    assertEquals(50, pf.distTo(4), 0);
    assertEquals(130, pf.timeTo(4), 0);
  }
  @Test
  public void test1_10() {
    PathFinder pf = new PathFinder(g, 4, false, TransportationType.CAR, true, true);
    assertEquals(50, pf.distTo(5), 0);
    assertEquals(169, pf.timeTo(5), 0);
  }
}
