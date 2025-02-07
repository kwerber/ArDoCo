/* Licensed under MIT 2021-2022. */
package edu.kit.kastel.mcse.ardoco.core.recommendationgenerator.extractors;

import java.util.Map;

import edu.kit.kastel.informalin.framework.configuration.Configurable;
import edu.kit.kastel.mcse.ardoco.core.api.agent.AbstractExtractor;
import edu.kit.kastel.mcse.ardoco.core.api.agent.RecommendationAgentData;
import edu.kit.kastel.mcse.ardoco.core.api.data.model.IModelState;
import edu.kit.kastel.mcse.ardoco.core.api.data.recommendationgenerator.IRecommendationState;
import edu.kit.kastel.mcse.ardoco.core.api.data.text.IWord;
import edu.kit.kastel.mcse.ardoco.core.api.data.textextraction.ITextState;
import edu.kit.kastel.mcse.ardoco.core.api.data.textextraction.MappingKind;
import edu.kit.kastel.mcse.ardoco.core.common.util.CommonUtilities;

/**
 * This analyzer searches for name type patterns. If these patterns occur recommendations are created.
 *
 * @author Sophie Schulz
 * @author Jan Keim
 */
public class NameTypeExtractor extends AbstractExtractor<RecommendationAgentData> {

    @Configurable
    private double probability = 1.0;

    /**
     * Creates a new NameTypeAnalyzer
     */
    public NameTypeExtractor() {
        // empty
    }

    @Override
    public void exec(RecommendationAgentData data, IWord word) {
        var textState = data.getTextState();
        for (var model : data.getModelIds()) {
            var modelState = data.getModelState(model);
            var recommendationState = data.getRecommendationState(modelState.getMetamodel());
            addRecommendedInstanceIfNameAfterType(textState, word, modelState, recommendationState);
            addRecommendedInstanceIfNameBeforeType(textState, word, modelState, recommendationState);
            addRecommendedInstanceIfNameOrTypeBeforeType(textState, word, modelState, recommendationState);
            addRecommendedInstanceIfNameOrTypeAfterType(textState, word, modelState, recommendationState);
        }
    }

    /**
     * Checks if the current node is a type in the text extraction state. If the names of the text extraction state
     * contain the previous node. If that's the case a recommendation for the combination of both is created.
     *
     * @param textExtractionState text extraction state
     * @param word                the current word
     */
    private void addRecommendedInstanceIfNameBeforeType(ITextState textExtractionState, IWord word, IModelState modelState,
            IRecommendationState recommendationState) {
        if (textExtractionState == null || word == null) {
            return;
        }

        var similarTypes = CommonUtilities.getSimilarTypes(word, modelState);

        if (!similarTypes.isEmpty()) {
            textExtractionState.addNounMapping(word, MappingKind.TYPE, this, probability);

            var nameMappings = textExtractionState.getMappingsThatCouldBeOfKind(word.getPreWord(), MappingKind.NAME);
            var typeMappings = textExtractionState.getMappingsThatCouldBeOfKind(word, MappingKind.TYPE);

            CommonUtilities.addRecommendedInstancesFromNounMappings(similarTypes, nameMappings, typeMappings, recommendationState, this, probability);
        }
    }

    /**
     * Checks if the current node is a type in the text extraction state. If the names of the text extraction state
     * contain the following node. If that's the case a recommendation for the combination of both is created.
     *
     * @param textExtractionState text extraction state
     * @param word                the current word
     */
    private void addRecommendedInstanceIfNameAfterType(ITextState textExtractionState, IWord word, IModelState modelState,
            IRecommendationState recommendationState) {
        if (textExtractionState == null || word == null) {
            return;
        }

        var sameLemmaTypes = CommonUtilities.getSimilarTypes(word, modelState);
        if (!sameLemmaTypes.isEmpty()) {
            textExtractionState.addNounMapping(word, MappingKind.TYPE, this, probability);

            var typeMappings = textExtractionState.getMappingsThatCouldBeOfKind(word, MappingKind.TYPE);
            var nameMappings = textExtractionState.getMappingsThatCouldBeOfKind(word.getNextWord(), MappingKind.NAME);

            CommonUtilities.addRecommendedInstancesFromNounMappings(sameLemmaTypes, nameMappings, typeMappings, recommendationState, this, probability);
        }
    }

    /**
     * Checks if the current node is a type in the text extraction state. If the name_or_types of the text extraction
     * state contain the previous node. If that's the case a recommendation for the combination of both is created.
     *
     * @param textExtractionState text extraction state
     * @param word                the current word
     */
    private void addRecommendedInstanceIfNameOrTypeBeforeType(ITextState textExtractionState, IWord word, IModelState modelState,
            IRecommendationState recommendationState) {
        if (textExtractionState == null || word == null) {
            return;
        }

        var sameLemmaTypes = CommonUtilities.getSimilarTypes(word, modelState);

        if (!sameLemmaTypes.isEmpty()) {
            textExtractionState.addNounMapping(word, MappingKind.TYPE, this, probability);

            var typeMappings = textExtractionState.getMappingsThatCouldBeOfKind(word, MappingKind.TYPE);
            var nortMappings = textExtractionState.getMappingsThatCouldBeMultipleKinds(word.getPreWord(), MappingKind.NAME, MappingKind.TYPE);

            CommonUtilities.addRecommendedInstancesFromNounMappings(sameLemmaTypes, nortMappings, typeMappings, recommendationState, this, probability);
        }
    }

    /**
     * Checks if the current node is a type in the text extraction state. If the name_or_types of the text extraction
     * state contain the afterwards node. If that's the case a recommendation for the combination of both is created.
     *
     * @param textExtractionState text extraction state
     * @param word                the current word
     */
    private void addRecommendedInstanceIfNameOrTypeAfterType(ITextState textExtractionState, IWord word, IModelState modelState,
            IRecommendationState recommendationState) {
        if (textExtractionState == null || word == null) {
            return;
        }

        var sameLemmaTypes = CommonUtilities.getSimilarTypes(word, modelState);
        if (!sameLemmaTypes.isEmpty()) {
            textExtractionState.addNounMapping(word, MappingKind.TYPE, this, probability);

            var typeMappings = textExtractionState.getMappingsThatCouldBeOfKind(word, MappingKind.TYPE);
            var nortMappings = textExtractionState.getMappingsThatCouldBeMultipleKinds(word.getNextWord(), MappingKind.NAME, MappingKind.TYPE);

            CommonUtilities.addRecommendedInstancesFromNounMappings(sameLemmaTypes, nortMappings, typeMappings, recommendationState, this, probability);
        }
    }

    @Override
    protected void delegateApplyConfigurationToInternalObjects(Map<String, String> additionalConfiguration) {
        // handle additional config
    }
}
