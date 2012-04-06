package dk.itu.grp11.enums;

public enum MapBound
{
  MINX(0), MAXX(1), MINY(2), MAXY(3);
  
  int index;
  MapBound(int index) {
    this.index = index;
  }
  public int index() {
    return index;
  }
}