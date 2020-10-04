package com.project.parser.config;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 04.10.2020.
 */
public enum ProjectType {
    DEFAULT,
    MAVEN,
    GRADLE;

    private final static Map<String, ProjectType> TYPES = Arrays.stream(values())
            .collect(Collectors.toMap(Enum::name, Function.identity()));

    public static ProjectType getType(String type) {
        return StringUtils.isNotBlank(type)
                ? TYPES.getOrDefault(type.toUpperCase(), DEFAULT)
                : DEFAULT;
    }
}
