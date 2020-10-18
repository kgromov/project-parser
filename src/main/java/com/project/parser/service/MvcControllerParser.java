package com.project.parser.service;

import com.project.parser.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.parser.utils.AnnotationUtils.getAnnotationValue;
import static com.project.parser.utils.AnnotationUtils.hasAnnotationByName;

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
                .filter(m -> hasAnnotationByName(m.getAnnotations(), "Mapping"))
                .map(m -> {
                    String methodName = m.getName();
                    String from = getAnnotationValue(m.getAnnotations(), "Mapping").orElse("");
                    String viewName = getViewName(m).orElse("");
                    String resolvedViewByClassConstant = fields.getOrDefault(viewName, viewName);
                    String to = commonPathPrefix + '/' + resolvedViewByClassConstant;
                    HttpMethod httpMethod = getAnnotationValue(m.getAnnotations(), "method")
                            .map(HttpMethod::valueOf).orElse(HttpMethod.GET);
                    return new RequestMethod(methodName, from, to, httpMethod, m.getArguments());
                })
                .collect(Collectors.toList());
    }

    private Optional<String> getViewName(Method method) {
        if (method.getReturnType().toLowerCase().contains("void")) {
            return Optional.empty();
        }
        String methodSourceCode = method.getMethodSourceCode();
        int lengthOfReturnStatement = "return".length();
        int indexOfReturn = methodSourceCode.lastIndexOf("return");
        int lastIndexOfCatch = methodSourceCode.lastIndexOf("catch");
        int lastIndexOfSemiColon;
        if (indexOfReturn < lastIndexOfCatch) {
            lastIndexOfSemiColon = methodSourceCode.substring(indexOfReturn, lastIndexOfCatch)
                    .lastIndexOf(';');
        } else {
            lastIndexOfSemiColon = methodSourceCode.lastIndexOf(';');
        }
        return Optional.of(methodSourceCode.substring(indexOfReturn + lengthOfReturnStatement + 1, lastIndexOfSemiColon).trim());
    }
}
