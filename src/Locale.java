/**
 * Simulates a single location where members of a predator species and members of a prey species interact.  Stores
 * populations and tracks their statistics over time.  All actual changes to populations, though, are handled by
 * generation and passed back to locale.
 */

import java.util.ArrayList;
// import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Locale{
    private List<Predator> predList;
    private int numPrey;
    private int generation;

    // Locale log starts with a header
    private String localeLog = "Generation, Prey, Preds, Max KR, Avg KR \n";

    /**
     * Constructor for a new locale
     * @param predPop Starting predator population size
     * @param preyPop Starting prey population size
     * @param predKillRate Starting predator kill rate
     */
    public Locale(int predPop, int preyPop, double predKillRate){
        this.numPrey = preyPop;
        this.predList = new ArrayList<Predator>(predPop);
        this.generation = 0;
        for (int i = 0; i < predPop; i++) {
            Predator pred = new Predator(predKillRate);
            predList.add(pred);
        }
        updateLog();
    }

    /**
     * Returns the predator population as a list
     * @return Predator population in list form
     */
    public List<Predator> getPredList(){
        return predList;
    }

    /**
     * Getter for the number of preds in the population
     * @return Number of predators
     */
    public int getNumPreds() {
        return predList.size();
    }

    /**
     * Setter for the predator population
     * @param newPreds List of predators
     */
    public void setPredList(List<Predator> newPreds) {
        predList = newPreds;
    }

    /**
     * Finds the average kill rate of the predator population
     * @return Average kill rate
     */
    public double getAvgKillRate() {
        double sumKillRates = 0.0;
        for (Predator pred : predList) {
            sumKillRates += pred.getKillRate();
        }
        if (predList.size() > 0) {
            return sumKillRates / predList.size();
        } else {
            return 0;
        }
    }

    /**
     * Finds the highest kill rate of any predator in the population
     * @return Maximum kill rate
     */
    public double getMaxKillRate() {
        double maxKillRate = 0.0;
        for (Predator pred : predList) {
            if (pred.getKillRate() > maxKillRate) {
                maxKillRate = pred.getKillRate();
            }
        }
        return maxKillRate;
    }

    /**
     * Getter for the number of prey
     * @return Number of prey
     */
    public int getNumPrey(){
        return this.numPrey;
    }

    /**
     * Setter for the number of prey
     * @param preyPop New number of prey
     */
    public void setNumPrey(int preyPop) {
        numPrey = preyPop;
    }

    /**
     * Subtracts an int from the number of prey, printing an error if the prey population goes into the negatives
     * @param deaths Int to subtract
     */
    public void reduceBasePrey(int deaths) {
        numPrey -= deaths;
        if (numPrey < 0) {
            System.out.println("Error message:  Out of food");
        }
    }

    /**
     * Takes a random int, removes and returns a random predator based on int
     * @return Popped predator
     */
    public Predator popPred(int index){
        Predator result = predList.remove(index);
        return result;
    }

    /**
     * Adds a new predator to the locale's predator list
     * @param newPred Predator to be added
     */
    public void addPred(Predator newPred){
        predList.add(newPred);
    }

    /**
     * Tells the locale to take a snapshot of its current populations and add the statistics to its log.  Also
     * increments the locale's generation number.
     */
    public void updateLog() {
        String newLine = generation + ","
                + numPrey + ","
                + predList.size() + ","
                + getMaxKillRate() + ","
                + getAvgKillRate() + "\n";
        localeLog += newLine;
        generation ++;
    }

    /**
     * Returns the locale's log of all generations
     * @return
     */
    public String toString() {
        return localeLog;
    }
}