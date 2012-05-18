package dk.itu.grp11.main;

import java.util.HashSet;

import dk.itu.grp11.data.Parser;
import dk.itu.grp11.enums.MapBound;
/**
 * The Session class is used to give each user connecting to our fileserver a unique ID, so that our
 * application can hold information about things that are specific for each user, such as 
 * roads drawn, and the viewbox coordinates.
 *
 * @author Group 11
 */
//TODO javadoc
public class Session {
  @SuppressWarnings("unused")
  private String sessionID;
  private int xStart;
  private int yStart;
  private int xDiff;
  private int yDiff;
  private HashSet<Integer> roadsDrawn = new HashSet<Integer>();
  
  public Session(String sessionID) {
    this.sessionID = sessionID;
    resetMinMax();
  }
  
  public void resetMinMax() {
    Parser ps = Parser.getParser();
    xStart = (int)Math.floor(ps.mapBound(MapBound.MINX));
    yStart = (int)Math.floor(ps.mapBound(MapBound.MINY));
    xDiff = (int)Math.ceil(ps.mapBound(MapBound.MAXX)-ps.mapBound(MapBound.MINX));
    yDiff = (int)Math.ceil(ps.mapBound(MapBound.MAXY)-ps.mapBound(MapBound.MINY));
  }
  
  public String removeRoads(String ids) {
    synchronized(this) {
      String[] split = ids.split(",");
      for (String id : split) {
        try {
          roadsDrawn.remove(Integer.parseInt(id));
        } catch (NumberFormatException e) {}
      }
    }
    return "Success";
  }
  
  public int  getXStart() { return xStart; }
  public int  getYStart() { return yStart; }
  public int  getXDiff() { return xDiff; }
  public int  getYDiff() { return yDiff; }
  public void setXStart(int xStart) { this.xStart = xStart; }
  public void setYStart(int yStart) { this.yStart = yStart; }
  public void setXDiff(int xDiff) { this.xDiff = xDiff; }
  public void setYDiff(int yDiff) { this.yDiff = yDiff; }
  public void addRoadID(int ID) { roadsDrawn.add(ID); }
  public boolean isRoadDrawn(int ID) { return roadsDrawn.contains(ID); }
}
