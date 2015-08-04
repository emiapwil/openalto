package org.openalto.alto;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

import org.openalto.alto.common.resource.ResourceEntry;
import org.openalto.alto.common.resource.ResourceType;
import org.openalto.alto.client.ALTORequest;
import org.openalto.alto.client.ALTORequestBuilder;
import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;
import org.openalto.alto.client.wrapper.ird.IRDRequestBuilder;
import org.openalto.alto.client.wrapper.ird.IRDResponseParser;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:3000/myapp/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.openalto.alto package
        final ResourceConfig rc = new ResourceConfig().packages("org.openalto.alto");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void tryRaw() {
        Client client = ClientBuilder.newClient();

        Response response = client.target("http://localhost:3400/alto/").request().buildGet().invoke();
        System.out.println(response.readEntity(String.class));
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:3400/alto/");
        ResourceType type = ResourceType.DIRECTORY_TYPE;

        ResourceEntry resource = new ResourceEntry(uri, type);

        Client client = ClientBuilder.newClient();

        ALTOResponseParser arp = new IRDResponseParser();
        ALTORequestBuilder arb = new IRDRequestBuilder(client, arp);

        ALTORequest request = arb.request(resource, null);
        ALTOResponse response = request.invoke();
        if (response == null) {
            System.out.println("Something wrong with the connection");
            return;
        }
        if (response.isError()) {
            String msg = response.get().toString();
            System.out.println(msg);
            return;
        }
        List<ResourceEntry> resourceList = (List<ResourceEntry>)response.get();
        if (resourceList == null) {
            System.out.println("Empty ALTO IRD");
            return;
        }
        for (ResourceEntry re: resourceList) {
            System.out.println("-------------------");
            System.out.println("ResourceId:   " + re.getResourceId());
            System.out.println("URI:          " + re.getURI());
            System.out.println("ResourceType: " + re.getType());
            System.out.println("-------------------");
        }
    }
}

