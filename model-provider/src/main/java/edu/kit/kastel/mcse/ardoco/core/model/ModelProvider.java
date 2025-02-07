/* Licensed under MIT 2021-2022. */
package edu.kit.kastel.mcse.ardoco.core.model;

import java.util.Map;

import org.eclipse.collections.api.list.ImmutableList;

import edu.kit.kastel.mcse.ardoco.core.api.data.model.IModelInstance;
import edu.kit.kastel.mcse.ardoco.core.api.data.model.IModelState;

/**
 * The model extractor extracts the instances and relations via an connector. The extracted items are stored in a model
 * extraction state.
 *
 * @author Sophie
 */
public final class ModelProvider {

    private final IModelConnector modelConnector;

    /**
     * Instantiates a new model provider.
     *
     * @param modelConnector the model connector
     */
    public ModelProvider(IModelConnector modelConnector) {
        this.modelConnector = modelConnector;
    }

    public IModelState execute(Map<String, String> additionalSettings) {
        ImmutableList<IModelInstance> instances = modelConnector.getInstances();
        return new ModelExtractionState(modelConnector.getModelId(), modelConnector.getMetamodel(), instances, additionalSettings);
    }

}
