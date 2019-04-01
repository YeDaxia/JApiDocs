package io.github.yedaxia.apidocs.plugin.rap;

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

    public static final String MOCK = "@mock=";

    /**
     * get rap type of param node
     *
     * @param nodeType
     * @return
     */
    public static String rapTypeOfNode(String nodeType) {
        String pType = nodeType;
        if (pType == null || pType.length() == 0) {
            return STRING;
        }

        if (isBooleanType(pType)) {
            return BOOLEAN;
        }

        if (isStringType(pType)) {
            return STRING;
        }

        if (isNumberType(pType)) {
            return NUMBER;
        }

        if (pType.endsWith("[]")) {
            String cType = pType.replace("[]", "");

            if (isBooleanType(cType)) {
                return ARRAY_BOOLEAN;
            }

            if (isStringType(cType)) {
                return ARRAY_STRING;
            }

            if (isNumberType(cType)) {
                return ARRAY_NUMBER;
            }

            return ARRAY_OBJECT;

        } else {
            return OBJECT;
        }
    }

    /**
     * is nodeType string an array or not
     * @param nodeType
     * @return
     */
    public static boolean isArrayType(String nodeType){
        return nodeType != null && nodeType.endsWith("[]");
    }

    /**
     * get mock type of param node
     *
     * @param nodeType
     * @return
     */
    public static String mockTypeOfNode(String nodeType) {
        if(isArrayType(nodeType)){
            nodeType = nodeType.replace("[]", "");
        }

        if (isBooleanType(nodeType)) {
            return MOCK + "@boolean".toUpperCase();
        } else if (isFloatType(nodeType)) {
            return MOCK + "@float".toUpperCase();
        } else if (isIntType(nodeType)) {
            return MOCK + "@integer".toUpperCase();
        } else if (isCharType(nodeType)) {
            return MOCK + "@character".toUpperCase();
        } else if ("date".equalsIgnoreCase(nodeType)) {
            return MOCK + "@datetime".toUpperCase();
        } else if("string".equalsIgnoreCase(nodeType)){
            return MOCK+ "@string".toUpperCase();
        } else {
            return "";
        }
    }

    /**
     * return mock value
     * @param value
     * @return
     */
    public static String mockValue(Object value){
        return MOCK + value;
    }

    private static boolean isBooleanType(String pType) {
        return pType != null && pType.equalsIgnoreCase("boolean");
    }

    private static boolean isNumberType(String pType) {
        return pType != null && isFloatType(pType) || isIntType(pType);
    }

    private static boolean isFloatType(String pType) {
        return pType != null && (pType.equalsIgnoreCase("float")
                || pType.equalsIgnoreCase("double"));
    }

    private static boolean isIntType(String pType) {
        return pType != null && (pType.equalsIgnoreCase("int")
                || pType.equalsIgnoreCase("byte")
                || pType.equalsIgnoreCase("short")
                || pType.equalsIgnoreCase("long"));
    }

    private static boolean isStringType(String pType) {
        return pType != null && (pType.equalsIgnoreCase("date")
                || pType.equalsIgnoreCase("string"));
    }

    private static boolean isCharType(String pType) {
        return pType != null && ("char".equalsIgnoreCase(pType)
                || "Character".equalsIgnoreCase(pType));
    }
}
