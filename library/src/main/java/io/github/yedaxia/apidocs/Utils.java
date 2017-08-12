package io.github.yedaxia.apidocs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utils {

    /**
     * object to json
     * @param map
     * @return
     */
	public static String toJson(Object map){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(map);
	}

	/**
	 * write content to file
	 * @param f
	 * @param content
	 * @throws IOException
	 */
	public static void writeToDisk(File f,String content) throws IOException{
		FileWriter writer = new FileWriter(f);
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
    public static String cleanUrl(String rawUrl){
        return rawUrl.replace("\"","").trim();
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
}
