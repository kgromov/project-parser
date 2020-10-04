package com.project.parser.service;

import com.project.parser.model.Module;
import com.project.parser.model.Project;
import lombok.RequiredArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by konstantin on 27.09.2020.
 */
@RequiredArgsConstructor
public class MavenProjectParser implements ProjectParser {
    // FIXME: DI in next versions
    private final ClassParser classParser;
    private final ResourceParser resourceParser;

    @Override
    public Project parseProject(Path projectRoot) throws Exception {
        Path pom = projectRoot.resolve("pom.xml");
        Project project = new Project();
        project.setName(projectRoot.getFileName().toString());
        project.setPath(projectRoot);

        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = dBuilder.parse(pom.toFile());
        NodeList moduleNodes = document.getElementsByTagName("module");

        if (moduleNodes.getLength() == 0) {
            setupProjectWithoutModules(project);
        }
        else {
            setupProjectWithModules(project, moduleNodes);
        }

        return project;
    }

    private void setupProjectWithModules(Project project, NodeList moduleNodes) {
        List<Module> modules = new ArrayList<>();
        for (int i = 0; i < moduleNodes.getLength(); i++) {
            Node moduleNode = moduleNodes.item(i);
            String moduleName = moduleNode.getTextContent();
            Module module = new Module();
            module.setName(moduleName);
            module.setPath(project.getPath().resolve(moduleName));
            // FIXME: in next versions should be lazy
            module.setClasses(classParser.parse(module.getPath()));
            module.setResources(resourceParser.parse(module.getPath()));
            project.addModule(module);
//            modules.add(module);
        }
        // 16s vs 18s consequently - WTF?
       /* modules.parallelStream().forEach(m ->
        {
            ClassParser parser = new ClassParser(new FieldParser());
            parser.parse(m.getPath());
        });
        project.setModules(modules);*/
    }

    private void setupProjectWithoutModules(Project project) {
        project.setClassses(classParser.parse(project.getPath()));
        project.setResources(resourceParser.parse(project.getPath()));
    }
}
