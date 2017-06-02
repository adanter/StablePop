import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main{
    public static void main(String[] args){
        Metapopulation meta = new Metapopulation(3 , 3, 10, 2000, .01, .02);
        Generation generation = new Generation(2, .008, 100000, 50, .7,.1);

        for (int i = 1; i <= 500; i++) {
            for (int x = 0; x < meta.getxDimension(); x++){
                for (int y = 0; y < meta.getyDimension(); y++){
                    generation.runGeneration(meta.getLocaleAt(x, y));
                }
            }
            meta.migrate(.1, .01);
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
