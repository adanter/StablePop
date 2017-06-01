//For each pred:
//          Use pred's kill rate to determine how much it eats.
//          Record this value with the pred.
//          Decrement the prey population by this amount.
//        Group the preds into random breeding pairs.
//        Find the number of children for each breeding pair using the fitness/reproduction function from Ito et al.
//        Use crossover to create children for each breeding pair, then apply mutation to the offspring.
//        Shuffle the list of offspring.
//        Multiply the prey population by its growth constant.

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generation{
    private Random random;
    private Locale locale;
    private double preyGrowth;
    private double predGrowth;
    private double mutRate;

    public Generation(double preyGrowthRate, double predGrowthRate, double mutRate) {
        this.random = new Random();
        this.preyGrowth = preyGrowthRate;
        this.predGrowth = predGrowthRate;
        this.mutRate = mutRate;
    }
    
    public void runGeneration(Locale newLoc, int genNumber) {
        this.locale = newLoc;
        for (Predator pred : locale.getPredList()){
            pred.setKills(hunt(pred));
        }
        int newPreyPop = ((int)Math.ceil(locale.getBasePrey() * preyGrowth));
        //keep prey population from exceeding 1 million
        if (newPreyPop > 100000){
            newPreyPop = 100000;
        }
        locale.setBasePrey(newPreyPop);
        locale.shufflePredList(random);
        locale.setPredList(makeKids(locale.getPredList()));
        locale.killPreds();
        locale.shufflePredList(random);
        locale.updateLog(genNumber);
    }

    //this code shuffles: Collections.shuffle(*insert list here*, random);

    //figures out number of kills
    //returns the number of kills
    //side-effect: reduces the base prey pop by number of kills
    private int hunt(Predator pred) {
        int killCount = 0;
        for (int i = 0; i < locale.getBasePrey(); i++){
            if (random.nextFloat() <= pred.getKillRate()){
                killCount++;
            }
        }
        locale.reduceBasePrey(killCount);
        return killCount;
    }

    /**
     * Uses fitness method modified from Ito et al. to determine how many kids to produce for two predators, then
     * produces said kids using crossover and mutation.
     * @return
     */
    private List<Predator> makeKids(List<Predator> predators) {
        List<Predator> kids = new ArrayList<Predator>();
        Predator pred1;
        Predator pred2;
        int i;
        for (i = 0; i < predators.size() - 1; i += 2) {
            pred1 = predators.get(i);
            pred2 = predators.get(i+1);
            int pairFitness = Math.min(pred1.getKills() + pred2.getKills(), 100);
            pairFitness = (int)Math.floor(pairFitness * predGrowth);
//            //cap max number of kids
//            if (pairFitness > 4){
//                pairFitness = 4;
//            }
            Random rand = new Random();
            for (int j = 0; j < pairFitness; j++) {
                float crossingPoint = rand.nextFloat();
                double kidKillRate = crossingPoint * pred1.getKillRate() + (1 - crossingPoint) * pred2.getKillRate();
                Predator kid = new Predator(kidKillRate);
                kids.add(kid);
                //TODO: implement mutation
            }
        }
        if (i < predators.size()){
            Predator oddPred = predators.get(i);
            int soloFitness = Math.min(oddPred.getKills(), 100);
            for (int j = 0; j < oddPred.getKills(); j++){
                Predator kid = new Predator(oddPred.getKillRate());
                kids.add(kid);
            }
        }
        for (Predator kid : kids) {
            kid = mutate(kid);
        }
        kids.addAll(predators);
        return kids;
    }

    private Predator mutate (Predator pred) {
        Double increase = random.nextDouble() * mutRate;
        if (random.nextBoolean()) {
            increase = -1 * increase;
        }
        pred.setKillRate(pred.getKillRate() * (1 + increase));
        return pred;
    }
}