package apidoc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.reflect.FieldUtils;

public class ResponseBuilder {
	
	private Class resultClass;
	
	public ResponseBuilder(Class resultClass) {
		this.resultClass = resultClass;
	}
	
	public ResponseNode build(){
		ResponseNode rootNode = new ResponseNode();
		Class rsClass;
		if(resultClass.isArray()){
			rootNode.isList = true;
			rsClass = resultClass.getComponentType();
		}else{
			rsClass = resultClass;
		}
		parseClassFields(rsClass,rootNode);
		return rootNode;
	}
	
	private void parseClassFields(Class<?> rClass,ResponseNode tableNode){
		JavaFileParser fileParser = new JavaFileParser(rClass);
		fileParser.parse();
	
		Field[] fields = rClass.getFields();
		
		if(fields == null || fields.length == 0){
			fields = rClass.getDeclaredFields();
		}
		
		tableNode.className = rClass.getSimpleName();
		tableNode.childNodes = new ArrayList<RecordNode>();
		for (Field field : fields) {
			parseField(field,tableNode.childNodes,fileParser);
		}
	}
	
	private void parseField(Field f,List<RecordNode> columnNodes, JavaFileParser fileParser){ 
		if(!f.isAccessible()){
			f.setAccessible(true);
		}
		if(Modifier.isStatic(f.getModifiers())){
			return;
		}
		RecordNode c = new RecordNode();
		c.name = f.getName();
		c.description = fileParser.getFieldDescription(f.getName());
		Class<?> type = f.getType();
		if(type == int.class || type == Integer.class){
			c.type = "int";
		}else if(type == byte.class || type == Byte.class){
			c.type = "byte";
		}else if(type == short.class || type == Short.class){
			c.type = "short";
		}else if(type == long.class || type ==  Long.class){
			c.type = "long";
		}else if(type == float.class || type ==  Float.class){
			c.type = "float";
		}else if(type == double.class || type == Double.class){
			c.type = "double";
		}else if(type == char.class || type == Character.class){
			c.type = "char";
		}else if(type == boolean.class || type == Boolean.class){
			c.type = "boolean";
		}else if(type == String.class){
			c.type = "String";
		}else if(type == Date.class){
			c.type = "Date";
		}else if(type == List.class || type == ArrayList.class){
			Type genericType = f.getGenericType();
			if(genericType instanceof ParameterizedType){
			    ParameterizedType aType = (ParameterizedType) genericType;
			    Type[] fieldArgTypes = aType.getActualTypeArguments();
			    if(fieldArgTypes.length == 1){
			    	Class genericClass = (Class) fieldArgTypes[0];
			    	String listType  = getPrimitiveArrayType(genericClass);
			    	if(listType == null){
			    		c.type = genericClass.getSimpleName() + "[]";
			    		c.childTableNode = new ResponseNode();
			    		parseClassFields(genericClass,c.childTableNode);
			    	}else{
			    		c.type = listType;
			    	}
			    }
			}
		}else if(type.isArray()){
			Class<?> cType = type.getComponentType();
			String arrayType  = getPrimitiveArrayType(cType);
			if(arrayType == null){
				c.type = cType.getSimpleName() + "[]";
	    		c.childTableNode = new ResponseNode();
	    		parseClassFields(cType,c.childTableNode);
			}else{
				c.type = arrayType;
			}
		}else{
			c.type = type.getSimpleName();
    		c.childTableNode = new ResponseNode();
    		parseClassFields(type,c.childTableNode);
		}
		columnNodes.add(c);
	}
	
	private String getPrimitiveArrayType(Type type){
		if(type == int.class || type == Integer.class){
			return "int[]";
		}else if(type == byte.class || type == Byte.class){
			return "byte[]";
		}else if(type == short.class || type == Short.class){
			return "short[]";
		}else if(type == long.class || type ==  Long.class){
			return "long[]";
		}else if(type == float.class || type ==  Float.class){
			return "float[]";
		}else if(type == double.class || type == Double.class){
			return "double[]";
		}else if(type == char.class || type == Character.class){
			return "char[]";
		}else if(type == String.class){
			return "String[]";
		}else{
			return null;
		}
	}
}
