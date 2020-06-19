<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>${controller.description}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/google-code-prettify@1.0.5/bin/prettify.min.css">
    <link rel="stylesheet" href="style.css">
</head>
<body onload="PR.prettyPrint()">
<nav class="navbar">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="index.html">
                ${projectName}
            </a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="https://github.com/YeDaxia/JApiDocs" target="_blank">GitHub</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">${currentApiVersion}<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <#list apiVersionList as version>
                            <#if version != currentApiVersion>
                            <li><a href="../${version}/index.html">${version}</a></li>
                            </#if>
                        </#list>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
<div class="book with-summary">
    <div class="book-summary">
        <#include "api-common-catalog.html.ftl"/>
    </div>
    <div class="book-body">
        <div class="body-inner">
            <div class="book-header">
                <div class="d-flex justify-content-between">
                    <a class="header-menu toggle-catalog" href="javascript:void(0)"><i
                            class="glyphicon glyphicon-align-justify"></i> ${i18n.getMessage('catalog')}</a>
                </div>
            </div>
            <div class="page-wrapper">
                <div class="page-inner">
                    <div class="action-list">
                        <#list controller.requestNodes as reqNode>
                        <div class="action-item">
                        <#assign requestNode = reqNode/>
                        <#if reqNode.lastRequestNode?? && reqNode.changeFlag == 2>
                            <ul class="nav nav-tabs" role="tablist">
                                <li role="presentation" class="active"><a href="#current" aria-controls="current" role="tab" data-toggle="tab">${i18n.getMessage('currentVersion')}</a></li>
                                <li role="presentation"><a href="#last" aria-controls="last" role="tab" data-toggle="tab">${i18n.getMessage('lastVersion')}</a></li>
                            </ul>
                            <div class="tab-content">
                                <div role="tabpanel" class="tab-pane active" id="current">
                                    <#include "api-request-node.html.ftl"/>
                                </div>
                                <div role="tabpanel" class="tab-pane" id="last">
                                    <#assign requestNode = reqNode.lastRequestNode/>
                                    <#include "api-request-node.html.ftl"/>
                                </div>
                            </div>
                         <#else>
                            <#include "api-request-node.html.ftl"/>
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
        <#list controllerNodeList as ctrolNode>
            <#list ctrolNode.requestNodes as reqNode>
            {name: '${ctrolNode.description}.${(reqNode.description)!''}', url: '${reqNode.codeFileUrl}'},
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