<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->

<style>
	.widget a {height: 36px;}
	.widget a span {font-size: 0.5em; height: 18px;}
	.widget a span:first-child {line-height: 18px;}
</style>

<div class="panel panel-default">
	<div class="panel-heading">文章分类<a href="<c:url value='/article/timeline' />" class="btn btn-sm btn-link">更多</a></div>
	<div class="widget panel-body list-badge widget-category">...</div>
</div>

<div class="panel panel-default">
	<div class="panel-heading">文章存档<a href="<c:url value='/article/timeline' />" class="btn btn-sm btn-link">更多</a></div>
	<div class="widget panel-body list-badge widget-archive">...</div>
</div>

<div class="panel panel-default">
	<div class="panel-heading">阅读排行</div>
	<div class="widget panel-body list-badge widget-hits">...</div>
</div>