package dk.itu.grp11.test;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.junit.Test;

import dk.itu.grp11.data.Parser;
import dk.itu.grp11.data.Road;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.util.QuadTree;

public class QuadTreeTest {

  @Test
  public void depthTest() {
    Parser p = Parser.getParser();
    HashMap<RoadType, QuadTree<Double, Road>> roads = p.roads();
    for (QuadTree<Double, Road> roadType : roads.values()) {
      double depth = roadType.depth();
      double best = Math.ceil(Math.log(roadType.size())/Math.log(4));
      DecimalFormat d = new DecimalFormat("#.##");
      System.out.println("Factor compared to balanced tree: "+d.format(depth/best)+"x for tree with "+roadType.size()+" elements");
    }
  }

}
