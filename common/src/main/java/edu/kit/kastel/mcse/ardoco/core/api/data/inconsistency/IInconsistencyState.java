/* Licensed under MIT 2021-2022. */
package edu.kit.kastel.mcse.ardoco.core.api.data.inconsistency;

import java.util.List;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import edu.kit.kastel.informalin.framework.common.ICopyable;
import edu.kit.kastel.informalin.framework.configuration.IConfigurable;
import edu.kit.kastel.mcse.ardoco.core.api.data.recommendationgenerator.IRecommendedInstance;

/**
 * Inconsistency state holding data and information about inconsistency.
 * 
 * @author Jan Keim
 */
public interface IInconsistencyState extends ICopyable<IInconsistencyState>, IConfigurable {

    /**
     * Returns a list of inconsistencies held by this state
     *
     * @return list of inconsistencies
     */
    ImmutableList<IInconsistency> getInconsistencies();

    /**
     * Add an Inconsistency to this state
     *
     * @param inconsistency the inconsistency to add
     * @return true if added successfully
     */
    boolean addInconsistency(IInconsistency inconsistency);

    /**
     * Remove an Inconsistency from this state
     *
     * @param inconsistency the inconsistency to remove
     * @return true if removed successfully
     */
    boolean removeInconsistency(IInconsistency inconsistency);

    default boolean addRecommendedInstances(List<IRecommendedInstance> recommendedInstances) {
        var success = true;
        for (var recommendedInstance : recommendedInstances) {
            success &= addRecommendedInstance(recommendedInstance);
        }
        return success;
    }

    boolean addRecommendedInstance(IRecommendedInstance recommendedInstance);

    default boolean removeRecommendedInstances(List<IRecommendedInstance> recommendedInstances) {
        var success = true;
        for (var recommendedInstance : recommendedInstances) {
            success &= removeRecommendedInstance(recommendedInstance);
        }
        return success;
    }

    boolean removeRecommendedInstance(IRecommendedInstance recommendedInstance);

    /**
     * Sets the recommended Instances
     * 
     * @param recommendedInstances the recommendedInstances to set
     */
    void setRecommendedInstances(List<IRecommendedInstance> recommendedInstances);

    /**
     * Returns the recommended Instances
     * 
     * @return the recommendedInstances
     */
    MutableList<IRecommendedInstance> getRecommendedInstances();

}
