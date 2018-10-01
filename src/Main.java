import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main{
    public static void main(String[] args){

        ////////////////////////////////////////////////////////////////////////
        ///
        /// Experimental Parameters
        ///
        ////////////////////////////////////////////////////////////////////////

        /*
         * Set basic testing parameters.
         * testName is the name of the files that results will be written to.  This includes a testName.txt file 
         * documenting experimental parameters and a testName.csv file containing results.
         * Setting a random seed is important for replicating results.
         */
        String testName = "output";
        Random random = new Random();
        long randomSeed = random.nextLong();
        random.setSeed(randomSeed);

        /*
         * Set test duration.  2000 generations is usually a good start to see if an experiment has potential, and 
         * 20000 is best for making sure that a test really is stable.
         * Do note that this simulation can take several minutes to run.
         */
        int numberOfGenerations = 2000;

        /*
         * Set metapopulation size.
         * The metapopulation is a rectangular grid of locales, each with their own predator and prey population. 
         * xDimension and yDimension specify the size of this grid.  A 1x1 metapopulation is ideal for tests which do
         * not involve migration.
         */
        int xDimension = 3;
        int yDimension = 2;

        /*
         * Set initial population sizes for each locale within the metapopulation.
         * Normally the populations will reach an equilibrium (or not) on their own, so these parameters aren't the
         * most important.
         */
        int startingPredators = 50;
        int startingPrey = 10000;

        /*
         * Set migration rates.
         * Migration is allowed on a locale-by-locale basis.  When emigration is allowed for a locale, each predator
         * in the locale has a set chance to migrate to a random neighboring locale.  For prey, a fixed portion of the
         * population emigrates to a random neighboring locale.
         */
        // Chance that each locale will allow any of its predators or prey to move to a new locale during each 
        // generation
        double emigrationAllowed = 0.3;
        // Chance that an individual predator will move, if permitted by its locale
        double individualEmigrationRate = .01;
        // Portion of the prey population that will migrate, if permitted by its locale
        double preyMigration = 0.01;

        /*
         * Set starting kill rate range for predators.
         * A predator's kill rate is its overall hunting ability, represented as the chance that the predator will kill
         * each of the prey within its locale.  Thus, a predator with a kill rate of .5 would be expected to kill half
         * of the prey within its locale on its own.
         * These bounds set the range of kill rates for the first generation of predators, but have no effect after
         * that point.
         */
        double lowerKillRateBound = .001;
        double upperKillRateBound = .005;

        /*
         * Set predator mutation rate.
         * After the first generation, all predators' kill rates are adjusted up or down by a random proportion up to
         * the mutationRate.  This simulates random genetic changes independent of sexual reproduction.
         */
        double mutationRate = .01;

        /* 
         * Set growth rates and limiters for prey.
         */
        // Factor by which prey populations in each locale are multiplied every generation
        double preyGrowthRate = 1.3;
        // Prey population cap for each locale
        int maxNumberOfPrey = 100000;

        /*
         * Set growth rates and limiters for predators.
         */
        // Number of children a predator will have for each kill it makes in a generation
        double predGrowthRate = .005;
        // Maximum number of offspring per predator.  A mating couple can have twice as many offspring.
        int maxChildrenPerPredator = 50;
        // Chance that each predator will die after reproductive phase each generation
        double predMortalityRate = .3;




        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///
        /// Execute simulation
        ///
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
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
            meta.migrate(emigrationAllowed, individualEmigrationRate, preyMigration);
        }




        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///
        /// Log results
        ///
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        FileWriter fileWriter = null;
        BufferedWriter bw = null;

        // Format output file for different operating systems.
        String os = System.getProperty("os.name");
        String lineEnding = "\n";
        if(os.contains("Windows")) {
            lineEnding = "\r" + lineEnding;
        }


        /*
         * Write experimental parameters to a .txt file
         */

        String params = "" +
                "random seed:           " + randomSeed + lineEnding +
                "number of generations: " + numberOfGenerations + lineEnding +
                "x dimension:           " + xDimension + lineEnding +
                "y dimension:           " + yDimension + lineEnding +
                "starting preds:        " + startingPredators + lineEnding +
                "starting prey:         " + startingPrey + lineEnding +
                "lower KR bound:        " + lowerKillRateBound + lineEnding +
                "upper KR bound:        " + upperKillRateBound + lineEnding +
                "prey growth rate:      " + preyGrowthRate + lineEnding +
                "pred growth rate:      " + predGrowthRate + lineEnding +
                "prey population cap:   " + maxNumberOfPrey + lineEnding +
                "pred child cap:        " + maxChildrenPerPredator + lineEnding +
                "pred mortality rate:   " + predMortalityRate + lineEnding +
                "mutation rate:         " + mutationRate + lineEnding +
                "emigration chance:     " + emigrationAllowed + lineEnding +
                "emigration rate:       " + individualEmigrationRate + lineEnding +
                "prey migration rate:   " + preyMigration;

        System.out.println(params);


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


        /*
         * Write experimental results to a .csv file
         */

        String results = "";
        for (int x = 0; x < meta.getxDimension(); x++) {
            for (int y = 0; y < meta.getyDimension(); y++) {
                results += "Locale " + x + " " + y + ", " + lineEnding;
                results += meta.getLocaleAt(x, y).toString();
            }
        }

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
    }
}
