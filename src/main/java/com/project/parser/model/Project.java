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
public class Project {
    private String name;
    private Path path;
    private Collection<Module> modules;
    private Collection<Clazz> classses;
    private Collection<Resource> resources;

}
