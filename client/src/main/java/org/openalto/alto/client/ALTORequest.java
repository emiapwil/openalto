package org.openalto.alto.client;

import javax.ws.rs.client.Invocation;

import org.openalto.alto.common.resource.ResourceEntry;

public interface ALTORequest {

    public ALTOResponse invoke();

    public Invocation getInvocation();

    public ResourceEntry getResourceEntry();

    public Object getParams();

}
