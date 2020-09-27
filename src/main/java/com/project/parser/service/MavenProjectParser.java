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

        List<Module> modules = new ArrayList<>();
        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = dBuilder.parse(pom.toFile());
        NodeList moduleNodes = document.getElementsByTagName("module");
        for (int i = 0;  i < moduleNodes.getLength(); i++) {
            Node moduleNode = moduleNodes.item(i);
            String moduleName = moduleNode.getTextContent();
            try {
                Module module = new Module();
                module.setName(moduleName);
                module.setPath(projectRoot.resolve(moduleName));
                // FIXME: in next versions should be lazy
//                module.setClasses(classParser.parse(module.getPath()));
//            module.setResources(resourceParser.parse(module.getPath()));
//                project.addModule(module);
                modules.add(module);
            }
            catch (Exception e) {
                int a = 1;
            }
        }
        // 16s vs 18s consequently - WTF?
        modules.parallelStream().forEach(m ->
        {
            ClassParser parser = new ClassParser(new FieldParser());
            parser.parse(m.getPath());
        });
        project.setModules(modules);
        return project;
    }
}
