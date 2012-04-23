package dk.itu.grp11.data;

import dk.itu.grp11.enums.MapBound;

public class Session {
  private String sessionID;
  private int xStart;
  private int yStart;
  private int xDiff;
  private int yDiff;
  
  public Session(String sessionID) {
    this.sessionID = sessionID;
    resetMinMax();
  }
  
  public void resetMinMax() {
    xStart = (int)Math.floor(Parser.getMapBound(MapBound.MINX));
    yStart = (int)Math.floor(Parser.getMapBound(MapBound.MINY));
    xDiff = (int)Math.ceil(Parser.getMapBound(MapBound.MAXX)-Parser.getMapBound(MapBound.MINX));
    yDiff = (int)Math.ceil(Parser.getMapBound(MapBound.MAXY)-Parser.getMapBound(MapBound.MINY));
  }
  
  public int  getXStart() { return xStart; }
  public int  getYStart() { return yStart; }
  public int  getXDiff() { return xDiff; }
  public int  getYDiff() { return yDiff; }
  public void setXStart(int xStart) { this.xStart = xStart; }
  public void setYStart(int yStart) { this.yStart = yStart; }
  public void setXDiff(int xDiff) { this.xDiff = xDiff; }
  public void setYDiff(int yDiff) { this.yDiff = yDiff; }
}
