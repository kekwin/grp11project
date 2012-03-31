package dk.itu.grp11.enums;

import java.awt.Color;

import dk.itu.grp11.exceptions.RoadTypeDoesNotExistException;
/**
 * Describes 
 *
 */
public enum RoadType {
  MOTORVEJ(1, 20, new Color(0,0,0)),
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
  STITUNNEL(48, 20, new Color(0,0,0));
  
  int id;
  int stroke;
  Color color;
  
  RoadType(int id, int stroke, Color color) {
    this.id = id;
    this.stroke = stroke;
    this.color = color;
  }
  
  public int id() {
    return id;
  }
  
  public static RoadType getTypeById(int id) {
	  for(RoadType rt : RoadType.values()) {
		  if(rt.id() == id) return rt;
	  }
	  throw new RoadTypeDoesNotExistException(); //If no road with such id exist
  }
  
  @Override
  public String toString() {
    return "ID:" + id + " STROKE:" + stroke + " COLOR:" + color;
  }
}
