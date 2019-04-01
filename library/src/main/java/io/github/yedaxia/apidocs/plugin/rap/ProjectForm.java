package io.github.yedaxia.apidocs.plugin.rap;

/**
 * @author yeguozhong yedaxia.github.com
 */
class ProjectForm {

    private Integer id;
    private String projectData;
    private String deletedObjectListData;
    private String versionPosition;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectData() {
        return projectData;
    }

    public void setProjectData(String projectData) {
        this.projectData = projectData;
    }

    public String getDeletedObjectListData() {
        return deletedObjectListData;
    }

    public void setDeletedObjectListData(String deletedObjectListData) {
        this.deletedObjectListData = deletedObjectListData;
    }

    public String getVersionPosition() {
        return versionPosition == null ? "4" : versionPosition;
    }

    public void setVersionPosition(String versionPosition) {
        this.versionPosition = versionPosition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
