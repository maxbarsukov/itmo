<!DOCTYPE html>
<html lang="en">
<head>
    <%@page pageEncoding="UTF-8" contentType="text/html; charset=utf8" %>
    <%
        String contextPath = request.getContextPath();
    %>
    <title>Swagger UI <%=contextPath%>
    </title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/js/swagger-ui/swagger-ui.css" />
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/js/swagger-ui/index.css" />
    <link rel="icon" type="image/png" href="<%=contextPath%>/js/swagger-ui/favicon-32x32.png" sizes="32x32" />
    <link rel="icon" type="image/png" href="<%=contextPath%>/js/swagger-ui/favicon-16x16.png" sizes="16x16" />
</head>

<body>
<div id="swagger-ui"></div>
<script src="<%=contextPath%>/js/swagger-ui/swagger-ui-bundle.js" charset="UTF-8"> </script>
<script src="<%=contextPath%>/js/swagger-ui/swagger-ui-standalone-preset.js" charset="UTF-8"> </script>
<script src="<%=contextPath%>/js/swagger-ui/swagger-initializer.js" charset="UTF-8"> </script>
</body>
</html>
