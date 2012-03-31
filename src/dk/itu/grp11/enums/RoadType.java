package dk.itu.grp11.enums;

import java.awt.Color;
import java.util.HashMap;

import dk.itu.grp11.exceptions.RoadTypeDoesNotExistException;
/**
 * Defines different roadtypes, their id, stroke thickness and color of the stroke.
 *
 */
public enum RoadType {
  MOTORVEJ(1, 200, new Color(255,0,0)),
  MOTORTRAFIKVEJ(2, 20, new Color(0,0,0)),
  PRIMAERRUTE_OVER_6M(3, 20, new Color(0,0,0)),
  SEKUNDAERRUTE_OVER_6M(4, 20, new Color(0,0,0)),
  VEJ_3_TIL_6M(5, 20, new Color(0,0,0)),
  ANDEN_VEJ(6, 20, new Color(0,0,0)),
  STI(8, 20, new Color(0,0,0)),
  MARKVEJ(10, 20, new Color(0,0,0)),
  GÅGADER(11, 20, new Color(0,0,0)),
  PROJ_MOTORVEJ(21, 20, new Color(0,0,0)),
  PROJ_MOTORTRAFIKVEJ(22, 20, new Color(0,0,0)),
  PROJ_PRIMAERVEJ(23, 20, new Color(0,0,0)),
  PROJ_SEKUNDAERVEJ(24, 20, new Color(0,0,0)),
  PROJ_VEJ_3_TIL_6M(25, 20, new Color(0,0,0)),
  PROJ_VEJ_UNDER_3M(26, 20, new Color(0,0,0)),
  PROJ_STI(28, 20, new Color(0,0,0)),
  MOTORVEJSAFKOERSEL(31, 20, new Color(0,0,0)),
  MOTORTRAFIKVEJSARKOERSEL(32, 20, new Color(0,0,0)),
  PRIMAERVEJSAFKOERSEL(33, 20, new Color(0,0,0)),
  SEKUNDAERVEJSAFKOERSEL(34, 20, new Color(0,0,0)),
  ANDEN_VEJAFKOERSEL(35, 20, new Color(0,0,0)),
  MOTORVEJSTUNNEL(41, 20, new Color(0,0,0)),
  MOTORTRAFIKVEJSTUNNEL(42, 20, new Color(0,0,0)),
  PRIMAERVEJSTUNNEL(43, 20, new Color(0,0,0)),
  SEKUNDAERVEJSTUNNEL(44, 20, new Color(0,0,0)),
  ANDEN_VEJTUNNEL(45, 20, new Color(0,0,0)),
  MINDRE_VEJTUNNEL(46, 20, new Color(0,0,0)),
  STITUNNEL(48, 20, new Color(0,0,0)),
  
  //TODO Unknown road types in kdv_unload.txt. What to do with them?
  UKENDT1(99, 20, new Color(0,0,0)),
  UKENDT2(95, 20, new Color(0,0,0)),
  UKENDT3(80, 20, new Color(0,0,0)),
  UKENDT4(0, 20, new Color(0,0,0));
  
  int id;
  int stroke;
  Color color;
  private static java.util.Map<Integer, RoadType> roadTypes = new HashMap<>();
  
  static {
    for(RoadType rt : RoadType.values()) {
      roadTypes.put(rt.getId(), rt);
    }
  }
  
  RoadType(int id, int stroke, Color color) {
    this.id = id;
    this.stroke = stroke;
    this.color = color;
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
    return "ID:" + id + " STROKE:" + stroke + " COLOR:" + color;
  }
}
