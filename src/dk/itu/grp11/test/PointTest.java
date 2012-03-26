package dk.itu.grp11.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.itu.grp11.exceptions.NegativeValueException;
import dk.itu.grp11.main.Point;

public class PointTest {
  //Testing Point with positive values
  @Test
  public void test0() {
    dk.itu.grp11.main.Point point = new Point(42, 313, 1337);
    
    assertEquals(point.getID(), 42);
    assertEquals(point.getX(), 313, 0);
    assertEquals(point.getY(), 1337, 0);
  }
  
  //Testing Point with negative ID and positive values
  @Test
  public void test1() {
    @SuppressWarnings("unused")
    dk.itu.grp11.main.Point point = new Point(-42, 313, 1337);
  }
  
  //Testing Point with negative values
  @Test(expected=NegativeValueException.class)
  public void test2() {
    @SuppressWarnings("unused")
    dk.itu.grp11.main.Point point = new Point(-42, -313, -1337);
  }
}
