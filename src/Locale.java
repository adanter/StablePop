/**
 * Simulates a single location where members of a predator species and members of a prey species interact.  Stores
 * populations and tracks their statistics over time.  All actual changes to populations, though, are handled by
 * generation and passed back to locale.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Locale{
    private List<Predator> predList;
    private int numPrey;
    private int generation;
    private String localeLog = "Generation, Prey, Preds, Max KR, Avg KR \n";

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

    public List<Predator> getPredList(){
        return predList;
    }

    public int getNumPreds() {
        return predList.size();
    }

    public void setPredList(List<Predator> newPreds) {
        predList = newPreds;
    }
    
    public void shufflePredList(Random r) {
        Collections.shuffle(this.predList, r);
    }

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

    public double getMaxKillRate() {
        double maxKillRate = 0.0;
        for (Predator pred : predList) {
            if (pred.getKillRate() > maxKillRate) {
                maxKillRate = pred.getKillRate();
            }
        }
        return maxKillRate;
    }

    public int getNumPrey(){
        return this.numPrey;
    }

    public void setNumPrey(int preyPop) {
        numPrey = preyPop;
    }

    public void reduceBasePrey(int deaths) {
        numPrey -= deaths;
        if (numPrey < 0) {
            System.out.println("Error message:  Out of food");
        }
    }
    
    //takes a random int, removes and returns a random predator based on int
    public Predator popPred(){
        Random random = new Random();
        Predator result = predList.remove(random.nextInt(predList.size()));
        return result;
    }
    
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