package io.github.yedaxia.apidocs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Utils {

    /**
     * object to pretty json
     * @param map
     * @return
     */
	public static String toPrettyJson(Object map){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(map);
	}

    /**
     * object to simple json
     * @param map
     * @return
     */
	public static String toJson(Object map){
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    /**
     * json string to object
     * @param json
     * @param type
     * @param <T>
     * @return
     */
	public static<T> T jsonToObject(String json, Class<T> type){
	    Gson gson = new Gson();
	    return gson.fromJson(json, type);
    }

	/**
	 * write content to file
	 * @param f
	 * @param content
	 * @throws IOException
	 */
	public static void writeToDisk(File f,String content) throws IOException{
		mkdirsForFile(f);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"utf-8"));
		writer.write(content);
		writer.close();
	}

	/**
	 * close stream
	 * @param stream
	 */
	public static void closeSilently(Closeable stream){
		if(stream != null){
			try{
				stream.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

    /**
     * simple read stream to String
     * @param in
     * @return
     * @throws IOException
     */
	public static String streamToString(InputStream in) throws IOException{
	    StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(in, "utf-8");
        char[] buffer = new char[4096];
        int bytesRead = -1;
        while ((bytesRead = reader.read(buffer)) != -1) {
            stringBuilder.append(buffer, 0, bytesRead);
        }
        reader.close();
	    return stringBuilder.toString();
    }

    /**
     * judge input string is not empty
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
	    return str != null && !str.isEmpty();
    }

    /**
     * some parse url may has double quotation, remove them
     * @param rawUrl
     * @return
     */
    public static String removeQuotations(String rawUrl){
        return rawUrl.replace("\"","").trim();
    }

	/**
	 * remove some characters like [* \n]
	 * @param content
	 * @return
	 */
	public static String cleanCommentContent(String content){
		return content.replace("*","").replace("\n", "").trim();
	}

	/**
	 * get url with base url
	 * @param baseUrl
	 * @param relativeUrl
	 * @return
	 */
	public static String getActionUrl(String baseUrl, String relativeUrl){

		if(baseUrl == null){
			return relativeUrl;
		}

		if(baseUrl.endsWith("/")){
			baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		}

		if(!relativeUrl.startsWith("/")){
			relativeUrl = "/" + relativeUrl;
		}

		return baseUrl + relativeUrl;
	}

    /**
     * make first word lower case
     * @param name
     * @return
     */
	public static String decapitalize(String name) {
		if(name != null && name.length() != 0) {
			if(name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
				return name;
			} else {
				char[] chars = name.toCharArray();
				chars[0] = Character.toLowerCase(chars[0]);
				return new String(chars);
			}
		} else {
			return name;
		}
	}

    /**
     * make first word capitalize
     * @param name
     * @return
     */
	public static String capitalize(String name) {
		if(name != null && name.length() != 0) {
			char[] chars = name.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			return new String(chars);
		} else {
			return name;
		}
	}

    /**
     * join string array , （ e.g. ([a,a,a] , .) = a.a.a )
     * @param array
     * @param separator
     * @return
     */
	public static String joinArrayString(String[] array, String separator){
		if(array == null || array.length == 0){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for(int i = 0, len = array.length ; i != len ; i++){
			builder.append(array[i]);
			if(i != len -1){
				builder.append(separator);
			}
		}
		return builder.toString();
	}

    /**
     * get file name without extension
     * @param javaFile
     * @return
     */
	public static String getJavaFileName(File javaFile){
		String fileName = javaFile.getName();
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

    /**
     * search files match filter, store in result
     * @param rootPath
     * @param filter
     * @param result
     * @param stopAtFirstResult stop when first file matches
     */
	public static void wideSearchFile(File rootPath, FilenameFilter filter, List<File> result, boolean stopAtFirstResult){
	    File[] fileList = rootPath.listFiles();
	    List<File> dirPaths = new ArrayList<>();
	    for(File f : fileList){
	        if(f.isFile() && filter.accept(f, f.getName())){
	            result.add(f);
	            if(stopAtFirstResult){
	                return;
                }
            }else if(f.isDirectory()){
                dirPaths.add(f);
            }
        }

        for(File dir : dirPaths){
	        if(stopAtFirstResult && !result.isEmpty()){
	            return;
            }
            wideSearchFile(dir, filter, result, stopAtFirstResult);
        }
    }

    /**
     * judge dir is in file's path or not
     * @param f
     * @param stopPath stopPath
     * @param dirName
     * @return
     */
    public static boolean hasDirInFile(File f, File stopPath, String dirName){
        File p = f.getParentFile();
        while((stopPath == null && p != null) || (stopPath != null && !p.getAbsolutePath().equals(stopPath.getAbsolutePath()))){
	        if(dirName.equals(p.getName())){
	            return true;
            }
            p = p.getParentFile();
        }
        return false;
    }

    /**
     * the project is a play framework or not
     * @param projectDir 工程目录
     * @return
     */
    public static boolean isPlayFramework(File projectDir){
        File ymlFile = new File(projectDir, "conf/dependencies.yml");
        if(!ymlFile.exists()){
            return false;
        }
        File routesFile = new File(projectDir, "conf/routes");
        if(!routesFile.exists()){
            return false;
        }
        return true;
    }

    /**
     * the project is a spring mvc framework or not
     * @param javaSrcDir
     * @return
     */
    public static boolean isSpringFramework(File javaSrcDir){
        List<File> result = new ArrayList<>();
        Utils.wideSearchFile(javaSrcDir, new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".java") && ParseUtils.compilationUnit(f).getImports().stream().anyMatch(im -> im.getNameAsString().contains("org.springframework.web"));
            }
        }, result, true);
        return result.size() > 0;
    }

	/**
	 * the project is a jfinal framework or not
	 * @param javaSrcDir
	 * @return
	 */
	public static boolean isJFinalFramework(File javaSrcDir){
		List<File> result = new ArrayList<>();
		Utils.wideSearchFile(javaSrcDir, new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return name.endsWith(".java") && ParseUtils.compilationUnit(f).getImports().stream().anyMatch(im -> im.getNameAsString().contains("com.jfinal.core"));
			}
		}, result, true);
		return result.size() > 0;
	}


    /**
     * is value type or not
     * @param value
     * @return
     */
	public static boolean isValueType(Object value){
	    return value instanceof Number
                || value instanceof String
                || value instanceof java.util.Date;
    }

	/**
	 * get simple class name
	 *
	 * @param packageClass
	 * @return
	 */
	public static String getClassName(String packageClass){
		String[] parts = packageClass.split("\\.");
		return parts[parts.length - 1];
	}

	/**
	 * get project build tool type
	 * @param projectDir
	 * @return
	 */
	private static BuildToolType getProjectBuildTool(File projectDir){
		if(new File(projectDir,"settings.gradle").exists()){
			return BuildToolType.GRADLE;
		}

		if(new File(projectDir,"pom.xml").exists()){
			return BuildToolType.MAVEN;
		}

		return BuildToolType.UNKOWN;
	}

    /**
     * get project modules name
     * @param projectDir
     * @return
     */
	public static List<String> getModuleNames(File projectDir){
        BuildToolType buildToolType = getProjectBuildTool(projectDir);

        List<String> moduleNames = new ArrayList<>();

        //gradle
        if(buildToolType == BuildToolType.GRADLE){
            try{
                BufferedReader settingReader = new BufferedReader(new InputStreamReader(new
                        FileInputStream(new File(projectDir, "settings.gradle"))));
                String lineText;
                String keyword = "include ";
                while((lineText = settingReader.readLine()) != null){
                    int inIndex = lineText.indexOf(keyword);
                    if(inIndex != -1){
                        moduleNames.add(lineText.substring(inIndex + keyword.length()).replace("'","").replace("\"",""));
                    }
                }
                Utils.closeSilently(settingReader);
            }catch (IOException ex){
                LogUtils.error("read setting.gradle error", ex);
            }
        }

        // maven
        if(buildToolType == BuildToolType.MAVEN){
            try{
                SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
                saxParser.parse(new File(projectDir,"pom.xml"), new DefaultHandler(){

                    String moduleName;
                    boolean isModuleTag;

                    @Override
                    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                        if("module".equalsIgnoreCase(qName)){
                            isModuleTag = true;
                        }
                    }

                    @Override
                    public void endElement(String uri, String localName, String qName) throws SAXException {
                        if("module".equalsIgnoreCase(qName)){
                            moduleNames.add(moduleName);
                            isModuleTag = false;
                        }
                    }

                    @Override
                    public void characters(char[] ch, int start, int length) throws SAXException {
                        if(isModuleTag){
                            moduleName = new String(ch, start, length);
                        }
                    }
                });
            }catch (Exception ex){
                LogUtils.error("read pom.xml error", ex);
            }
        }

        if(!moduleNames.isEmpty()){
            LogUtils.info("find multi modules in this project: %s", Arrays.toString(moduleNames.toArray()));
        }

	    return moduleNames;
    }

	/**
	 * create dirs for file
	 *
	 * @param file
	 */
	public static void mkdirsForFile(File file){
		if(file.isFile() && !file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
	}
}
