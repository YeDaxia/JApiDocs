package io.github.yedaxia.apidocs.ext.rap;

import io.github.yedaxia.apidocs.parser.ControllerNode;
import io.github.yedaxia.apidocs.parser.ParamNode;
import io.github.yedaxia.apidocs.parser.RequestNode;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author yeguozhong yedaxia.github.com
 */
class Project {

    private int id;
    private int userId;
    private String name;
    private Date createDate;
    private Date updateTime;
    private String introduction;
    private int workspaceModeInt;
    private String relatedIds = "";
    private int groupId;
    private int mockNum;
    private int teamId;
    private short accessType;
    private Set<Module> moduleList = new HashSet<Module>();
    private String projectData;
    private List<String> memberAccountList;
    private String version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getWorkspaceModeInt() {
        return workspaceModeInt;
    }

    public void setWorkspaceModeInt(int workspaceModeInt) {
        this.workspaceModeInt = workspaceModeInt;
    }

    public String getRelatedIds() {
        return relatedIds;
    }

    public void setRelatedIds(String relatedIds) {
        this.relatedIds = relatedIds;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getMockNum() {
        return mockNum;
    }

    public void setMockNum(int mockNum) {
        this.mockNum = mockNum;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public short getAccessType() {
        return accessType;
    }

    public void setAccessType(short accessType) {
        this.accessType = accessType;
    }

    public Set<Module> getModuleList() {
        return moduleList;
    }

    public void setModuleList(Set<Module> moduleList) {
        this.moduleList = moduleList;
    }

    public String getProjectData() {
        return projectData;
    }

    public void setProjectData(String projectData) {
        this.projectData = projectData;
    }

    public List<String> getMemberAccountList() {
        return memberAccountList;
    }

    public void setMemberAccountList(List<String> memberAccountList) {
        this.memberAccountList = memberAccountList;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static Project valueOf(int id, ControllerNode controllerNode){
        Project project = new Project();
        project.setId(id);
        Module module = new Module();
        module.setId(-10);
        module.setName("API 列表");
        project.getModuleList().add(module);
        Page page = new Page();
        page.setId(-11);
        page.setName(controllerNode.getDescription());
        int actionId = -12;

        for(RequestNode requestNode : controllerNode.getRequestNodes()){
            Action action = new Action();
            action.setId(actionId--);
            action.setName(requestNode.getDescription());
            action.setRequestUrl(requestNode.getUrl());
            action.setRequestType("get".equalsIgnoreCase(requestNode.getMethod()) ? "1" : "2");

            for(ParamNode paramNode : requestNode.getParamNodes()){
                Parameter parameter = new Parameter();
                parameter.setIdentifier(paramNode.getName());
                parameter.setName(paramNode.getDescription());
                parameter.setDataType(DataType.rapTypeOfNode(paramNode.getType()));
                //parameter.setRemark(""); mock data
            }

            page.getActionList().add(action);
        }
        return project;
    }


}
