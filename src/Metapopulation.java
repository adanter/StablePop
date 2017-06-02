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
     * @param predKillRate Starting kill rate for predators
     *                     TODO: Instead of a flat start kill rate, pass a min and a max and assign randomly
     */
    public Metapopulation(int xDimension, int yDimension, int predPop, int preyPop, double predKillRate) {
        this.xDimension = xDimension;
        this.yDimension = yDimension;
        this.popArray = new ArrayList<>(xDimension);
        for (int i = 0; i < xDimension; i++){
            ArrayList<Locale> localeList = new ArrayList<>(yDimension);
            for (int j = 0; j < yDimension; j++){
                localeList.add(j, new Locale(predPop, preyPop, predKillRate));
            }
            this.popArray.add(i, localeList);
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
     * Chooses two locations at random (possibly the same location) and moves a predator from one to the other, if the
     * former has any predators
     * @param migrationRate Chance that the function will do anything
     */
    public void migrate(double migrationRate){
        Random random = new Random();
        for (int x = 0; x < xDimension; x++){
            for (int y = 0; y < yDimension; y++){
                if (random.nextFloat() < migrationRate){
                    int x2 = Math.floorMod((x + (random.nextInt(xDimension) - 1)) , xDimension);
                    int y2 = Math.floorMod((y + (random.nextInt(yDimension) - 1)) , yDimension);
                    if (getLocaleAt(x, y).getPredList().size() > 0){
                        System.out.print(getLocaleAt(x2, y2).getNumPreds() + " - ");
                        getLocaleAt(x2, y2).addPred(getLocaleAt(x, y).popPred());
                        System.out.println(getLocaleAt(x2, y2).getNumPreds());
                    }
                }
            }
        }
    }

    public int getNumPreyAt(int x, int y) {
        return getLocaleAt(x, y).getNumPrey();
    }

    public int getNumPredsAt(int x, int y) {
        return getLocaleAt(x, y).getPredList().size();
    }

    public int getTotalPrey() {
        int totalPrey = 0;
        for (int i = 0; i < xDimension; i++) {
            for (int j = 0; i < yDimension; i++) {
                totalPrey += getLocaleAt(i,j).getNumPrey();
            }
        }
        return totalPrey;
    }

    public int getTotalPreds() {
        int totalPreds = 0;
        for (int i = 0; i < xDimension; i++) {
            for (int j = 0; i < yDimension; i++) {
                totalPreds += getLocaleAt(i,j).getNumPreds();
            }
        }
        return totalPreds;
    }

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