package com.project.parser.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 04.10.2020.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestMethod {
    private String name;
    private String from;
    private String to;
    private HttpMethod httpMethod;
    private Map<String, String> arguments;

    public String argumentsToString() {
        return arguments.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));
    }
}
