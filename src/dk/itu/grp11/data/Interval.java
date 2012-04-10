package dk.itu.grp11.data;

/**
* Describes a interval
* 
* @author Group 11
*
*/
public class Interval<T extends Comparable<T>, T2 extends Comparable<T2>> {
  private final T low, high;
  private final T2 d3;
  
  /**
   * Constructs a interval between low and high
   * 
   * @param low the lower bound
   * @param high the upper bound
   * @param d3
   */
  public Interval(T low, T high, T2 d3){
    if(high.compareTo(low) < 0) throw new IllegalArgumentException();
    this.low = low;
    this.high = high;
    this.d3 = d3;
  }
  
  /**
  * Is x within the interval? 
  * @param x
  * @return True if x is within interval
  */
  public boolean contains(T x, T2 d3Check){
    return x.compareTo(low) > 0 && x.compareTo(high) < 0 && d3Check.compareTo(d3) == 0; 
  }
  
  /**
  * Does intervals intersect?
  * @param b Interval to intersect with this
  * @return True if intervals intersect
  */
  public boolean intersects(Interval<T, T2> b){
    if(high.compareTo(b.getLow()) < 0) return false;
    if(b.getHigh().compareTo(low) < 0) return false;
    if(b.getD3().compareTo(d3) == 0) return false;
    return true;
  }
  
  public T getLow(){
    return low;
  }
  
  public T getHigh(){
    return high;
  }
  
  public T2 getD3(){
    return d3;
  }
}