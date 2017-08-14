package io.github.yedaxia.apidocs.ext.rap;

import io.github.yedaxia.apidocs.parser.ParamNode;

/**
 * @author yeguozhong yedaxia.github.com
 */
class DataType {

    public static String STRING = "string";
    public static String NUMBER = "number";
    public static String BOOLEAN = "boolean";
    public static String OBJECT = "object";
    public static String ARRAY = "array";
    public static String ARRAY_NUMBER = "array<number>";
    public static String ARRAY_STRING = "array<string>";
    public static String ARRAY_BOOLEAN = "array<boolean>";
    public static String ARRAY_OBJECT = "array<object>";

    /**
     * get rap type of param node
     * @param nodeType
     * @return
     */
    public static String rapTypeOfNode(String nodeType){
        String pType = nodeType;
        if(pType == null || pType.length() == 0){
            return STRING;
        }

        if(isBooleanType(pType)){
            return BOOLEAN;
        }

        if(isStringType(pType)){
            return STRING;
        }

        if(isNumberType(pType)){
            return NUMBER;
        }

        if(pType.endsWith("[]")){
            String cType = pType.replace("[]","");

            if(isBooleanType(cType)){
                return ARRAY_BOOLEAN;
            }

            if(isStringType(cType)){
                return ARRAY_STRING;
            }

            if(isNumberType(cType)){
                return ARRAY_NUMBER;
            }

            return ARRAY;

        }else{
            return OBJECT;
        }
    }

    /**
     * get mock type of param node
     * @param nodeType
     * @return
     */
    public static String mockTypeOfNode(String nodeType){
        if(nodeType.endsWith("[]")){

        }else{
            if(isBooleanType(nodeType)){
                return "@boolean";
            }else if(isFloatType(nodeType)){
                return "@float";
            }else if(isIntType(nodeType)){
                return "@integer";
            }else if(isCharType(nodeType)){
                return "@character";
            }else if(nodeType.equalsIgnoreCase("date")){
                return "@datetime";
            }else{
                return "@string";
            }
        }
        return "";
    }

    private static boolean isBooleanType(String pType){
        return pType.equalsIgnoreCase("boolean");
    }

    private static boolean isNumberType(String pType){
        return isFloatType(pType) || isIntType(pType);
    }

    private static boolean isFloatType(String pType){
        return pType.equalsIgnoreCase("float")
                || pType.equalsIgnoreCase("double");
    }

    private static boolean isIntType(String pType){
        return pType.equalsIgnoreCase("int")
                || pType.equalsIgnoreCase("byte")
                || pType.equalsIgnoreCase("short")
                || pType.equalsIgnoreCase("long");
    }

    private static boolean isStringType(String pType){
        return pType.equalsIgnoreCase("date")
                || pType.equalsIgnoreCase("string");
    }

    private static boolean isCharType(String pType){
        return "char".equalsIgnoreCase(pType)
                || "Character".equalsIgnoreCase(pType);
    }
}
