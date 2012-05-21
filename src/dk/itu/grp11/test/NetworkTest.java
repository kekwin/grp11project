package dk.itu.grp11.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashSet;

import org.junit.Test;

import dk.itu.grp11.data.Parser;
import dk.itu.grp11.data.Road;
import dk.itu.grp11.route.Network;

public class NetworkTest {
  private Network g;
  
  public NetworkTest() {
    Parser p = Parser.getTestParser(new File("src\\dk\\itu\\grp11\\test\\test_points.txt"), new File("src\\dk\\itu\\grp11\\test\\test_roads.txt"), null, null, null);
    g = p.network();
  }
  @Test
  public void test0_1() {
    assertEquals(7, g.numPoints());
    assertEquals(9, g.numRoads());
    HashSet<String> adjacent = new HashSet<String>();
    for (Road road : g.adjacent(3)) {
      adjacent.add(road.getFrom()+"-"+road.getTo());
    }
    assertEquals(5, adjacent.size());
    assertEquals(true, adjacent.contains("3-2"));
    assertEquals(true, adjacent.contains("3-4"));
    assertEquals(true, adjacent.contains("3-5"));
    assertEquals(true, adjacent.contains("3-6"));
    assertEquals(true, adjacent.contains("3-7"));
  }
}
