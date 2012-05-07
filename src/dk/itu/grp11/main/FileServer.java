package dk.itu.grp11.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

import dk.itu.grp11.data.Map;
import dk.itu.grp11.enums.TransportationType;

/**
 * This starts a http server at the specified port. It then
 * listens for requests and answers them by creating a new thread
 * of requestparser.
 * 
 * @author Group 11
 *
 */
public class FileServer {
  private static FileServer fs = null;
  private static int reqCount = 0;
  
  public static HashMap<String, Session> sessions = new HashMap<String, Session>();
  
  private int port = 90;
  private Map map;

  /**
   * Constructor for fileserver. Resets min/max values and sets
   * port and map to use
   * 
   * @param port The port to listen to
   * @param map The map to get data from
   */
  public FileServer(Map map) {
    this.map = map;
    System.out.println(map.getRoute(30722, 621556, TransportationType.CAR, true));
  }
  
  public static FileServer getFileServer() {
    if (fs == null) fs = new FileServer(Map.getMap());
    return fs;
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
}

