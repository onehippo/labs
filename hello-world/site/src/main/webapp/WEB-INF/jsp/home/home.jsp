<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<html>
<head>
</head>
<body>
<c:choose>
    <c:when test="${not empty document}">
        <h1><c:out value="${document.title}"/></h1>
        <div>
            <hst:html hippohtml="${document.content}"/>
        </div>
    </c:when>
    <c:otherwise>
        <h1>Goodbye? cruel world</h1>
    </c:otherwise>
</c:choose>
</body>
</html>