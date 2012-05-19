package dk.itu.grp11.enums;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import dk.itu.grp11.exceptions.RoadTypeDoesNotExistException;

/**
 * Defines different road types; their id, stroke thickness, color of the
 * stroke, at what zoom level they should be shown and what types of
 * transportations are allowed.
 * 
 * @author Group 11
 */
public enum RoadType {
  COASTLINE(101, 0.1, new Color(0,0,75), 1),
  MOTORVEJ(1, 0.3, new Color(255,0,0), 1, TransportationType.CAR),
  PROJ_MOTORVEJ(21, 0.1, new Color(0,0,0), 1, TransportationType.CAR),
  MOTORVEJSTUNNEL(41, 0.1, new Color(0,0,0), 1, TransportationType.CAR),
  FAERGEFORBINDELSE(80, 0.1, new Color(0,0,255), 1, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  
  MOTORTRAFIKVEJ(2, 0.2, new Color(0,255,0), 1, TransportationType.CAR),
  PROJ_MOTORTRAFIKVEJ(22, 0.1, new Color(0,255,0), 1, TransportationType.CAR),
  MOTORTRAFIKVEJSTUNNEL(42, 0.1, new Color(0,255,0), 1, TransportationType.CAR),
  PRIMAERRUTE_OVER_6M(3, 0.1, new Color(0,0,0), 1, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  PROJ_PRIMAERVEJ(23, 0.1, new Color(0,0,0), 1, TransportationType.CAR),
  
  SEKUNDAERRUTE_OVER_6M(4, 0.1, new Color(150,150,150), 4, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  MOTORVEJSAFKOERSEL(31, 0.1, new Color(255,100,100), 4, TransportationType.CAR),
  MOTORTRAFIKVEJSARKOERSEL(32, 0.1, new Color(100,255,100), 4, TransportationType.CAR),
  PRIMAERVEJSAFKOERSEL(33, 0.1, new Color(100,100,100), 4, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  SEKUNDAERVEJSAFKOERSEL(34, 0.1, new Color(150,150,150), 4, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  PROJ_SEKUNDAERVEJ(24, 0.1, new Color(150,150,150), 4, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  PRIMAERVEJSTUNNEL(43, 0.1, new Color(0,0,0), 4, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  SEKUNDAERVEJSTUNNEL(44, 0.1, new Color(150,150,150), 4, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  
  VEJ_3_TIL_6M(5, 0.1, new Color(150,150,150), 8, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  PROJ_VEJ_3_TIL_6M(25, 0.1, new Color(150,150,150), 8, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  
  ANDEN_VEJ(6, 0.1, new Color(64,128,128), 8, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  ANDEN_VEJAFKOERSEL(35, 0.1, new Color(64,128,128), 16, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  PROJ_VEJ_UNDER_3M(26, 0.1, new Color(64,128,128), 16, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  ANDEN_VEJTUNNEL(45, 0.1, new Color(64,128,128), 16, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  MINDRE_VEJTUNNEL(46, 0.1, new Color(64,128,128), 16, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  
  STI(8, 0.05, new Color(220,100,60), 32, TransportationType.BICYCLE, TransportationType.WALK),
  MARKVEJ(10, 0.1, new Color(126,49,23), 32, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK),
  GÅGADE(11, 0.1, new Color(185,185,0), 32, TransportationType.WALK),
  PROJ_STI(28, 0.05, new Color(220,100,60), 32, TransportationType.BICYCLE, TransportationType.WALK),
  STITUNNEL(48, 0.05, new Color(220,100,60), 32, TransportationType.BICYCLE, TransportationType.WALK),
  UKENDT1(95, 0.1, new Color(0,0,0), 32, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK), //Unknown roadtype, not described by krak
  UKENDT2(0, 0.1, new Color(0,0,0), 32, TransportationType.CAR, TransportationType.BICYCLE, TransportationType.WALK), //Unknown roadtype, not described by krak
  
  STEDNAVN(99, 0.3, new Color(255,255,0), 100000),
  
  ROUTE(100, 0.5, new Color(0,0,255), 100000);
  
  private int id;
  private double stroke;
  private Color color;
  private int zoomLevel;
  private HashSet<TransportationType> transportation;
  
  private static Map<Integer, RoadType> roadTypes = new HashMap<>();
  
  static {
    for(RoadType rt : RoadType.values()) {
      roadTypes.put(rt.getId(), rt);
    }
  }
  
  public static Collection<RoadType> getRoadTypes() {
    return roadTypes.values();
  }

  private RoadType(int id, double stroke, Color color, int zoomLevel, TransportationType... transportation) {
    this.id = id;
    this.stroke = stroke;
    this.color = color;
    this.zoomLevel = zoomLevel;

    if (transportation != null) {
      this.transportation = new HashSet<>();
      for (TransportationType tt : transportation)
        this.transportation.add(tt);
    }
  }

  /**
   * @return the id of the road type
   */
  public int getId() {
    return id;
  }

  /**
   * 
   * @return the stroke width of the road type, defined in %.
   */
  public double getStroke() {
    return stroke;
  }

  /**
   * 
   * @return the color of the road type, as a Color object.
   */
  public Color getColor() {
    return color;
  }

  /**
   * 
   * @return the zoom level of the road type.
   */
  public int getZoomLevel() {
    return zoomLevel;
  }

  /**
   * 
   * @return The color of the road type, as a string.
   */
  public String getColorAsString() {
    return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
  }

  /**
   * Converts a road id to the corresponding RoadType
   * 
   * @param id
   *          the id of the RoadType, we wish to get.
   * @return returns the road type from the given id.
   */
  public static RoadType getById(int id) {
    if (!roadTypes.containsKey(id))
      throw new RoadTypeDoesNotExistException(); // If no road with such id
                                                 // exist
    return roadTypes.get(id);
  }

  /**
   * Is the transportation type allowed on this road?
   * 
   * @param tt
   *          The type to check if allowed
   * @return true is transportation type is allowed, otherwise false
   */
  public boolean isAllowed(TransportationType tt) {
    return transportation.contains(tt);
  }

  @Override
  public String toString() {
    return RoadType.class + "[id=" + id + " stroke=" + stroke + " color=" + color + " zoomLevel=" + zoomLevel + "]";
  }
}
