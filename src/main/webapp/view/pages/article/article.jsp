<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->
<%@ taglib prefix="sec" uri="security_tld" %> <!-- http://www.springframework.org/security/tags" -->

<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" href="<c:url value='/view/style/themes/default.css?v=${version}'/>" type="text/css">
	<link rel="stylesheet" href="<c:url value='/view/css/article/article.css?v=${version}'/>" type="text/css">
    <title><c:out value="${fn:substring(title,0,32)}" />·MBLOG</title>
</head>
<body>
    <c:set var="position" value="relative" scope="request"></c:set>
    <c:import url="../nav/header.jsp" />
    
    <div class="article-content">
	    <div class="hidden-xs hidden-print hidden-sm col-md-2 col-lg-2">
	    	<c:import url="articleWidget.jsp" />
	    </div>
	    
	    <div class="pjax-content col-xs-12 col-sm-12 col-md-10 col-lg-10">
	    	<c:import url="articlePjax.jsp"></c:import>
		</div>
    </div>
    
    <div class="hidden-print _loading">
	    <c:import url="../common/loading.jsp" />
    </div>
    
	<c:import url="../nav/footer.jsp" />
	
	<script>
		var logined = false;
		<sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
			logined = true;
		</sec:authorize>
	</script>
	
	<script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
    <script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
    <script src="<c:url value="/view/js/article/article.js?v=${version}" />"></script>
</body>
</html>