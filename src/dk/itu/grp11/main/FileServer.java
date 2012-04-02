package dk.itu.grp11.main;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import dk.itu.grp11.enums.MinMax;


public class FileServer {
  private int height;
  private int width;
  private int x;
  private int y;
  
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
  }
  
  private String getOutput() {
    String e = "\n";
    return
    		"<html>" +e+
    		"  <head>" +e+
    		"    <title>Map of Denmark</title>" +e+
    		"    <style type=\"text/css\">" +e+ 
    		"" + getCSS() + 
    		"    </style>" +e+ 
    		"    <script type=\"text/JavaScript\" src=\"jQuery.min.js\"></script>" +e+
    		"    <script type=\"text/JavaScript\" src=\"load.js\"></script>" +e+ 
    		"  </head>" +e+ 
    		"  <body>" +e+
    		"    <div class=\"overlay\">" +e+ 
    		"      <h2>Det virker!</h2>" +e+
    		"      <span>x: "+x+" y: "+y+" & height: "+height+" & width: "+width+"</span>" +e+
    		"    </div>" +e+ 
    		"    <svg id=\"map\" xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" viewbox=\""+x+" -"+y+" "+width+" "+height+"\" class=\"map\">" +e+
    		map.getPart(x, y, width, height) +
    		"    </svg>" +e+
    		"  </body>" +e+
    		"</html>"+e;
  }
  
  private String getCSS() {
    String s = "      ";
    String e = "\n";
    return 
        s+"html {" +e+
        s+"  height: 100%;" +e+
        s+"  width: 100%;" +e+
        s+"}" +e+
        s+"div.overlay {" +e+
        s+"  position: relative;" +e+
        s+"  z-index: 1;" +e+
        s+"}" +e+
        s+"svg.map {" +e+
        s+"  position: absolute;" +e+
        s+"  top: 0;" +e+
        s+"  bottom: 0;" +e+
        s+"  left: 0;" +e+
        s+"  right: 0;" +e+
        s+"  z-index: 0;" +e+
        s+"}"+e+
        s+"#select-area {" +e+
        s+"  position: absolute;" +e+
        s+"  background: black;" +e+
        s+"}" +e;
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
        if (request != null) parseRequest(request);
                
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
  
  private void parseRequest(String request) throws IOException {
    if (request.getClass() != String.class) { processRequest("NoClass"); return; }
    if (request.indexOf("favicon.ico") != -1) { processRequest("favicon.ico"); return; }
    if (request.indexOf("jQuery.min.js") != -1) { processRequest("jQuery.min.js"); return; }
    if (request.indexOf("load.js") != -1) { processRequest("load.js"); return; }
    String[] tmp = request.split(" ");
    String[] requestSplit = tmp[1].split("\\?");
    if (requestSplit.length == 2) {
      String params = requestSplit[1];
      String[] paramSplit = params.split("&");
      for (String param : paramSplit) {
        String[] parameter = param.split("=");
        switch (parameter[0]) {
        case "height":
          height = Integer.parseInt(parameter[1]);
        break;
        case "width":
          width = Integer.parseInt(parameter[1]);
        break;
        case "x":
          x = Integer.parseInt(parameter[1]);
        break;
        case "y":
          y = Integer.parseInt(parameter[1]);
        break;
        }
      }
    }
    xStart = (int)Math.floor(map.getMinMax(MinMax.MINX.id()));
    yStart = (int)Math.floor(map.getMinMax(MinMax.MINY.id()));
    xDiff = (int)Math.ceil(map.getMinMax(MinMax.MAXX.id())-map.getMinMax(MinMax.MINX.id()));
    yDiff = (int)Math.ceil(map.getMinMax(MinMax.MAXY.id())-map.getMinMax(MinMax.MINY.id()));
    if (height == 0) height = yDiff;
    if (width == 0) width = xDiff;
    if (x == 0) x = xStart;
    if (y == 0) y = yStart;
    processRequest(null);
  }

  private void processRequest(String file) throws IOException {
    InputStream outStream = null;
    String contenttype = "text/html";
    if (file == null) {
      outStream = new ByteArrayInputStream(getOutput().getBytes("UTF-8"));
    } else {
      File f = new File("src\\dk\\itu\\grp11\\contrib\\"+file);
      outStream = new FileInputStream(f);
      contenttype = "text/javascript";
    }
    pout.print("HTTP/1.0 200 OK\r\n");
    pout.print("Content-Type: "+contenttype+"\r\n");
    pout.print(": "+new Date()+"\r\n"+
               "Server: IXWT FileServer 1.0\r\n\r\n");
    sendOutput(outStream, out); // send raw output
    log(con, "200 OK");
  }
    
  private void log(Socket con, String msg) {
    System.err.println(new Date()+" ["+
                       con.getInetAddress().getHostAddress()+
                       ":"+con.getPort()+"] "+msg);
  }
  
  private void sendOutput(InputStream outStream, OutputStream out) 
      throws IOException {
    byte[] buffer = new byte[1000];
    while (outStream.available()>0) 
      out.write(buffer, 0, outStream.read(buffer));
  }
}

