package dk.itu.grp11.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.itu.grp11.main.Road;

public class RoadTest {
  //Testing Point with positive values
  @Test
  public void test0() {
    Road road = new Road(2947, 5728, "Road name", 3);
    
    assertEquals(road.getP1(), 2947);
    assertEquals(road.getP2(), 5728);
    assertEquals(road.getName(), "Road name");
    assertEquals(road.getType(), 3);
  }
}
