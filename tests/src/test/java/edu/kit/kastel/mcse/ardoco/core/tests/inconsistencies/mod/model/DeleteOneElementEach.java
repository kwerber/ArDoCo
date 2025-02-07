/* Licensed under MIT 2021-2022. */
package edu.kit.kastel.mcse.ardoco.core.tests.inconsistencies.mod.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.kastel.mcse.ardoco.core.api.data.model.IModelInstance;
import edu.kit.kastel.mcse.ardoco.core.api.data.model.Metamodel;
import edu.kit.kastel.mcse.ardoco.core.api.data.text.IText;
import edu.kit.kastel.mcse.ardoco.core.model.IModelConnector;
import edu.kit.kastel.mcse.ardoco.core.tests.inconsistencies.mod.IModificationStrategy;
import edu.kit.kastel.mcse.ardoco.core.tests.inconsistencies.mod.Modifications;
import edu.kit.kastel.mcse.ardoco.core.tests.inconsistencies.mod.ModifiedElement;

public class DeleteOneElementEach implements IModificationStrategy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IModelConnector model;
    private final ImmutableList<IModelInstance> originalModelElements;

    public DeleteOneElementEach(IModelConnector model) {
        this.model = model;
        originalModelElements = this.model.getInstances();
    }

    @Override
    public Iterator<ModifiedElement<IModelConnector, IModelInstance>> getModifiedModelInstances() {
        return new DeleteOneElementEachIterator();
    }

    private class DeleteOneElementEachIterator implements Iterator<ModifiedElement<IModelConnector, IModelInstance>> {

        private int currentDeletion = 0;

        @Override
        public ModifiedElement<IModelConnector, IModelInstance> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int deleted = currentDeletion++;
            IModelInstance deletionModelElement = originalModelElements.get(deleted);
            logger.info("Current deletion model element: {}", deletionModelElement.getFullName());
            return ModifiedElement.of(new ModelWrapper(deleted), deletionModelElement, Modifications.DELETE_ELEMENT);
        }

        @Override
        public boolean hasNext() {
            return currentDeletion < originalModelElements.size();
        }
    }

    private class ModelWrapper implements IModelConnector {

        private final int skip;

        public ModelWrapper(int skip) {
            this.skip = skip;
        }

        @Override
        public ImmutableList<IModelInstance> getInstances() {
            var instances = originalModelElements.toList();
            instances.remove(skip);
            return instances.toImmutable();
        }

        @Override
        public String getModelId() {
            return model.getModelId();
        }

        @Override
        public Metamodel getMetamodel() {
            return model.getMetamodel();
        }

    }

    @Override
    public Iterator<ModifiedElement<IText, Integer>> getModifiedTexts() {
        throw new UnsupportedOperationException();
    }

}
