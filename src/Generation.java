/**
 * A "generation" object contains a number of parameters and functions which
 * determine how the populations of a locale change from generation to 
 * generation.  One generation object can be used on many locales over many 
 * generations.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generation{
    private Random random;
    private Locale locale;
    private double preyGrowth;
    private double predGrowth;
    private int preyCap;
    private int predGrowthCap;
    private double predMortality;
    private double mutRate;

    /**
     * Constructor for a generation object
     * @param preyGrowthRate Exponential factor for prey reproduction
     * @param predGrowthRate Conversion factor between predator kills and 
     *                       predator kids
     * @param preyCap Maximum number to which a prey population can grow
     * @param predGrowthRateCap Maximum number of children that can be produced
     *                          for each predator in a population
     * @param predMortality Chance for each predator to die at end of generation
     * @param mutRate Mutation rate for predators
     */
    public Generation(double preyGrowthRate, double predGrowthRate, int preyCap,
     int predGrowthRateCap, double predMortality, double mutRate, Random random)
    {
        this.preyGrowth = preyGrowthRate;
        this.predGrowth = predGrowthRate;
        this.preyCap = preyCap;
        this.predGrowthCap = predGrowthRateCap;
        this.predMortality = predMortality;
        this.mutRate = mutRate;
        this.random = random;
    }

    /**
     * Simulates a generation for a given locale, setting new prey and pred 
     * populations
     * @param newLoc Locale to be updated
     */
    public void runGeneration(Locale newLoc) {
        this.locale = newLoc;

        // Have predators hunt prey, setting kills for the preds and decreasing the prey population
        for (Predator pred : locale.getPredList()){
            hunt(pred);
        }

        // Grow the prey population up to a maximum of its growth cap
        int newPreyPop = ((int)Math.ceil(locale.getNumPrey() * preyGrowth));
        newPreyPop = Math.min(newPreyPop, preyCap);
        locale.setNumPrey(newPreyPop);

        // Allow predators to reproduce, then kill part of them at random.
        locale.shufflePredList(random);
        locale.setPredList(makeKids(locale.getPredList()));
        locale.shufflePredList(random);
        locale.setPredList(killPreds(locale.getPredList()));

        // Tell the locale to add a new entry to its demographic stats.
        locale.updateLog();
    }

    /**
     * Gives the target predator a chance to kill each of the prey in the 
     * population
     * @param pred A predator on the hunt
     */
    private void hunt(Predator pred) {
        int killCount = 0;
        for (int i = 0; i < locale.getNumPrey(); i++){
            if (random.nextFloat() <= pred.getKillRate()){
                killCount++;
            }
        }
        locale.reduceBasePrey(killCount);
        pred.setKills(killCount);
    }

    /**
     * Takes a population of predators and allows them to breed based on their 
     * kill rates from the last generation.
     * Combines fitness evaluation with reproduction, crossover, and mutation.
     * @param predators List of predators to be bred.  Assumes list is 
     *                  pre-shuffled.
     * @return Modified list of predators, with kids
     */
    private List<Predator> makeKids(List<Predator> predators) {
        List<Predator> kids = new ArrayList<>();
        Predator pred1;
        Predator pred2;
        int i;
        // Chooses pairs of predators in order to reproduce.  Each breeding pair has a number of children determined by
        // their total kills, the pred growth rate, and the pred growth cap.
        for (i = 0; i < predators.size() - 1; i += 2) {
            pred1 = predators.get(i);
            pred2 = predators.get(i+1);
            int pairFitness = pred1.getKills() + pred2.getKills();
            pairFitness = (int)Math.min(Math.floor(pairFitness * predGrowth), (2 * predGrowthCap));

            Random rand = new Random();
            for (int j = 0; j < pairFitness; j++) {
                float crossingPoint = rand.nextFloat();
                double kidKillRate = crossingPoint * pred1.getKillRate() + (1 - crossingPoint) * pred2.getKillRate();
                Predator kid = new Predator(kidKillRate);
                kids.add(kid);
            }
        }
        // If there is an odd number of predators in the input population, the last reproduces asexually in a comparable
        // method to that of its fellows
        if (i < predators.size()){
            Predator oddPred = predators.get(i);
            int soloFitness = Math.min(oddPred.getKills(), predGrowthCap);
            for (int j = 0; j < soloFitness; j++){
                Predator kid = new Predator(oddPred.getKillRate());
                kids.add(kid);
            }
        }

        // Each new predator is mutated before being added to the population.
        for (int j = 0; j < kids.size(); j++) {
            Predator kid = kids.remove(j);
            kids.add(j, mutate(kid));
        }

        kids.addAll(predators);
        return kids;
    }

    /**
     * Allows a predator's kill rate to be increased or decreased by a 
     * proportion from 0 to mutRate
     * @param pred Predator to be mutated
     * @return Mutated version of the predator
     */
    private Predator mutate (Predator pred) {
        Double increase = random.nextDouble() * mutRate;
        if (random.nextBoolean()) {
            increase = -1 * increase;
        }
        pred.setKillRate(pred.getKillRate() * (1 + increase));
        return pred;
    }

    /**
     * Removes a proportion of a list of predators equal to the pred mortality 
     * rate
     * @param predators Predator population to be culled
     * @return Culled predator population
     */
    private List<Predator> killPreds (List<Predator> predators) {
        int cutoff = predators.size() - (int)Math.ceil(predators.size() * predMortality);
        predators = predators.subList(0, cutoff);
        return predators;
    }
}