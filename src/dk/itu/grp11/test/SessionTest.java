package dk.itu.grp11.test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.Test;
import dk.itu.grp11.data.Map;
import dk.itu.grp11.data.Parser;
import dk.itu.grp11.main.FileServer;
import dk.itu.grp11.main.RequestParser;
import dk.itu.grp11.main.Session;

public class SessionTest {
  Parser ps = Parser.getParser();
  
  //Testing resetMinMax
  @Test
  public void test1(){
    Session s = new Session("sessionID");
    int newXDif = 100;
    int newXStart = 5;
    int newYDif = 50;
    int newYStart = 10;
    
    int xDif = s.getXDiff();
    int yDif = s.getYDiff();
    int xStart = s.getXStart();
    int yStart = s.getYStart();
    
    s.setXDiff(newXDif);
    s.setYDiff(newYDif);
    s.setXStart(newXStart);
    s.setYStart(newYStart);
    
    s.resetMinMax();
    
    assertEquals(xDif, s.getXDiff());
    assertEquals(yDif, s.getYDiff());
    assertEquals(xStart, s.getXStart());
    assertEquals(yStart, s.getYStart());
    
  }
  
}