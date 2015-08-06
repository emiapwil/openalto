package org.openalto.alto.common.decoder;

import com.fasterxml.jackson.databind.JsonNode;

public interface ALTODecoder<T> {

    public T decode(String text);

    public T decode(JsonNode node);

}
