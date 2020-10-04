package com.project.parser.model;

import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 04.10.2020.
 */
@Getter
@EqualsAndHashCode
@ToString
public class Method {
    private final String name;
    private final String returnType;
    private final Map<String, String> arguments;
    private final Map<String, Annotation> annotations;

    public Method(JavaMethod method) {
        this.name = method.getName();
        this.returnType = method.getReturnType().getFullyQualifiedName();
        this.arguments = method.getParameters().stream()
                .collect(Collectors.toMap(JavaParameter::getName, p -> p.getType().getFullyQualifiedName()));
        this.annotations = method.getAnnotations().stream()
                .map(Annotation::new)
                .collect(Collectors.toMap(Annotation::getName, Function.identity()));
    }

    public Map<String, String> getArguments() {
        return Collections.unmodifiableMap(arguments);
    }

    public Map<String, Annotation> getAnnotations() {
        return Collections.unmodifiableMap(annotations);
    }
}
