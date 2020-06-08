package io.github.yedaxia.apidocs.doc;

import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.LogUtils;
import io.github.yedaxia.apidocs.Utils;
import io.github.yedaxia.apidocs.codegenerator.ios.ModelCodeGenerator;
import io.github.yedaxia.apidocs.codegenerator.java.JavaCodeGenerator;
import io.github.yedaxia.apidocs.parser.AbsControllerParser;
import io.github.yedaxia.apidocs.parser.ControllerNode;
import io.github.yedaxia.apidocs.parser.RequestNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsDocGenerator {

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
    public void generateDocs() {
        LogUtils.info("generate api docs start...");
        generateControllersDocs();
        generateIndex(controllerNodeList);
        LogUtils.info("generate api docs done !!!");
    }

    private void generateControllersDocs() {
        File[] controllerFiles = DocContext.getControllerFiles();
        File docPath = new File(DocContext.getDocPath());

        for (File controllerFile : controllerFiles) {
            LogUtils.info("start to parse controller file : %s", controllerFile.getName());
            ControllerNode controllerNode = controllerParser.parse(controllerFile);
            if (controllerNode.getRequestNodes().isEmpty()) {
                continue;
            }

            controllerNode.setSrcFileName(controllerFile.getAbsolutePath());
            final String docFileName = String.format("%s_%s.html", controllerNode.getPackageName().replace(".", "_"), controllerNode.getClassName());
            controllerNode.setDocFileName(docFileName);
            for (RequestNode requestNode : controllerNode.getRequestNodes()) {
                requestNode.setCodeFileUrl(String.format("%s#%s", docFileName, requestNode.getMethodName()));
            }

            controllerNodeList.add(controllerNode);
            LogUtils.info("success to parse controller file : %s", controllerFile.getName());
        }

        for (ControllerNode controllerNode : controllerNodeList) {
            try {
                controllerNode.setControllerNodes(controllerNodeList);
                LogUtils.info("start to generate docs for controller file : %s", controllerNode.getSrcFileName());
                final String controllerDocs = controllerDocBuilder.buildDoc(controllerNode);
                docFileLinkList.add(new Link(controllerNode.getDescription(), String.format("%s", controllerNode.getDocFileName())));
                Utils.writeToDisk(new File(docPath, controllerNode.getDocFileName()), controllerDocs);
                LogUtils.info("success to generate docs for controller file : %s", controllerNode.getSrcFileName());
            } catch (IOException e) {
                LogUtils.error("generate docs for controller file : " + controllerNode.getSrcFileName() + " fail", e);
            }
        }
    }

    public List<ControllerNode> getControllerNodeList() {
        return controllerNodeList;
    }

    abstract void generateIndex(List<ControllerNode> controllerNodeList);
}
