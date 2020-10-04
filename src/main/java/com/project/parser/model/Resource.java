package com.project.parser.model;

import lombok.*;

import java.nio.file.Path;

/**
 * Created by konstantin on 27.09.2020.
 */
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Resource {
    private final String name;
    private final String extension;
    private final Path path;
}
