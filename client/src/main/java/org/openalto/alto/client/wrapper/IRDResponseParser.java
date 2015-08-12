package org.openalto.alto.client.wrapper;

import java.util.List;

import org.openalto.alto.common.resource.ResourceEntry;

import org.openalto.alto.common.type.ALTOData;
import org.openalto.alto.common.type.MetaData;

import org.openalto.alto.common.decoder.basic.DefaultIRDDecoder;

public class IRDResponseParser
        extends DecoderParser<ALTOData<MetaData, List<ResourceEntry>>> {

    public IRDResponseParser() {
        super(new DefaultIRDDecoder());
    }
}
