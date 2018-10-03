/**
 * Data transfer object for parameters related to starting populations
 */

public class PopulationStart {
	private int predPop;

	private int preyPop;

	private double lowerKillRate;

	private double upperKillRate;

	public PopulationStart(
		int predPop, 
		int preyPop, 
		double lowerKillRate, 
		double upperKillRate
	) {
		this.predPop = predPop;
		this.preyPop = preyPop;
		this.lowerKillRate = lowerKillRate;
		this.upperKillRate = upperKillRate;
	}

	public int getStartingPredPop() {
		return predPop;
	}

	public int getStartingPreyPop() {
		return preyPop;
	}

	public double getLowerKillRateBound() {
		return lowerKillRate;
	}

	public double getUpperKillRateBound() {
		return upperKillRate;
	}
}