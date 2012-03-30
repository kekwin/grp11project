package dk.itu.grp11.enums;

public enum MinMax
{
  MINX(0), MAXX(1), MINY(2), MAXY(3);
  
  int index;
  MinMax(int index) {
    this.index = index;
  }
  public int id() {
    return index;
  }
}
