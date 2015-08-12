package org.openalto.alto.client.wrapper;

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.decoder.basic.DefaultCostMap;
import org.openalto.alto.common.decoder.basic.DefaultCostMapDecoder;

public class CostMapResponseParser
        extends DecoderParser<ALTOData<MetaData, DefaultCostMap>> {

    public CostMapResponseParser() {
        super(new DefaultCostMapDecoder());
    }
}
