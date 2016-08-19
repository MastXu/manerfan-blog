<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="panel panel-default">
	<div class="panel-heading">
		<span>文章列表</span>
		<span class="pull-right text-warning" style="margin-left: 5px;"><c:out value="${funcparam}" /></span>
		<span class="pull-right text-success"><c:out value="${displayname}" /></span>
	</div>
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

<input id="funcname" type="hidden" value='<c:out value="${funcname}" />'>
<input id="funcparam" type="hidden" value='<c:out value="${funcparam}" />'>
