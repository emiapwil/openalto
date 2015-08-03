package org.openalto.alto.common;

import javax.ws.rs.core.MediaType;

public class ALTOMediaType extends MediaType {

    public static final String
    DIRECTORY = "application/alto-directory+json";

    public static final MediaType
    DIRECTORY_TYPE = new ALTOMediaType("directory");

    public static final String
    NETWORK_MAP = "application/alto-networkmap+json";

    public static final MediaType
    NETWORK_MAP_TYPE = new ALTOMediaType("networkmap");

    public static final String
    NETWORK_MAP_FILTER = "application/alto-networkmapfilter+json";

    public static final MediaType
    NETWORK_MAP_FILTER_TYPE = new ALTOMediaType("networkmapfilter");

    public static final String
    COST_MAP = "application/alto-costmap+json";

    public static final MediaType
    COST_MAP_TYPE = new ALTOMediaType("costmap");

    public static final String
    COST_MAP_FILTER = "application/alto-costmapfilter+json";

    public static final MediaType
    COST_MAP_FILTER_TYPE = new ALTOMediaType("costmapfilter");


    public static final String
    ENDPOINT_PROP = "application/alto-endpointprop+json";

    public static final MediaType
    ENDPOINT_PROP_TYPE = new ALTOMediaType("endpointprop");


    public static final String
    ENDPOINT_PROP_PARAMS = "application/alto-endpointpropparams+json";

    public static final MediaType
    ENDPOINT_PROP_PARAMS_TYPE = new ALTOMediaType("endpointpropparams");


    public static final String
    ENDPOINT_COST = "application/alto-endpointcost+json";

    public static final MediaType
    ENDPOINT_COST_TYPE = new ALTOMediaType("endpointcost");

    public static final String
    ENDPOINT_COST_PARAMS = "application/alto-endpointcostparams+json";

    public static final MediaType
    ENDPOINT_COST_PARAMS_TYPE = new ALTOMediaType("endpointcostparams");

    public static final String
    ERROR = "application/alto-error+json";

    public static final MediaType
    ERROR_TYPE = new ALTOMediaType("error");

    public ALTOMediaType(String altoType) {
        super("application", String.format("alto-%s+json", altoType));
    }
}
