package org.openalto.alto.client;

import javax.ws.rs.core.Response;

public interface ALTOResponse {

    public boolean isError();

    public ALTORequest getRequest();

    public Response getResponse();

    public Object get();

}
