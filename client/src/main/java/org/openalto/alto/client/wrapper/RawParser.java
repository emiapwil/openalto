package org.openalto.alto.client.wrapper;

import javax.ws.rs.core.Response;

import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.MetaData;

public class RawParser extends ALTOResponseParser {

    @Override
    public ALTOResponse parse(Response response) {
        String msg = response.readEntity(String.class);
        ALTOData<?, String> data = new ALTOData<MetaData, String>(null, msg);
        return new ALTOResponseBase(false, response, data);
    }
}
