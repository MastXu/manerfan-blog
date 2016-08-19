<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" href="<c:url value='/view/style/themes/default.css?v=${version}'/>" type="text/css">

    <title>ManerFan的博客</title>
    
    <style>
    	.search-bg{background: url(<c:url value='/view/images/search-bg.jpg' />) no-repeat center center;position: absolute;top: 0;left: 0;width: 100%;height: 100%;z-index: -1;}
    	.navbar .form-search {display: none !important;}
    	footer {background-color: transparent;border: none;}
	    footer * {color:#3f3f3f;}
    </style>
</head>
<body>
    <c:set var="position" value="fixed" scope="request"></c:set>
    <c:import url="./nav/header.jsp" />
    
    <div class="search-bg"></div>
    
    <div class="search-crumb">
    	<div class="search-logo">
    		<div>M</div>
    		<div><span>BLOG</span><span>全文检索</span></div>
    	</div>
		<form class="search-input input-group" action="<c:url value='/article/search' />" method='GET'>
			<input type="text" name="kw" class="form-control">
			<span class="input-group-btn">
				<button class="btn btn-default" type="submit"><span class="glyphicon glyphicon-search"></span> 咻一下</button>
			</span>
		</form>
	</div>
	
	<div class="quick-access">
		<a type="button" class="btn btn-link" href="<c:url value='/article' />"><i class="icon-th-list"></i>博文列表</a>
		<a type="button" class="btn btn-link" href="<c:url value='/article/timeline' />"><i class="icon-lightbulb"></i>时间线</a>
		<a type="button" class="btn btn-link" href="<c:url value='/editor' />">
			<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
				<i class="icon-pencil-squared"></i>编辑器
	        </sec:authorize>
	        <sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
				<i class="icon-pencil-squared"></i>写博客
	        </sec:authorize>
		</a>
		<a type="button" class="btn btn-link" target="_blank" href="https://github.com/ManerFan/manerfan-blog"><i class="icon-github-circled"></i>GitHub</a>
	</div>
	
	<%-- <div class="quick-access">
		<a type="button" class="btn btn-link"  target="_blank" href="<c:url value='/about/author' />"><i class="icon-user-md"></i>关于作者</a>
		<a type="button" class="btn btn-link"  target="_blank" href="<c:url value='/about/mblog' />"><i class="icon-user-md"></i>关于博客</a>
	</div> --%>
    
	<c:import url="./nav/footer.jsp" />
	
	<script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
    <script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
    <script src="<c:url value="/view/js/home.js?v=${version}" />"></script>
</body>
</html>