package com.project.parser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by konstantin on 27.09.2020.
 */
@Getter
@Setter
@EqualsAndHashCode
public class Module {
    private String name;
    private Path path;
    private Collection<Clazz> classes;
    private Collection<Resource> resources;

    public Collection<Clazz> getClasses() {
        return classes != null ? classes : Collections.emptyList();
    }

}
