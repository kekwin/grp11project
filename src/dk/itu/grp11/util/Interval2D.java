package dk.itu.grp11.util;

/*************************************************************************
 *  Compilation:  javac Interval2D.java
 *  Execution:    java Interval2D
 *  
 *  2-dimensional interval data type.
 *
 *************************************************************************/

public class Interval2D<T extends Comparable<T>> {
    private final Interval1D<T> x;
    private final Interval1D<T> y;

    public Interval2D(Interval1D<T> x, Interval1D<T> y) {
        this.x = x;
        this.y = y;
    }

    // does this interval intersect that one?
    public boolean intersects(Interval2D<T> that) {
        if (!this.x.intersects(that.x)) return false;
        if (!this.y.intersects(that.y)) return false;
        return true;
    }

    // does this interval contain x?
    public boolean contains(T rx, T ry) {
        return x.contains(rx)  && y.contains(ry);
    }
        
    public Interval1D<T> getIntervalX(){
      return x;
    }
    
    public Interval1D<T> getIntervalY(){
      return y;
    }
}

