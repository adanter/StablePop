//keeps track of contents of a closed environment
/*
    Thinking in text...
    Our algorithm for running a locale is as follows:
    Initialize the predator and prey populations.  All preds have the same starting kill rate.
    For each run:
      For each generation:
        For each pred:
          Use pred's kill rate to determine how much it eats.
          Record this value with the pred.
          Decrement the prey population by this amount.
        Group the preds into random breeding pairs.
        Find the number of children for each breeding pair using the fitness/reproduction function from Ito et al.
        Use crossover to create children for each breeding pair, then apply mutation to the offspring.
        Shuffle the list of offspring.
        Multiply the prey population by its growth constant.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Locale{
    private List<Predator> predList;
    // basePrey is the number of basic prey currently in the locale
    private int basePrey;
    private double mortality;

    public Locale(int predPop, int preyPop, double predMortality, double predKillRate){
        this.basePrey = preyPop;
        this.predList = new ArrayList<Predator>(predPop);
        this.mortality = predMortality;
        for (int i = 0; i < predPop; i++) {
            Predator pred = new Predator(predKillRate);
            predList.add(pred);
        }
    }

    public List<Predator> getPredList(){
        return predList;
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
        return sumKillRates / predList.size();
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

    public int getBasePrey(){
        return this.basePrey;
    }

    public void setBasePrey(int preyPop) {
        basePrey = preyPop;
    }

    public void reduceBasePrey(int deaths) {
        basePrey -= deaths;
        if (basePrey < 0) {
            System.out.println("Error message:  Out of food");
        }
    }
    
    //takes a random int, removes and returns a random predator based on int
    public Predator popPred(){
        Random random = new Random();
        return predList.remove(random.nextInt(predList.size()));
    }
    
    public void addPred(Predator newPred){
        predList.add(newPred);
    }
    
    public void killPreds(){
        int cutoff = predList.size() - (int)Math.ceil(predList.size() * mortality);
        predList = predList.subList(0, cutoff);
    }
}