public class Main{
    public static void main(String[] args){
        System.out.println("hi");
        Metapopulation meta = new Metapopulation(1, 10, 600, .70, .005);
        //Locale locale = new Locale(10, 600, .70);
        Generation generation = new Generation(1.6);
        for (int i = 0; i < 10000; i++) {
            generation.runGeneration(meta.getLocaleAt(0, 0));
            System.out.println("--Round " + (i+1) + "--");
            System.out.println("Pred pop = " + meta.getNumPredsAt(0, 0));
            System.out.println("Prey pop = " + meta.getNumPreyAt(0, 0));
        }
    }
}
