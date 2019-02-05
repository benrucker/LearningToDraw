package circle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.jgap.*;

import com.anji.integration.*;
//import com.anji.polebalance.DoublePoleBalanceFitnessFunction;
import com.anji.util.*;

public class CircleFitnessFunction implements BulkFitnessFunction, Configurable {
	
	final String XKEY = "startx";
	final String YKEY = "starty";
	
	float startx;
	float starty;
	int timesteps;
	
	final int speed = 10;
	
	
	ActivatorTranscriber factory;
	
	private final static Logger logger = Logger.getLogger( CircleFitnessFunction.class );
			
    public void init(Properties properties) throws Exception {
        try {
        	logger.info("init circle fitness func");
        	factory = (ActivatorTranscriber) properties.singletonObjectProperty( ActivatorTranscriber.class );

            //this.factory = (ActivatorTranscriber)properties.singletonObjectProperty(class$com$anji$integration$ActivatorTranscriber == null ? 
            //		(class$com$anji$integration$ActivatorTranscriber = DoublePoleBalanceFitnessFunction.class$("com.anji.integration.ActivatorTranscriber"))
            //		: class$com$anji$integration$ActivatorTranscriber);
            //this.startx = properties.getFloatProperty(XKEY,300);
            startx=300;
        	this.startx = properties.getFloatProperty(YKEY,0);
            this.timesteps = properties.getIntProperty("timesteps", 100);
        }
        catch (Exception exception) {
            throw new IllegalArgumentException("invalid properties: " + exception.getClass().toString() + ": " + exception.getMessage());
        }
    }
	
	@Override
	public void evaluate(List chromosomes) {
		// loop thru chromosomes
		// check each fitness
		// means stepping thru the circle-making process
//		logger.error("wtf\n");
		
		Iterator it = chromosomes.iterator();
		while ( it.hasNext() ) {
			Chromosome c = (Chromosome) it.next();
			evaluate( c );
		}
	}
	
	public void evaluate(Chromosome chr) {
        try {
        	if (chr == null)
        		throw new NullPointerException("chr is null");
            Activator activator = factory.newActivator(chr);
            //int n = drawCircleCartesian(activator,chr.getId());
            //int n = drawCircleCart2(activator,chr.getId());
            int n = drawCirclePolar(activator,chr.getId());
    		//logger.error("wtf\n");

            chr.setFitnessValue(n);
        }
        catch (Throwable throwable) {
            logger.warn((Object)("error evaluating chromosome " + chr.toString()), throwable);
            chr.setFitnessValue(0);
        }
	}
	
	private int drawCircleCartesian(Activator activator, long id) {
		double[] stimuli = {startx,starty,0,0,0,300};
		double[][] results = new double[101][2];

		for (int step = 0; step < timesteps; step++) {
			results[step][0] = stimuli[0]; // x pos
			results[step][1] = stimuli[1]; // y pos
			
			double[] accel = activator.next(stimuli);
			//System.out.println(Arrays.toString(accel));
			//logger.info(Arrays.toString(accel));
			stimuli = updateStimuliCartesian(stimuli,accel);
		}
		
		results[timesteps][0] = stimuli[0]; // x pos
		results[timesteps][1] = stimuli[1]; // y pos
		
		int fitness = evaluate(results);
		
		try {
			exportResults(results,id);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//return 100;
		return fitness;
	}

	@SuppressWarnings("unused")
	private double[] updateStimuliCartesian(double[] stimuli, double[] accel) {
		stimuli[0] += stimuli[2]; // x pos + x vel
		stimuli[1] += stimuli[3]; // y pos + y vel
		stimuli[2] += accel[0]; // x vel + x accel
		stimuli[3] += accel[1]; // y vel + y accel
		stimuli[4] = Math.sqrt(Math.pow(stimuli[0], 2) + Math.pow(stimuli[1], 2));
		return stimuli;
	}
	
	private int drawCircleCart2(Activator activator, long id) {
		// stim
		// x,y,x^2,y^2
		
		double[] stimuli = {0,0,0,0};
		double[][] results = new double[101][2];
		
		double[] vel = {0,0};
		
		for (int step = 0; step < timesteps; step++) {
			results[step][0] = stimuli[0]; // x pos
			results[step][1] = stimuli[1]; // y pos
			
			//update accel, then vel, then pos
			
			double[] accel = activator.next(stimuli);
			

			vel[0] += accel[0] - .5; // x vel + x accel
			vel[1] += accel[1] - .5; // y vel + y accel
			
			//System.out.println(Arrays.toString(accel));
			//logger.info(Arrays.toString(accel));
			stimuli = updateStimuliCart2(stimuli,vel);
		}
		
		results[timesteps][0] = stimuli[0]; // x pos
		results[timesteps][1] = stimuli[1]; // y pos
		
		int fitness = evaluate(results);
		
		try {
			exportResults(results,id);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//return 100;
		return fitness;
	}
	
	@SuppressWarnings("unused")
	private double[] updateStimuliCart2(double[] stimuli, double[] vel) {
		stimuli[0] += vel[0]; // x pos + x vel
		stimuli[1] += vel[1]; // y pos + y vel
		
		stimuli[2] = Math.pow(stimuli[0], 2);
		stimuli[3] = Math.pow(stimuli[1], 2);
		return stimuli;
	}
	
	private int drawCirclePolar(Activator activator, long id) {
		
		//double[] stimuli = {0,0,0,0,0};
		double[] stimuli = {0,0,0};
		
		double[][] results = new double[timesteps+1][2];

		for (int step = 0; step < timesteps; step++) {
			
			//if (outOfBounds(stimuli[0],stimuli[1])) return 0;
			
			
			results[step][0] = stimuli[0]; // pos r
			results[step][1] = stimuli[1]; // pos theta
			
			double[] accel = activator.next(stimuli);
			//System.out.println(Arrays.toString(accel));
			//logger.info(Arrays.toString(accel));
			stimuli = updateStimuliPolar(stimuli,accel,step);
		}
		
		results[timesteps][0] = stimuli[0]; // pos r
		results[timesteps][1] = stimuli[1]; // pos theta
		
		int fitness = evaluatePolar(results);
		
		try {
			exportResults(polarToCart(results),id);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//return 100;
		return fitness;
	}
	
	@SuppressWarnings("unused")
	private double[] updateStimuliPolar(double[] stimuli, double[] accel, int step) {
		//stimuli[0] position r
		//stimuli[1] position theta
		//stimuli[2] velocity r
		//stimuli[3] velocity theta
		//stimuli[4] dist from 0,0
		
		stimuli[2] += accel[0] - .5;

 		double[] newPos = addVectors(stimuli[0],stimuli[1],
//				Math.max(5,stimuli[2]),Math.max(2*Math.PI,stimuli[3]));
				speed,stimuli[2]);
		
		stimuli[0] = newPos[0]; // pos r
		stimuli[1] = newPos[1]; // pos t
				
		
		/*
		double[] newVel = addVectors(stimuli[2],stimuli[3],
//				Math.max(2,accel[0]),Math.max(2*Math.PI,accel[1]));
				accel[0],accel[1]);
		
		stimuli[2] = newVel[0]; 
		stimuli[3] = newVel[1]; 
		
		stimuli[4] = Math.sqrt(Math.pow(stimuli[0], 2) + Math.pow(stimuli[1], 2));
		
		stimuli[6] = accel[0];
		stimuli[7] = accel[1];
		*/
		//stimuli[3] = (double)step;
		//stimuli[4] = Math.sin((double)step/99.0 * (2*Math.PI));
		//stimuli[8] = Math.cos(step/99 * (2*Math.PI));
		return stimuli;
		
	}
	
	private double[] addVectors(double r1, double t1, double r2, double t2) {
		double r3 = Math.sqrt(r1*r1+r2*r2+2*r1*r2*Math.cos(t2-t1));
		double t3 = t1 + Math.atan2(r2*Math.sin(t2-t1), r1+r1*Math.cos(t2-t1));
		
		return new double[]{r3,t3};
	}
	
	private int evaluatePolar(double[][] circle) {
		return evaluate(polarToCart(circle));
		
	}
	
	private int evaluate(double[][] circle) {
		
		int circularity = (int)(checkCircularity(circle));
		double startEndDistApart = distance(circle[0],circle[circle.length-1]);
		
		double area = MathPolygon.calcArea(circle);
		int targetArea = 300;
		
		double dArea = Math.abs(targetArea-area);
		
		// map these values to add to fitness the closer they are instead of
		// banishing the chromosomes to negative fitness for all of eternity
		// pls
		
		//int areaFitness = (int)map(dArea,0,300,300,0);
		//int endsDistFitness = (int)map(startEndDistApart,0,500,500,0);
		
		/*logger.info("circ=" + circularity + 
				" ends dist="+startEndDistApart + 
				" deltaArea=" + dArea);
		*/
		
		//return Math.max(circularity - startEndDistApart - dArea,0);
		
		// max: circ=1000 areaF=300 endsF=500
		//      total=1800
		//return circularity + areaFitness + endsDistFitness;
		
		// max: circ=1000 areaF=300
		//      total=1300
		
		//double[] centroid = findCentroid(circle);
		
		//double distUniformity = -deltaDistFromCentroid(circle,centroid) + 1000;
		
		if (area > 9000 || area < 100) return 0;
		
//		return circularity + Math.max(areaFitness,0);
		return (int) (circularity + Math.min(area,300));
	}
	
	private double[] findCentroid(double[][] points) {
	    double centroidX = 0, centroidY = 0;

        for(double[] p : points) {
            centroidX += p[0];
            centroidY += p[1];
        }
        
        return new double[] {centroidX / points.length, centroidY / points.length};
	}
	
	private double deltaDistFromCentroid(double[][] points, double[] centroid) {
		return maxDistFromCentroid(points,centroid) - minDistFromCentroid(points,centroid);
	}
	
	private double minDistFromCentroid(double[][] points, double[] centroid) {
		double smallest = Double.MAX_VALUE;
		for (double[] p : points)
			smallest = distance(p,centroid) < smallest ? distance(p,centroid) : smallest;
		return smallest;
	}

	private double maxDistFromCentroid(double[][] points, double[] centroid) {
		double biggest = Double.MIN_VALUE;
		for (double[] p : points)
			biggest = distance(p,centroid) > biggest ? distance(p,centroid) : biggest;
		return biggest;
	}
	
	// https://stackoverflow.com/questions/7505991/arduino-map-equivalent-function-in-java
	private double map(double x, int in_min, int in_max, int out_min, int out_max)
	{
	  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	private double checkCircularity(double[][] points) {
		// Q = 4piA / L^2
		// where Q <= 1
		// 		 A = area
		//       L = perimeter
		
		double area = MathPolygon.calcArea(points);
		double perim = MathPolygon.getPerimeter(points);
		
		double circQuotient;
		if (area == 0 || perim == 0) {
			circQuotient = 0;
		} else {
			circQuotient = (4 * Math.PI * area) / (Math.pow(perim, 2));
		}
		
		return circQuotient*500;
	}
	
	private double distance(double[] one, double[] two) {
		return MathPolygon.calcDist(one, two);
	}
	
	@Override
	public int getMaxFitnessValue() {
		return 1000;
	}
	
	private double[][] polarToCart(double[][] points) {
		double[][] out = new double[points.length][2];
		for (int i = 0; i < points.length; i++) {
			out[i] = Arrays.copyOf(polarToCart(points[i]),2);
		}
		return out;
	}

	private double[] polarToCart(double[] point) {
		double[] out = new double[2];
		
		double r = Double.valueOf(point[0]);
		double t = Double.valueOf(point[1]);
		
		double x = r * Math.cos(t);
		double y = r * Math.sin(t);
		
		out[0]=x;
		out[1]=y;
		
		return out;
	}
	
	private boolean outOfBounds(double r, double t) {
		double[] point = polarToCart(new double[] {r,t});
		for (double p : point) {
			if (p < -500 || p > 500)
				return true;
		}
		return false;
	}

	private void exportResults(double[][] points, long id) throws IOException {
		//if (id % 500 != 0) return;
		
		String out = "";
		for (double[] p: points) {
			out += p[0] + " " + p[1] + "\n";
		}
		
//		BufferedWriter writer = new BufferedWriter(new FileWriter("E:\\Documents\\Programming\\circleoutput\\"+id+".txt"));
		BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\UserDocs\\circleoutput\\"+id+".txt"));
	    writer.write(out);
	    writer.close();
		
	}

}
