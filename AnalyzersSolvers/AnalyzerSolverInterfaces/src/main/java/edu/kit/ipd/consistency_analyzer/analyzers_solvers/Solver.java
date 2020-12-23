package edu.kit.ipd.consistency_analyzer.analyzers_solvers;

/**
 * A solver works on the base of its knowledge (its holding states).
 *
 * @author Sophie
 *
 */
public abstract class Solver implements ISolver {

	protected DependencyType type;

	/**
	 * An analyzer can be executed without additional arguments
	 *
	 */
	@Override
	public abstract void exec();

	/**
	 * Creates a new analyzer of the specified type.
	 *
	 * @param type the analyzer type
	 */
	public Solver(DependencyType type) {
		this.type = type;
	}

	/**
	 * Returns the dependency type of the current solver.
	 *
	 * @return the dependency type of the current solver
	 */
	public DependencyType getDependencyType() {
		return type;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
}
