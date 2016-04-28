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

    <title>MBLOG</title>
    
    <style>
    	.panel-heading .btn, .list-group .btn {float: right; margin-right: 5px; padding: 0;}
    	._loading{display:none;position:absolute;top:0;width:100%;height:100%;z-index:999;background-color:#88a825;opacity:0.6;}
    </style>
</head>
<body>
    <c:set var="position" value="relative" scope="request"></c:set>
    <c:import url="../nav/header.jsp" />
    
    <div class="article-content">
	    <div class="hidden-xs hidden-sm col-md-2 col-lg-2">
	    	<c:import url="articleWidget.jsp" />
	    </div>
	    
	    <div class="col-xs-12 col-sm-12 col-md-10 col-lg-10">
	    	<div class="panel panel-default">
				<div class="panel-heading">文章列表</div>
				<div class="panel-body">
					<div class="list-group article-list"></div>
	                <div class="pagination pull-right article-pagination" style="margin-bottom: 0;">
					    <a href="#" class="first" data-action="first">&laquo;</a>
					    <a href="#" class="previous" data-action="previous">&lsaquo;</a>
					    <input type="text" />
					    <a href="#" class="next" data-action="next">&rsaquo;</a>
					    <a href="#" class="last" data-action="last">&raquo;</a>
					</div>
				</div>
			</div>
	    </div>
    </div>
    
    <div class="hidden-print _loading">
	    <c:import url="../common/loading.jsp" />
    </div>
    
	<c:import url="../nav/footer.jsp" />
	
	<script>
		var funcname = '<c:out value="${funcname}" />';
		var funcparam = '<c:out value="${funcparam}" />';
		var logined = false;
	</script>
	<sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
		<script>logined = true;	</script>
	</sec:authorize>
	
	<script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
    <script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
    <script src="<c:url value="/view/js/article/articleList.js?v=${version}" />"></script>
</body>
</html>