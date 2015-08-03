package org.openalto.alto.client;

import java.util.List;
import java.util.LinkedList;

import javax.ws.rs.core.Response;

import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

import org.openalto.alto.common.ALTOMediaType;
import org.openalto.alto.common.resource.ResourceEntry;

public class IRDResponseParser extends ALTOResponseParser {

    @Override
    public ALTOResponse parse(Response response) {
        if (response == null)
            return null;

        if (!success(response.getStatusInfo())) {
            return null;
        }

        if (!response.getMediaType().equals(ALTOMediaType.DIRECTORY_TYPE)) {
            return null;
        }

        List<ResourceEntry> list = new LinkedList<ResourceEntry>();

        //TODO
        return new ALTOResponseBase(false, response, null);
    }
}
