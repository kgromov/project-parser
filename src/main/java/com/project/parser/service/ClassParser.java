package com.project.parser.service;

import com.project.parser.model.Annotation;
import com.project.parser.model.Clazz;
import com.project.parser.model.Method;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 27.09.2020.
 */
@RequiredArgsConstructor
public class ClassParser {
    private final FieldParser fieldParser;

    public Collection<Clazz> parse(Path sourceDir) {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        builder.addSourceTree(sourceDir.toFile());
        Collection<JavaClass> classes = builder.getClasses();
        return classes.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private Clazz map(JavaClass javaClass) {
        Clazz clazz = new Clazz();
        clazz.setName(javaClass.getName());
        clazz.setFullName(javaClass.getFullyQualifiedName());
        clazz.setPath(Paths.get(javaClass.getSource().getURL().toURI()));
        clazz.setPackageName(javaClass.getPackageName());
        clazz.setFields(fieldParser.parse(javaClass));
        javaClass.getMethods().stream().map(Method::new).forEach(clazz::addMethod);
        javaClass.getAnnotations().stream().map(Annotation::new).forEach(clazz::addAnnotation);
        return clazz;
    }
}
