package apidoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {
	
	private static final String CTRL_DESCRIPTION = "@Description";

	public static String getType(Class type){
		if(type == int.class || type == Integer.class){
			return "int";
		}else if(type == byte.class || type == Byte.class){
			return "byte";
		}else if(type == short.class || type == Short.class){
			return "short";
		}else if(type == long.class || type ==  Long.class){
			return "long";
		}else if(type == float.class || type ==  Float.class){
			return "float";
		}else if(type == double.class || type == Double.class){
			return "double";
		}else if(type == char.class || type == Character.class){
			return "char";
		}else if(type == boolean.class || type == Boolean.class){
			return "boolean";
		}else if(type == String.class){
			return "String";
		}else if(type == Date.class){
			return "Date";
		}else if(type == File.class){
			return "File";
		}else{
			return "unknow";
		}
	}
	
	public static String getRouteFilePath(){
		return System.getProperty("user.dir")+"/conf/routes";
	}
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
	}
	public static File  getJavaFile(Class clazz){
//		String appPath = Play.applicationPath.getAbsolutePath().replace(".", "app/");
		String appPath = System.getProperty("user.dir")+"/app/";
		String javaFilePath = appPath+clazz.getTypeName().replace('.', '/') + ".java";
		return new File(javaFilePath);
	}
	
	public static File getTemplateFile(String fileName){
//		String appPath = Play.applicationPath.getAbsolutePath().replace(".", "app/");   
		String appPath = System.getProperty("user.dir")+"/app/";   
		String tClsName = Utils.class.getName();
		String pkgPath= tClsName.substring(0, tClsName.lastIndexOf('.')).replace('.', '/') + '/';
		String javaFilePath = appPath+pkgPath+ fileName;
		return new File(javaFilePath);
	}
	
	public static String toJson(Object map){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(map);
	}
	
	public static String reverse(String str){
        return new StringBuffer(str).reverse().toString();
    }
	
	public static String getCtrlDescription(Class controller){
		File ctrlFile = Utils.getJavaFile(controller);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(ctrlFile));
			String lineText;
			int lineCount = 0;
			while((lineText = reader.readLine()) != null){
				if(lineCount > 100){
					return null;
				}
				if(lineText.contains(CTRL_DESCRIPTION)){
					lineText = lineText.replace(':', ' ');
					int sIndex = lineText.indexOf(CTRL_DESCRIPTION) + CTRL_DESCRIPTION.length();
					return lineText.substring(sIndex, lineText.length()).trim();
				}
				lineCount++;
			}
		} catch ( IOException e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(reader);
		}
		return null;
	}
	
	public static void writeToDisk(File f,String content) throws IOException{
		FileWriter writer = new FileWriter(f);
		writer.write(content);
		writer.close();
	}
	
	public static Class getListTypeClass(Class listClass){
		Type genericType = listClass.getGenericSuperclass();
		if(genericType instanceof ParameterizedType){
		    ParameterizedType aType = (ParameterizedType) genericType;
		    Type[] fieldArgTypes = aType.getActualTypeArguments();
		    if(fieldArgTypes.length == 1){
		    	return (Class) fieldArgTypes[0];
		    }
		}
		return null;
	}
	
	public static String getCommentText(String lineText){
		return lineText.substring(lineText.indexOf('*') + 1).trim();
	}
	
	public static boolean isMSiteUrl(String url){
		return url.contains("dev.yiyoushuo.com");
	}
	
	public static boolean isFieldLine(String lineText){
		return lineText.matches("\\s*((public)|(private)|(protected)){1}[^\\(\\)]+;\\s*//.+");
	}
	
	public static String getFieldComment(String lineText){
		int bIndex = lineText.indexOf("//");
		if(bIndex == -1){
			return "";
		}
		return lineText.substring(bIndex+2, lineText.length()).trim();
	}
	
	public static String getFieldName(String lineText){
		String subText;
		if(lineText.contains("=")){
			subText = lineText.substring(0, lineText.indexOf("=")).trim();
		}else{
			subText = lineText.substring(0, lineText.indexOf(";")).trim();
		}
		return subText.substring(subText.lastIndexOf(" ")).trim();
	}
}
