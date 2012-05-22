package dk.itu.grp11.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import dk.itu.grp11.data.Parser;
import dk.itu.grp11.data.Point;
import dk.itu.grp11.data.Road;
import dk.itu.grp11.enums.MapBound;
import dk.itu.grp11.enums.RoadType;
import dk.itu.grp11.files.ResourceGetter;
import dk.itu.grp11.util.Interval1D;
import dk.itu.grp11.util.Interval2D;

public class ParserTest {
  
  //Testing to see whether (some) points get the right id and values assigned or not
  @Test
  public void assignedIdTest() throws IOException {
    Map<Integer, Point> points = Parser.getParser().points();
    
    //The first point
    assertEquals(1, points.get(1).getID());
    assertEquals(595527.51786, points.get(1).getX(), 0);
    assertEquals(6402050.98297, points.get(1).getY(), 0);
    
    //The last point
    assertEquals(675902, points.get(675902).getID());
    assertEquals(692067.66450, points.get(675902).getX(), 0);
    assertEquals(6049914.43018, points.get(675902).getY(), 0);
  }

  //Testing if all roads, with zoom level 1, are included at zoom level 1
  @Test
  public void allRoadsTest() throws IOException {
    Parser ps = Parser.getParser();
    // Road types to count
    Set<RoadType> roadTypes = new HashSet<>();
    roadTypes.add(RoadType.MOTORVEJ);
    roadTypes.add(RoadType.MOTORTRAFIKVEJ);
    roadTypes.add(RoadType.PROJ_MOTORVEJ);
    roadTypes.add(RoadType.PROJ_MOTORTRAFIKVEJ);
    roadTypes.add(RoadType.MOTORVEJSTUNNEL);
    roadTypes.add(RoadType.MOTORTRAFIKVEJSTUNNEL);
    roadTypes.add(RoadType.PRIMAERRUTE_OVER_6M);
    roadTypes.add(RoadType.FAERGEFORBINDELSE);
    
    int roadCount = 0;
    
    // Count number of roads for every road type
    for(RoadType rt : roadTypes) {
      Interval1D<Double> intervalX = new Interval1D<Double>(ps.mapBound(MapBound.MINX), ps.mapBound(MapBound.MAXX));
      Interval1D<Double> intervalY = new Interval1D<Double>(ps.mapBound(MapBound.MINY), ps.mapBound(MapBound.MAXY));
      Interval2D<Double> rect = new Interval2D<Double>(intervalX, intervalY);
      Set<Road> roadsInViewbox = ps.roads().get(rt).query2D(rect);
      roadCount += roadsInViewbox.size();
    }
    
    // Expecting all roads with zoom level 1 to be included
    assertEquals(countRoads(roadTypes), roadCount);
  }
  
  //Counts number of roads directly from file
  public int countRoads(Set<RoadType> roadTypes) throws IOException {
    InputStream f = ResourceGetter.class.getResourceAsStream("kdv_unload.txt");
    BufferedReader input = new BufferedReader(new InputStreamReader(f));
    String line = input.readLine(); line = input.readLine();
    int count = 0;
    while(line != null) {
      String[] r = line.split(",");
      if(roadTypes.contains(RoadType.getById(Integer.parseInt(r[5])))) count++;
      line = input.readLine();
    }
    return count;
  }
  
  /*
   * Testing the speed of the roadname prefix finder
   */
  //TODO Well, her er ikke noget assertion - det er blot tidstagning. Vi kunne sige at det max måtte tage x tid, men det giver vel næppe mening? Det ville i øvrigt konstant faile på langsomme computere tænker jeg.
  @Test
  public void prefixSpeedTest() {
    Parser ps = Parser.getParser();
    System.out.println("Prefix time test");

    Set<String> p = new HashSet<String>();
    for (String s : ps.roadNames().keySet()) {
      p.add(s.substring(0, 2));
    }

    long max = 0;
    long accumulated = 0;
    int zeroOne = 0;
    for (String s : p) {
      long start = System.currentTimeMillis();
      ps.roadsWithPrefix(s);
      long diff = System.currentTimeMillis() - start;

      // System.out.println(s+": " + diff + "ns");
      if (diff > max)
        max = diff;
      if (diff <= 1)
        zeroOne++;
      accumulated += diff;
    }
    System.out.println("Tests: " + p.size());
    System.out.println("Zero or one: " + zeroOne);
    System.out.println("Maximum: " + max + "ns");
    System.out.println("Accumulated: " + accumulated + "ns");
    System.out.println("Average: " + accumulated / p.size() + "ns");
  }
}
