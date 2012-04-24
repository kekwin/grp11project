package dk.itu.grp11.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.itu.grp11.data.Road;
import dk.itu.grp11.enums.RoadType;

public class RoadTest {
  //Testing Point with positive values
  @Test
  public void test0() {
    Road road = new Road(2947, 5728, "Road name", RoadType.PRIMAERRUTE_OVER_6M);
    
    assertEquals(2947, road.getFrom());
    assertEquals(5728, road.getTo());
    assertEquals("Road name", road.getName());
    assertEquals(RoadType.PRIMAERRUTE_OVER_6M, road.getType());
  }
}
