import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main{
    public static void main(String[] args){
        Random random = new Random();

        //Set general run parameters:
        String testName = "output";
        long randomSeed = random.nextLong();
        random.setSeed(randomSeed);
        int numberOfGenerations = 2000;

        //Set metapopulation dimensions:
        int xDimension = 3;
        int yDimension = 2;

        //Set initial population sizes for each locale within the metapopulation
        int startingPredators = 50;
        int startingPrey = 2000;

        //Set initial kill rates for predators
        double lowerKillRateBound = .005;
        double upperKillRateBound = .005;

        //Set growth rates and limiters
        double preyGrowthRate = 1.3;
        double predGrowthRate = .005;
        int maxNumberOfPrey = 100000;
        int maxChildrenPerPredator = 50;
        double predMortalityRate = .3;

        //Set genetic factors
        double mutationRate = 0;

        //Set chance that a) a locale will allow emigration and b) when their locale allows it, predators will emigrate
        double emigrationAllowed = 0;
        double individualEmigrationRate = .01;

        Metapopulation meta = new Metapopulation(xDimension, yDimension, startingPredators, startingPrey,
                lowerKillRateBound, upperKillRateBound, random);
        Generation generation = new Generation(preyGrowthRate, predGrowthRate, maxNumberOfPrey, maxChildrenPerPredator,
                predMortalityRate, mutationRate, random);

        for (int i = 1; i <= numberOfGenerations; i++) {
            System.out.println(i);
            for (int x = 0; x < meta.getxDimension(); x++){
                for (int y = 0; y < meta.getyDimension(); y++){
                    generation.runGeneration(meta.getLocaleAt(x, y));
                }
            }

            meta.migrate(emigrationAllowed, individualEmigrationRate);
        }





        /*
         * Write everything to output files: a .csv for results and a .txt for parameters
         */
        String results = "";
        for (int x = 0; x < meta.getxDimension(); x++) {
            for (int y = 0; y < meta.getyDimension(); y++) {
                results += "Locale " + x + " " + y + ", \n";
                results += meta.getLocaleAt(x, y).toString();
            }
        }

        String params = "" +
                "random seed:           " + randomSeed + "\n" +
                "number of generations: " + numberOfGenerations + "\n" +
                "x dimension:           " + xDimension + "\n" +
                "y dimension:           " + yDimension + "\n" +
                "starting preds:        " + startingPredators + "\n" +
                "starting prey:         " + startingPrey + "\n" +
                "lower KR bound:        " + lowerKillRateBound + "\n" +
                "upper KR bound:        " + upperKillRateBound + "\n" +
                "prey growth rate:      " + preyGrowthRate + "\n" +
                "pred growth rate:      " + predGrowthRate + "\n" +
                "prey population cap:   " + maxNumberOfPrey + "\n" +
                "pred child cap:        " + maxChildrenPerPredator + "\n" +
                "pred mortality rate:   " + predMortalityRate + "\n" +
                "mutation rate:         " + mutationRate + "\n" +
                "emigration chance:     " + emigrationAllowed + "\n" +
                "emigration rate:       " + individualEmigrationRate;

        FileWriter fileWriter = null;
        BufferedWriter bw = null;

        try {
            fileWriter = new FileWriter(testName + ".csv");
            bw = new BufferedWriter(fileWriter);
            bw.write(results);

        } catch (IOException oops){
            oops.printStackTrace();
        } finally {
            try {
                bw.close();
                fileWriter.close();
            } catch (IOException darn) {
                darn.printStackTrace();
            }
        }

        try {
            fileWriter = new FileWriter(testName + ".txt");
            bw = new BufferedWriter(fileWriter);
            bw.write(params);

        } catch (IOException oops){
            oops.printStackTrace();
        } finally {
            try {
                bw.close();
                fileWriter.close();
            } catch (IOException darn) {
                darn.printStackTrace();
            }
        }



    }
}
