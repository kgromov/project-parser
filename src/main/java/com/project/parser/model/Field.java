package com.project.parser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Created by konstantin on 27.09.2020.
 */
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Field {
    private final String name;
    private final String type;
    private final String value;
    private final boolean isStatic;
}
