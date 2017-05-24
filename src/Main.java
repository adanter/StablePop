import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main{


    public static void main(String[] args){

        Metapopulation meta = new Metapopulation(1, 10, 600, .70, .005);
        //Locale locale = new Locale(10, 600, .70);
        Generation generation = new Generation(2, .01);
        File output = new File("log");
        try {
            PrintWriter pw = new PrintWriter(output);
        } catch (FileNotFoundException oops) {
            System.out.println("We just created that file!");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Generation, Pred Pop, Prey Pop, Max Kill Rate, Avg Kill Rate\n");

        for (int i = 0; i < 1000; i++) {
            sb.append(i + ",");
            sb.append("");
            generation.runGeneration(meta.getLocaleAt(0, 0));
            System.out.println("--Round " + (i+1) + "--");
            System.out.println("Pred pop = " + meta.getNumPredsAt(0, 0));
            System.out.println("Prey pop = " + meta.getNumPreyAt(0, 0));
        }
    }
}
