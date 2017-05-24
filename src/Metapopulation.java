import java.util.List;
import java.util.ArrayList;

//whoah... so meta...
public class Metapopulation {
    private List popArray;
    //the total number of populations is actually numPops squared
    public Metapopulation(int numPops, int predPop, int preyPop, double predMortality, double predKillRate) {
        
        this.popArray = new ArrayList<ArrayList<Locale>>(numPops);
        
        for (int i = 0; i < numPops; i++){
            this.popArray.add(i, new ArrayList<Locale>(numPops));
            for (int j = 0; j < numPops; j++){
                this.popArray.get(i).add(j, new Locale(predPop, preyPop, 
                                         predMortality, predKillRate));
            }
        }
    }
    
    public Locale getLocaleAt(int x, int y) {
        return this.popArray.get(x).get(y);
    }
    
    public int getNumPreyAt(int x, int y) {
        return getLocaleAt(x, y).getBasePrey();
    }
    
    public int getNumPredsAt(int x, int y) {
        return getLocaleAt(x, y).getPredList().size();
    }
}