package edu.kit.kastel.mcse.ardoco.core.model.pcm;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.kit.kastel.mcse.ardoco.core.datastructures.definitions.IModelInstance;

class PapyrusUMLOntologyModelConnectorTest {
    private static final Logger logger = LogManager.getLogger();

    private static PapyrusUMLOntologyModelConnector loadModel(String modelFile) {
        File file = new File(modelFile);

        String absolutePath = file.getAbsolutePath();
        return new PapyrusUMLOntologyModelConnector(absolutePath);
    }

    @Test
    @DisplayName("Get all instances from TEAMMATES ontology")
    void getInstancesFromTeammatesTest() {
        PapyrusUMLOntologyModelConnector connectorTeaStore = loadModel("src/test/resources/teammates.uml.owl");
        if (connectorTeaStore == null) {
            logger.debug("connector is null");
            Assertions.assertTrue(false, "Connector is null, thus the model was not loaded.");
        }
        ImmutableList<IModelInstance> instances = connectorTeaStore.getInstances();

        Assertions.assertFalse(instances.isEmpty(), "There need to be some instances contained in the model.");

        if (logger.isDebugEnabled()) {
            logger.debug("Listing TEAMMATES instances:");
            for (IModelInstance instance : instances) {
                String info = instance.toString();
                logger.debug(info);
                logger.debug(instance.getNames());
            }
            logger.debug("\n");
        }

        connectorTeaStore = null;
    }

}
