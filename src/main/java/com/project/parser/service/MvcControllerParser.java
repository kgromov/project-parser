package com.project.parser.service;

import com.project.parser.model.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 04.10.2020.
 */
public class MvcControllerParser {

    public List<RequestMethod> parse(Clazz clazz) {
        Map<String, String> fields = clazz.getFields().stream()
                .filter(field -> field.getType().equals(String.class.getName()))
                .collect(Collectors.toMap(Field::getName, Field::getValue));
        String commonPathPrefix = getAnnotationValue(clazz.getAnnotations(), "RequestMapping").orElse("");
        return clazz.getMethods().stream()
                .filter(Method::hasAnnotations)
                .filter(m -> m.hasAnnotationByName("Mapping"))
                .map(m -> {
                    String methodName = m.getName();
                    String from = getAnnotationValue(m.getAnnotations(), "Mapping").orElse("");
                    Optional<String> viewName = getViewName(m);
                    String to = commonPathPrefix + '/' + viewName.orElse("");
                    HttpMethod httpMethod = getAnnotationValue(m.getAnnotations(), "method")
                            .map(HttpMethod::valueOf).orElse(HttpMethod.GET);
                    return new RequestMethod(methodName, from, to, httpMethod);
                })
                .collect(Collectors.toList());
    }

    private Optional<String> getAnnotationValue(Map<String, Annotation> annotations, String annotationName) {
        return annotations.entrySet().stream()
                .filter(e -> e.getKey().endsWith(annotationName))
                .map(Map.Entry::getValue)
                .map(Annotation::getParams)
                .map(p -> p.get("value"))
                .filter(Objects::nonNull)
                .findFirst();
    }

    private Optional<String> getViewName(Method method) {
        String methodSourceCode = method.getMethodSourceCode();
        int indexOfReturn = methodSourceCode.lastIndexOf("return");
        int lastIndexOfCatch = methodSourceCode.lastIndexOf("catch");
        int lastIndexOfSemiColon = -1;
        if (indexOfReturn < lastIndexOfCatch) {
            lastIndexOfSemiColon = methodSourceCode.substring(indexOfReturn, lastIndexOfCatch)
                    .lastIndexOf(';');
        } else {
            lastIndexOfSemiColon = methodSourceCode.lastIndexOf(';');
        }
        return Optional.of(methodSourceCode.substring(indexOfReturn + 1, lastIndexOfSemiColon).trim());
    }
}
