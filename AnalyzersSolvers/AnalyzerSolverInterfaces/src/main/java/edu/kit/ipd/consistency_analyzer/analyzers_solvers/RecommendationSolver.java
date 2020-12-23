package edu.kit.ipd.consistency_analyzer.analyzers_solvers;

import edu.kit.ipd.consistency_analyzer.datastructures.IModelExtractionState;
import edu.kit.ipd.consistency_analyzer.datastructures.IRecommendationState;
import edu.kit.ipd.consistency_analyzer.datastructures.ITextExtractionState;

/**
 * A solver that creates recommendations.
 *
 * @author Sophie
 *
 */
public abstract class RecommendationSolver extends Solver implements IRecommendationSolver {

	protected ITextExtractionState textExtractionState;
	protected IModelExtractionState modelExtractionState;
	protected IRecommendationState recommendationState;

	/**
	 * Creates a new solver.
	 *
	 * @param dependencyType       the dependencies of the analyzer
	 * @param graph                the PARSE graph to look up
	 * @param textExtractionState  the text extraction state to look up
	 * @param modelExtractionState the model extraction state to look up
	 * @param recommendationState  the model extraction state to work with
	 */
	protected RecommendationSolver(//
			DependencyType dependencyType, ITextExtractionState textExtractionState, //
			IModelExtractionState modelExtractionState, IRecommendationState recommendationState) {
		super(dependencyType);
		this.textExtractionState = textExtractionState;
		this.modelExtractionState = modelExtractionState;
		this.recommendationState = recommendationState;
	}

	@Override
	public abstract void exec();
}
