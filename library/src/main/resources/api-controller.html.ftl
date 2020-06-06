<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${description}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/google-code-prettify@1.0.5/bin/prettify.min.css">
    <link rel="stylesheet" href="style.css">
</head>
<body onload="PR.prettyPrint()">
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">
                JApiDocs
            </a>
        </div>
    </div>
</nav>
<div class="book with-summary">
    <div class="book-summary">
        <div class="search-box form-group">
            <input type="text" class="form-control" id="inputSearch" placeholder="搜索接口">
            <span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
        </div>
        <div id="accordion" class="catalog">
            <#list controllerNodes as ctrolNode>
            <div class="panel">
                <div id="heading${ctrolNode?index}" data-parent="#accordion" class="catalog-title" data-toggle="collapse"
                     aria-expanded="true" data-target="#collapse${ctrolNode?index}" aria-controls="collapse${ctrolNode?index}">
                    <i class="glyphicon glyphicon-align-justify"></i> ${ctrolNode.description}
                </div>
                <div id="collapse${ctrolNode?index}" class="collapse <#if ctrolNode.docFileName == docFileName>in </#if>" aria-labelledby="heading${ctrolNode?index}">
                    <#list ctrolNode.requestNodes as reqNode>
                        <a class="catalog-item" href="${reqNode.codeFileUrl}">
                            ${reqNode.description}
                        </a>
                    </#list>
                </div>
            </div>
            </#list>
        </div>
    </div>
    <div class="book-body">
        <div class="body-inner">
            <div class="book-header">
                <div class="d-flex justify-content-between">
                    <a class="header-menu toggle-catalog" href="javascript:void(0)"><i
                            class="glyphicon glyphicon-align-justify"></i> 目录</a>
                </div>
            </div>
            <div class="page-wrapper">
                <div class="page-inner">
                    <div class="action-list">
                        <#list requestNodes as reqNode>
                        <div class="action-item">
                            <h2 id="${reqNode.methodName}"><a href="${reqNode.methodName}">${reqNode.description} <#if reqNode.deprecated><span class="badge">过期</span></#if></a></h2>
                            <p><strong>请求URL</strong></p>
                            <p>
                                <code>${reqNode.url}</code>
                                <#list reqNode.method as method>
                                    <span class="label label-default">${method}</span>
                                </#list>
                            </p>
                            <#if reqNode.paramNodes?size != 0>
                            <p><strong>参数列表</strong></p>
                            <#assign isJsonReqBody = false/>
                            <#list reqNode.paramNodes as paramNode>
                                <#if paramNode.jsonBody>
                                    <pre class="prettyprint lang-json">${paramNode.description}</pre>
                                    <#assign isJsonReqBody = true/>
                                </#if>
                            </#list>
                            <#if !isJsonReqBody>
                                <table class="table table-bordered">
                                    <tr>
                                        <th>参数名</th>
                                        <th>类型</th>
                                        <th>必须</th>
                                        <th>描述</th>
                                    </tr>
                                    <#list reqNode.paramNodes as paramNode>
                                    <tr>
                                        <td>${paramNode.name}</td>
                                        <td>${paramNode.type}</td>
                                        <td>${paramNode.required?string('是','否')}</td>
                                        <td>${paramNode.description}</td>
                                    </tr>
                                    </#list>
                                </table>
                            </#if>
                            </#if>
                            <#if reqNode.responseNode??>
                                <p><strong>返回结果</strong></p>
                                <pre class="prettyprint lang-json">${reqNode.responseNode.toJsonApi()}</pre>
                                <#if reqNode.androidCodePath??>
                                    <div class="form-group">
                                        <a type="button" class="btn btn-sm btn-default" href="${reqNode.androidCodePath}">Android Model</a>
                                        <a type="button" class="btn btn-sm btn-default" href="${reqNode.iosCodePath}">iOS Model</a>
                                    </div>
                                </#if>
                            </#if>
                        </div>
                        <hr>
                        </#list>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"></script>
<script src="https://cdn.jsdelivr.net/autocomplete.js/0/autocomplete.jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/google-code-prettify@1.0.5/bin/prettify.min.js"></script>
<script>

    var search_source_data = [
        <#list controllerNodes as ctrolNode>
            <#list ctrolNode.requestNodes as reqNode>
            {name: '${ctrolNode.description}.${reqNode.description}', url: '${reqNode.codeFileUrl}'},
            </#list>
        </#list>
    ];



    $('.toggle-catalog').click(function () {
        $('.book').toggleClass('with-summary');
    });

    $('#inputSearch').autocomplete({hint: false}, [
        {
            source: function (query, callback) {
                var result = [];
                for(var i = 0; i !== search_source_data.length; i++){
                    if(search_source_data[i].name.indexOf(query) !== -1){
                        result.push(search_source_data[i]);
                    }
                }
                callback(result);
            },
            displayKey: 'name',
            templates: {
                suggestion: function (suggestion) {
                    return suggestion.name;
                }
            }
        }
    ]).on('autocomplete:selected', function (event, suggestion, dataset, context) {
        self.location = suggestion.url;
    });
</script>
</body>
</html>