package org.openalto.alto.client;

import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status;

/**
 * Root resource (exposed at "test" path)
 */
@Path("test")
public class TestServerResource {

    @Path("ird")
    @GET
    @Produces({"application/alto-directory+json", "application/alto-error+json"})
    public Response getIRD() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @Path("networkmap")
    @GET
    @Produces({"application/alto-networkmap+json", "application/alto-error+json"})
    public Response getNetworkMap() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @Path("costmap")
    @GET
    @Produces({"application/alto-costmap+json", "application/alto-error+json"})
    public Response getCostMap() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @Path("ecs")
    @POST
    @Consumes({"application/alto-endpointcostparams+json"})
    @Produces({"application/alto-endpointcost+json", "application/alto-error+json"})
    public Response getECS() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @Path("eps")
    @POST
    @Consumes({"application/alto-endpointpropparams+json"})
    @Produces({"application/alto-endpointprop+json", "application/alto-error+json"})
    public Response getEPS() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}
