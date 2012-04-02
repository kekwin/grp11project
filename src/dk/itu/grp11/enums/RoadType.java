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
  MOTORTRAFIKVEJ(2, 20, new Color(0,0,0), 2),
  PRIMAERRUTE_OVER_6M(3, 20, new Color(0,0,0), 4),
  SEKUNDAERRUTE_OVER_6M(4, 20, new Color(0,0,0), 4),
  VEJ_3_TIL_6M(5, 20, new Color(0,0,0), 4),
  ANDEN_VEJ(6, 20, new Color(0,0,0), 4),
  STI(8, 20, new Color(0,0,0), 4),
  MARKVEJ(10, 20, new Color(0,0,0), 4),
  GÅGADER(11, 20, new Color(0,0,0), 4),
  PROJ_MOTORVEJ(21, 200, new Color(0,0,0), 1),
  PROJ_MOTORTRAFIKVEJ(22, 20, new Color(0,0,0), 2),
  PROJ_PRIMAERVEJ(23, 20, new Color(0,0,0), 4),
  PROJ_SEKUNDAERVEJ(24, 20, new Color(0,0,0), 4),
  PROJ_VEJ_3_TIL_6M(25, 20, new Color(0,0,0), 4),
  PROJ_VEJ_UNDER_3M(26, 20, new Color(0,0,0), 4),
  PROJ_STI(28, 20, new Color(0,0,0), 4),
  MOTORVEJSAFKOERSEL(31, 200, new Color(0,0,0), 1),
  MOTORTRAFIKVEJSARKOERSEL(32, 20, new Color(0,0,0), 2),
  PRIMAERVEJSAFKOERSEL(33, 20, new Color(0,0,0), 4),
  SEKUNDAERVEJSAFKOERSEL(34, 20, new Color(0,0,0), 4),
  ANDEN_VEJAFKOERSEL(35, 20, new Color(0,0,0), 4),
  MOTORVEJSTUNNEL(41, 200, new Color(0,0,0), 1),
  MOTORTRAFIKVEJSTUNNEL(42, 20, new Color(0,0,0), 2),
  PRIMAERVEJSTUNNEL(43, 20, new Color(0,0,0), 4),
  SEKUNDAERVEJSTUNNEL(44, 20, new Color(0,0,0), 4),
  ANDEN_VEJTUNNEL(45, 20, new Color(0,0,0), 4),
  MINDRE_VEJTUNNEL(46, 20, new Color(0,0,0), 4),
  STITUNNEL(48, 20, new Color(0,0,0), 4),
  FAERGEFORBINDELSE(80, 200, new Color(0,0,255),1),
  STEDNAVN(99, 20, new Color(0,0,0), 1),
  
  //TODO Unknown road types in kdv_unload.txt. What to do with them?
  UKENDT1(95, 20, new Color(0,0,0), 4),
  UKENDT2(0, 20, new Color(0,0,0), 4);
  
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
