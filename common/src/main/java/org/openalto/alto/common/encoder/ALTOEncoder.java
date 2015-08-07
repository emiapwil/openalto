package org.openalto.alto.common.encoder;

import com.fasterxml.jackson.databind.JsonNode;

public interface ALTOEncoder {

    public boolean canEncode(Object obj);

    public String encodeAsString(Object obj);

    public JsonNode encode(Object obj);
}
