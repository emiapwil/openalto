package org.openalto.alto.client.wrapper;

import javax.ws.rs.core.Response;

import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.decoder.basic.DefaultEndpointPropertyResult;
import org.openalto.alto.common.decoder.basic.DefaultEndpointPropertyResultDecoder;

public class EndpointPropertyResponseParser
        extends DecoderParser<ALTOData<MetaData, DefaultEndpointPropertyResult>> {

    public EndpointPropertyResponseParser() {
        super(new DefaultEndpointPropertyResultDecoder());
    }
}
