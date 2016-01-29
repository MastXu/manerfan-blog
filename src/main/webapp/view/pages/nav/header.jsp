<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>  

<nav class="navbar navbar-default" style="position: <c:out value='${position}' default='relative' />;">
	<div class="container-fluid">
		<div class="navbar-header">
			<a href="<c:url value='/' />" class="navbar-brand">MBLOG</a>
		</div>
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav">
				<li><a href="#">Link <span class="sr-only">(current)</span></a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
		            <li><a href="<c:url value='/login' />"><span class="glyphicon glyphicon-log-in"></span> 登陆</a></li>
		        </sec:authorize>
		        <sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
					<li class="dropdown">
					    <a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false"><span class="glyphicon glyphicon-user"></span> <sec:authentication property="principal.username"/> <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="<c:url value='/settings' />"><span class="glyphicon glyphicon-cog"></span> 设置</a></li>
							<li role="separator" class="divider"></li>
							<li><a href="<c:url value='/logout' />"><span class="glyphicon glyphicon-log-out"></span> 登出</a></li>
						</ul></li>
				</sec:authorize>
			</ul>
		</div>
	</div>
</nav>