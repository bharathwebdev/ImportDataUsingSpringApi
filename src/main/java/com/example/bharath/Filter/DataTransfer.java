package com.example.bharath.Filter;

import java.util.*;

public class DataTransfer extends HashMap<String, Object>{

    public List<Map<String, Object>> transform(List<Map<String, Object>> originalData,List<String> ignoreColumnsName) {
        List<Map<String, Object>> transformedData = new ArrayList<>();

        for (Map<String, Object> element : originalData) {
            Map<String, Object> transformedElement = transformElement(element,ignoreColumnsName);
            transformedData.add(transformedElement);
        }
        return transformedData;
    }

    private Map<String, Object> transformElement(Map<String, Object> element,List<String> ignoreColumnsName) {


        ignoreColumnsName.forEach(el->element.keySet().remove(el));


        return element;
    }



}

