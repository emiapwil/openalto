package org.openalto.alto.common.type;

public class ALTOData<M, D> {

    private M m_meta;
    private D m_data;

    public ALTOData(M meta, D data) {
        m_meta = meta;
        m_data = data;
    }

    public M getMeta() {
        return m_meta;
    }

    public D getData() {
        return m_data;
    }
}
