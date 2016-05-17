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

    <title><c:out value="${fn:substring(title,0,32)}" />·MBLOG</title>
    
    <style>
    	.panel-heading .btn, .list-group .btn {float: right; margin-right: 5px; padding: 0;}
    	.callout-danger {margin-bottom: 15px;display: inline-block;padding: 2px 12px;border-left: 3px solid #ce4844;}
    	article {padding: 0 35px 40px;}
    	._loading{display:none;position:absolute;top:0;width:100%;height:100%;z-index:999;background-color:#88a825;opacity:0.6;}
    </style>
</head>
<body>
    <c:set var="position" value="relative" scope="request"></c:set>
    <c:import url="../nav/header.jsp" />
    
    <div class="article-content">
	    <div class="hidden-xs hidden-print hidden-sm col-md-2 col-lg-2">
	    	<c:import url="articleWidget.jsp" />
	    </div>
	    
	    <div class="col-xs-12 col-sm-12 col-md-10 col-lg-10">
	    	<div class="panel panel-default">
				<div class="panel-heading">
					<span><c:out value="${title}" /></span>
				</div>
				<div class="panel-body">
					<div style="display:inline-block; width: 100%;">
						<div class="pull-left">
							<span class='label label-info has-badge'>阅读<span class='badge'><c:out value="${hits}" /></span></span>
							<span class='label label-info'><i class="icon-calendar"><c:out value="${createTime}" /></i></span>
						</div>
						<div class="pull-right">
							<sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
								<button data-uid="<c:out value='${uid}' />" type="button" class="btn btn-danger btn-xs btn-article-recycle"><i class="glyphicon glyphicon-trash"></i>删除</button>
								<a href="/editor/<c:out value='${uid}' />" type="button" class="btn btn-primary btn-xs btn-article-edit"><i class="glyphicon glyphicon-edit"></i>编辑</a>
							</sec:authorize>
						</div>
					</div>
					<pre><i class="glyphicon glyphicon-list"></i> <span>分类</span></pre>
					<span class="callout-danger">版权声明：本文为博主原创文章，未经博主允许不得转载。</span>
					<article><c:out value="${content}" escapeXml="false"/></article>
					<c:import url='../share/bdshare.jsp' />
				</div>
			</div>
			<nav>
				<ul class="pager">
					<c:choose>
						<c:when test="${not empty articlePrev}">
							<li class="previous"><a href="<c:url value='/article/${articlePrev.uid}' />"><span aria-hidden="true">&larr;</span> <c:out value="${fn:substring(articlePrev.title,0,16)}" /></a></li>		
						</c:when>
						<c:otherwise>
							<li class="previous disabled"><a>已是第一篇</a></li>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${not empty articleNext}">
							<li class="next"><a href="<c:url value='/article/${articleNext.uid}' />"><span aria-hidden="true">&rarr;</span> <c:out value="${fn:substring(articleNext.title,0,16)}" /></a></li>		
						</c:when>
						<c:otherwise>
							<li class="next disabled"><a>已是最后一篇</a></li>
						</c:otherwise>
					</c:choose>
				</ul>
			</nav>
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
	
	<script type="text/javascript" src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS_HTML"></script>
	<script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
    <script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
    <script src="<c:url value="/view/js/article/article.js?v=${version}" />"></script>
</body>
</html>