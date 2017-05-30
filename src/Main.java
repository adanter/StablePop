import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main{


    public static void main(String[] args){

        Metapopulation meta = new Metapopulation(4, 10, 2000, .70, .005);
        //Locale locale = new Locale(10, 600, .70);
        Generation generation = new Generation(2, .008);
        File output = new File("log");
        try {
            PrintWriter pw = new PrintWriter(output);
        } catch (FileNotFoundException oops) {
            System.out.println("We just created that file!");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Generation, Pred Pop, Prey Pop, Max Kill Rate, Avg Kill Rate\n");

        for (int i = 0; i < 150; i++) {
            sb.append(i + ",");
            sb.append("");
            System.out.println("\n--Round " + (i+1) + "--");
            for (int x = 0; x < meta.getArrayWidth(); x++){
                for (int y = 0; y < meta.getArrayWidth(); y++){
                    generation.runGeneration(meta.getLocaleAt(x, y));
                    System.out.println("[" + x + ", " + y + "]");
                    System.out.println("Pred pop = " + meta.getNumPredsAt(x, y));
                    System.out.println("Prey pop = " + meta.getNumPreyAt(x, y));
                    meta.migrate(.05);
                }
            }
        }
    }
}
