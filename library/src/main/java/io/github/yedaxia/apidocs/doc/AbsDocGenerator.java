package io.github.yedaxia.apidocs.doc;

import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.LogUtils;
import io.github.yedaxia.apidocs.Utils;
import io.github.yedaxia.apidocs.parser.AbsControllerParser;
import io.github.yedaxia.apidocs.parser.ControllerNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsDocGenerator{

    private AbsControllerParser controllerParser;
    private IControllerDocBuilder controllerDocBuilder;
    private List<String> docFileNameList = new ArrayList<>();
    private List<ControllerNode> controllerNodeList = new ArrayList<>();

    public AbsDocGenerator(AbsControllerParser controllerParser, IControllerDocBuilder controllerDocBuilder) {
        this.controllerParser = controllerParser;
        this.controllerDocBuilder = controllerDocBuilder;
    }

    /**
     * generate api Docs
     */
    public void generateDocs(){
        LogUtils.info("generate api docs start...");
        generateControllersDocs();
        generateIndex(docFileNameList);
        LogUtils.info("generate api docs done !!!");
    }

    private void generateControllersDocs(){
        File[] controllerFiles = DocContext.getControllerFiles();
        for (File controllerFile : controllerFiles) {
            try {
                ControllerNode controllerNode = controllerParser.parse(controllerFile);
                if(controllerNode.getRequestNodes().isEmpty()){
                    continue;
                }
                controllerNodeList.add(controllerNode);
                LogUtils.info("start to generate docs for controller file : %s", controllerFile.getName());
                String controllerDocs = controllerDocBuilder.buildDoc(controllerNode);
                String docName = controllerNode.getDescription();
                docFileNameList.add(docName);
                Utils.writeToDisk(new File(DocContext.getDocPath(), docName+".html"),controllerDocs);
                LogUtils.info("success to generate docs for controller file : %s", controllerFile.getName());
            } catch (IOException e) {
                LogUtils.error("generate docs for controller file : "+controllerFile.getName()+" fail", e);
            }
        }

    }

    public List<ControllerNode> getControllerNodeList(){
        return controllerNodeList;
    }

	abstract void generateIndex(List<String> docFileNameList);
}
