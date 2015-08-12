package org.openalto.alto.client.wrapper;

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.decoder.basic.DefaultEndpointCostResult;
import org.openalto.alto.common.decoder.basic.DefaultEndpointCostResultDecoder;

public class EndpointCostResponseParser
        extends DecoderParser<ALTOData<MetaData, DefaultEndpointCostResult>> {

    public EndpointCostResponseParser() {
        super(new DefaultEndpointCostResultDecoder());
    }
}
