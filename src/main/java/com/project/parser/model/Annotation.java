package com.project.parser.model;

import com.thoughtworks.qdox.model.JavaAnnotation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 04.10.2020.
 */
@EqualsAndHashCode
@ToString
public class Annotation {
    @Getter
    private final String name;
    private final Map<String, String> params;

    public Annotation(JavaAnnotation annotation) {
        this.name = annotation.getType().getName();
        params = annotation.getPropertyMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
    }

    public Map<String, String> getParams() {
        return Collections.unmodifiableMap(params);
    }
}
