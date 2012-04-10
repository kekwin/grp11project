package dk.itu.grp11.data;


/**
 * Describes an interval in two dimensions, horizontal and vertical
 * 
 * @author Group 11
 */

public class Interval2D<T extends Comparable<T>, T2 extends Comparable<T2>> {
  private final Interval<T, T2> intervalX, intervalY;
  
  /**
   * Constructs a 2-dimensional interval
   * 
   * @param intervalX the horizontal interval
   * @param intervalY the vertical interval
   */
  public Interval2D(Interval<T, T2> intervalX, Interval<T, T2> intervalY){
    this.intervalX = intervalX;
    this.intervalY = intervalY;
  }
  
  /**
  * Does intervals intersect?
  * @param b Interval to intersect with this
  * @return True if they intersect 
  */
  public boolean intersects(Interval2D<T, T2> b){
    if (intervalX.intersects(b.getIntervalX())) return true;
    if (intervalY.intersects(b.getIntervalY())) return true;
    return false;
  }
  
  /**
  * Is coordinate within rectangle?
  * @param x x-coordinate
  * @param y y-coordinate
  * @return True if coordinate is within rectangle
  */
  public boolean contains(T x, T y, T2 d3){
    return intervalX.contains(x, d3) && intervalY.contains(y, d3);
  }
  
  public Interval<T, T2> getIntervalX(){
    return intervalX;
  }
  
  public Interval<T, T2> getIntervalY(){
    return intervalY;
  }
}
