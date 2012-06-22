package dk.itu.grp11.util;

import java.awt.geom.Point2D;

public class LatLonToUTM {
  //TODO javadoc
  /**
  * This class is used to convert Lattitude and Longtitude into UTM, this is due
  * to the fact that our coastline data is defined with lat/lon, and our application
  * uses UTM, as that is what was defined the the KRAK dat set.
  *
  * @author Group 11
  */
  //TODO cleanup comments
  // Math for the Formula is found here: http://www.uwgb.edu/dutchs/usefuldata/UTMFormulas.HTM
  // It is very complicated.
  // Here its made into java code, and seems to work
  public static Point2D convert(double lat, double lon) {
    double a = 6378137; 
    double b = 6356752.314; 
    double k0 = 0.9996;
    double e = Math.sqrt(1-(b/a)*(b/a));
    double ee = (e*e)/(1-e*e);
    
    double latr = (lat*Math.PI)/180;
    // Not sure why longtitude is never used, seems weird. But its the same in the given formula..
    //double lonr = (lon*Math.PI)/180;
    int zone = 32;
    int zoneCM = 6*zone-183;
    double deltaLon = lon-zoneCM;
    double dL = deltaLon*Math.PI/180;
    double N = a/Math.sqrt(1-e*e*Math.pow(Math.sin(latr),2));
    double T = Math.pow(Math.tan(latr),2);
    double C0 = ee*(Math.pow(Math.cos(latr),2));  
    double A0 = dL*Math.cos(latr);
    double M = a*((1-(e*e/4)-3*Math.pow(e,4)/64-5*Math.pow(e,6)/256)*latr-(3*e*e/8+3*Math.pow(e,4)/32+45*Math.pow(e,6)/1024)*Math.sin(2*latr)+(15*Math.pow(e,4)/256+45*Math.pow(e,6)/1024)*Math.sin(4*latr)-(35*Math.pow(e,6)/3072)*Math.sin(6*latr));
    double x = 500000+(k0*N*(A0 + (1-T+C0)*(Math.pow(A0,3))/6 + (5-18*T+T*T+72*C0-58*ee)*(Math.pow(A0,5))/120));
    double y = k0*(M + N*Math.tan(latr)*(A0*A0/2 + (5 - T +9*C0 +4*C0*C0 )*Math.pow(A0,4)/24 + (61-148*T+16*T*T)*Math.pow(A0,6)/720 ));
    
    return new Point2D.Double(x,y);
  }
}