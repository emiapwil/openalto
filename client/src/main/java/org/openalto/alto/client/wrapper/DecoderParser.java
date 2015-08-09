package org.openalto.alto.client.wrapper;

import javax.ws.rs.core.Response;

import org.openalto.alto.client.ALTOResponse;
import org.openalto.alto.client.ALTOResponseParser;

import org.openalto.alto.common.decoder.ALTODecoder;

public class DecoderParser<T> extends ALTOResponseParser {

    private ALTODecoder<T> m_decoder;

    public DecoderParser(ALTODecoder<T> decoder) {
        m_decoder = decoder;
    }

    @Override
    public ALTOResponse parse(Response response) {
        String data = response.readEntity(String.class);
        return new ALTOResponseBase(false, response, m_decoder.decode(data));
    }
}
