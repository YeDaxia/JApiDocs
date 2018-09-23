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
    private List<Link> docFileLinkList = new ArrayList<>();
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
        generateIndex(docFileLinkList);
        LogUtils.info("generate api docs done !!!");
    }

    private void generateControllersDocs(){
        File[] controllerFiles = DocContext.getControllerFiles();
        final String docsPathName = "docs";
        File docPath = new File(DocContext.getDocPath(),docsPathName);
        if(!docPath.exists()){
            docPath.mkdirs();
        }
        for (File controllerFile : controllerFiles) {
            try {
                LogUtils.info("start to parse controller file : %s", controllerFile.getName());
                ControllerNode controllerNode = controllerParser.parse(controllerFile);
                if(controllerNode.getRequestNodes().isEmpty()){
                    continue;
                }
                controllerNodeList.add(controllerNode);
                LogUtils.info("start to generate docs for controller file : %s", controllerFile.getName());
                final String controllerDocs = controllerDocBuilder.buildDoc(controllerNode);
                final String docName = String.format("%s_%s.html", controllerNode.getPackageName().replace(".","_"), controllerNode.getClassName());
                docFileLinkList.add(new Link(controllerNode.getDescription(), String.format("%s/%s", docsPathName, docName)));
                Utils.writeToDisk(new File(docPath, docName),controllerDocs);
                LogUtils.info("success to generate docs for controller file : %s", controllerFile.getName());
            } catch (IOException e) {
                LogUtils.error("generate docs for controller file : "+controllerFile.getName()+" fail", e);
            }
        }

    }

    public List<ControllerNode> getControllerNodeList(){
        return controllerNodeList;
    }

	abstract void generateIndex(List<Link> docFileLinkList);
}
