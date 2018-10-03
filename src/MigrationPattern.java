/**
 * Data transfer object for parameters related to migration
 */

public class MigrationPattern {
	private double migrationChance;

	private double predMigRate;

	private double preyMigRate;

	public MigrationPattern(
		double migrationChance, 
		double predMigRate, 
		double preyMigRate
	) {
		this.migrationChance = migrationChance;
		this.predMigRate = predMigRate;
		this.preyMigRate = preyMigRate;
	}

	public double getMigrationChance() {
		return this.migrationChance;
	}

	public double getPredMigrationRate() {
		return this.predMigRate;
	}

	public double getPreyMigrationRate() {
		return this.preyMigRate;
	}
}