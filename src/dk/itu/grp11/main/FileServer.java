package dk.itu.grp11.main;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import dk.itu.grp11.data.Map;
import dk.itu.grp11.data.Parser;
import dk.itu.grp11.enums.MapBound;


public class FileServer {
  private int xStart;
  private int yStart;
  private int xDiff;
  private int yDiff;
  
  private int port;
  private Map map;

  private Socket con;
  private BufferedReader in;
  private OutputStream out;
  private PrintStream pout;

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
        con = ss.accept();
        in = new BufferedReader
            (new InputStreamReader(con.getInputStream()));
        out = new BufferedOutputStream(con.getOutputStream());
        pout = new PrintStream(out);

        String request = in.readLine();
        con.shutdownInput(); // ignore the rest
        log(con, request);
        if (request != null) {
          RequestParser p = new RequestParser(this, pout, out, con, map);
          p.run(request);
        }
                
        pout.flush();
      } catch (IOException e) { 
        System.err.println(e); 
      }
      try {
        if (con!=null) 
          con.close(); 
      } catch (IOException e) { 
        System.err.println(e); 
      }
    }
    
  }
  public static void log(Socket con, String msg) {
    System.err.println(new Date()+" ["+
                       con.getInetAddress().getHostAddress()+
                       ":"+con.getPort()+"] "+msg);
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

