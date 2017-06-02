/**
 * A metapopulation is an array of individual locations, each with their own predator and prey populations.  Functions
 * within Metapopulation allow individuals to move from one population to neighboring populations, allowing for some
 * genetic mixing but mostly allowing decimated locations to be repopulated by new colonists.
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Metapopulation {
    private List<List<Locale>> popArray;
    private int xDimension;
    private int yDimension;

    /**
     * Constructor for a metapopulation
     * @param xDimension Width of the metapopulation
     * @param yDimension Height of the metapopulation
     * @param predPop Starting number of predators for each location
     * @param preyPop Starting number of prey for each location
     * @param lowerKillRate Lower bound for starting kill rate for predators.  Technically, the order in which the two
     *                      bounds are passed does not matter, since the math will produce the same result either way.
     * @param upperKillRate Upper bound for starting kill rate for predators
     */
    public Metapopulation(int xDimension, int yDimension, int predPop, int preyPop, double lowerKillRate, double upperKillRate) {
        this.xDimension = xDimension;
        this.yDimension = yDimension;
        this.popArray = new ArrayList<>(xDimension);
        double killRateRange = upperKillRate - lowerKillRate;
        double newKillRate;
        Random random = new Random();
        for (int i = 0; i < xDimension; i++){
            ArrayList<Locale> localeList = new ArrayList<>(yDimension);
            for (int j = 0; j < yDimension; j++){
                newKillRate = lowerKillRate + (random.nextDouble() * killRateRange);
                localeList.add(j, new Locale(predPop, preyPop, newKillRate));
            }
            this.popArray.add(i, localeList);
        }
    }

    /**
     * Chooses two locations at random (possibly the same location) and moves a predator from one to the other, if the
     * former has any predators
     * @param migrationRate Chance that the function will do anything
     */
    public void migrate(double migrationRate, double migrationChance){
        Random random = new Random();
        for (int x = 0; x < xDimension; x++){
            for (int y = 0; y < yDimension; y++){
                if (random.nextFloat() < migrationRate){
                    Locale srcLoc = getLocaleAt(x, y);
                    
                    int x2 = Math.floorMod((x + (random.nextInt(xDimension) - 1)) , xDimension);
                    int y2 = Math.floorMod((y + (random.nextInt(yDimension) - 1)) , yDimension);
                    Locale newLoc = getLocaleAt(x2, y2);
                    
                    int max = srcLoc.getPredList().size();  
                    for (int i = 0; i < max; i++){
                        if (random.nextFloat() < migrationChance){
                            newLoc.addPred(srcLoc.popPred(i));
                            max--;
                        }
                    }
                    
//                    if (getLocaleAt(x, y).getPredList().size() > 0){
//                        System.out.print(getLocaleAt(x2, y2).getNumPreds() + " - ");
//                        getLocaleAt(x2, y2).addPred(getLocaleAt(x, y).popPred());
//                        System.out.println(getLocaleAt(x2, y2).getNumPreds());
//                    }
                }
            }
        }
    }

    /**
     * Returns a single location within the metapopulation
     * @param x Horizontal coordinate of the location
     * @param y Vertical coordinate of the location
     * @return Location at (x,y)
     */
    public Locale getLocaleAt(int x, int y) {
        return this.popArray.get(x).get(y);
    }

    /**
     * Getter for the width of the metapopulation
     * @return Width of population
     */
    public int getxDimension() {
        return xDimension;
    }

    /**
     * Getter for the height of the metapopulation
     * @return Height of the population
     */
    public int getyDimension() {
        return yDimension;
    }

    /**
     * Returns the size of the prey population at the specified coordinates.
     * @param x Horizontal coordinate of the target location
     * @param y Vertical coordinate of the target location
     * @return int representing the number of prey at the target location
     */
    public int getNumPreyAt(int x, int y) {
        return getLocaleAt(x, y).getNumPrey();
    }

    /**
     * Returns the size of the predator population at the specified coordinates.
     * @param x Horizontal coordinate of the target location
     * @param y Vertical coordinate of the target location
     * @return int representing the number of predators at the target location
     */
    public int getNumPredsAt(int x, int y) {
        return getLocaleAt(x, y).getNumPreds();
    }

    /**
     * Finds the total number of prey across the metapopulation.
     * @return int representing the total number of prey
     */
    public int getTotalPrey() {
        int totalPrey = 0;
        for (int i = 0; i < xDimension; i++) {
            for (int j = 0; i < yDimension; i++) {
                totalPrey += getLocaleAt(i,j).getNumPrey();
            }
        }
        return totalPrey;
    }

    /**
     * Finds the total number of predators across the metapopulation.
     * @return int representing the total number of predators
     */
    public int getTotalPreds() {
        int totalPreds = 0;
        for (int i = 0; i < xDimension; i++) {
            for (int j = 0; i < yDimension; i++) {
                totalPreds += getLocaleAt(i,j).getNumPreds();
            }
        }
        return totalPreds;
    }

    /**
     * Finds the highest kill rate of any predator in the metapopulation
     * @return double representing the highest kill rate
     */
    public double getMaxKillRate() {
        double maxKillRate = 0;
        for (int x = 0; x < xDimension; x++) {
            for (int y = 0; y < yDimension; y++) {
                double localMax =  getLocaleAt(x, y).getMaxKillRate();
                if (localMax > maxKillRate) {
                    maxKillRate = localMax;
                }
            }
        }
        return maxKillRate;
    }
}