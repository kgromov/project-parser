package com.project.parser.output;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.project.parser.config.ParserConfig;
import com.project.parser.model.Clazz;
import com.project.parser.model.Module;
import com.project.parser.model.Project;
import com.project.parser.output.model.ReportLine;
import com.project.parser.service.MvcControllerParser;
import com.project.parser.utils.AnnotationUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by konstantin on 18.10.2020.
 */
@Slf4j
public class CsvExporter implements Exporter {
    private final MvcControllerParser mvcControllerParser;

    public CsvExporter() {
        mvcControllerParser = new MvcControllerParser();
    }

    @Override
    @SneakyThrows
    public void export(Project project) {
        try (Writer writer = new FileWriter(getOutputFile().toString())) {
            StatefulBeanToCsv sbc = new StatefulBeanToCsvBuilder(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();

            List<ReportLine> list = new ArrayList<>();
            List<Module> modules = new ArrayList<>(project.getModules());
            for (int i = 0; i < Math.max(modules.size(), 1); i++) {
                String moduleName = modules.isEmpty() ? "" : modules.get(i).getName();
                Collection<Clazz> classes = modules.isEmpty() ? project.getClasses() : modules.get(i).getClasses();
                classes.stream()
                        .filter(this::isClassIncludedIntoReport)
                        .forEach(clazz ->
                        {
                            String clazzName = clazz.getFullName();
                            mvcControllerParser.parse(clazz).forEach(method -> {
                                ReportLine line = ReportLine.builder()
                                        .moduleName(moduleName)
                                        .className(clazzName)
                                        .methodName(method.getName())
                                        .fromEndpoint(method.getFrom())
                                        .toView(method.getTo())
                                        .httpMethod(method.getHttpMethod().name())
                                        .parameters(method.argumentsToString())
                                        .build();
                                list.add(line);
                            });
                        });
            }
            sbc.write(list);
        }
    }

    @Override
    @SneakyThrows
    public Path getOutputFile() {
        ParserConfig config = ParserConfig.getConfig();
        Path projectPath = config.getProjectPath();
        String outputFileName = Files.exists(projectPath) ? projectPath.getFileName().toString() : "documentation";
        Path outputDir = Paths.get(config.getOutputDir());
        Files.createDirectories(outputDir);
        return outputDir.resolve(outputFileName + ".csv");
    }

    protected boolean isClassIncludedIntoReport(Clazz clazz) {
        return clazz.hasAnnotations() && AnnotationUtils.hasAnnotationByName(clazz.getAnnotations(), "Controller");
    }
}
