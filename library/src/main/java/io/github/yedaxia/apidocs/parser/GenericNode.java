package io.github.yedaxia.apidocs.parser;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * generic node of model class
 *
 * @author yeguozhong yedaxia.github.com
 */
public class GenericNode {

    private Type classType; // for source
    private Class modelClass; //for reflection
    private String placeholder;
    private File fromJavaFile;
    private List<GenericNode> childGenericNode = new ArrayList<>();

    public GenericNode(){}

    public Type getClassType() {
        return classType;
    }

    public void setClassType(Type classType) {
        this.classType = classType;
    }

    public File getFromJavaFile() {
        return fromJavaFile;
    }

    public void setFromJavaFile(File fromJavaFile) {
        this.fromJavaFile = fromJavaFile;
    }

    public List<GenericNode> getChildGenericNode() {
        return childGenericNode;
    }

    public void setChildGenericNode(List<GenericNode> childGenericNode) {
        this.childGenericNode = childGenericNode;
    }

    public void addChildGenericNode(GenericNode childNode){
        this.childGenericNode.add(childNode);
    }

    public String getPlaceholder() {
        return placeholder;
    }


    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Class getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class modelClass) {
        this.modelClass = modelClass;
    }
}
