package com.example.bharath.Filter;
import java.util.*;

public class TransformedMap extends LinkedHashMap<String, Object> {

    public TransformedMap(Object name) {
        System.out.println(name);
    }

    @Override
    public Object put(String key, Object value) {
        if (!key.equals("id")) {
            return super.put(key, value);
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        for (Map.Entry<? extends String, ? extends Object> entry : m.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
        }
    }

}
