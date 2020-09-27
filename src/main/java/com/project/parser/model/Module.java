package com.project.parser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;

/**
 * Created by konstantin on 27.09.2020.
 */
@Getter
@EqualsAndHashCode
public class Module {
    private String name;
    private Collection<Clazz> classes;
    private Collection<Resource> resources;
}
