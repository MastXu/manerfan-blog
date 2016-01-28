<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>  

<nav class="navbar navbar-default" style="position: <c:out value='${position}' default='relative' />;">
	<div class="container-fluid">
		<div class="navbar-header">
			<a href="#" class="navbar-brand">BLOG</a>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<li><a href="#">Link <span class="sr-only">(current)</span></a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
		            <li><a href="<c:url value='/login' />">登陆</a></li>
		        </sec:authorize>
		        <sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
		            <li><a href="#" ><sec:authentication property="principal.username"/></a></li>
		        </sec:authorize>
			</ul>
		</div>
		<%-- <sec:authorize access="hasRole('ROLE_ANONYMOUS')">
			<a href="<c:url value='/login' />">登陆</a>
		</sec:authorize> --%>
	</div>
</nav>