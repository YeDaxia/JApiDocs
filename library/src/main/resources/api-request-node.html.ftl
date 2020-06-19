<h2 id="${requestNode.methodName}"><a href="#">${(requestNode.description)!''} <#if requestNode.deprecated><span class="badge">${i18n.getMessage('deprecated')}</span></#if></a></h2>
<#if requestNode.author??>
    <p class="text-muted"><em>${i18n.getMessage('author')}: ${requestNode.author}</em></p>
</#if>
<p><strong>${i18n.getMessage('requestUrl')}</strong></p>
<p>
    <code>${requestNode.url}</code>
    <#list requestNode.method as method>
        <span class="label label-default">${method}</span>
    </#list>
    <#if requestNode.changeFlag == 1>
        <span class="label label-success">${i18n.getMessage('new')}</span>
    <#elseif requestNode.changeFlag == 2>
        <span class="label label-warning">${i18n.getMessage('modify')}</span>
    </#if>
</p>
<#if requestNode.paramNodes?size != 0>
    <p><strong>${i18n.getMessage('requestParameters')}</strong></p>
    <#assign isJsonReqBody = false/>
    <#list requestNode.paramNodes as paramNode>
        <#if paramNode.jsonBody>
            <pre class="prettyprint lang-json">${paramNode.description}</pre>
            <#assign isJsonReqBody = true/>
        </#if>
    </#list>
    <#if !isJsonReqBody>
        <table class="table table-bordered">
            <tr>
                <th>${i18n.getMessage('parameterName')}</th>
                <th>${i18n.getMessage('parameterType')}</th>
                <th>${i18n.getMessage('parameterNeed')}</th>
                <th>${i18n.getMessage('description')}</th>
            </tr>
            <#list requestNode.paramNodes as paramNode>
                <tr>
                    <td>${paramNode.name}</td>
                    <td>${paramNode.type}</td>
                    <td>${paramNode.required?string(i18n.getMessage('yes'),i18n.getMessage('no'))}</td>
                    <td>${paramNode.description}</td>
                </tr>
            </#list>
        </table>
    </#if>
</#if>
<#if requestNode.responseNode??>
    <p><strong>${i18n.getMessage('responseResult')}</strong></p>
    <pre class="prettyprint lang-json">${requestNode.responseNode.toJsonApi()}</pre>
    <#if requestNode.androidCodePath??>
        <div class="form-group">
            <a type="button" class="btn btn-sm btn-default" href="${requestNode.androidCodePath}"><i class="fa fa-android" aria-hidden="true"></i> Android Model</a>
            <a type="button" class="btn btn-sm btn-default" href="${requestNode.iosCodePath}"><i class="fa fa-apple" aria-hidden="true"></i> iOS Model</a>
        </div>
    </#if>
</#if>
