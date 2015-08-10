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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.openalto.alto.common.resource.ResourceEntry;
import org.openalto.alto.common.resource.ResourceType;
import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.MetaData;
import org.openalto.alto.common.type.CostType;
import org.openalto.alto.common.type.EndpointAddress;
import org.openalto.alto.common.type.ResourceTag;
import org.openalto.alto.common.decoder.basic.DefaultNetworkMap;
import org.openalto.alto.common.decoder.basic.DefaultCostMap;
import org.openalto.alto.common.decoder.basic.DefaultEndpointCostResult;
import org.openalto.alto.common.encoder.ALTOEncoder;
import org.openalto.alto.common.encoder.basic.InetAddressFixer;
import org.openalto.alto.common.encoder.basic.DefaultEndpointCostParam;
import org.openalto.alto.common.encoder.basic.DefaultEndpointCostParamEncoder;
import org.openalto.alto.common.encoder.basic.DefaultNetworkMapFilter;
import org.openalto.alto.common.encoder.basic.DefaultNetworkMapFilterEncoder;
import org.openalto.alto.common.encoder.basic.DefaultCostMapFilter;
import org.openalto.alto.common.encoder.basic.DefaultCostMapFilterEncoder;

import org.openalto.alto.client.ALTORequest;
import org.openalto.alto.client.ALTORequestBuilder;
import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

import org.openalto.alto.client.wrapper.RawParser;
import org.openalto.alto.client.wrapper.IRDRequestBuilder;
import org.openalto.alto.client.wrapper.IRDResponseParser;
import org.openalto.alto.client.wrapper.CostMapRequestBuilder;
import org.openalto.alto.client.wrapper.CostMapResponseParser;
import org.openalto.alto.client.wrapper.NetworkMapRequestBuilder;
import org.openalto.alto.client.wrapper.NetworkMapResponseParser;
import org.openalto.alto.client.wrapper.EndpointCostResponseParser;
import org.openalto.alto.client.wrapper.EndpointCostRequestBuilder;
import org.openalto.alto.client.wrapper.FilteredCostMapRequestBuilder;
import org.openalto.alto.client.wrapper.FilteredNetworkMapRequestBuilder;



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
        System.out.println("Test IRD:");
        try {
            testIRD();
        } catch (Exception e) {
        }

        System.out.println("Test Network Map:");
        try {
            testNM();
        } catch (Exception e) {
        }

        System.out.println("Test Filtered Network Map:");
        try {
            testFNM();
        } catch (Exception e) {
        }

        System.out.println("Test Cost Map:");
        try {
            testCM();
        } catch (Exception e) {
        }

        System.out.println("Test Filtered Cost Map:");
        try {
            testFCM();
        } catch (Exception e) {
        }

        System.out.println("Test Endpoint Cost Service:");
        try {
            testECS();
        } catch (Exception e) {
        }
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

        ALTOData<MetaData, DefaultNetworkMap> nm;
        nm = (ALTOData<MetaData, DefaultNetworkMap>)response.get();

        MetaData meta = nm.getMeta();
        ResourceTag vtag = (ResourceTag)meta.get("vtag");

        System.out.println("Resource ID: " + vtag.getResourceId());
        System.out.println("Tag:         " + vtag.getTag());

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

    public static void testCM() throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:3400/alto/test_sf_costmap");
        ResourceType type = ResourceType.COST_MAP_TYPE;

        ResourceEntry resource = new ResourceEntry(uri, type);

        Client client = ClientBuilder.newClient();

        ALTOResponseParser arp = new CostMapResponseParser();
        ALTORequestBuilder arb = new CostMapRequestBuilder(client, arp);

        ALTORequest request = arb.request(resource, null);
        ALTOResponse response = request.invoke();

        if (!isValidesponse(response))
            return;

        ALTOData<MetaData, DefaultCostMap> cm;
        cm = (ALTOData<MetaData, DefaultCostMap>)response.get();

        MetaData meta = cm.getMeta();

        Collection<ResourceTag> dependentVtags;
        dependentVtags = (Collection<ResourceTag>)meta.get("dependent-vtags");
        System.out.println("Depends:");
        for (ResourceTag vtag: dependentVtags) {
            System.out.println("\tResource ID: " + vtag.getResourceId());
            System.out.println("\tTag:         " + vtag.getTag());
        }

        CostType resultCostType = (CostType)meta.get("cost-type");
        System.out.println("Mode:   " + resultCostType.getMode());
        System.out.println("Metric: " + resultCostType.getMetric());

        DefaultCostMap costMap = cm.getData();

        for (Map.Entry<?, Map<String, Object>> entry: costMap.getCostMatrix().entrySet()) {
            Object source = entry.getKey();
            Map<?, Object> costFromMap = entry.getValue();

            for (Map.Entry<?, Object> _entry: costFromMap.entrySet()) {
                Object dst = _entry.getKey();
                Object cost = _entry.getValue();
                System.out.println(source.toString() + " --> " + dst.toString()
                                   + ": " + cost.toString());
            }
        }
    }

    public static void testFCM() throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:3400/alto/test_fcmlite");
        ResourceType type = ResourceType.FILTERED_COST_MAP_TYPE;

        ResourceEntry resource = new ResourceEntry(uri, type);

        Client client = ClientBuilder.newClient();

        ALTOEncoder encoder = new DefaultCostMapFilterEncoder();
        ALTOResponseParser arp = new CostMapResponseParser();
        ALTORequestBuilder arb = new FilteredCostMapRequestBuilder(client, arp, encoder);

        CostType costType = new CostType("numerical", "routingcost");
        DefaultCostMapFilter filter = new DefaultCostMapFilter(costType);

        filter.addSource("PID1");
        filter.addDestination("PID2");
        filter.addDestination("PID3");

        ALTORequest request = arb.request(resource, filter);
        ALTOResponse response = request.invoke();

        if (!isValidesponse(response))
            return;

        ALTOData<MetaData, DefaultCostMap> cm;
        cm = (ALTOData<MetaData, DefaultCostMap>)response.get();

        MetaData meta = cm.getMeta();

        Collection<ResourceTag> dependentVtags;
        dependentVtags = (Collection<ResourceTag>)meta.get("dependent-vtags");
        System.out.println("Depends:");
        for (ResourceTag vtag: dependentVtags) {
            System.out.println("\tResource ID: " + vtag.getResourceId());
            System.out.println("\tTag:         " + vtag.getTag());
        }

        CostType resultCostType = (CostType)meta.get("cost-type");
        System.out.println("Mode:   " + resultCostType.getMode());
        System.out.println("Metric: " + resultCostType.getMetric());

        DefaultCostMap costMap = cm.getData();

        for (Map.Entry<?, Map<String, Object>> entry: costMap.getCostMatrix().entrySet()) {
            Object source = entry.getKey();
            Map<?, Object> costFromMap = entry.getValue();

            for (Map.Entry<?, Object> _entry: costFromMap.entrySet()) {
                Object dst = _entry.getKey();
                Object cost = _entry.getValue();
                System.out.println(source.toString() + " --> " + dst.toString()
                                   + ": " + cost.toString());
            }
        }
    }

    public static void testFNM() throws IOException, URISyntaxException {
        URI uri = new URI("http://localhost:3400/alto/test_fnmlite");
        ResourceType type = ResourceType.FILTERED_NETWORK_MAP_TYPE;

        ResourceEntry resource = new ResourceEntry(uri, type);

        Client client = ClientBuilder.newClient();

        ALTOEncoder encoder = new DefaultNetworkMapFilterEncoder();
        ALTOResponseParser arp = new NetworkMapResponseParser();
        ALTORequestBuilder arb = new FilteredNetworkMapRequestBuilder(client, arp, encoder);

        Set<String> pids = new HashSet<String>();
        pids.add("PID1");
        pids.add("PID3");

        Set<String> addrTypes = new HashSet<String>();
        addrTypes.add("ipv4");

        DefaultNetworkMapFilter filter = new DefaultNetworkMapFilter(pids, addrTypes);
        ALTORequest request = arb.request(resource, filter);
        ALTOResponse response = request.invoke();

        if (!isValidesponse(response))
            return;

        ALTOData<MetaData, DefaultNetworkMap> nm;
        nm = (ALTOData<MetaData, DefaultNetworkMap>)response.get();

        MetaData meta = nm.getMeta();
        ResourceTag vtag = (ResourceTag)meta.get("vtag");

        System.out.println("Resource ID: " + vtag.getResourceId());
        System.out.println("Tag:         " + vtag.getTag());

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

        ALTOResponseParser arp = new EndpointCostResponseParser();
        DefaultEndpointCostParamEncoder encoder = new DefaultEndpointCostParamEncoder();
        ALTORequestBuilder arb = new EndpointCostRequestBuilder(client, arp, encoder);

        CostType type = new CostType("numerical", "routingcost");
        DefaultEndpointCostParam param = new DefaultEndpointCostParam(type);

        InetAddressFixer srcs[] = {
            new InetAddressFixer("ipv4", InetAddress.getByName("192.0.2.2"))
        };

        InetAddressFixer dsts[] = {
            new InetAddressFixer("ipv4", InetAddress.getByName("192.0.2.89")),
            new InetAddressFixer("ipv4", InetAddress.getByName("198.51.100.134")),
            new InetAddressFixer("ipv4", InetAddress.getByName("203.0.113.45"))
        };

        for (InetAddressFixer addr: srcs) {
            param.addSource(addr);
        }

        for (InetAddressFixer addr: dsts) {
            param.addDestination(addr);
        }

        ALTORequest request = arb.request(resource, param);
        ALTOResponse response = request.invoke();

        if (!isValidesponse(response))
            return;

        ALTOData<MetaData, DefaultEndpointCostResult> data;
        data = (ALTOData<MetaData, DefaultEndpointCostResult>)response.get();

        MetaData meta = data.getMeta();
        DefaultEndpointCostResult ecr = data.getData();

        CostType costType = (CostType)meta.get("cost-type");
        System.out.println("mode:   " + costType.getMode());
        System.out.println("metric: " + costType.getMetric());

        Map<EndpointAddress<?>, Map<EndpointAddress<?>, Object>> ecsMap;
        ecsMap = ecr.getCostMatrix();
        for (Map.Entry<?, Map<EndpointAddress<?>, Object>> entry: ecsMap.entrySet()) {
            Object source = entry.getKey();
            Map<?, Object> costFromMap = entry.getValue();

            for (Map.Entry<?, Object> _entry: costFromMap.entrySet()) {
                Object dst = _entry.getKey();
                Object cost = _entry.getValue();
                System.out.println(source.toString() + " --> " + dst.toString()
                                   + ": " + cost.toString());
            }
        }
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

