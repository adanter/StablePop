import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main{
    public static void main(String[] args){
        //numPops is actually the sqrt of the total number of locales!
        int numPops = 3;
        Metapopulation meta = new Metapopulation(numPops, 10, 2000, .70, .005);
        Generation generation = new Generation(2, .008, .1);

        for (int i = 1; i <= 500; i++) {
            for (int x = 0; x < meta.getArrayWidth(); x++){
                for (int y = 0; y < meta.getArrayWidth(); y++){
                    generation.runGeneration(meta.getLocaleAt(x, y), i);
                }
            }
            meta.migrate(.1);
        }

        String output = "";

        for (int x = 0; x < meta.getArrayWidth(); x++) {
            for (int y = 0; y < meta.getArrayWidth(); y++) {
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
