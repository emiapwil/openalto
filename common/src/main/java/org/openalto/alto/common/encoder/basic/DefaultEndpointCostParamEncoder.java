package org.openalto.alto.common.encoder.basic;

import org.openalto.alto.common.type.EndpointAddress;

import org.openalto.alto.common.standard.RFC7285;

public class DefaultEndpointCostParamEncoder
        extends CostFilterEncoder<EndpointAddress<?>> {

    public DefaultEndpointCostParamEncoder() {
        super(RFC7285.PARAM_ENDPOINTS);
    }
}
