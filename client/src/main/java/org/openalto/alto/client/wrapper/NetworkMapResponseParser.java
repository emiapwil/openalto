package org.openalto.alto.client.wrapper;

import javax.ws.rs.core.Response;

import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.decoder.basic.DefaultNetworkMap;
import org.openalto.alto.common.decoder.basic.DefaultNetworkMapDecoder;

public class NetworkMapResponseParser
        extends DecoderParser<ALTOData<MetaData, DefaultNetworkMap>> {

    public NetworkMapResponseParser() {
        super(new DefaultNetworkMapDecoder());
    }
}
