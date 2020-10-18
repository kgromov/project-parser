package com.project.parser.utils;

import com.project.parser.model.Annotation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by konstantin on 18.10.2020.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotationUtils {

    public static Optional<String> getAnnotationValue(Map<String, Annotation> annotations, String annotationName) {
        return annotations.entrySet().stream()
                .filter(e -> e.getKey().endsWith(annotationName))
                .map(Map.Entry::getValue)
                .map(Annotation::getParams)
                .map(p -> p.get("value"))
                .filter(Objects::nonNull)
                .findFirst();
    }

    public boolean hasAnnotation(Map<String, Annotation> annotations, Class<?> clazz) {
        return annotations.containsKey(clazz.getName());
    }


    public static boolean hasAnnotationByName(Map<String, Annotation> annotations, String annotationName) {
        return annotations.keySet().stream().anyMatch(name -> name.endsWith(annotationName));
    }
}
