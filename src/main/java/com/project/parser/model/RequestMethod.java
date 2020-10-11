package com.project.parser.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by konstantin on 04.10.2020.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestMethod {
    private String methodName;
    private String from;
    private String to;
    private HttpMethod httpMethod;
}
