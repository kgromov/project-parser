package com.project.parser.service;

import com.project.parser.model.Project;

import java.nio.file.Path;

/**
 * Created by konstantin on 27.09.2020.
 */
public interface ProjectParser {
    Project parseProject(Path projectRoot) throws Exception;
}
