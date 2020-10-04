package com.project.parser.service;

import com.project.parser.model.Resource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * Created by konstantin on 27.09.2020.
 */
public class ResourceParser {
    private FileVisitor fileVisitor;

    @SneakyThrows
    public Collection<Resource> parse(Path sourceDir) {
        CommonRecourseVisitor fileVisitor = new CommonRecourseVisitor();
        List<Path> resourceDirs = Files.find(sourceDir, Short.MAX_VALUE, isResourceFolder()).collect(Collectors.toList());
        for (Path resourceDir : resourceDirs) {
            Files.walkFileTree(resourceDir, fileVisitor);
        }
        return fileVisitor.getResources();
    }

    protected BiPredicate<Path, BasicFileAttributes> isResourceFolder() {
        return (path, attrs) -> {
            if (!attrs.isDirectory()) {
                return false;
            }
            Path parent = path.getParent();
            return path.getFileName().toString().equals("resources")
                    && parent != null
                    && (parent.getFileName().toString().equals("main")
                    || parent.getFileName().toString().equals("test"));
        };
    }

    @RequiredArgsConstructor
    private static class CommonRecourseVisitor implements FileVisitor<Path> {
        private final Collection<Resource> resources = new ArrayList<>();

        public Collection<Resource> getResources() {
            return Collections.unmodifiableCollection(resources);
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            String fileName = file.getFileName().toString();
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            Resource resource = new Resource(fileName, extension, file);
            resources.add(resource);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }
}
