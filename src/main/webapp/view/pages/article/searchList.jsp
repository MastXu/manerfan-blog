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

    <title><c:out value="${keywords}"/>·MBLOG</title>
    
    <style>
    	.navbar .form-search {display: none !important;}
    	.search-crumb {padding-top: 20px;}
    	.quick-access {margin-top: 10px;}
    	.pager {text-align: left;}
    </style>
</head>
<body>
    <c:set var="position" value="relative" scope="request"></c:set>
    <c:import url="../nav/header.jsp" />
    
    <div class="search-crumb">
    	<div class="search-logo">
    		<div>M</div>
    		<div><span>BLOG</span><span>全文检索</span></div>
    	</div>
		<form class="article-search-pjax-submit search-input input-group" action="<c:url value='/article/search' />" method='GET'>
			<input id="keywords" type="text" name="kw" value="<c:out value='${keywords}' />" class="form-control">
			<span class="input-group-btn">
				<button class="article-search-pjax-submit btn btn-default" type="submit"><span class="glyphicon glyphicon-search"></span> 咻一下</button>
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
    
    <div class="article-content">
	    <div class="col-xs-12 col-sm-12 col-md-10 col-lg-10">
	    	<div class="pjax-content">
	    		<c:import url="searchListPjax.jsp"></c:import>
	    	</div>
			<ul class="pager">
				<li class="disabled prev-page"><a href="#"><span aria-hidden="true">&larr;</span> 上一页</a></li>
				<li class="disabled next-page"><a href="#">下一页 <span aria-hidden="true">&rarr;</span></a></li>
			</ul>
		</div>
	    
	    <div class="hidden-xs hidden-sm col-md-2 col-lg-2">
	    	<c:import url="articleWidget.jsp" />
	    </div>
    </div>
    
	<c:import url="../nav/footer.jsp" />
	
	<script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
    <script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
    <script src="<c:url value="/view/js/article/searchList.js?v=${version}" />"></script>
</body>
</html>