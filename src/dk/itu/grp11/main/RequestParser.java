package dk.itu.grp11.main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

import dk.itu.grp11.enums.MapBound;

public class RequestParser extends Thread {
  private int xStart;
  private int yStart;
  private int xDiff;
  private int yDiff;
  
  private Map map;
  
  private Socket con;
  private OutputStream out;
  private PrintStream pout;
  
  public RequestParser(PrintStream pout, OutputStream out, Socket con, Map map) {
    this.con = con;
    this.out = out;
    this.pout = pout;
    this.map = map;
  }
  public void run(String request) {
    try {
      parseRequest(request);
    } catch (IOException e) {
      System.err.println(e);
    }
  }
  private void parseRequest(String request) throws IOException {
    xStart = (int)Math.floor(map.getMinMax(MapBound.MINX.index()));
    yStart = (int)Math.floor(map.getMinMax(MapBound.MINY.index()));
    xDiff = (int)Math.ceil(map.getMinMax(MapBound.MAXX.index())-map.getMinMax(MapBound.MINX.index()));
    yDiff = (int)Math.ceil(map.getMinMax(MapBound.MAXY.index())-map.getMinMax(MapBound.MINY.index()));
    
    //Get file request and params
    String[] split = request.split("\\?");
    String[] split2 = split[0].split(" ");
    String file = split2[1].substring(1);
    HashMap<String, String> params = new HashMap<String, String>();
    if (split.length > 1) {
      String[] split3 = split[1].split(" ");
      String[] paramSplit = split3[0].split("&");
      for (String param : paramSplit) {
        String[] split4 = param.split("=");
        params.put(split4[0], split4[1]);
      }
    };
    if (file.length() == 0) processRequest("head.html", null);
    else processRequest(file, params);
  }
  private String getMinMax() {
    return xStart + " -" + yStart + " " + xDiff + " " + yDiff;
  }

  private void processRequest(String file, HashMap<String, String> params) throws IOException {
    InputStream outStream = null;
    String contenttype = "text/html";
    if (file.indexOf("getMinMax") != -1) {
      outStream = new ByteArrayInputStream(getMinMax().getBytes("UTF-8"));
    } else if (file.indexOf("getZoomLevel") != -1) { 
      outStream = new ByteArrayInputStream((""+map.getZoomLevel(Double.parseDouble(params.get("width")), Double.parseDouble(params.get("height")))).getBytes("UTF-8"));
    }else if (file.indexOf("getMap") != -1) {
      outStream = new ByteArrayInputStream(map.getPart(Double.parseDouble(params.get("x")), Double.parseDouble(params.get("y")), Double.parseDouble(params.get("width")), Double.parseDouble(params.get("height")), xDiff, yDiff, Integer.parseInt(params.get("zoomlevel"))).getBytes("UTF-8"));
    } else {
      File f = null;
      if (file.indexOf(".png") != -1) f = new File("src\\dk\\itu\\grp11\\files\\"+file);
      else if (file.indexOf(".gif") != -1) f = new File("src\\dk\\itu\\grp11\\files\\"+file);
      else if (file.indexOf("svg") != -1) f = new File("src\\dk\\itu\\grp11\\files\\"+file);
      else f = new File("src\\dk\\itu\\grp11\\contrib\\"+file);
      outStream = new FileInputStream(f);
      if (file.indexOf(".js") != -1) contenttype = "text/javascript";
      else if (file.indexOf(".html") != -1) contenttype = "text/html";
      else if (file.indexOf(".css") != -1) contenttype = "text/css";
      else if (file.indexOf(".png") != -1 || file.indexOf(".gif") != -1) contenttype = "Image";
      else contenttype = "text";
    }
    pout.print("HTTP/1.0 200 OK\r\n");
    pout.print("Content-Type: "+contenttype+"\r\n");
    pout.print(": "+new Date()+"\r\n"+
               "Server: IXWT FileServer 1.0\r\n\r\n");
    sendOutput(outStream, out); // send raw output
    FileServer.log(con, "200 OK");
  }
  
  private void sendOutput(InputStream outStream, OutputStream out) 
      throws IOException {
    byte[] buffer = new byte[1000];
    while (outStream.available()>0) 
      out.write(buffer, 0, outStream.read(buffer));
  }
}
