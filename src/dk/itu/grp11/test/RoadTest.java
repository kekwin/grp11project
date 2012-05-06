package dk.itu.grp11.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.itu.grp11.data.Road;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.enums.TrafficDirection;

public class RoadTest {
  //Testing Point with positive values
  @Test
  public void test0() {
    Road road = new Road(2947, 5728, "Road name", 5000, 5200, RoadType.PRIMAERRUTE_OVER_6M, TrafficDirection.BOTH_WAYS, 6000, 1);
    
    assertEquals(2947, road.getFrom());
    assertEquals(5728, road.getTo());
    assertEquals("Road name", road.getName());
    assertEquals(5000, road.getFromZip());
    assertEquals(5200, road.getToZip());
    assertEquals(RoadType.PRIMAERRUTE_OVER_6M, road.getType());
    assertEquals(TrafficDirection.BOTH_WAYS, road.getDirection());
    assertEquals(6000, road.getLength(), 0);
    assertEquals(1, road.getTime(), 0);
  }
}
