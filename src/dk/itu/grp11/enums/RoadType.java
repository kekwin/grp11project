package dk.itu.grp11.enums;

import java.awt.Color;
import java.util.HashMap;

import dk.itu.grp11.exceptions.RoadTypeDoesNotExistException;
/**
 * Defines different roadtypes, their id, stroke thickness and color of the stroke.
 *
 */
public enum RoadType {
  MOTORVEJ(1, 200, new Color(255,0,0), 1),
  MOTORTRAFIKVEJ(2, 150, new Color(0,255,0), 2),
  PRIMAERRUTE_OVER_6M(3, 100, new Color(0,0,0), 4),
  SEKUNDAERRUTE_OVER_6M(4, 100, new Color(0,0,0), 4),
  VEJ_3_TIL_6M(5, 20, new Color(0,0,0), 400),
  ANDEN_VEJ(6, 20, new Color(0,0,0), 400),
  STI(8, 20, new Color(0,0,0), 400),
  MARKVEJ(10, 20, new Color(0,0,0), 400),
  GÅGADE(11, 20, new Color(0,0,0), 400),
  PROJ_MOTORVEJ(21, 200, new Color(0,0,0), 1),
  PROJ_MOTORTRAFIKVEJ(22, 150, new Color(0,255,0), 2),
  PROJ_PRIMAERVEJ(23, 20, new Color(0,0,0), 400),
  PROJ_SEKUNDAERVEJ(24, 20, new Color(0,0,0), 400),
  PROJ_VEJ_3_TIL_6M(25, 20, new Color(0,0,0), 400),
  PROJ_VEJ_UNDER_3M(26, 20, new Color(0,0,0), 400),
  PROJ_STI(28, 20, new Color(0,0,0), 400),
  MOTORVEJSAFKOERSEL(31, 200, new Color(0,0,0), 1),
  MOTORTRAFIKVEJSARKOERSEL(32, 150, new Color(0,255,0), 2),
  PRIMAERVEJSAFKOERSEL(33, 100, new Color(0,0,0), 4),
  SEKUNDAERVEJSAFKOERSEL(34, 100, new Color(0,0,0), 4),
  ANDEN_VEJAFKOERSEL(35, 20, new Color(0,0,0), 400),
  MOTORVEJSTUNNEL(41, 200, new Color(0,0,0), 1),
  MOTORTRAFIKVEJSTUNNEL(42, 150, new Color(0,255,0), 2),
  PRIMAERVEJSTUNNEL(43, 100, new Color(0,0,0), 4),
  SEKUNDAERVEJSTUNNEL(44, 100, new Color(0,0,0), 4),
  ANDEN_VEJTUNNEL(45, 20, new Color(0,0,0), 400),
  MINDRE_VEJTUNNEL(46, 20, new Color(0,0,0), 400),
  STITUNNEL(48, 20, new Color(0,0,0), 400),
  FAERGEFORBINDELSE(80, 200, new Color(0,0,255), 10),
  STEDNAVN(99, 20, new Color(0,0,0), 1),
  UKENDT1(95, 20, new Color(0,0,0), 400), //Unknown roadtype, not described by krak
  UKENDT2(0, 20, new Color(0,0,0), 400); //Unknown roadtype, not described by krak
  
  private int id;
  private int stroke;
  private Color color;
  private int zoomLevel;
  private static java.util.Map<Integer, RoadType> roadTypes = new HashMap<>();
  
  static {
    for(RoadType rt : RoadType.values()) {
      roadTypes.put(rt.getId(), rt);
    }
  }
  
  RoadType(int id, int stroke, Color color, int zoomLevel) {
    this.id = id;
    this.stroke = stroke;
    this.color = color;
    this.zoomLevel = zoomLevel;
  }
  
  public int getId() {
    return id;
  }
  
  public int getStroke() {
    return stroke;
  }
  
  public Color getColor() {
    return color;
  }
  
  public int getZoomLevel() {
    return zoomLevel;
  }
  
  public String getColorAsString() {
    return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
  }

  public static RoadType getById(int id) {
    if(!roadTypes.containsKey(id))
      throw new RoadTypeDoesNotExistException(); //If no road with such id exist
    return roadTypes.get(id);
  }
  
  @Override
  public String toString() {
    return RoadType.class + " ID:" + id + " STROKE:" + stroke + " COLOR:" + color;
  }
}
