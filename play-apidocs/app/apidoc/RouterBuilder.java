package apidoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class RouterBuilder {

	public String routeFile;
	
	public RouterBuilder(String routeFile){
		this.routeFile = routeFile;
	}

	public List<RouteNode> build(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(routeFile)));
			String lineText;
			String[] nodes;
			List<RouteNode> routeNodes = new ArrayList<>();
			while((lineText = reader.readLine()) != null){
				lineText = lineText.trim();
				if(StringUtils.isEmpty(lineText) || lineText.startsWith("#")){
					continue;
				}
				nodes = lineText.split("\\s+");
				if(nodes.length < 3 || nodes[2].matches("\\d+")){
					continue;
				}
				RouteNode routeNode = new RouteNode();
				routeNode.method = nodes[0].trim();
				routeNode.routeUrl = nodes[1].trim();
				
				String[] actions = nodes[2].split("\\.");
				if(actions.length == 1){
					continue;
				}
				StringBuilder contrlClassBuilder = new StringBuilder();
				contrlClassBuilder.append("controllers");
				for (int i = 0; i != actions.length - 1; i++) {
					contrlClassBuilder.append('.');
					contrlClassBuilder.append(actions[i]);
				}
				try {
					Class controlClass = Class.forName(contrlClassBuilder.toString());
					routeNode.controller = controlClass;
					routeNode.actionMethod = actions[actions.length - 1];
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					continue;
				}
				
				if(nodes.length  > 3){
					StringBuilder commentBuilder = new StringBuilder();
					for(int i = 3 ; i != nodes.length ;++i){
						commentBuilder.append(nodes[i]);
						commentBuilder.append(' ');
					}
					routeNode.comment = commentBuilder.substring(1, commentBuilder.length()).trim();
				}
				
				routeNodes.add(routeNode);
			}
			return routeNodes;
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(reader);
		}
		return null;
	}
	
}
