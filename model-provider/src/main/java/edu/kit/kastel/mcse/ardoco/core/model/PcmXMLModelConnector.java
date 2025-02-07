/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.core.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

import edu.kit.kastel.informalin.framework.models.pcm.PCMModel;
import edu.kit.kastel.informalin.framework.models.pcm.PCMRepository;
import edu.kit.kastel.mcse.ardoco.core.api.data.model.IModelInstance;
import edu.kit.kastel.mcse.ardoco.core.api.data.model.Metamodel;

public class PcmXMLModelConnector implements IModelConnector {

    private final PCMRepository repository;

    public PcmXMLModelConnector(File file) throws IOException {
        this(new FileInputStream(file));
    }

    public PcmXMLModelConnector(InputStream is) {
        PCMModel pcmModel = new PCMModel(is);
        this.repository = Objects.requireNonNull(pcmModel.getRepository());
    }

    @Override
    public String getModelId() {
        return repository.getId();
    }

    @Override
    public Metamodel getMetamodel() {
        return Metamodel.ARCHITECTURE;
    }

    @Override
    public ImmutableList<IModelInstance> getInstances() {
        return Lists.immutable.withAll(repository.getComponents()).collect(e -> new Instance(e.getEntityName(), e.getType(), e.getId()));
    }
}
