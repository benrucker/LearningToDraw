package circle;

import java.awt.Point;
import java.util.Arrays;

public class MathPolygon {
	
	public static double getPerimeter(double[][] p) {
/*		double sum = 0;
		for (int i = 0; i < p.length; i++) {
			if (i == p.length-1) {
				sum += calcDist(p[i],p[0]);
			}
			else {
				sum += calcDist(p[i],p[i+1]);
			}
		}
		
		return sum;
		*/
		
		double sum = 0;

		int i,j;
		i=p.length-1;
		for (j=0; j < p.length; ++j) {
			sum += calcDist(p[j],p[i]);
		   i=j;
		}
		return sum;

	}
	
	public static double calcDist(double[] p1, double[] p2) {
/*		double diffx = p2[0] - p1[0];
		double diffy = p2[1] - p1[1];
		
		double distSq = Math.pow(diffx, 2) + Math.pow(diffy, 2);
		
		double dist = Math.sqrt(distSq);
		
		return dist;*/
		
	    double ac = Math.abs(p2[1] - p1[1]);
	    double cb = Math.abs(p2[0] - p1[0]);
	         
	    return Math.hypot(ac, cb);
	}
	
	// shoelace formula
    public static double calcArea(double[][] p) 
    { 
        double area = 0.0; 
      
        int j = p.length - 1; 
        for (int i = 0; i < p.length; i++) 
        { 
            area += (p[j][0] + p[i][0]) * (p[j][1] - p[i][1]); 
              
            j = i;  
        } 
      
        return Math.abs(area / 2.0); 
    } 
	
}
