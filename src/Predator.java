public class Predator{
    //how good the predator is at acquiring food
    private double killRate;
    private int kills;
    //private int age;

    public Predator(double initRate){
        this.killRate = initRate;
    }
    
    public double getKillRate(){
        return killRate;
    }

    public void setKills(int numKills) {
        this.kills = numKills;
    }

    public int getKills() {
        return kills;
    }
}