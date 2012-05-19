package dk.itu.grp11.util;

/*************************************************************************
 *  Compilation:  javac Interval1D.java
 *  Execution:    java Interval1D
 *  
 *  1-dimensional interval data type.
 *
 *************************************************************************/

public class Interval1D<T extends Comparable<T>> {
    private final T low;
    private final T high;

    public Interval1D(T low, T high) {
        if (high.compareTo(low) > 0) {
            this.low  = low;
            this.high = high;
        }
        else throw new RuntimeException("Illegal interval");
    }

    // left endpoint
    public T getLow() { 
        return low;
    }

    // right endpoint
    public T getHigh() { 
        return high;
    }

    // does this interval intersect that one?
    public boolean intersects(Interval1D<T> b) {
      if(high.compareTo(b.getLow()) < 0) return false;
      if(b.getHigh().compareTo(low) < 0) return false;
        return true;
    }

    // does this interval contain x?
    public boolean contains(T x) {
        return x.compareTo(low) > 0 && x.compareTo(high) < 0;
    }
}
