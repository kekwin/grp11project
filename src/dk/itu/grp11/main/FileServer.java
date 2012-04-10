package dk.itu.grp11.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import dk.itu.grp11.data.Map;
import dk.itu.grp11.data.Parser;
import dk.itu.grp11.enums.MapBound;

/**
 * This starts a http server at the specified port. It then
 * listens for requests and answers them by creating a new thread
 * of requestparser.
 * 
 * @author Group 11
 *
 */
public class FileServer {
  private static int reqCount = 0;
  
  private int xStart;
  private int yStart;
  private int xDiff;
  private int yDiff;
  
  private int port;
  private Map map;

  /**
   * Constructor for fileserver. Resets min/max values and sets
   * port and map to use
   * 
   * @param port The port to listen to
   * @param map The map to get data from
   */
  public FileServer(int port, Map map) {
    this.port = port;
    this.map = map;
    resetMinMax();
  }
  
  /**
   * Reset the min/max values of the fileserver, so that they are the same as
   * the ones in parser
   */
  public void resetMinMax() {
    xStart = (int)Math.floor(Parser.getMapBound(MapBound.MINX));
    yStart = (int)Math.floor(Parser.getMapBound(MapBound.MINY));
    xDiff = (int)Math.ceil(Parser.getMapBound(MapBound.MAXX)-Parser.getMapBound(MapBound.MINX));
    yDiff = (int)Math.ceil(Parser.getMapBound(MapBound.MAXY)-Parser.getMapBound(MapBound.MINY));
  }

  /**
   * Runs the fileserver
   */
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
        //Listens for request. It stays on this line until a request is made to the server
        Socket con = ss.accept();
        //Create a new thread for parsing the request
        RequestParser p = new RequestParser(this, map, con);
        //Start the request
        p.start();
      } catch (IOException e) { 
        System.err.println(e); 
      }
    }
    
  }
  
  /**
   * Writes message to the log.
   * 
   * @param msg The message to display
   */
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

