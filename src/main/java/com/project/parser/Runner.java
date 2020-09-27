package com.project.parser;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.library.ClassLibrary;
import com.thoughtworks.qdox.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by konstantin on 27.09.2020.
 */
public class Runner {
    public static void main(String[] args) {
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
