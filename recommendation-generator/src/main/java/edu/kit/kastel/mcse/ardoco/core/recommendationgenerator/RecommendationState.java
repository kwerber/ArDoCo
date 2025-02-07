/* Licensed under MIT 2021-2022. */
package edu.kit.kastel.mcse.ardoco.core.recommendationgenerator;

import java.util.HashSet;
import java.util.Map;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.mcse.ardoco.core.api.agent.IClaimant;
import edu.kit.kastel.mcse.ardoco.core.api.data.AbstractState;
import edu.kit.kastel.mcse.ardoco.core.api.data.recommendationgenerator.IRecommendationState;
import edu.kit.kastel.mcse.ardoco.core.api.data.recommendationgenerator.IRecommendedInstance;
import edu.kit.kastel.mcse.ardoco.core.api.data.textextraction.INounMapping;
import edu.kit.kastel.mcse.ardoco.core.common.util.SimilarityUtils;

/**
 * The recommendation state encapsulates all recommended instances and relations. These recommendations should be
 * contained by the model by their probability.
 *
 * @author Sophie
 */
public class RecommendationState extends AbstractState implements IRecommendationState {

    private MutableList<IRecommendedInstance> recommendedInstances;

    @Override
    public IRecommendationState createCopy() {
        var recommendationState = new RecommendationState(this.configs);
        recommendationState.recommendedInstances = recommendedInstances.collect(IRecommendedInstance::createCopy);
        return recommendationState;
    }

    /**
     * Creates a new recommendation state.
     */
    public RecommendationState(Map<String, String> configs) {
        super(configs);
        recommendedInstances = Lists.mutable.empty();
    }

    /**
     * Returns all recommended instances.
     *
     * @return all recommended instances as list
     */
    @Override
    public ImmutableList<IRecommendedInstance> getRecommendedInstances() {
        return Lists.immutable.withAll(recommendedInstances);
    }

    /**
     * Adds a recommended instance without a type.
     *
     * @param name         name of that recommended instance
     * @param probability  probability of being in the model
     * @param nameMappings name mappings representing that recommended instance
     */
    @Override
    public void addRecommendedInstance(String name, IClaimant claimant, double probability, ImmutableList<INounMapping> nameMappings) {
        this.addRecommendedInstance(name, "", claimant, probability, nameMappings, Lists.immutable.empty());
    }

    /**
     * Adds a recommended instance.
     *
     * @param name         name of that recommended instance
     * @param type         type of that recommended instance
     * @param probability  probability of being in the model
     * @param nameMappings name mappings representing the name of the recommended instance
     * @param typeMappings type mappings representing the type of the recommended instance
     * @return the added recommended instance
     */
    @Override
    public IRecommendedInstance addRecommendedInstance(String name, String type, IClaimant claimant, double probability,
            ImmutableList<INounMapping> nameMappings, ImmutableList<INounMapping> typeMappings) {
        var recommendedInstance = new RecommendedInstance(name, type, claimant, probability, //
                Lists.immutable.withAll(new HashSet<>(nameMappings.castToCollection())),
                Lists.immutable.withAll(new HashSet<>(typeMappings.castToCollection())));
        this.addRecommendedInstance(recommendedInstance);

        return recommendedInstance;
    }

    /**
     * Adds a recommended instance to the state. If the in the stored instance an instance with the same name and type
     * is contained it is extended. If an recommendedInstance with the same name can be found it is extended. Elsewhere
     * a new recommended instance is created.
     */
    private void addRecommendedInstance(IRecommendedInstance ri) {
        if (recommendedInstances.contains(ri)) {
            return;
        }

        var risWithExactName = recommendedInstances.select(r -> r.getName().equalsIgnoreCase(ri.getName())).toImmutable();
        var risWithExactNameAndType = risWithExactName.select(r -> r.getType().equalsIgnoreCase(ri.getType()));

        if (risWithExactNameAndType.isEmpty()) {
            processRecommendedInstancesWithNoExactNameAndType(ri, risWithExactName);
        } else {
            risWithExactNameAndType.get(0).addMappings(ri.getNameMappings(), ri.getTypeMappings());
        }
    }

    private void processRecommendedInstancesWithNoExactNameAndType(IRecommendedInstance ri, ImmutableList<IRecommendedInstance> risWithExactName) {
        if (risWithExactName.isEmpty()) {
            recommendedInstances.add(ri);
        } else {
            var added = false;

            for (IRecommendedInstance riWithExactName : risWithExactName) {
                var areWordsSimilar = SimilarityUtils.areWordsSimilar(riWithExactName.getType(), ri.getType());
                if (areWordsSimilar || recommendedInstancesHasEmptyType(ri, riWithExactName)) {
                    riWithExactName.addMappings(ri.getNameMappings(), ri.getTypeMappings());
                    added = true;
                    break;
                }
            }

            if (!added && !ri.getType().isBlank()) {
                recommendedInstances.add(ri);
            }
        }
    }

    private static boolean recommendedInstancesHasEmptyType(IRecommendedInstance ri, IRecommendedInstance riWithExactName) {
        return riWithExactName.getType().isBlank() && !ri.getType().isBlank();
    }

    /**
     * Returns all recommended instances that contain a given mapping as type.
     *
     * @param mapping given mapping to search for in types
     * @return the list of recommended instances with the mapping as type.
     */
    @Override
    public ImmutableList<IRecommendedInstance> getRecommendedInstancesByTypeMapping(INounMapping mapping) {
        return recommendedInstances.select(sinstance -> sinstance.getTypeMappings().contains(mapping)).toImmutable();
    }

    /**
     * Returns all recommended instances that contain a given mapping.
     *
     * @param mapping given mapping to search for
     * @return the list of recommended instances with the mapping.
     */
    @Override
    public ImmutableList<IRecommendedInstance> getAnyRecommendedInstancesByMapping(INounMapping mapping) {
        return recommendedInstances //
                .select(sinstance -> sinstance.getTypeMappings().contains(mapping) || sinstance.getNameMappings().contains(mapping))
                .toImmutable();
    }

    /**
     * Returns all recommended instances that contain a given name.
     *
     * @param name given name to search for in names
     * @return the list of recommended instances with that name.
     */
    @Override
    public ImmutableList<IRecommendedInstance> getRecommendedInstancesByName(String name) {
        return recommendedInstances.select(ri -> ri.getName().toLowerCase().contentEquals(name.toLowerCase())).toImmutable();
    }

    /**
     * Returns all recommended instances that contain a similar name.
     *
     * @param name given name to search for in names
     * @return the list of recommended instances with a similar name.
     */
    @Override
    public ImmutableList<IRecommendedInstance> getRecommendedInstancesBySimilarName(String name) {
        MutableList<IRecommendedInstance> ris = Lists.mutable.empty();
        for (IRecommendedInstance ri : recommendedInstances) {
            if (SimilarityUtils.areWordsSimilar(ri.getName(), name)) {
                ris.add(ri);
            }
        }

        return ris.toImmutable();
    }

    /**
     * Returns all recommended instances that contain a given name and type.
     *
     * @param type given type to search for in types
     * @return the list of recommended instances with that name and type
     */
    @Override
    public ImmutableList<IRecommendedInstance> getRecommendedInstancesByType(String type) {
        return recommendedInstances.select(ri -> ri.getType().toLowerCase().contentEquals(type.toLowerCase())).toImmutable();
    }

    /**
     * Returns all recommended instances that contain a similar type.
     *
     * @param type given type to search for in types
     * @return the list of recommended instances with a similar type.
     */
    @Override
    public ImmutableList<IRecommendedInstance> getRecommendedInstancesBySimilarType(String type) {
        return recommendedInstances.select(ri -> SimilarityUtils.areWordsSimilar(ri.getType(), type)).toImmutable();
    }

}
