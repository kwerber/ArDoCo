/* Licensed under MIT 2022. */
package edu.kit.kastel.mcse.ardoco.core.api.data;

import edu.kit.kastel.mcse.ardoco.core.api.data.inconsistency.IInconsistencyState;

public interface IInconsistencyData extends IData {
    void setInconsistencyState(String model, IInconsistencyState state);

    IInconsistencyState getInconsistencyState(String model);
}
