package org.openalto.alto.common.encoder.basic;

import org.openalto.alto.common.standard.RFC7285;

public class DefaultCostMapFilterEncoder
        extends CostFilterEncoder<String> {

    public DefaultCostMapFilterEncoder() {
        super(RFC7285.PARAM_PIDS);
    }
}
