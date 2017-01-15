package apidoc.codegenerator;

public class CodeBuilder {
	private String fileName;
	private String codeBody;
	private String codeTemplate;
	
	public CodeBuilder(String fileName, String codeBody, String codeTemplate) {
		super();
		this.fileName = fileName;
		this.codeBody = codeBody;
		this.codeTemplate = codeTemplate;
	}
	
	public String build(){
		codeTemplate = codeTemplate.replace("${FILE_NAME}", fileName);
		codeTemplate = codeTemplate.replace("${MODEL_LIST}", codeBody);
		return codeTemplate;
	}
}
