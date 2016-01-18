<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html <c:if test="${cache}">manifest="cache.manifest"</c:if> >

    <head>
        <title>Editor</title>
        <link rel="canonical" href="http://www.manerfan.com/">
        <link rel="icon" href="<c:url value="/view/images/editor/stackedit-32.ico" />" type="image/x-icon">
        <link rel="icon" sizes="192x192" href="<c:url value="/view/images/editor/logo-highres.png" />" >
        <link rel="shortcut icon" href="<c:url value="/view/images/editor/stackedit-32.ico" />" type="image/x-icon">
        <link rel="shortcut icon" sizes="192x192" href="<c:url value="/view/images/editor/logo-highres.png" />" >
        <link rel="apple-touch-icon-precomposed" sizes="152x152" href="<c:url value="/view/images/editor/logo-ipad-retina.png" />">
        <meta charset="UTF-8">
        <meta name="description" content="Full-featured, open-source Markdown editor based on PageDown, the Markdown library used by Stack Overflow and the other Stack Exchange sites.">
        <meta name="author" content="Benoit Schweblin">
        <meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
        <meta name="mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <meta name="msvalidate.01" content="5E47EE6F67B069C17E3CDD418351A612">
        <script>
            // 配置Require Config
            // Use ?debug to serve original JavaScript files instead of minified
            /*window.baseDir = 'view';
            window.require = {
                baseUrl: window.baseDir,
                deps: ['js/main']
            };*/
        </script>
        <script src="<c:url value="/view/js/require.js" />"></script>
        <script src="<c:url value="/view/js/main.js" />"></script>
        <script src="<c:url value="/view/js/editor/main.js" />"></script>
        <!-- 也可以通过data-main配置Require Config -->
        <!-- <script data-main="view/js/main.js" src="<c:url value="/view/js/require.js" />"></script> -->
    </head>

    <body></body>

</html>
