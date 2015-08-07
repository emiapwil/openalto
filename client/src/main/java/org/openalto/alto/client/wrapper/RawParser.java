package org.openalto.alto.client.wrapper;

import javax.ws.rs.core.Response;

import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

public class RawParser extends ALTOResponseParser {

    @Override
    public ALTOResponse parse(Response response) {
        return new ALTOResponseBase(false, response, response.readEntity(String.class));
    }
}
