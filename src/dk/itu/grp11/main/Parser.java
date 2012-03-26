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
    Point[] tmp = new Point[675902];
    int index = 0;
    StringBuilder builder = new StringBuilder();
    try {
      BufferedReader input =  new BufferedReader(new FileReader(nodes));
      try {
        String line = null; 
        /*
        * readLine is a bit quirky :
        * it returns the content of a line MINUS the newline.
        * it returns null only for the END of the stream.
        * it returns an empty String if two newlines appear in a row.
        */
        while (( line = input.readLine()) != null){
          builder.append(line);
          builder.append(System.getProperty("line.separator"));
          tmp[index] = createPoint(line);
          System.out.println(tmp[index]);
          index++;
        }
      }
      finally {
        input.close();
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }
    
   
  
    
    return tmp;
  }
  
  public Point createPoint(String input)
  {
    Point tmp;
    String[] inputSplit = input.split(",");
    tmp =   
    new Point(
        Integer.parseInt(inputSplit[2]),
        Double.parseDouble(inputSplit[3]),
        Double.parseDouble(inputSplit[4])
      );
    
    return tmp;
  }
  public Road[] parseRoads(){
    return null;
  }
}
