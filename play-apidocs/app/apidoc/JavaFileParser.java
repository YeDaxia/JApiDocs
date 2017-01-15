package apidoc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class JavaFileParser {
	
	private static final String PARAM_ANNOTATION = "@param";
	private static final String DESCRIPTION_ANNOTATION = "@Description";
	
	private Map<String,Map<String,String>> methodParamMap = new HashMap<>();
	private Map<String,String> methodDescpMap = new HashMap<>();
	private Map<String,String> fieldDescpMap = new HashMap<>();
	
	private Class javaClass;
	
	public JavaFileParser(Class javaClass){
		this.javaClass = javaClass;
	}
	
	public void parse(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(Utils.getJavaFile(javaClass)));
			String lineText;
			Map paramComMap = null;
			String mDescription = null;
			boolean inClassBody = false;
			while((lineText = reader.readLine()) != null){
				if(!inClassBody && lineText.contains("class") && lineText.contains("public")){
					inClassBody = true;
					continue;
				}
				
				if(Utils.isFieldLine(lineText)){
					String fieldComent = Utils.getFieldComment(lineText);
					String fieldName = Utils.getFieldName(lineText);
					if(StringUtils.isNotEmpty(fieldComent)){
						fieldDescpMap.put(fieldName, fieldComent);
					}
					continue;
				}
				
				if(isMethodLine(lineText) && paramComMap != null){
					String methodName = getMethodName(lineText);
					methodParamMap.put(methodName, paramComMap);
					if(mDescription != null){
						methodDescpMap.put(methodName, mDescription);
					}
					paramComMap = null;
					mDescription = null;
					continue;
				}
				
				if(lineText.contains(PARAM_ANNOTATION)){
					if(paramComMap == null){
						paramComMap = new HashMap<>();
					}
					String[] paramComment = parseParamLine(lineText);
					if(StringUtils.isEmpty(paramComment[1])){
						lineText = reader.readLine();
						String commentText = Utils.getCommentText(lineText);
						if(commentText.startsWith(PARAM_ANNOTATION)){
							paramComMap.put(paramComment[0], paramComment[1]);
							paramComment = parseParamLine(lineText);
						}else if(commentText.startsWith("/") || commentText.startsWith("@")){
							
						}else{
							paramComment[1] = commentText;
						}
					}
					paramComMap.put(paramComment[0], paramComment[1]);
					continue;
				}
				
				if(lineText.contains(DESCRIPTION_ANNOTATION)){
					if(paramComMap == null){
						paramComMap = new HashMap<>();
					}
					mDescription = getMDescription(lineText);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(reader);
		}
	}
	
	public Map<String,String> getMethodParams(String method){
		return methodParamMap.get(method);
	}
	
	public String getMethodDescription(String method){
		return methodDescpMap.get(method);
	}
	
	public String getFieldDescription(String fieldName){
		return fieldDescpMap.get(fieldName);
	}
	
	private boolean isMethodLine(String lineText){
		return lineText.contains("public ") && lineText.contains("void ");
	}
	
	private String getMethodName(String lineText){
		String subLine = lineText.substring(lineText.indexOf("public "));
		subLine = subLine.substring(0, subLine.indexOf('('));
		return subLine.substring(subLine.lastIndexOf(" ")).trim();
	}
	
	private String[] parseParamLine(String lineText){
		int nameIndex = lineText.indexOf(PARAM_ANNOTATION);
		String subText = lineText.substring(nameIndex + PARAM_ANNOTATION.length(), lineText.length()).trim();
		int separateIndex = subText.indexOf(" ");
		if(separateIndex == -1){
			return new String[]{subText,""};
		}
		String paramName = subText.substring(0, separateIndex).trim();
		String paramComment = subText.substring(paramName.length()).trim();
		return new String[]{paramName,paramComment};
	}
	
	private String getMDescription(String lineText){
		int nameIndex = lineText.indexOf(DESCRIPTION_ANNOTATION);
		return lineText.substring(nameIndex + DESCRIPTION_ANNOTATION.length(), lineText.length()).trim();
	}
}
