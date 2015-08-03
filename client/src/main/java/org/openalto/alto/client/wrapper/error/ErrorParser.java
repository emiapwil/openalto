package org.openalto.alto.client;

import javax.ws.rs.core.Response;

import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

import org.openalto.alto.common.ALTOMediaType;

public class ErrorParser extends ALTOResponseParser {

    @Override
    public ALTOResponse parse(Response response) {
        if (response == null)
            return null;

        if (!success(response.getStatusInfo())) {
            return null;
        }

        if (!response.getMediaType().equals(ALTOMediaType.ERROR_TYPE)) {
            return null;
        }

        //TODO Extract more information for subtypes
        String msg = response.readEntity(String.class);
        return new ALTOResponseParser.ALTOResponseBase(true, response, msg);
    }
}
