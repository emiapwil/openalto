package org.openalto.alto.client;

import javax.ws.rs.core.Response;

public abstract class ALTOResponseParser {

    protected boolean success(Response.StatusType status) {
        if (status == null)
            return false;
        return Response.Status.Family.SUCCESSFUL.equals(status.getFamily());
    }

    public abstract ALTOResponse parse(Response response);

    public static class ALTOResponseBase implements ALTOResponse {

        private ALTORequest m_request;
        private Response m_response;
        private Object m_data;
        private boolean m_isError;

        public ALTOResponseBase(boolean isError, Response response, Object data) {
            m_isError = isError;
            m_response = response;
            m_data = data;
        }

        public ALTOResponseBase(ALTOResponse response) {
            m_request = response.getRequest();
            m_response = response.getResponse();
            m_data = response.get();
            m_isError = response.isError();
        }

        public ALTOResponseBase setRequest(ALTORequest request) {
            m_request = request;
            return this;
        }

        @Override
        public ALTORequest getRequest() {
            return m_request;
        }

        public ALTOResponseBase setResponse(Response response) {
            m_response = response;
            return this;
        }

        @Override
        public Response getResponse() {
            return m_response;
        }

        public ALTOResponseBase setData(Object data) {
            m_data = data;
            return this;
        }

        @Override
        public Object get() {
            return m_data;
        }

        public ALTOResponseBase setIsError(boolean isError) {
            m_isError = isError;
            return this;
        }

        @Override
        public boolean isError() {
            return m_isError;
        }
    }
}
