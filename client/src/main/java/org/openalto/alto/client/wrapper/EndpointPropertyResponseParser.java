package org.openalto.alto.client.wrapper;

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
