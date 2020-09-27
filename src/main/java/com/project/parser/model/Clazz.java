package com.project.parser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.nio.file.Path;
import java.util.Collection;

/**
 * Created by konstantin on 27.09.2020.
 */
@Getter
@EqualsAndHashCode
public class Clazz {
    private String name;
    private String fullName;
    private String packageName;
    private Path path;
    private Collection<Field> fields;

}
