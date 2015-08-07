package org.openalto.alto;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openalto.alto.common.resource.ResourceEntry;
import org.openalto.alto.common.resource.ResourceType;
import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.type.EndpointAddress;
import org.openalto.alto.common.decoder.basic.DefaultNetworkMap;
import org.openalto.alto.common.decoder.basic.DefaultNetworkMap;
import org.openalto.alto.common.encoder.basic.InetAddressFixer;
import org.openalto.alto.common.encoder.basic.DefaultEndpointCostParam;
import org.openalto.alto.common.encoder.basic.DefaultEndpointCostParamEncoder;

import org.openalto.alto.client.ALTORequest;
import org.openalto.alto.client.ALTORequestBuilder;
import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

import org.openalto.alto.client.wrapper.RawParser;
import org.openalto.alto.client.wrapper.IRDRequestBuilder;
import org.openalto.alto.client.wrapper.IRDResponseParser;
import org.openalto.alto.client.wrapper.NetworkMapRequestBuilder;
import org.openalto.alto.client.wrapper.NetworkMapResponseParser;
import org.openalto.alto.client.wrapper.ECSRequestBuilder;


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
    public static void main(String[] args) throws Exception {
        testIRD();
        testNM();
        testECS();
    }

    public static void testIRD() throws IOException, URISyntaxException {
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

    public static void testNM() throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:3400/alto/test_sf_networkmap");
        ResourceType type = ResourceType.NETWORK_MAP_TYPE;

        ResourceEntry resource = new ResourceEntry(uri, type);

        Client client = ClientBuilder.newClient();

        ALTOResponseParser arp = new NetworkMapResponseParser();
        ALTORequestBuilder arb = new NetworkMapRequestBuilder(client, arp);

        ALTORequest request = arb.request(resource, null);
        ALTOResponse response = request.invoke();

        if (!isValidesponse(response))
            return;

        ALTOData<?, DefaultNetworkMap> nm = (ALTOData<?, DefaultNetworkMap>)response.get();
        Map<String, Set<EndpointAddress<?>>> nodes = nm.getData().getNodes();
        for (Map.Entry<String, Set<EndpointAddress<?>>> entry: nodes.entrySet()) {
            String pid = entry.getKey();
            Set<EndpointAddress<?>> addrSet = entry.getValue();

            System.out.println("------------------");
            System.out.println(pid);
            for (EndpointAddress<?> addr: addrSet) {
                System.out.println("\t" + addr.getFamily() + ": " + addr.getAddr().toString());
            }
            System.out.println("------------------");
        }
    }

    public static void testECS() throws Exception {
        URI uri = new URI("http://localhost:3400/alto/test_ecslite");
        ResourceType resourceType = ResourceType.ENDPOINT_COST_SERVICE_TYPE;

        ResourceEntry resource = new ResourceEntry(uri, resourceType);

        Client client = ClientBuilder.newClient();

        ALTOResponseParser arp = new RawParser();
        DefaultEndpointCostParamEncoder encoder = new DefaultEndpointCostParamEncoder();
        ALTORequestBuilder arb = new ECSRequestBuilder(client, arp, encoder);

        CostType type = new CostType("numerical", "routingcost");
        DefaultEndpointCostParam param = new DefaultEndpointCostParam(type);

        InetAddressFixer srcs[] = {
            new InetAddressFixer(InetAddress.getByName("192.0.2.2"))
        };

        InetAddressFixer dsts[] = {
            new InetAddressFixer(InetAddress.getByName("192.0.2.89")),
            new InetAddressFixer(InetAddress.getByName("198.51.100.34")),
            new InetAddressFixer(InetAddress.getByName("203.0.113.45"))
        };

        for (InetAddressFixer addr: srcs) {
            param.addSource(new EndpointAddress<InetAddressFixer>("ipv4", addr));
        }

        for (InetAddressFixer addr: dsts) {
            param.addDestination(new EndpointAddress<InetAddressFixer>("ipv4", addr));
        }

        ALTORequest request = arb.request(resource, param);
        ALTOResponse response = request.invoke();

        if (!isValidesponse(response))
            return;

        ALTOData<MetaData, String> data;
        data = (ALTOData<MetaData, String>)response.get();
        System.out.println(data.getData());
   }

   public static boolean isValidesponse(ALTOResponse response) {
        if (response == null) {
            System.out.println("Something wrong with the connection");
            return false;
        }
        if (response.isError()) {
            String msg = response.get().toString();
            System.out.println(msg);
            return false;
        }
        return true;
    }
}

