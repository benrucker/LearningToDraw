package circle;
import java.io.IOException;

import org.jgap.Chromosome;

import com.anji.neat.*;
import com.anji.persistence.Persistence;
import com.anji.util.DummyConfiguration;
import com.anji.util.Properties;

// pass properties text filename to Evolver
// aka the "properties"
// it will parse itself



public class CircleEvaluator {
	
	public static void main(String[] args) throws Exception {
		Properties properties = new Properties("circle_benchmark.properties");
		
		//NeatActivator neat = new NeatActivator();
		//neat.init(PROPERTIES);
		CircleFitnessFunction fitness = new CircleFitnessFunction();
		fitness.init(properties);
		
		Persistence persist = (Persistence)properties.newObjectProperty("persistence");
		
		Chromosome chromosome = persist.loadChromosome(args[0],new DummyConfiguration());
		if (chromosome == null) {
			throw new IllegalArgumentException("no chromosome found: " + args[0]);
		}
		
		//fitness.enableDisplay();
		fitness.evaluate(chromosome);
		System.out.println("Fitness = " + chromosome.getFitnessValue());
		
	}

}