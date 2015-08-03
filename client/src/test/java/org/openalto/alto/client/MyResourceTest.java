package org.openalto.alto.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MyResourceTest {

    private HttpServer server;
    private Client client;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = TestServer.startServer();
        // create the client
        client = ClientBuilder.newClient();

        // Uncomment the following line to enable JSON support for client
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIRD() {
        String uri = TestServer.BASE_URI + "test" + "/" + "ird";
        WebTarget target = client.target(uri);
        assertEquals(target.request().buildGet().invoke().getStatus(), 501);
    }
}
