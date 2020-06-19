<div class="search-box form-group">
    <input type="text" class="form-control" id="inputSearch" placeholder="${i18n.getMessage('searchPlaceholder')}">
    <span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
</div>
<div id="accordion" class="catalog">
    <#list controllerNodeList as ctrolNode>
        <div class="panel">
            <div id="heading${ctrolNode?index}" data-parent="#accordion" class="catalog-title" data-toggle="collapse"
                 aria-expanded="true" data-target="#collapse${ctrolNode?index}" aria-controls="collapse${ctrolNode?index}">
                <i class="glyphicon glyphicon-align-justify"></i> ${ctrolNode.description}
            </div>
            <div id="collapse${ctrolNode?index}" class="collapse <#if (controller?? && ctrolNode.docFileName == controller.docFileName) || ctrolNode?index == 0>in </#if>" aria-labelledby="heading${ctrolNode?index}">
                <#list ctrolNode.requestNodes as reqNode>
                    <a class="catalog-item" href="${reqNode.codeFileUrl}">
                        ${(reqNode.description)!''}
                        <#if reqNode.changeFlag == 1>
                            <span class="label label-success">${i18n.getMessage('new')}</span>
                        <#elseif reqNode.changeFlag == 2>
                            <span class="label label-warning">${i18n.getMessage('modify')}</span>
                        </#if>
                    </a>
                </#list>
            </div>
        </div>
    </#list>
</div>