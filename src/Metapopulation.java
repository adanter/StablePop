/**
 * A metapopulation is an array of individual locales, each with their own
 * predator and prey populations.  Functions within Metapopulation allow 
 * individuals to move from one population to neighboring populations.  This 
 * allows for some genetic mixing, but more importantly allows decimated
 * locales to be repopulated.
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Metapopulation {
    private List<List<Locale>> popArray;
    private int xDimension;
    private int yDimension;
    private Random random;

    /**
     * Constructor for a metapopulation
     * @param xDimension Width of the metapopulation
     * @param yDimension Height of the metapopulation
     * @param predPop Starting number of predators for each location
     * @param preyPop Starting number of prey for each location
     * @param lowerKillRate Lower bound for starting kill rate for predators
     * @param upperKillRate Upper bound for starting kill rate for predators
     * @param random Random for use in math
     */
    public Metapopulation(int xDimension, int yDimension, int predPop, 
        int preyPop, double lowerKillRate, double upperKillRate, 
        Random random) {

        this.xDimension = xDimension;
        this.yDimension = yDimension;
        this.popArray = new ArrayList<>(xDimension);
        double killRateRange = upperKillRate - lowerKillRate;
        double newKillRate;
        this.random = random;
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
     * Gives each locale a chance to allow emigration of its predators and prey
     * to an adjacent locale in the grid, then moves predators and prey
     * accordingly.
     *
     * "Adjacent" in this case means straight lines only, not diagonals.  Also,
     * the grid "wraps", so a locale on the far left side counts as adjacent to
     * its counterpart on the far right side, and so on.
     *
     * @param migrationRate Chance that migration will occur any given locale
     * @param migrationChance Chance that any given predator will migrate when
     *                        its locale allows
     * @param preyMigrationChance Fraction of prey that will migrate when the
     *                            locale allows
     */
    public void migrate(double migrationRate, double migrationChance, double preyMigrationChance){
        Locale sourceLoc;
        Locale destLoc;
        // Coordinates for destination locale
        int x2;
        int y2;
        for (int x = 0; x < xDimension; x++){
            for (int y = 0; y < yDimension; y++){
                if (random.nextFloat() < migrationRate){
                    sourceLoc = getLocaleAt(x, y);
                    int direction = random.nextInt(4);
                    switch (direction) {
                        // East
                        case 0:
                            x2 = Math.floorMod(x+1, xDimension);
                            y2 = y;
                            break;
                        // North
                        case 1:
                            x2 = x;
                            y2 = Math.floorMod(y+1, yDimension);
                            break;
                        // West
                        case 2:
                            x2 = Math.floorMod(x-1, xDimension);
                            y2 = y;
                            break;
                        // South
                        default:
                            x2 = x;
                            y2 = Math.floorMod(y-1, yDimension);
                            break;
                    }
                    destLoc = getLocaleAt(x2, y2);
                    
                    int max = sourceLoc.getNumPreds();
                    for (int predIndex = 0; predIndex < max; predIndex++){
                        if (random.nextFloat() < migrationChance){
                            destLoc.addPred(sourceLoc.popPred(predIndex));
                            max--;
                        }
                    }
                    if (preyMigrationChance > 0) {
                        int preyTransfer = (int)(sourceLoc.getNumPrey() 
                            * preyMigrationChance);
                        sourceLoc.reduceBasePrey(preyTransfer);
                        destLoc.setNumPrey(destLoc.getNumPrey() + preyTransfer);
                    }
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
        for (int x = 0; x < xDimension; x++) {
            for (int y = 0; y < yDimension; y++) {
                totalPrey += getLocaleAt(x,y).getNumPrey();
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
        for (int x = 0; x < xDimension; x++) {
            for (int y = 0; y < yDimension; y++) {
                totalPreds += getLocaleAt(x,y).getNumPreds();
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