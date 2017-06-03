import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main{
    public static void main(String[] args){
        Metapopulation meta = new Metapopulation(4, 4, 10, 2000, 0.009, .011);
        Generation generation = new Generation(1.3, .005, 100000, 50, .5, .5);

        for (int i = 1; i <= 2000; i++) {
            System.out.println(i);
            for (int x = 0; x < meta.getxDimension(); x++){
                for (int y = 0; y < meta.getyDimension(); y++){
                    generation.runGeneration(meta.getLocaleAt(x, y));
                }
            }
            meta.migrate(.05, .02);
        }

        String output = "";

        for (int x = 0; x < meta.getxDimension(); x++) {
            for (int y = 0; y < meta.getyDimension(); y++) {
                output += "Locale " + x + " " + y + ", \n";
                output += meta.getLocaleAt(x, y).toString();
            }
        }

        FileWriter fileWriter = null;
        BufferedWriter bw = null;

        try {
            fileWriter = new FileWriter("output.csv");
            bw = new BufferedWriter(fileWriter);
            bw.write(output);
        } catch (IOException oops){
            oops.printStackTrace();
        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fileWriter != null)
                    fileWriter.close();

            } catch (IOException darn) {
                darn.printStackTrace();
            }

        }
    }
}
