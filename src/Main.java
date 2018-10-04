import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main{
    ////////////////////////////////////////////////////////////////////////////
    ///
    /// Experimental Parameters
    ///
    ////////////////////////////////////////////////////////////////////////////

    /*
     * Set name for output files.
     *
     * testName is the name of the files that results will be written to.  This
     * includes a testName.txt file documenting experimental parameters and a 
     * testName.csv file containing results.
     */
    private String testName = "output";

    /*
     * Set test duration.  
     *
     * 2000 generations is a good length for a preliminary run, but 20000 
     * generations is a better length to ensure that a set of parameters truly
     * is stable.
     */
    private int numberOfGenerations = 20;

    /*
     * Set metapopulation size.
     *
     * The metapopulation is a rectangular grid of locales, each with their own 
     * predator and prey population. xDimension and yDimension specify the size
     * of this grid.  A 1x1 metapopulation is ideal for tests which do not 
     * involve migration.
     */
    private int xDimension = 3;
    private int yDimension = 2;

    /*
     * Set initial population sizes for each locale.
     *
     * Normally the populations will reach equilibrium or extinction on their 
     * own, so these values aren't the most important, but an initial imbalance
     * in population sizes can cause the simulation to take longer to stabilize.
     *
     * Again, these are the starting populations for each locale, not the
     * overall metapopulation.
     */
    private int startingPredators = 50;
    private int startingPrey = 10000;

    /*
     * Set migration rates.
     *
     * Migration is allowed on a locale-by-locale basis.  When emigration is 
     * allowed for a locale, each predator in the locale has a set chance to
     * migrate to a random neighboring locale.  For prey, a fixed portion of the
     * population emigrates to a random neighboring locale.
     *
     * emigrationAllowed: Chance that each locale will allow any of its 
     *  predators or prey to move to a new locale during each generation
     *
     * individualEmigrationRate:  Chance that each individual predator will move
     *  if its locale currently allows emigration
     *
     * preyMigration:  Fraction of the prey population that will migrate if its
     *  locale currently allows emigration
     */
    private double emigrationAllowed = 0.3;
    private double individualEmigrationRate = .01;
    private double preyMigration = 0.01;

    /*
     * Set starting kill rate range for predators.
     * 
     * A predator's kill rate is its overall hunting ability, represented as the
     * chance that the predator will kill each of the prey within its locale.
     * Thus, a predator with a kill rate of .5 would be expected to kill half
     * of the prey within its locale on its own.
     *
     * These bounds set the range of kill rates for the first generation of 
     * predators but are not referenced after that point.
     */
    private double lowerKillRateBound = .001;
    private double upperKillRateBound = .005;

    /*
     * Set predator mutation rate.
     *
     * After the first generation, all predators' kill rates are adjusted up or
     * down by a random proportion up to the mutationRate.  This simulates
     * random genetic changes independent of sexual reproduction.  
     *
     * Without any mutation, populations in artificial life experiments tend to
     * lose diversity over time.  For this reason, it is usually good to include
     * at least a small mutation rate in a simulation.
     */
    private double mutationRate = .01;

    /* 
     * Set growth rates and limiters for prey.
     *
     * preyGrowthRate: Each generation, the surviving prey population in each
     *  locale is multiplied by this number.
     *
     * maxNumberOfPrey: Prey population cap for each locale.  This is used both
     *  to keep the simulation from bogging down and to prevent int cap
     *  overflow.
     */
    private double preyGrowthRate = 1.3;
    private int maxNumberOfPrey = 100000;

    /*
     * Set growth rates and limiters for predators.
     *
     * predGrowthRate:  Conversion factor between a predator's kills during
     *  a generation and its number of offspring
     *
     * maxChildrenPerPredator:  Caps the maximum number of children each
     *  predator can have during a generation.  Couples can have up to twice
     *  this many offspring.
     *
     * predMortalityRate:  Proportion of the predator population that will die
     *  at the end of each generation (rounded up)
     */
    private double predGrowthRate = .005;
    private int maxChildrenPerPredator = 50;
    private double predMortalityRate = .3;


    ////////////////////////////////////////////////////////////////////////////
    ///
    /// Core Functionality
    ///
    ////////////////////////////////////////////////////////////////////////////

    /*
     * Using a specific random seed allows results to be replicated, which is 
     * good practice for science.
     */
    private long randomSeed;

    /*
     * The simulation itself is handled by a metapopulation object
     */
    private Metapopulation meta;

    /*
     * Writers used in generating output
     */
    private FileWriter fileWriter = null;
    private BufferedWriter bw = null;

    /*
     * Since different operating systems use different line endings, the correct
     * ending for the current OS is stored as a class variable.
     */
    private String lineEnding;

    public Main() {
        // Set random seed and create a random to pass to a generation and a
        // metapopulation
        Random random = new Random();
        this.randomSeed = random.nextLong();
        random.setSeed(this.randomSeed);

        // Use starting population params to construct a PopulationStart data
        // transfer object
        PopulationStart popStart = new PopulationStart(
            this.startingPredators,
            this.startingPrey,
            this.lowerKillRateBound,
            this.upperKillRateBound
        );

        // Use migration params to construct a MigrationPattern data transfer 
        // object
        MigrationPattern migration = new MigrationPattern(
            this.emigrationAllowed, 
            this.individualEmigrationRate, 
            this.preyMigration
        );

        // Instantiate a generation
        Generation generation = new Generation(
            this.preyGrowthRate,
            this.predGrowthRate,
            this.maxNumberOfPrey,
            this.maxChildrenPerPredator,
            this.predMortalityRate,
            this.mutationRate,
            random
        );

        // Instantiate metapopulation
        this.meta = new Metapopulation(
            this.xDimension, 
            this.yDimension,
            popStart,
            migration,
            random,
            generation
        );

        // Set the right line ending for use in output files
        String os = System.getProperty("os.name");
        if(os.contains("Windows")) {
            this.lineEnding = "\r\n";
        }
        else {
            this.lineEnding = "\n";
        }
    }

    /**
     * Executes the simulation and writes parameters and results to output files
     */
    public void executeSimulation() {
        // Run simulation
        this.meta.runSimulation(this.numberOfGenerations);

        // Generate output files
        outputParameters();
        outputResults();
    }

    /**
     * Writes experimental parameters to a .txt file
     */
    private void outputParameters() {
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

        try {
            fileWriter = new FileWriter(testName + ".txt");
            bw = new BufferedWriter(fileWriter);
            bw.write(params);

        } catch (IOException writeException){
            writeException.printStackTrace();

        } finally {
            try {
                bw.close();
                fileWriter.close();

            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
        }
    }

    /**
     * Writes a generation-by-generation simulation log to a .csv file
     */
    private void outputResults() {
        // Since each locale logs its own history, each locale's log
        // is added to the output sequentially.
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

        } catch (IOException writeException){
            writeException.printStackTrace();

        } finally {
            try {
                bw.close();
                fileWriter.close();

            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    ///
    /// Main Function
    ///
    ////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args){
        Main main = new Main();
        main.executeSimulation();        
    }
}
