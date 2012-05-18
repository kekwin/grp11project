package dk.itu.grp11.test;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.itu.grp11.enums.TrafficDirection;

public class TrafficDirectionTest {

  //Proper input
  @Test
  public void convertCleanInputTest() {
    assertEquals(TrafficDirection.BOTH_WAYS, TrafficDirection.getDirectionById(""));
    assertEquals(TrafficDirection.DRIVING_NOT_ALLOWED, TrafficDirection.getDirectionById("n"));
    assertEquals(TrafficDirection.FROM_TO, TrafficDirection.getDirectionById("ft"));
    assertEquals(TrafficDirection.TO_FROM, TrafficDirection.getDirectionById("tf"));
  }
  
  //Different bad inputs (some which are handled in the getDirectionById()-method)
  @Test
  public void convertBadInputTest() {
    boolean b;
    
    if(TrafficDirection.BOTH_WAYS == TrafficDirection.getDirectionById("b")) b = true; else b = false;
    assertFalse(b);
    
    if(TrafficDirection.BOTH_WAYS == TrafficDirection.getDirectionById(" ")) b = true; else b = false;
    assertTrue(b);
    
    if(TrafficDirection.BOTH_WAYS == TrafficDirection.getDirectionById(",")) b = true; else b = false;
    assertFalse(b);
    
    if(TrafficDirection.FROM_TO == TrafficDirection.getDirectionById("ft ")) b = true; else b = false;
    assertTrue(b);
    
    if(TrafficDirection.FROM_TO == TrafficDirection.getDirectionById(" ft")) b = true; else b = false;
    assertTrue(b);
    
    if(TrafficDirection.FROM_TO == TrafficDirection.getDirectionById(" ft ")) b = true; else b = false;
    assertTrue(b);
    
    if(TrafficDirection.TO_FROM == TrafficDirection.getDirectionById("tff")) b = true; else b = false;
    assertFalse(b);
  }
}