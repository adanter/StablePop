import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main{


    public static void main(String[] args){
        File output = new File("log");
        try {
            PrintWriter pw = new PrintWriter(output);
        } catch (FileNotFoundException oops) {
            System.out.println("We just created that file!");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Generation, Pred Pop, Prey Pop, Max Kill Rate, Avg Kill Rate\n");

        Locale locale = new Locale(100, 2500, .25, .01);
        Generation generation = new Generation(2,.01);
        for (int i = 0; i < 1000; i++) {
            sb.append(i + ",");
            sb.append("");
            generation.runGeneration(locale);
            System.out.println("--Round " + (i+1) + "--");
            System.out.println("Pred pop = " + locale.getPredList().size());
            System.out.println("Prey pop = " + locale.getBasePrey());
        }
    }
}
