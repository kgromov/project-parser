package com.project.parser.service;

import com.project.parser.model.Field;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 27.09.2020.
 */
public class FieldParser {

    public Collection<Field> parse(JavaClass clazz) {
        List<JavaField> fields = clazz.getFields();
        return fields.stream()
                .map(f -> new Field(f.getName(),
                        f.getType().getFullyQualifiedName(),
                        f.getInitializationExpression(),
                        f.isStatic()))
                .collect(Collectors.toList());
    }
}
