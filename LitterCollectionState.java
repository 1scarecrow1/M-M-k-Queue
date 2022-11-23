package Assignment1;

import java.util.Random;

import general.Counter;
import general.SystemState;
import general.Utils;
import general.annotations.AutoCounter;
import general.annotations.AutoMeasure;
import general.annotations.Initialize;


/** This class models the system state for a M/M/1000 queueing system for trash accumulation and collection. 
 * 
 * @author M-P
 *
 */

public class LitterCollectionState extends SystemState<LitterCollectionState>
{
	// Parameters
       private final Random random;
       private final double lambda;
       private final int capacity; 
       private final int sensorLevel; 
             
	// State variables
       //number of bags
	   private int b; 
	   
	// Counter variables
	@AutoCounter("Total Cost")
	private Counter totalCost;

	public LitterCollectionState(int capacity, int sensorLevel, double lambda, double timeHorizon, long seed) {
		super(timeHorizon, seed);
		this.capacity = capacity; 
		this.sensorLevel = sensorLevel;
		this.lambda = lambda;
		random = new Random(seed);
		reset();
	}

	@Initialize
	public void initReplication() {
		double nextArrivalTime = Utils.nextInterArrivalTime(random, lambda);
	    addEvent(nextArrivalTime, this::doArrival);
	}
	
	public void doArrival(double eventTime) {
			b++;
			if (b == sensorLevel) {
				//call method to empty container
				double containerTime = eventTime + 2;
				addEvent(containerTime, this::emptyContainer);
				
				//generate next arrival
				double currentTime = eventTime;
				double nextInterArrivalTime = Utils.nextInterArrivalTime(random, lambda);
				double nextArrivalTime = currentTime + nextInterArrivalTime;
				addEvent(nextArrivalTime, this::doArrival);
				}
			else {
			//generate next arrival
			double currentTime = eventTime;
			double nextInterArrivalTime = Utils.nextInterArrivalTime(random, lambda);
			double nextArrivalTime = currentTime + nextInterArrivalTime;
			addEvent(nextArrivalTime, this::doArrival);
		}
	}
			
		
		public void emptyContainer(double eventTime) {
			double prevTime = getCurrentTime();
			double containerTime = eventTime; 
	     	totalCost.incrementBy(100);
	     	 		
	     	if (b > capacity) {
	     		totalCost.incrementBy((containerTime - prevTime)*(b-capacity)*10);
			 }
	     	b = 0;
		}
	
	
	@AutoMeasure("Total Cost")
	public Double getPerformanceMeasure() {
		return totalCost.getValue();
	}
	
	@Override
	public void reset() {
		b = 0;
	}
}
