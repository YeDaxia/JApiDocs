package apidoc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import play.data.validation.Required;

public class RequestBuilder {
	
	private RouteNode routeNode;
	
	public RequestBuilder(RouteNode routeNode){
		this.routeNode = routeNode;
	}
	
	public RequestNode build() {
		Method[] methods = routeNode.controller.getDeclaredMethods();
		if(ArrayUtils.isEmpty(methods)){
			return null;
		}
		
		for (Method method : methods) {
			if(routeNode.actionMethod.equals(method.getName())){
				ApiResult apiResult = method.getAnnotation(ApiResult.class);
				if(apiResult == null){
					return null;
				}
				String methodName = method.getName();
				RequestNode requestNode = new RequestNode();
				requestNode.method = routeNode.method;
				requestNode.url = routeNode.routeUrl;
				requestNode.resultClass = apiResult.name();
				Parameter[] params = method.getParameters();
				JavaFileParser javaFileParser = new JavaFileParser(routeNode.controller);
				javaFileParser.parse();
				Map<String,String> paramComments = javaFileParser.getMethodParams(methodName);
				String mDescription = javaFileParser.getMethodDescription(methodName);
				requestNode.description = mDescription != null ? mDescription : routeNode.comment;
				if(ArrayUtils.isNotEmpty(params)){
					String[] paramNames;
					try {
						paramNames = getMethodParamNames(method);
					} catch (NotFoundException e) {
						e.printStackTrace();
						return null;
					}
					List<ParamNode> paramNodeList = new ArrayList<>(paramNames.length);
					ParamNode paramNode;
					Parameter parameter;
					for (int i = 0; i < paramNames.length; i++) {
						paramNode = new ParamNode();
						paramNode.name = paramNames[i];
						parameter = params[i];
						paramNode.type = Utils.getType(parameter.getType());
						paramNode.description = paramComments.get(paramNames[i]);
						if(parameter.getAnnotation(Required.class) != null){
							paramNode.required = true;
						}
						paramNodeList.add(paramNode);
					}
					
					requestNode.paramNodes = paramNodeList;
				}
				return requestNode;
			}
		}
		return null;
	}
	
	private String[] getMethodParamNames(Method method) throws NotFoundException{
	 	Class clazz = method.getDeclaringClass();  
        String methodName = method.getName();  
        ClassPool pool = ClassPool.getDefault();  
        pool.insertClassPath(new ClassClassPath(clazz));  
        CtClass cc = pool.get(clazz.getName());  
        CtMethod cm = cc.getDeclaredMethod(methodName);  
        MethodInfo methodInfo = cm.getMethodInfo();  
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);  
        if (attr == null) {  
            throw new NotFoundException("attr is null");
        }  
        String[] paramNames = new String[cm.getParameterTypes().length];  
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;  
        for (int i = 0; i < paramNames.length; i++)  
            paramNames[i] = attr.variableName(i + pos);  
        return paramNames;
	}
}
