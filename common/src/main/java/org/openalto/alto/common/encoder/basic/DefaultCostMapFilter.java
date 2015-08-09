package org.openalto.alto.common.encoder.basic;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

import org.openalto.alto.common.type.CostType;

public class DefaultCostMapFilter
        extends CostFilterParam<String> {

    public DefaultCostMapFilter(CostType type) {
        super(type);
    }

}
