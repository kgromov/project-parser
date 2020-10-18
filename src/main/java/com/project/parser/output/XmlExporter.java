package com.project.parser.output;

import com.project.parser.config.ParserConfig;
import com.project.parser.model.Clazz;
import com.project.parser.model.Module;
import com.project.parser.model.Project;
import com.project.parser.model.RequestMethod;
import com.project.parser.service.MvcControllerParser;
import com.project.parser.utils.AnnotationUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * Created by konstantin on 18.10.2020.
 */
@Slf4j
public class XmlExporter implements Exporter {

    private final MvcControllerParser mvcControllerParser;

    public XmlExporter() {
        mvcControllerParser = new MvcControllerParser();
    }


    @Override
    public void export(Project project) {
        long start = System.currentTimeMillis();
        try {
            Document document = initXmlDocument();
            // create the root element
            Element root = document.createElement("Project");
            root.setAttribute("name", project.getName());
            // modules/classes/methods: params, endpoints
            Collection<Module> modules = project.getModules();
            // TODO: template with condition is required - classes from module or project itself
            if (modules.isEmpty()) {
                appendClasses(document, root, project.getClasses());
            } else {
                appendModules(document, root, modules);
            }
            // append root
            document.appendChild(root);
            // add xsl
            Node pi = document.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"index.xsl\"");
            document.insertBefore(pi, root);
            exportToXml(document);
        } finally {
            System.out.println(String.format("Time to export project to xml= %d ms", System.currentTimeMillis() - start));
        }
    }

    @SneakyThrows
    private Document initXmlDocument() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // use factory to get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();
        // create instance of DOM
        return db.newDocument();
    }

    @SneakyThrows
    private void exportToXml(Document document) {
        ParserConfig config = ParserConfig.getConfig();
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        // send DOM to file
        Path projectPath = config.getProjectPath();
        String outputFileName = Files.exists(projectPath) ? projectPath.getFileName().toString() : "documentation";
        Path outputDir = Paths.get(config.getOutputDir());
        Files.createDirectories(outputDir);
        Path outputFilePath = outputDir.resolve(outputFileName + ".xml");
        tr.transform(new DOMSource(document), new StreamResult(new FileOutputStream(outputFilePath.toString())));
    }

    private void appendModules(Document document, Element projectNode, Collection<Module> modules) {
        Element modulesNode = document.createElement("Modules");
        try {
            modules.forEach(module -> {
                Element moduleNode = document.createElement("Module");
                try {
                    moduleNode.setAttribute("name", module.getName());
                    appendClasses(document, moduleNode, module.getClasses());
                } finally {
                    modulesNode.appendChild(moduleNode);
                }
            });
        } finally {
            projectNode.appendChild(modulesNode);
        }
    }

    @SneakyThrows
    private void appendClasses(Document document, Element parentNode, Collection<Clazz> classes) {
        Element classesNode = document.createElement("Classes");
        try {
            classes.stream()
                    .filter(this::isClassIncludedIntoReport)
                    .forEach(clazz ->
                    {
                        Element classNode = document.createElement("Class");
                        try {
                            classNode.setAttribute("name", clazz.getFullName());
                            Element methods = document.createElement("Methods");
                            mvcControllerParser.parse(clazz).forEach(method -> appendMethod(document, classNode, method));
                            classNode.appendChild(methods);
                        } finally {
                            classesNode.appendChild(classNode);
                        }
                    });
        } finally {
            parentNode.appendChild(classesNode);
        }

    }

    protected void appendMethod(Document document, Element methodsNode, RequestMethod method) {
        Element methodNode = document.createElement("Method");
        try {
            Element nameNode = document.createElement("name");
            nameNode.setTextContent(method.getName());
            methodNode.appendChild(nameNode);
            Element fromNode = document.createElement("from");
            fromNode.setTextContent(method.getFrom());
            methodNode.appendChild(fromNode);
            Element toNode = document.createElement("to");
            toNode.setTextContent(method.getTo());
            methodNode.appendChild(toNode);
            Element httpMethodNode = document.createElement("httpMethod");
            httpMethodNode.setTextContent(method.getHttpMethod().name());
            methodNode.appendChild(httpMethodNode);
            Element argumentsdNode = document.createElement("arguments");
            argumentsdNode.setTextContent(method.argumentsToString());
            methodNode.appendChild(argumentsdNode);
        } finally {
            methodsNode.appendChild(methodNode);
        }

    }

    protected boolean isClassIncludedIntoReport(Clazz clazz) {
        return clazz.hasAnnotations() && AnnotationUtils.hasAnnotationByName(clazz.getAnnotations(), "Controller");
    }
}
