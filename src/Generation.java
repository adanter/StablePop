/**
 * Predator and prey populations are stored in locales and migration is handled
 * by the metapopulation, but life itself is controlled by the generation 
 * object.
 *
 * Generation's primary use is to take a locale and simulate a cycle of
 * predation, reproduction, and death - a single generation.  One generation
 * object can be used on many locales many times.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;

public class Generation{
    private Random random;
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
    public Generation(
        double preyGrowthRate, 
        double predGrowthRate, 
        int preyCap, 
        int predGrowthRateCap, 
        double predMortality, 
        double mutRate, 
        Random random
    ) {
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
    public void runGeneration(Locale locale) {
        // Have predators hunt prey, setting kills for the preds and decreasing
        // the prey population accordingly
        for (Predator pred : locale.getPredList()){
            hunt(pred, locale);
        }

        // Grow the prey population up to a maximum of its growth cap
        int newPreyPop = ((int)Math.ceil(locale.getNumPrey() * preyGrowth));
        newPreyPop = Math.min(newPreyPop, preyCap);
        locale.setNumPrey(newPreyPop);

        // Allow predators to reproduce, then kill part of them at random.
        List<Predator> predators = locale.getPredList();
        predators = makeKids(predators);
        predators = killPreds(predators);
        locale.setPredList(predators);

        // Tell the locale to add a new entry to its demographic stats.
        locale.updateLog();
    }

    /**
     * Gives the target predator a chance to kill each of the prey in the 
     * population
     * @param pred Predator on the hunt
     */
    private void hunt(Predator pred, Locale locale) {
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
     * @param predators List of predators to be bred.
     * @return Modified list of predators, with kids
     */
    private List<Predator> makeKids(List<Predator> predators) {
        // Randomize the predator list before assigning mating pairs
        Collections.shuffle(predators, this.random);

        List<Predator> kids = new ArrayList<>();
        Predator pred1;
        Predator pred2;
        int predsBred;
        // Chooses pairs of predators in order to reproduce.  Each breeding
        // pair has a number of children determined by their total kills, the
        // pred growth rate, and the pred growth cap.
        for (predsBred = 0; predsBred < predators.size() - 1; predsBred += 2) {
            pred1 = predators.get(predsBred);
            pred2 = predators.get(predsBred+1);
            int pairFitness = pred1.getKills() + pred2.getKills();
            pairFitness = (int)Math.min(
                Math.floor(pairFitness * predGrowth), 
                (2 * predGrowthCap)
            );

            for (int kidsMade = 0; kidsMade < pairFitness; kidsMade++) {
                // Kid killRate is a randomly-weighted average of parents' KRs
                float crossingPoint = this.random.nextFloat();
                double firstParentGenes = crossingPoint * pred1.getKillRate();
                double secondParentGenes = 
                    (1 - crossingPoint) * pred2.getKillRate();
                double kidKillRate = firstParentGenes + secondParentGenes;

                Predator kid = new Predator(kidKillRate);
                kids.add(kid);
            }
        }
        // If there is an odd number of predators in the input population, the 
        // last reproduces without a mate, creating clones of itself.
        if (predsBred < predators.size()){
            Predator oddPred = predators.get(predsBred);
            int soloFitness = Math.min(oddPred.getKills(), predGrowthCap);
            for (int kidsMade = 0; kidsMade < soloFitness; kidsMade++){
                Predator kid = new Predator(oddPred.getKillRate());
                kids.add(kid);
            }
        }

        // Each new predator is mutated before being added to the population.
        for (int mutated = 0; mutated < kids.size(); mutated++) {
            Predator kid = kids.remove(mutated);
            kids.add(mutated, mutate(kid));
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
        Double increase = this.random.nextDouble() * mutRate;
        if (this.random.nextBoolean()) {
            increase = -1 * increase;
        }
        pred.setKillRate(pred.getKillRate() * (1 + increase));
        return pred;
    }

    /**
     * Removes a proportion of a list of predators equal to the pred mortality 
     * rate, representing death through factors such as old age, disease, and
     * accident.
     * @param predators Predator population to be culled
     * @return Culled predator population
     */
    private List<Predator> killPreds (List<Predator> predators) {
        // Shuffle to make sure we're killing at random
        Collections.shuffle(predators, this.random);

        // Round cutoff down to allow a cutoff of 0
        int cutoff = predators.size() - 
            (int)Math.ceil(predators.size() * predMortality);
        predators = predators.subList(0, cutoff);
        return predators;
    }
}