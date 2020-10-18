package com.project.parser.output;

import com.project.parser.model.Project;

import java.nio.file.Path;

/**
 * Created by konstantin on 18.10.2020.
 */
public interface Exporter {
    void export(Project project);

    Path getOutputFile();
}
