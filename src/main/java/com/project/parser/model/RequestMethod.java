package com.project.parser.model;

import com.thoughtworks.qdox.model.JavaMethod;

import java.util.Map;

/**
 * Created by konstantin on 04.10.2020.
 */
public class RequestMethod extends Method {
    private Map<String, String> endpointToView;

    public RequestMethod(JavaMethod method) {
        super(method);
    }
}
