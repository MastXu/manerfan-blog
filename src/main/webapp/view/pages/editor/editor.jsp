<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->
<%@ taglib prefix="sec" uri="security_tld" %> <!-- http://www.springframework.org/security/tags" -->

<!DOCTYPE html>
<%-- <html <c:if test="${cache}">manifest="cache.manifest"</c:if> > --%>
<html>

    <head>
        <title>Markdown 编辑器</title>
        <link rel="stylesheet" href="<c:url value='/view/style/themes/default.css?v=${version}'/>" type="text/css">
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
        
        <script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
    	<script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
        <script src="<c:url value="/view/js/editor/main.js?v=${version}" />"></script>
        <!-- 也可以通过data-main配置Require Config -->
        <!-- <script data-main="view/js/main.js" src="<c:url value="/view/js/require.js" />"></script> -->
        
        <style>
        	._loading{position:absolute;top:0;width:100%;height:100%;z-index:999;background-color:#88a825}
        	._loading_opacity{opacity:0.6;}
        </style>
    </head>

    <body>
        <c:set var="position" value="relative" scope="request"></c:set>
        <c:import url="../nav/header.jsp" />
        <div class="hidden-print _loading">
	        <c:import url="../common/loading.jsp" />
        </div>
        <input type="hidden" name="fileId" value="<c:out value='${fileId}'/>">
        <c:import url="bodyEditor.jsp" />
        
        <sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
			<script>var hasLogin = true;</script>
		</sec:authorize>
    </body>

</html>
