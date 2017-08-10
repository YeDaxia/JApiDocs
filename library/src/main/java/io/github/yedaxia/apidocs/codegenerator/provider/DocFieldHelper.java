package io.github.yedaxia.apidocs.codegenerator.provider;

import io.github.yedaxia.apidocs.Utils;

/**
 * Created by user on 2016/12/25.
 */
public class DocFieldHelper {

    public static String getPrefFieldName(String originFieldName){
        String[] names = originFieldName.split("_");
        if(names.length == 1){
            return Utils.decapitalize(names[0]);
        }
        StringBuilder fieldNameBuilder = new StringBuilder();
        fieldNameBuilder.append(Utils.decapitalize(names[0]));
        for (int i = 1; i < names.length; i++) {
            fieldNameBuilder.append(Utils.capitalize(names[i]));
        }
        return fieldNameBuilder.toString();
    }

    public static String getPrefFieldType(String fieldType) {
        if(fieldType.equalsIgnoreCase("int") || fieldType.equalsIgnoreCase("integer")){
            return "int";
        }else if(fieldType.equalsIgnoreCase("short")){
            return "short";
        }else if(fieldType.equalsIgnoreCase("byte")){
            return "byte";
        }else if(fieldType.equalsIgnoreCase("long")){
            return "long";
        }else if(fieldType.equalsIgnoreCase("boolean") || fieldType.equalsIgnoreCase("bool")){
            return "boolean";
        }else if(fieldType.equalsIgnoreCase("float")){
            return "float";
        }else if(fieldType.equalsIgnoreCase("double")){
            return "double";
        }else if(fieldType.equalsIgnoreCase("String") || fieldType.equalsIgnoreCase("Date")){
            return "String";
        }else{
        	return fieldType;
        }
    }
    
    public static String getIosFieldType(String type){
    	if(type.equals("byte")){
    		return "int";
    	}else if(type.equals("int")){
    		return "NSInteger";
    	}else if(type.equals("short")){
    		return "short";
    	}else if(type.equals("long")){
    		return "long";
    	}else if(type.equals("float")){
    		return "CGFloat";
    	}else if(type.equals("double")){
    		return "double";
    	}else if(type.equals("boolean")){
    		return "BOOL";
    	}else if(type.equalsIgnoreCase("String")){
    		return "NSString";
    	}else{
    		return type;
    	}
    }
}
