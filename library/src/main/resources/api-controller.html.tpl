<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>${CONTROLLER_DESCRIPTION}</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/prettify/r298/prettify.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/prettify/r298/prettify.min.css" rel="stylesheet">
<link href="../style.css" rel="stylesheet" />
</head>
<body onload="PR.prettyPrint()">
<div id="wmd-preview" class="wmd-preview">
<h1><a href="../index.html">《首页</a> ${CONTROLLER_DESCRIPTION}:</h1>
<div class="toc">
    <ul>
    ${TOC}
    </ul>
</div>
<hr>
${ACTION_LIST}
</div>
</body>
</html>