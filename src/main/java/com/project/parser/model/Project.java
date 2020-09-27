package com.project.parser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by konstantin on 27.09.2020.
 */
@Getter
@Setter
@EqualsAndHashCode
public class Project {
    private String name;
    private Path path;
    private Collection<Module> modules = new ArrayList<>();
    private Collection<Clazz> classses;
    private Collection<Resource> resources;

    public void addModule(Module module) {
        modules.add(module);
    }

    public boolean isMultiModuleProject() {
        return modules != null && !modules.isEmpty();
    }

    public Collection<Module> getModules() {
        return modules != null ? modules : Collections.emptyList();
    }

    public Collection<Clazz> getClasses() {
        return classses != null ? classses : Collections.emptyList();
    }

    public Collection<Resource> getResources() {
        return resources != null ? resources : Collections.emptyList();
    }
}
