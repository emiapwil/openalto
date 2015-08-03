package org.openalto.alto.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import static org.openalto.alto.client.ALTOResponseParser.ALTOResponseBase;
import org.openalto.alto.common.resource.ResourceEntry;

public abstract class ALTORequestBuilder {

    protected Client m_client;
    protected ALTOResponseParser m_parser;

    public ALTORequestBuilder(Client client, ALTOResponseParser parser) {
        m_client = client;
        m_parser = parser;
    }

    public Client getClient() {
        return m_client;
    }

    public ALTOResponseParser getParser() {
        return m_parser;
    }

    public abstract ALTORequest request(ResourceEntry resource, Object params);

    protected static class ALTORequestBase implements ALTORequest {

        private ResourceEntry m_resource;
        private Object m_params;
        private Invocation m_invocation;
        private ALTOResponseParser m_parser;

        public ALTORequestBase(ResourceEntry resource,
                               Object params, ALTOResponseParser parser) {
            m_resource = resource;
            m_params = params;
            m_parser = parser;
        }

        public ALTORequestBase setInvocation(Invocation invocation) {
            m_invocation = invocation;
            return this;
        }

        @Override
        public ALTOResponse invoke() {
            ALTOResponse response = m_parser.parse(m_invocation.invoke());
            if (response == null)
                return null;
            return new ALTOResponseBase(response).setRequest(this);
        }

        @Override
        public Invocation getInvocation() {
            return m_invocation;
        }

        @Override
        public ResourceEntry getResourceEntry() {
            return m_resource;
        }

        @Override
        public Object getParams() {
            return m_params;
        }
    }
}
