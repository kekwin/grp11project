package dk.itu.grp11.test;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.itu.grp11.data.Parser;
import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.util.LatLonToUTM;;

public class LatLonToUTMTest {

  @Test
  public void aqccuracyGammelStrandTest() {
    // Testing a position (lat/lon): 56.824040  9.003210
    // This is inside the map.
    // Should be around these pixels values:
    // 500198.71052  6297799.08799

    assertEquals(500198.71052, LatLonToUTM.convert(56.824040,9.003210).getX(), 5);
    assertEquals(6297799.08799, LatLonToUTM.convert(56.824040,9.003210).getY(), 5);
  }
  
  @Test
  public void aqccuracyLondonTest() {
    // Testing a position (lat/lon): 51.488438  -0.020599
    // This is below the map and to the left.
    // lat pixel values should therefore be negative
    // lon pixel values should therefore be larger than the map height
    
    boolean lat = false;
    boolean lon = false;
    if(LatLonToUTM.convert(51.488438,-0.020599).getX() < 0) lat = true;
    if(LatLonToUTM.convert(51.488438,-0.020599).getY() > Parser.getParser().mapBound(MapBound.MAXY)-Parser.getParser().mapBound(MapBound.MINY)) lon = true;
    assertTrue(lat);
    assertTrue(lon);
  }
}