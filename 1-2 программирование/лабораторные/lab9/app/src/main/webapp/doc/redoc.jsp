<!DOCTYPE html>
<html>
<head>
    <%@page pageEncoding="UTF-8" contentType="text/html; charset=utf8" %>
    <%
        String contextPath = request.getContextPath();
    %>
    <title>ReDoc <%=contextPath%>
    </title>
    <!-- needed for adaptive design -->
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:300,400,700|Roboto:300,400,700" rel="stylesheet">

    <!--
    ReDoc doesn't change outer page styles
    -->
    <style>
        body {
            margin: 0;
            padding: 0;
        }
    </style>
</head>
<body>
<redoc spec-url="<%=contextPath%>/api/openapi.json"></redoc>
<script src="<%=contextPath%>/js/redoc/redoc.standalone.js"></script>
</body>
</html>
