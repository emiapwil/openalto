package org.openalto.alto.common.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import javax.script.SimpleBindings;

import org.openalto.alto.common.type.Capability;

public class ResourceEntry {

    /* the resource-id */
    private String m_rid;

    /* the URI */
    private URI m_uri;

    private ResourceType m_type;

    private Set<Capability<?>> m_capabilities;

    private List<URI> m_dependencies;

    private boolean m_isDefault = false;

    private SimpleBindings m_wildcardData = new SimpleBindings();

    public ResourceEntry(URI uri, ResourceType type) {
        m_uri = uri;
        m_type = type;
    }

    public ResourceEntry(URI uri, String type) {
        m_uri = uri;
        m_type = new ResourceType(type);
    }

    public ResourceEntry(String uri, String type) throws URISyntaxException {
        m_uri = new URI(uri);
        m_type = new ResourceType(type);
    }

    public ResourceEntry(String uri, ResourceType type)
                                    throws URISyntaxException {
        m_uri = new URI(uri);
        m_type = type;
    }

    public ResourceEntry setResourceID(String rid) {
        m_rid = rid;
        return this;
    }

    public String getResourceId() {
        return m_rid;
    }

    public ResourceEntry setURI(URI uri) {
        m_uri = uri;
        return this;
    }

    public ResourceEntry setURI(String uri) throws URISyntaxException {
        m_uri = new URI(uri);
        return this;
    }

    public URI getURI() {
        return m_uri;
    }

    public ResourceEntry setType(ResourceType type) {
        m_type = type;
        return this;
    }

    public ResourceEntry setType(String type) {
        m_type = new ResourceType(type);
        return this;
    }

    public ResourceType getType() {
        return m_type;
    }

    public ResourceEntry setCapabilities(Set<Capability<?>> cap) {
        m_capabilities = cap;
        return this;
    }

    public ResourceEntry addCapability(Capability<?> capability) {
        m_capabilities.add(capability);
        return this;
    }

    public Set<Capability<?>> getCapabilities() {
        return m_capabilities;
    }

    public ResourceEntry setDefault(boolean isDefault) {
        m_isDefault = isDefault;
        return this;
    }

    public boolean isDefault() {
        return m_isDefault;
    }

    public void putData(String name, Object data) {
        m_wildcardData.put(name, data);
    }

    public Object getData(String name) {
        return m_wildcardData.get(name);
    }
}
