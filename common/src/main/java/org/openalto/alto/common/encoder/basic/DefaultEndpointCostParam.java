package org.openalto.alto.common.encoder.basic;

import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.type.EndpointAddress;

public class DefaultEndpointCostParam
        extends CostFilterParam<EndpointAddress<?>> {

    public DefaultEndpointCostParam(CostType type) {
        super(type);
    }

}
