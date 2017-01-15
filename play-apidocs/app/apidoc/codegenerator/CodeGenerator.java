package apidoc.codegenerator;

import java.io.File;
import java.io.IOException;

import apidoc.Docs;
import apidoc.RecordNode;
import apidoc.ResponseNode;
import apidoc.Utils;

public abstract class CodeGenerator {
	
	private ResponseNode responseNode;
	private File codePath;
	private String codeRelativePath;
	
	public CodeGenerator(ResponseNode responseNode){
		this.responseNode = responseNode;
		this.codeRelativePath = getRelativeCodeDir();
		this.codePath = new File(Docs.getDocPath(), codeRelativePath);
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
		if (responseNode.childNodes == null || responseNode.childNodes.isEmpty()) {
			return "";
		}
		StringBuilder codeBodyBuilder = new StringBuilder();
		generateCodeForBuilder(responseNode,codeBodyBuilder);
		String sCodeTemplate = getCodeTemplate();
		CodeBuilder codeBuilder = new CodeBuilder(responseNode.className, codeBodyBuilder.toString(), sCodeTemplate);
		String javaFileName = responseNode.className + ".html";
		Utils.writeToDisk(new File(codePath, javaFileName), codeBuilder.build());
		String relateUrl = codeRelativePath + '/' + javaFileName;
		System.out.println(String.format("生成相应Android代码成功:%s", relateUrl));
		return relateUrl;
	}
	
	private void generateCodeForBuilder(ResponseNode rootNode,StringBuilder codeBodyBuilder) throws IOException{
		codeBodyBuilder.append(generateNodeCode(rootNode));
		codeBodyBuilder.append('\n');
		for (RecordNode recordNode : rootNode.childNodes) {
			if (recordNode.childTableNode != null) {
				generateCodeForBuilder(recordNode.childTableNode,codeBodyBuilder);
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
