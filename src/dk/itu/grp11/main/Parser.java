package dk.itu.grp11.main;

import java.io.*;


public class Parser {
  
  File nodes;
  File connections;
  // the constructor takes 2 arguments, the kdv.node_unload.txt file and the kdv_unload.txt file.
  public Parser(File nodeFile, File connectionFile){
    nodes = nodeFile;
    connections = connectionFile;
  }
  
  public Point[] parsePoints(){
    return null;
  }
  
  public Road[] parseRoads(){
    return null;
  }
}
