package org.openalto.alto.client.wrapper;

import javax.ws.rs.core.Response;

import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

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
