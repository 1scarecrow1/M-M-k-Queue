package Assignment1;

import general.Replication;
import general.Simulation;
import general.automagic.AutoReplication;

public class MainLitterCollection {

	public static void main(String[] args) {
		// parameters
		int capacity = 1000;
		double lambda = 48;
	    double timeHorizon = 365;
	    long n = 1000;
		long seed = 0;
		
		for (int i = 850; i <= 950; i= i + 5) {
			int sensorLevel = i;
			LitterCollectionState state = new LitterCollectionState(capacity, sensorLevel, lambda, timeHorizon, seed);
			Replication<LitterCollectionState> replication = new AutoReplication<LitterCollectionState>(state);

			Simulation<LitterCollectionState> simulation = new Simulation<>(replication);
			simulation.run(n);
			System.out.println("SensorLevel: " + sensorLevel);
			simulation.printEstimates();
			System.out.println("--------------------------------------------------------------------------------------------------------");
		}
	}
}
