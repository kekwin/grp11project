package dk.itu.grp11.util;

import java.util.HashSet;
import java.util.Set;

/*************************************************************************
 *  Compilation:  javac QuadTree.java
 *  Execution:    java QuadTree M N
 *
 *  Quad tree.
 * 
 *************************************************************************/

public class QuadTree<Key extends Comparable<Key>, Value>  {
    private Node root;
    private int size = 0;
    private int maxDepth = 0;

    // helper node data type
    private class Node {
        Key x, y;              // x- and y- coordinates
        Node NW, NE, SE, SW;   // four subtrees
        Value value;           // associated data

        Node(Key x, Key y, Value value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }


  /***********************************************************************
    *  Insert (x, y) into appropriate quadrant
    ***********************************************************************/
    public void insert(Key x, Key y, Value value) {
      int depth = 0;
        root = insert(root, x, y, value, depth);
        size++;
    }

    private Node insert(Node h, Key x, Key y, Value value, int depth) {
        depth++;
        if (h == null) {
          if (depth > maxDepth) maxDepth = depth;
          return new Node(x, y, value);
        }
        //// if (eq(x, h.x) && eq(y, h.y)) h.value = value;  // duplicate
        else if ( less(x, h.x) &&  less(y, h.y)) h.SW = insert(h.SW, x, y, value, depth);
        else if ( less(x, h.x) && !less(y, h.y)) h.NW = insert(h.NW, x, y, value, depth);
        else if (!less(x, h.x) &&  less(y, h.y)) h.SE = insert(h.SE, x, y, value, depth);
        else if (!less(x, h.x) && !less(y, h.y)) h.NE = insert(h.NE, x, y, value, depth);
        return h;
    }


  /***********************************************************************
    *  Range search.
    ***********************************************************************/

    public Set<Value> query2D(Interval2D<Key> rect) {
      Set<Value> found = new HashSet<Value>();
      return query2D(root, rect, found);
    }

    private Set<Value> query2D(Node h, Interval2D<Key> rect, Set<Value> found) {
        if (h == null) return null;
        Key xmin = rect.getIntervalX().getLow();
        Key ymin = rect.getIntervalY().getLow();
        Key xmax = rect.getIntervalX().getHigh();
        Key ymax = rect.getIntervalY().getHigh();
        if (rect.contains(h.x, h.y))
          found.add(h.value);
        if ( less(xmin, h.x) &&  less(ymin, h.y)) query2D(h.SW, rect, found);
        if ( less(xmin, h.x) && !less(ymax, h.y)) query2D(h.NW, rect, found);
        if (!less(xmax, h.x) &&  less(ymin, h.y)) query2D(h.SE, rect, found);
        if (!less(xmax, h.x) && !less(ymax, h.y)) query2D(h.NE, rect, found);
        return found;
    }


   /*************************************************************************
    *  helper comparison functions
    *************************************************************************/

    private boolean less(Key k1, Key k2) { return k1.compareTo(k2) <  0; }
    
   /*************************************************************************
    *  Getter/setter functions
    *************************************************************************/
    
    public int size() { return size; }
    public int depth() { return maxDepth; }
}
