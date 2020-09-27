package com.project.parser;

import com.project.parser.model.Project;
import com.project.parser.service.ClassParser;
import com.project.parser.service.FieldParser;
import com.project.parser.service.MavenProjectParser;
import com.project.parser.service.ResourceParser;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.library.ClassLibrary;
import com.thoughtworks.qdox.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by konstantin on 27.09.2020.
 */
@Slf4j
public class Runner {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        MavenProjectParser projectParser = new MavenProjectParser(
                new ClassParser(new FieldParser()),
                new ResourceParser());
        Project project = projectParser.parseProject(Paths.get("D:\\workspace\\konstantin-examples"));
        log.info("Time to parse project = {} ms", System.currentTimeMillis() - start);


        JavaProjectBuilder builder = new JavaProjectBuilder();
        File sourceDir = new File("D:\\workspace\\konstantin-examples");
        builder.addSourceTree(sourceDir);

        JavaClass rdfDataChecker = builder.getClassByName("oracle.checker.RdfDataChecker");
        List<JavaField> fields = rdfDataChecker.getFields();

        Collection<JavaClass> classes = builder.getClasses();
        Collection<JavaModule> modules = builder.getModules();
        Collection<JavaPackage> packages = builder.getPackages();
        Collection<JavaSource> sources = builder.getSources();
        List<JavaClass> javaClasses = new ArrayList<>(classes);
        JavaClass javaClass = javaClasses.get(0);
        ClassLibrary library = javaClass.getJavaClassLibrary();
        List<BeanProperty> beanProperties = javaClass.getBeanProperties();
        JavaSource source = javaClass.getSource();
        List<JavaTypeVariable<JavaGenericDeclaration>> typeParameters = javaClass.getTypeParameters();
        List<DocletTag> tags = javaClass.getTags();
        String comment = javaClass.getComment();
    }
}
