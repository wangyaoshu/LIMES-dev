package org.aksw.limes.core.cache;

import org.aksw.limes.core.data.Instance;

public interface Cache {

    void addTriple(String string, String string2, String string3);

    Instance getInstance(String string);

    int size();

}