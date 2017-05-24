import java.util.List;
import java.util.ArrayList;


//whoah... so meta...
public class Metapopulation {
    private List<List<Locale>> popArray;
    //the total number of populations is actually numPops squared
    public Metapopulation(int numPops, int predPop, int preyPop, double predMortality, double predKillRate) {

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
}