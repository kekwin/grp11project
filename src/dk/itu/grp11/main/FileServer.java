package dk.itu.grp11.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import dk.itu.grp11.data.Map;
import dk.itu.grp11.data.Parser;
import dk.itu.grp11.enums.MapBound;


public class FileServer {
  private static int reqCount = 0;
  
  private int xStart;
  private int yStart;
  private int xDiff;
  private int yDiff;
  
  private int port;
  private Map map;

  public FileServer(int port, Map map) {
    this.port = port;
    this.map = map;
    resetMinMax();
  }
  
  public void resetMinMax() {
    xStart = (int)Math.floor(Parser.getMapBound(MapBound.MINX));
    yStart = (int)Math.floor(Parser.getMapBound(MapBound.MINY));
    xDiff = (int)Math.ceil(Parser.getMapBound(MapBound.MAXX)-Parser.getMapBound(MapBound.MINX));
    yDiff = (int)Math.ceil(Parser.getMapBound(MapBound.MAXY)-Parser.getMapBound(MapBound.MINY));
  }

  public void run() {
    ServerSocket ss = null; 
    try {
      ss = new ServerSocket(port); 
    } catch (IOException e) {
      System.err.println("Could not start server: "+e);
      System.exit(-1);
    }
    System.out.println("FileServer accepting connections on port "+
                       port);
            
    while (true) {
      try {
        Socket con = ss.accept();
        RequestParser p = new RequestParser(this, map, con);
        p.start();
      } catch (IOException e) { 
        System.err.println(e); 
      }
    }
    
  }
  public void log(String msg) {
    System.err.println("Req no. "+(++reqCount)+"\t "+new Date()+": "+msg);
  }
  
  public int getXStart() { return xStart; }
  public int getYStart() { return yStart; }
  public int getXDiff()  { return xDiff;  }
  public int getYDiff()  { return yDiff;  }
  public void setXStart(int xStart) { this.xStart = xStart; }
  public void setYStart(int yStart) { this.yStart = yStart; }
  public void setXDiff(int xDiff) { this.xDiff = xDiff; }
  public void setYDiff(int yDiff) { this.yDiff = yDiff; }
}

