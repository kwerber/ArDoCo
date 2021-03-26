package edu.kit.ipd.consistency_analyzer.agents_extractors;

import java.util.List;
import java.util.Map;

import edu.kit.ipd.consistency_analyzer.agents_extractors.agents.Configuration;
import edu.kit.ipd.consistency_analyzer.common.SystemParameters;

public class GenericTextConfig extends Configuration {

    public static final GenericTextConfig DEFAULT_CONFIG = new GenericTextConfig();

    public final List<String> textExtractors;

    // ArticleTypeNameAnalyzer
    /**
     * The probability of the article type name analyzer.
     */
    public final double articleTypeNameAnalyzerProbability;

    // InDepArcsAnalyzer
    /**
     * The probability of the in dep arcs analyzer.
     */
    public final double inDepArcsAnalyzerProbability;

    // MultiplePartSolver
    /**
     * The probability of the multiple part solver.
     */
    public final double multiplePartSolverProbability;

    // NounAnalyzer
    /**
     * The probability of the noun analyzer.
     */
    public final double nounAnalyzerProbability;

    // OutDepArcsAnalyzer
    /**
     * The probability of the out dep arcs analyzer.
     */
    public final double outDepArcsAnalyzerProbability;

    // SeparatedNamesAnalyzer
    /**
     * The probability of the separated names analyzer.
     */
    public final double separatedNamesAnalyzerProbability;

    private GenericTextConfig() {
        SystemParameters CONFIG = new SystemParameters("/configs/TextAnalyzerSolverConfig.properties", true);
        textExtractors = CONFIG.getPropertyAsList("Extractors");
        articleTypeNameAnalyzerProbability = CONFIG.getPropertyAsDouble("ArticleTypeNameAnalyzer_Probability");
        inDepArcsAnalyzerProbability = CONFIG.getPropertyAsDouble("InDepArcsAnalyzer_Probability");
        multiplePartSolverProbability = CONFIG.getPropertyAsDouble("MultiplePartSolver_Probability");
        nounAnalyzerProbability = CONFIG.getPropertyAsDouble("NounAnalyzer_Probability");
        outDepArcsAnalyzerProbability = CONFIG.getPropertyAsDouble("OutDepArcsAnalyzer_Probability");
        separatedNamesAnalyzerProbability = CONFIG.getPropertyAsDouble("SeparatedNamesAnalyzer_Probability");
    }

    public GenericTextConfig(Map<String, String> configs) {
        textExtractors = getPropertyAsList("Extractors", configs);
        articleTypeNameAnalyzerProbability = getPropertyAsDouble("ArticleTypeNameAnalyzer_Probability", configs);
        inDepArcsAnalyzerProbability = getPropertyAsDouble("InDepArcsAnalyzer_Probability", configs);
        multiplePartSolverProbability = getPropertyAsDouble("MultiplePartSolver_Probability", configs);
        nounAnalyzerProbability = getPropertyAsDouble("NounAnalyzer_Probability", configs);
        outDepArcsAnalyzerProbability = getPropertyAsDouble("OutDepArcsAnalyzer_Probability", configs);
        separatedNamesAnalyzerProbability = getPropertyAsDouble("SeparatedNamesAnalyzer_Probability", configs);

    }

}
