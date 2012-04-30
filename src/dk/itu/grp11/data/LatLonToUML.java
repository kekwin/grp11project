package dk.itu.grp11.data;

public class LatLonToUML {
    
    private static final double a = 6378137; 
    private static final double b = 6356752.314; 
    private static final double f = (a-b)/a;
    private static final double invf = 1/f;
    private static final double rm = Math.sqrt(a*b);
    private static final double k0 = 0.9996;
    private static final double e = Math.sqrt(1-(b/a)*(b/a));
    private static final double ee = (e*e)/(1-e*e);
    
    public static double[] convert(double lat, double lon) {
        double pos[] = new double[2];
		double latr = (lat*Math.PI)/180;
        double lonr = (lon*Math.PI)/180;
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
        double y =k0*(M + N*Math.tan(latr)*(A0*A0/2 + (5 - T +9*C0 +4*C0*C0 )*Math.pow(A0,4)/24 + (61-148*T+16*T*T)*Math.pow(A0,6)/720 ));
        if(zone == 33) {
            x += 381655;
            //373859
            //383450
            //381655
        }
        System.out.println(x);
        System.out.println(y);
		pos[0]=x;
		pos[1]=y;
		System.out.println(pos[0] + " " + pos[1]);
        return pos;
    }
    public static void main(String[] args) {
		convert(5030303,500204);
	}
}