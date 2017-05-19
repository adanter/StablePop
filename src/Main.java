public class Main{
    public static void main(String[] args){
        System.out.println("hi");
        Locale locale = new Locale(10, 600, .25);
        Generation generation = new Generation(1.4);
        for (int i = 0; i < 50; i++) {
            generation.runGeneration(locale);
            System.out.println("--Round " + (i+1) + "--");
            System.out.println("Pred pop = " + locale.getPredList().size());
            System.out.println("Prey pop = " + locale.getBasePrey());
        }
    }
}
