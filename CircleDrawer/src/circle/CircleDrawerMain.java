package circle;
import java.io.IOException;

import org.jgap.Configuration;
import org.jgap.Genotype;

import com.anji.neat.*;
import com.anji.run.Run;
import com.anji.util.Properties;

// pass properties text filename to Evolver
// aka the "properties"
// it will parse itself



public class CircleDrawerMain {
	
	public static void main(String[] args) throws Exception {
		//Properties properties = new Properties("circle2_benchmark.properties");
		Properties properties = new Properties("dpbalance.properties");
/*		
		NeatActivator neat = new NeatActivator();
		neat.init(properties);
		CircleFitnessFunction fitness = new CircleFitnessFunction();
		fitness.init(properties);
		
		Evolver evolver = new Evolver();*/
		Configuration config = new NeatConfiguration(properties);
		
		Genotype genotype = Genotype.randomInitialGenotype(config);
		
		for (int x = 0; x < 100; x++) {
			genotype.evolve();
			
		}
	}

}
