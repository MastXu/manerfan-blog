<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html <c:if test="${cache}">manifest="cache.manifest"</c:if> >

    <head>
        <title>StackEdit Viewer</title>
        <link rel="canonical" href="http://www.manerfan.com/">
        <link rel="icon" href="res-min/img/stackedit-32.ico" type="image/x-icon">
        <link rel="icon" sizes="192x192" href="res-min/img/logo-highres.png">
        <link rel="shortcut icon" href="res-min/img/stackedit-32.ico" type="image/x-icon">
        <link rel="shortcut icon" sizes="192x192" href="res-min/img/logo-highres.png">
        <meta charset="UTF-8">
        <meta name="description" content="Full-featured, open-source Markdown editor based on PageDown, the Markdown library used by Stack Overflow and the other Stack Exchange sites.">
        <meta name="author" content="Benoit Schweblin">
        <meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
        <meta name="msvalidate.01" content="5E47EE6F67B069C17E3CDD418351A612">
        <script>
            // Use ?debug to serve original JavaScript files instead of minified
            window.baseDir = 'res';
            if (!/(\?|&)debug($|&)/.test(location.search)) {
                window.baseDir += '-min';
            }
            window.require = {
                baseUrl: window.baseDir,
                deps: ['main']
            };
        </script>
        <script src="res-min/require.js"></script>
    </head>

    <body class="viewer"></body>

</html>
