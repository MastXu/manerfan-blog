<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->
<%@ taglib prefix="sec" uri="security_tld" %> <!-- http://www.springframework.org/security/tags" -->

<style>
	.travis-ci.dropdown-menu {width:320px !important;}
	.github.dropdown-menu {width:220px !important;}
</style>

<nav id="header" class="not-print navbar navbar-default" style="position: <c:out value='${position}' default='relative' />;">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#header-collapse" aria-expanded="false">
	            <span class="sr-only">Toggle navigation</span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </button>
			<a href="<c:url value='/' />" class="navbar-brand">MBLOG</a>
		</div>
		<div class="collapse navbar-collapse" id="header-collapse">
			<ul class="nav navbar-nav">
				<li><a target="_blank" href="https://github.com/ManerFan/manerfan-blog"><i class="icon-github-circled"></i>GitHub</a></li>
			</ul>
			<ul class="nav navbar-nav">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">
						关于<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="#">关于我</a></li>
						<li><a href="#">关于博客</a></li>
					</ul>
				</li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
		            <li><a href="<c:url value='/login' />"><span class="glyphicon glyphicon-log-in"></span> 登陆</a></li>
		        </sec:authorize>
		        <sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
					<li class="dropdown">
					    <a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false">
							<span class="glyphicon glyphicon-user"></span>
								<sec:authentication property="principal.username"/>
							<span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<li><a href="<c:url value='/settings' />"><span class="glyphicon glyphicon-cog"></span> 设置</a></li>
							<li role="separator" class="divider"></li>
							<li><a href="<c:url value='/logout' />"><span class="glyphicon glyphicon-log-out"></span> 登出</a></li>
						</ul>
					</li>
				</sec:authorize>
			</ul>
		</div>
	</div>
</nav>