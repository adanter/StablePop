//For each pred:
//          Use pred's kill rate to determine how much it eats.
//          Record this value with the pred.
//          Decrement the prey population by this amount.
//        Group the preds into random breeding pairs.
//        Find the number of children for each breeding pair using the fitness/reproduction function from Ito et al.
//        Use crossover to create children for each breeding pair, then apply mutation to the offspring.
//        Shuffle the list of offspring.
//        Multiply the prey population by its growth constant.


public class Generation{
    private Random random;
    private Locale locale;

    public Generation() {
        this.random = new Random();
    }
    
    public void runGeneration(Locale newLoc){
        this.locale = newLoc;
        for (Predator pred : locale.getPredList()){
            pred.setKills(hunt(pred));
        }
    }
    
    //figures out number of kills
    //returns the number of kills
    //side-effect: reduces the base prey pop by number of kills
    private int hunt(Predator pred){
        int killCount = 0;
        for (int i = 0; i < locale.getBasePrey(); i++){
            if (random.nextFloat() <= pred.getKillRate()){
                killCount++;
            }
        }
        locale.reduceBasePrey(killCount);
        return killCount;
    }
}