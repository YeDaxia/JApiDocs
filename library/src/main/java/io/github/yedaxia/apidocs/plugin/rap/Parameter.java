package io.github.yedaxia.apidocs.plugin.rap;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yeguozhong yedaxia.github.com
 */
class Parameter {

    private int id;
    private String mockData;
    private String name;
    private String identifier;
    private String identifierChange;
    private String remarkChange;
    private String dataType;
    private String remark;
    private Set<Action> actionRequestList = new HashSet<Action>();
    private Set<Action> actionResponseList = new HashSet<Action>();
    private String validator = "";
    private Set<Parameter> parameterList = new HashSet<Parameter>();
    private Set<Parameter> complexParamerterList = new HashSet<Parameter>();

    public static Parameter newParameter(){
        Parameter p = new Parameter();
        p.setId(-1);
        return p;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMockData() {
        return mockData;
    }

    public void setMockData(String mockData) {
        this.mockData = mockData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierChange() {
        return identifierChange;
    }

    public void setIdentifierChange(String identifierChange) {
        this.identifierChange = identifierChange;
    }

    public String getRemarkChange() {
        return remarkChange;
    }

    public void setRemarkChange(String remarkChange) {
        this.remarkChange = remarkChange;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Set<Action> getActionRequestList() {
        return actionRequestList;
    }

    public void setActionRequestList(Set<Action> actionRequestList) {
        this.actionRequestList = actionRequestList;
    }

    public Set<Action> getActionResponseList() {
        return actionResponseList;
    }

    public void setActionResponseList(Set<Action> actionResponseList) {
        this.actionResponseList = actionResponseList;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public Set<Parameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(Set<Parameter> parameterList) {
        this.parameterList = parameterList;
    }

    public Set<Parameter> getComplexParamerterList() {
        return complexParamerterList;
    }

    public void setComplexParamerterList(Set<Parameter> complexParamerterList) {
        this.complexParamerterList = complexParamerterList;
    }
}
