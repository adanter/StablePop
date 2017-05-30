import java.util.List;
import java.util.ArrayList;
import java.util.Random;


//whoah... so meta...
public class Metapopulation {
    private List<List<Locale>> popArray;
    private int xDimension;
    private int yDimension;

    //the total number of populations is actually numPops squared
    public Metapopulation(int numPops, int predPop, int preyPop, double predMortality, double predKillRate) {
        this.xDimension = numPops;
        this.yDimension = numPops;
        this.popArray = new ArrayList<>(numPops);
        for (int i = 0; i < numPops; i++){
            //this.popArray.add(i, new ArrayList<Locale>(numPops));
            ArrayList<Locale> localeList = new ArrayList<>(numPops);
            for (int j = 0; j < numPops; j++){
                localeList.add(j, new Locale(predPop, preyPop,
                                         predMortality, predKillRate));
            }
            this.popArray.add(i, localeList);
        }
    }
    
    public Locale getLocaleAt(int x, int y) {
        return this.popArray.get(x).get(y);
    }
    
    public int getArrayWidth() {
        return popArray.size();
    }

    public int getNumPreyAt(int x, int y) {
        return getLocaleAt(x, y).getBasePrey();
    }

    public int getNumPredsAt(int x, int y) {
        return getLocaleAt(x, y).getPredList().size();
    }
    
    public void migrate(double migrationRate){
        Random random = new Random();
        for (int x = 0; x < popArray.size(); x++){
            for (int y = 0; y < popArray.size(); y++){
                if (random.nextFloat() < migrationRate){
                    int dx = random.nextInt(3) - 1;
                    int dy = random.nextInt(3) - 1;
                    if (0 <= (x + dx) && (x + dx) < popArray.size() && 
                        0 <= (y + dy) && (y + dy) < popArray.size() &&
                       getLocaleAt(x, y).getPredList().size() > 0){
                        //pop a pred from origin locale, add to new locale.
                        getLocaleAt(x+dx, y+dy).addPred(
                            getLocaleAt(x, y).popPred());
                    }
                }
            }
        }

    public int getTotalPrey() {
        int totalPrey = 0;
        for (int i = 0; i < xDimension; i++) {
            for (int j = 0; i < yDimension; i++) {
                totalPrey += getLocaleAt(i,j).getBasePrey();
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