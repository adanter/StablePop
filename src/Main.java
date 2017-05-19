public class Main{
    public static void main(String[] args){
        System.out.println("hi");
        Locale locale = new Locale(10,200);
        Generation generation = new Generation(1.5);
        for (int i = 0; i < 50; i++) {
            generation.runGeneration(locale);
            System.out.println("--Round " + (i+1) + "--");
            System.out.println("Pred pop = " + locale.getPredList().size());
            System.out.println("Prey pop = " + locale.getBasePrey());
        }
    }
}
