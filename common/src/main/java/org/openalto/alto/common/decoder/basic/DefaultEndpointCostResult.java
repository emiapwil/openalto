package org.openalto.alto.common.decoder.basic;

import org.openalto.alto.common.type.EndpointAddress;

public class DefaultEndpointCostResult
        extends CostResult<EndpointAddress<?>> {

    public DefaultEndpointCostResult(CostResult<EndpointAddress<?>> rhs) {
        super(rhs);
    }
}
