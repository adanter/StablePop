/**
 * Basic unit of a predator population.  Predators consist of a kill rate that determines what proportion of a prey
 * population they are likely to kill each generation, as well as a counter that keeps track of how many kills they
 * actually made in a generation.
 */
public class Predator{
    private double killRate;
    private int kills;

    /**
     * Creates a predator with 0 kills and a specified kill rate
     * @param initRate Predator's starting kill rate
     */
    public Predator(double initRate){
        this.killRate = initRate;
    }

    /**
     * Sets the predator's kill rate to a new double
     * @param killRate New kill rate
     */
    public void setKillRate(double killRate) {
        this.killRate = killRate;
    }

    /**
     * Returns the predator's kill rate
     * @return Kill rate
     */
    public double getKillRate(){
        return killRate;
    }

    /**
     * Sets the predator's number of kills to a new int
     * @param numKills New kill count
     */
    public void setKills(int numKills) {
        this.kills = numKills;
    }

    /**
     * Returns the predator's number of kills
     * @return Number of kills
     */
    public int getKills() {
        return kills;
    }
}