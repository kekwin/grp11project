package dk.itu.grp11.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.itu.grp11.data.Parser;
import dk.itu.grp11.enums.TransportationType;
import dk.itu.grp11.route.Network;
import dk.itu.grp11.route.PathFinder;

public class PathFinderTest {
  private Network g;
  public PathFinderTest() {
    Parser p = Parser.getTestParser(getClass().getResourceAsStream("test_points.txt"), getClass().getResourceAsStream("test_roads.txt"), null, null, null);
    g = p.network();
  }
  
  @Test
  /**
   * Tests the shortest path from point 1 to 5
   */
  public void test1_1() {
    PathFinder pf = new PathFinder(g, 1, false, TransportationType.CAR, true, true);
    assertEquals(39, pf.distTo(5), 0);
    assertEquals(174, pf.timeTo(5), 0);
  }
  @Test
  /**
   * Tests the fastest path from point 1 to 5
   */
  public void test1_2() {
    PathFinder pf = new PathFinder(g, 1, true, TransportationType.CAR, true, true);
    assertEquals(82, pf.distTo(5), 0);
    assertEquals(146, pf.timeTo(5), 0);
  }
  @Test
  /**
   * Tests the shortest path from point 5 to 6, avoiding the "obvious" road
   */
  public void test1_3() {
    PathFinder pf = new PathFinder(g, 5, false, TransportationType.CAR, true, true);
    assertEquals(30, pf.distTo(6), 0);
    assertEquals(65, pf.timeTo(6), 0);
  }
  @Test
  /**
   * Tests the shortest path from point 7 to 4
   */
  public void test1_4() {
    PathFinder pf = new PathFinder(g, 7, false, TransportationType.CAR, true, true);
    assertEquals(222, pf.distTo(4), 0);
    assertEquals(125, pf.timeTo(4), 0);
  }
  @Test
  /**
   * Tests the fastest path from point 7 to 4
   */
  public void test1_5() {
    PathFinder pf = new PathFinder(g, 7, true, TransportationType.CAR, true, true);
    assertEquals(223, pf.distTo(4), 0);
    assertEquals(87, pf.timeTo(4), 0);
  }
  @Test
  /**
   * Tests the shortest path from point 3 to 5, without the use of highways
   */
  public void test1_6() {
    PathFinder pf = new PathFinder(g, 3, false, TransportationType.CAR, true, false);
    assertEquals(1339, pf.distTo(5), 0);
    assertEquals(1338, pf.timeTo(5), 0);
  }
  @Test
  /**
   * Tests the shortest path from point 3 to 6, without the use of ferries
   */
  public void test1_7() {
    PathFinder pf = new PathFinder(g, 3, false, TransportationType.CAR, false, true);
    assertEquals(1365, pf.distTo(6), 0);
    assertEquals(1401, pf.timeTo(6), 0);
  }
  @Test
  /**
   * Tests the shortest path from point 3 to 5, without ferries and highways
   */
  public void test1_8() {
    PathFinder pf = new PathFinder(g, 3, false, TransportationType.CAR, false, false);
    assertEquals(Double.POSITIVE_INFINITY, pf.distTo(5), 0);
    assertEquals(Double.POSITIVE_INFINITY, pf.timeTo(5), 0); //18-05-12: returned 0, not expected (Double.POSITIVE_INFINITY expected)
  }
  @Test
  /**
   * Tests the shortest path from point 5 to 4
   */
  public void test1_9() {
    PathFinder pf = new PathFinder(g, 5, false, TransportationType.CAR, true, true);
    assertEquals(50, pf.distTo(4), 0);
    assertEquals(130, pf.timeTo(4), 0);
  }
  @Test
  /**
   * Tests the shortest path from point 4 to 5, without going against the direction of travel
   */
  public void test1_10() {
    PathFinder pf = new PathFinder(g, 4, false, TransportationType.CAR, true, true);
    assertEquals(50, pf.distTo(5), 0);
    assertEquals(169, pf.timeTo(5), 0);
  }
}
