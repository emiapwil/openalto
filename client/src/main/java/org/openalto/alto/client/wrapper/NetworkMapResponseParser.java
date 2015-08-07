package org.openalto.alto.client.wrapper;

import javax.ws.rs.core.Response;

import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

import org.openalto.alto.common.decoder.basic.DefaultNetworkMapDecoder;

public class NetworkMapResponseParser extends ALTOResponseParser {

    @Override
    public ALTOResponse parse(Response response) {
        String nm = response.readEntity(String.class);
        DefaultNetworkMapDecoder decoder = new DefaultNetworkMapDecoder();

        return new ALTOResponseBase(false, response, decoder.decode(nm));
    }
}
