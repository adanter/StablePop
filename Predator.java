public class Predator{
    //how good the predator is at acquiring food
    private float killRate;

    public Predator(float initRate){
        this.killRate = initRate;
    }
    
    public float getKillRate(){
        return killRate;
    }
}