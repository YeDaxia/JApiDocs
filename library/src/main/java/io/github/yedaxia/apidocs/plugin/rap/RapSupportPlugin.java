package io.github.yedaxia.apidocs.plugin.rap;

import io.github.yedaxia.apidocs.*;
import io.github.yedaxia.apidocs.http.DHttpRequest;
import io.github.yedaxia.apidocs.http.DHttpResponse;
import io.github.yedaxia.apidocs.http.DHttpUtils;
import io.github.yedaxia.apidocs.parser.ControllerNode;

import java.io.IOException;
import java.util.*;

/**
 * post request to rap : http://rapapi.org/
 *
 * @author yeguozhong yedaxia.github.com
 */
public class RapSupportPlugin implements IPluginSupport {

    private String rapHost;
    private Integer projectId; // project id in rap
    private String cookie;

    private List<ControllerNode> controllerNodeList;

    @Override
    public void execute(List<ControllerNode> controllerNodeList) {
        this.controllerNodeList = controllerNodeList;
        postToRap();
    }

    /**
     * do post
     */
    private void postToRap(){

        DocsConfig docsConfig = DocContext.getDocsConfig();
        if(controllerNodeList == null
                || controllerNodeList.isEmpty()
                || docsConfig == null
                || docsConfig.getRapHost() == null
                || docsConfig.getRapProjectId() == null){
            LogUtils.warn("docs config properties miss, we don't think you want to post to rap!");
            return;
        }

        this.rapHost = docsConfig.getRapHost();
        this.projectId = Integer.valueOf(docsConfig.getRapProjectId());
        this.cookie = docsConfig.getRapLoginCookie();

        if(!Utils.isNotEmpty(cookie)){
            String account = docsConfig.getRapAccount();
            String password = docsConfig.getRapPassword();
            DHttpResponse response = doLogin(loginUrl(rapHost),account, password);
            this.cookie = response.getHeader("Set-Cookie");
        }

        Set<Module> moduleSet = getModuleList();

        ProjectForm projectForm = new ProjectForm();
        projectForm.setId(projectId);

        Set<DeleteActionFrom> deleteModuleForms = new HashSet<>(moduleSet.size());
        if(moduleSet != null && !moduleSet.isEmpty()){
            for(Module module : moduleSet){
                if(Module.NAME.equalsIgnoreCase(module.getName())){
                    DeleteActionFrom delForm = new DeleteActionFrom();
                    delForm.setClassName("Module");
                    delForm.setId(module.getId());
                    deleteModuleForms.add(delForm);
                }
            }
        }
        projectForm.setDeletedObjectListData(Utils.toJson(deleteModuleForms));

        Project project = Project.valueOf(projectId, controllerNodeList);
        projectForm.setProjectData(Utils.toJson(project));

        postProject(projectForm);
    }

    public DHttpResponse doLogin(String loginUrl, String userName, String password){
        DHttpRequest request = new DHttpRequest();
        request.setAutoRedirect(false);
        request.setUrl(loginUrl);
        request.addParam("account", userName);
        request.addParam("password", password);
        try {
            return DHttpUtils.httpPost(request);
        }catch (IOException ex){
            LogUtils.error("login rap fail , userName : %s, pass : %s", userName, password);
            throw new RuntimeException(ex);
        }
    }

    private Set<Module> getModuleList(){
        try{
            DHttpResponse modelResp = DHttpUtils.httpGet(queryModelUrl(rapHost, projectId));
            if(modelResp.getCode() == 200){
                ModelResponse model = Utils.jsonToObject(modelResp.streamAsString(), ModelResponse.class);
                return model.getModel().getModuleList();
            }else{
                LogUtils.error("request module data fail, rapHost : %s, projectId : %s", rapHost, projectId);
                throw new RuntimeException("request module data fail , code : " + modelResp.getCode());
            }
        }catch (IOException e){
            LogUtils.error("get rap models fail", e);
            throw new RuntimeException(e);
        }
    }

    private void postProject(ProjectForm projectForm){
        DHttpRequest request = new DHttpRequest();
        request.setUrl(checkInUrl(rapHost));
        Map<String,String> params = new HashMap<>();
        params.put("id", String.valueOf(projectForm.getId()));
        params.put("projectData", projectForm.getProjectData());
        if(projectForm.getDeletedObjectListData() != null){
            params.put("deletedObjectListData", projectForm.getDeletedObjectListData());
        }
        if(projectForm.getDescription() != null){
            params.put("description", projectForm.getDescription());
        }
        if(projectForm.getVersionPosition() != null){
            params.put("versionPosition", projectForm.getVersionPosition());
        }
        request.setParams(params);

        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Cookie", cookie);
        request.setHeaders(headers);

        try{
            DHttpResponse  response = DHttpUtils.httpPost(request);
            if(response.getCode() == 200){
                LogUtils.info("post project to rap success, response : %s " , response.streamAsString());
            }else{
                LogUtils.error("post project to rap fail !!! code : %s", response.streamAsString());
            }
        }catch (IOException e){
            LogUtils.error("post project to rap fail", e);
            throw new RuntimeException(e);
        }
    }

    private String queryModelUrl(String host, Integer projectId){
        return String.format("%s/api/queryModel.do?projectId=%s",host, projectId);
    }

    private String checkInUrl(String host){
        return String.format("%s/workspace/checkIn.do", host);
    }

    private String loginUrl(String host){
        return String.format("%s/account/doLogin.do", host);
    }
}
