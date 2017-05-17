public class Predator{
    //how good the predator is at acquiring food
    private int killRate;
    
    public Predator(int initRate){
        killRate = initRate;
    }
    
    public int getKillRate(){
        return killRate;
    }
}