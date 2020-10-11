package com.project.parser.config;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by konstantin on 04.10.2020.
 */
@Slf4j
@Getter
public class ParserConfig {
    private static ParserConfig INSTANCE;

    private Path projectPath;
    private ProjectType type = ProjectType.DEFAULT;
    private boolean isEnableEnpointsParser;
    private Pattern modulesRegExp;
    private Set<String> inclusionModules = new HashSet<>();
    private Set<String> exclusionModules = new HashSet<>();

    private ParserConfig(Properties properties) {
        String path = getPropertyValue(properties, "project.path");
        this.projectPath = Paths.get(path);
        String type = getPropertyValue(properties,"project.type");
        this.type = ProjectType.getType(type);
        String endpointParserEnabled = getPropertyValue(properties, " project.enableEnpointsParsere");
        this.isEnableEnpointsParser = Boolean.parseBoolean(endpointParserEnabled);
        String inclusions = getPropertyValue(properties,"modules.include");
        if (StringUtils.isNotBlank(inclusions)) {
            Collections.addAll(inclusionModules, inclusions.split(","));
        }
        String exclusions = getPropertyValue(properties,"modules.exclude");
        if (StringUtils.isNotBlank(exclusions)) {
            Collections.addAll(exclusionModules, exclusions.split(","));
        }
        String modulesPattern = getPropertyValue(properties,"modules.pattern");
        if (StringUtils.isNotBlank(exclusions)) {
            try {
                this.modulesRegExp = Pattern.compile(modulesPattern);
            } catch (Exception e) {
                log.warn("Invalid regExp pattern: {}", modulesPattern);
            }
        }
    }

    @SneakyThrows
    private static Properties loadProperties() {
        Properties properties = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        properties.load(inputStream);
        return properties;
    }

    private static String getPropertyValue(Properties properties, String propertyName) {
        return properties.get(propertyName).toString().trim();
    }


    public static ParserConfig getConfig() {
        if (INSTANCE == null) {
            synchronized (ParserConfig.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ParserConfig(loadProperties());
                }
            }
        }
        return INSTANCE;
    }

    public Set<String> getInclusionModules() {
        return Collections.unmodifiableSet(inclusionModules);
    }

    public Set<String> getExclusionModules() {
        return Collections.unmodifiableSet(exclusionModules);
    }


    public boolean hasInclusions() {
        return !inclusionModules.isEmpty();
    }

    public boolean hasExclusions() {
        return !exclusionModules.isEmpty();
    }

    public boolean hasModulesRegExp() {
        return Objects.nonNull(modulesRegExp);
    }
}
