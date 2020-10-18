package com.project.parser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.*;

/**
 * Created by konstantin on 27.09.2020.
 */
@Getter
@Setter
@EqualsAndHashCode
public class Clazz {
    private String name;
    private String fullName;
    private String packageName;
    private Path path;
    private Collection<Field> fields = new ArrayList<>();
    private Collection<Method> methods = new ArrayList<>();
    private final Map<String, Annotation> annotations = new LinkedHashMap<>();

    public void addField(Field field) {
        fields.add(field);
    }

    public Collection<Field> getFields() {
        return Collections.unmodifiableCollection(fields);
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    public Collection<Method> getMethods() {
        return Collections.unmodifiableCollection(methods);
    }

    public void addAnnotation(Annotation annotation) {
        annotations.put(annotation.getName(), annotation);
    }

    public Map<String, Annotation> getAnnotations() {
        return Collections.unmodifiableMap(annotations);
    }

    public boolean hasAnnotations() {
        return !annotations.isEmpty();
    }
}
