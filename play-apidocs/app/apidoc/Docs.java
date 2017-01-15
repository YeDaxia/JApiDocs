package apidoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.map.HashedMap;

public class Docs {

	private static Map<Class,List<RequestNode>> contrllerNodeMap = new HashedMap();
	
	private static File docsPath;
	
	static{
		RouterBuilder routerBuilder = new RouterBuilder(Utils.getRouteFilePath());
		List<RouteNode> routeNodes = routerBuilder.build();
		List<RequestNode> requestNodeList;
		for (RouteNode routeNode : routeNodes) {
			RequestBuilder requestBuilder = new RequestBuilder(routeNode);
			requestNodeList = contrllerNodeMap.get(routeNode.controller);
			if(requestNodeList == null){
				requestNodeList = new ArrayList<>();
				contrllerNodeMap.put(routeNode.controller, requestNodeList);
			}
			RequestNode requestNode = requestBuilder.build();
			if(requestNode != null){
				requestNodeList.add(requestNode);	
			}
		}
	}

	public Docs(String docsPath){
		File path = new File(docsPath);
		if(!path.exists()){
			path.mkdirs();
		}
		this.docsPath = path;
	}
	
	public void buildMarkDownDocs(){
//		List<RequestNode> requestNodes ;
//		for (Entry<Class,List<RequestNode>> controllerNodeEntry : contrllerNodeMap.entrySet()) {
//			requestNodes = controllerNodeEntry.getValue();
//			if(requestNodes == null || requestNodes.isEmpty()){
//				return;
//			}
//			MarkdownDocBuilder tempateDocBuilder = new MarkdownDocBuilder(controllerNodeEntry.getKey(), requestNodes);
//			try {
//				String controllerDocs = tempateDocBuilder.build();
//				writeToDisk(controllerNodeEntry.getKey(),controllerDocs,".md");
//				System.out.println(String.format("生成%s的文档成功",controllerNodeEntry.getKey().getName()));
//			} catch (IOException e) {
//				System.err.println(String.format("生成%s的文档失败",controllerNodeEntry.getKey().getName()));
//				e.printStackTrace();
//			}
//		}
	}
	
	public void buildHtmlDocs(){
		List<RequestNode> requestNodes ;
		List<String> docFileList = new ArrayList<>();
		for (Entry<Class,List<RequestNode>> controllerNodeEntry : contrllerNodeMap.entrySet()) {
			requestNodes = controllerNodeEntry.getValue();
			if(requestNodes == null || requestNodes.isEmpty()){
				continue;
			}
			HtmlDocBuilder tempateDocBuilder = new HtmlDocBuilder(controllerNodeEntry.getKey(), requestNodes);
			try {
				String controllerDocs = tempateDocBuilder.build();
				Class controllerClass = controllerNodeEntry.getKey();
				String controllerDesc =  Utils.getCtrlDescription(controllerClass);
				String docName = controllerDesc == null ? controllerClass.getSimpleName() : controllerDesc;
				docFileList.add(docName);
				writeToDisk(controllerDocs,docName+".html");
				System.out.println(String.format("生成%s的文档成功",controllerNodeEntry.getKey().getName()));
			} catch (IOException e) {
				System.err.println(String.format("生成%s的文档失败",controllerNodeEntry.getKey().getName()));
				e.printStackTrace();
			}
		}
		
		if(docFileList.isEmpty()){
			return;
		}
		
		try {
			File tplIndexFile = Utils.getTemplateFile("api-index.html.tpl");
			String indexTemplate = org.apache.commons.fileupload.util.Streams.asString(new FileInputStream(tplIndexFile));
			StringBuilder indexBuilder = new StringBuilder();
			for (String docName : docFileList) {
				indexBuilder.append(String.format("<li><a href=\"%s\">%s</a></li>",docName+".html",docName));
			}
			indexTemplate = indexTemplate.replace("${API_LIST}", indexBuilder.toString());
			writeToDisk(indexTemplate,"index.html");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void writeToDisk(String docContent,String docName) throws IOException{
		File markdownFile = new File(docsPath,docName);
		FileWriter writer = new FileWriter(markdownFile);
		writer.write(docContent);
		writer.close();
		Utils.writeToDisk(markdownFile, docContent);
	}
	
	public static File getDocPath(){
		return docsPath;
	}
}
