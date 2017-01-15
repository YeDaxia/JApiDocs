package apidoc.codegenerator.ios.builder;

public class ModelBuilder {
	
	private String modelTemplate;
	private String objectName;
	private String properties;
	

	public ModelBuilder(String modelTemplate, String objectName, String properties) {
		super();
		this.modelTemplate = modelTemplate;
		this.objectName = objectName;
		this.properties = properties;
	}

	public String build(){
		modelTemplate = modelTemplate.replace("${CLASS_NAME}",objectName);
		modelTemplate = modelTemplate.replace("${FIELDS}",properties);
		return modelTemplate;
	}
}
