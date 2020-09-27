package com.project.parser.model;

import lombok.Getter;

import java.nio.file.Path;
import java.util.Collection;

/**
 * Created by konstantin on 27.09.2020.
 */
@Getter
// TODO: or apply composite pattern as it's walking though tree
public abstract class SourceUnit {
    private String name;
    private Path uri;
    private Collection<? extends SourceUnit> childrenUnits;

    public abstract Collection<? extends SourceUnit> getSpecificChildUnits();
}
