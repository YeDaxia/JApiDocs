package io.github.yedaxia.apidocs.codegenerator;

import io.github.yedaxia.apidocs.DocContext;
import io.github.yedaxia.apidocs.Utils;
import io.github.yedaxia.apidocs.parser.FieldNode;
import io.github.yedaxia.apidocs.parser.ResponseNode;

import java.io.File;
import java.io.IOException;

public abstract class CodeGenerator {

	private ResponseNode responseNode;
	private File codePath;
	private String codeRelativePath;
	
	public CodeGenerator(ResponseNode responseNode){
		this.responseNode = responseNode;
		this.codeRelativePath = getRelativeCodeDir();
		this.codePath = new File(DocContext.getDocPath(), codeRelativePath);
		if (!this.codePath.exists()) {
			this.codePath.mkdirs();
		}
	}
	
	/**
	 * 生成代码
	 * @return 返回代码的相对目录
	 * @throws IOException 
	 */
	public String generateCode() throws IOException{
		if (responseNode.getChildNodes() == null || responseNode.getChildNodes().isEmpty()) {
			return "";
		}
		StringBuilder codeBodyBuilder = new StringBuilder();
		generateCodeForBuilder(responseNode,codeBodyBuilder);
		String sCodeTemplate = getCodeTemplate();
		CodeFileBuilder codeBuilder = new CodeFileBuilder(responseNode.getClassName(), codeBodyBuilder.toString(), sCodeTemplate);
		String javaFileName = responseNode.getClassName() + ".html";
		Utils.writeToDisk(new File(codePath, javaFileName), codeBuilder.build());
		String relateUrl = codeRelativePath + '/' + javaFileName;
		return relateUrl;
	}
	
	private void generateCodeForBuilder(ResponseNode rootNode,StringBuilder codeBodyBuilder) throws IOException{
		codeBodyBuilder.append(generateNodeCode(rootNode));
		codeBodyBuilder.append('\n');
		for (FieldNode recordNode : rootNode.getChildNodes()) {
			if (recordNode.getChildResponseNode()!= null) {
				generateCodeForBuilder(recordNode.getChildResponseNode(), codeBodyBuilder);
			}
		}
	}
	
	/***
	 * 产生单个ResponseNode节点的Code
	 * @param respNode
	 * @return
	 * @throws IOException
	 */
	public abstract String generateNodeCode(ResponseNode respNode) throws IOException;
	
	/**
	 * 获取代码的写入的相对目录
	 * @return
	 */
	public abstract String getRelativeCodeDir();
	
	/**
	 * 获取最终的代码模板
	 * @return
	 */
	public abstract String getCodeTemplate();
}
