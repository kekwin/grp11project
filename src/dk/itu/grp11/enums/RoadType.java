package dk.itu.grp11.enums;

import java.awt.Color;
import java.util.HashMap;

import dk.itu.grp11.exceptions.RoadTypeDoesNotExistException;
/**
 * Defines different roadtypes, their id, stroke thickness and color of the stroke.
 *
 */
public enum RoadType {
  MOTORVEJ(1, 0.3, new Color(255,0,0), 1),
  MOTORTRAFIKVEJ(2, 0.2, new Color(0,255,0), 1),
  PROJ_MOTORVEJ(21, 0.1, new Color(0,0,0), 1),
  PROJ_MOTORTRAFIKVEJ(22, 0.1, new Color(0,255,0), 1),
  MOTORVEJSTUNNEL(41, 0.1, new Color(0,0,0), 1),
  MOTORTRAFIKVEJSTUNNEL(42, 0.1, new Color(0,255,0), 1),
  FAERGEFORBINDELSE(80, 0.1, new Color(0,0,255), 1),
  
  PRIMAERRUTE_OVER_6M(3, 0.1, new Color(0,0,0), 2),
  PROJ_PRIMAERVEJ(23, 0.1, new Color(0,0,0), 2),
  SEKUNDAERRUTE_OVER_6M(4, 0.1, new Color(150,150,150), 2),
  MOTORVEJSAFKOERSEL(31, 0.1, new Color(255,100,100), 2),
  MOTORTRAFIKVEJSARKOERSEL(32, 0.1, new Color(100,255,100), 2),
  PRIMAERVEJSAFKOERSEL(33, 0.1, new Color(100,100,100), 2),
  SEKUNDAERVEJSAFKOERSEL(34, 0.1, new Color(150,150,150), 2),
  PROJ_SEKUNDAERVEJ(24, 0.1, new Color(150,150,150), 2),
  PRIMAERVEJSTUNNEL(43, 0.1, new Color(0,0,0), 2),
  SEKUNDAERVEJSTUNNEL(44, 0.1, new Color(150,150,150), 2),
  
  VEJ_3_TIL_6M(5, 0.1, new Color(150,150,150), 20),
  PROJ_VEJ_3_TIL_6M(25, 0.1, new Color(150,150,150), 20),
  
  ANDEN_VEJ(6, 0.1, new Color(64,128,128), 40),
  ANDEN_VEJAFKOERSEL(35, 0.1, new Color(64,128,128), 40),
  PROJ_VEJ_UNDER_3M(26, 0.1, new Color(64,128,128), 40),
  ANDEN_VEJTUNNEL(45, 0.1, new Color(64,128,128), 40),
  
  STI(8, 0.05, new Color(220,100,60), 80),
  MARKVEJ(10, 0.1, new Color(126,49,23), 80),
  GÅGADE(11, 0.1, new Color(185,185,0), 80),
  PROJ_STI(28, 0.05, new Color(220,100,60), 80),
  MINDRE_VEJTUNNEL(46, 0.1, new Color(64,128,128), 80),
  STITUNNEL(48, 0.05, new Color(220,100,60), 80),
  UKENDT1(95, 0.1, new Color(0,0,0), 80), //Unknown roadtype, not described by krak
  UKENDT2(0, 0.1, new Color(0,0,0), 80), //Unknown roadtype, not described by krak
  
  STEDNAVN(99, 1, new Color(0,0,0), 10000);
  
  private int id;
  private double stroke;
  private Color color;
  private int zoomLevel;
  private static java.util.Map<Integer, RoadType> roadTypes = new HashMap<>();
  
  static {
    for(RoadType rt : RoadType.values()) {
      roadTypes.put(rt.getId(), rt);
    }
  }
  
  RoadType(int id, double stroke, Color color, int zoomLevel) {
    this.id = id;
    this.stroke = stroke;
    this.color = color;
    this.zoomLevel = zoomLevel;
  }
  
  public int getId() {
    return id;
  }
  
  public double getStroke() {
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
