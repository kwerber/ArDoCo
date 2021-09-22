package edu.kit.kastel.mcse.ardoco.core.model.pcm;

import java.util.List;
import java.util.Optional;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.informalin.ontology.OntologyConnector;
import edu.kit.kastel.informalin.ontology.OntologyInterface;
import edu.kit.kastel.mcse.ardoco.core.datastructures.Instance;
import edu.kit.kastel.mcse.ardoco.core.datastructures.definitions.IModelInstance;
import edu.kit.kastel.mcse.ardoco.core.datastructures.definitions.IModelRelation;
import edu.kit.kastel.mcse.ardoco.core.model.IModelConnector;

/**
 * The Class PcmOntologyModelConnector defines a {@link IModelConnector} that can read Papyrus UML
 * Models from Ontologies.
 */
public class PapyrusUMLOntologyModelConnector implements IModelConnector {
    private static final String NAME_PROPERTY = "name_-_NamedElement";

    private static Logger logger = LogManager.getLogger(PapyrusUMLOntologyModelConnector.class);

    private static final String[] TYPES = { "Interface", "Component" };
    private OntologyInterface ontologyConnector;

    /**
     * Instantiates a new pcm ontology model connector.
     *
     * @param ontologyUrl
     *            Can be a local URL (path to the ontology) or a remote URL
     */
    public PapyrusUMLOntologyModelConnector(String ontologyUrl) {
        ontologyConnector = new OntologyConnector(ontologyUrl);
    }

    public PapyrusUMLOntologyModelConnector(OntologyInterface ontologyConnector) {
        this.ontologyConnector = ontologyConnector;
    }

    @Override
    public ImmutableList<IModelInstance> getInstances() {
        MutableList<IModelInstance> instances = Lists.mutable.empty();

        for (String type : TYPES) {
            instances.addAll(getInstancesOfType(type));
        }

        return instances.toImmutable();
    }

    private List<IModelInstance> getInstancesOfType(String type) {
        List<IModelInstance> instances = Lists.mutable.empty();
        Optional<OntClass> optionalClass = ontologyConnector.getClass(type);
        if (optionalClass.isEmpty()) {
            return instances;
        }
        OntClass clazz = optionalClass.get();
        var entityNameProperty = getNameProperty();

        for (Individual individual : ontologyConnector.getIndividualsOfClass(clazz)) {
            var name = individual.getProperty(entityNameProperty)
                .getString();
            var identifier = getUID(name);
            var instance = new Instance(name, type, identifier);
            instances.add(instance);
        }
        return instances;

    }

    private String getUID(String name) {
        // TODO Generate UID
        return name;
    }

    private OntProperty getNameProperty() {
        Optional<OntProperty> optionalProperty = ontologyConnector.getProperty(NAME_PROPERTY);
        if (optionalProperty.isEmpty()) {
            throw new IllegalStateException("Cannot find the \"entityName\" property!");
        }
        return optionalProperty.get();
    }

    @Override
    public ImmutableList<IModelRelation> getRelations() {
        logger.warn("This method is not yet implemented and will return an empty list!");
        return Lists.immutable.empty();
    }

}
