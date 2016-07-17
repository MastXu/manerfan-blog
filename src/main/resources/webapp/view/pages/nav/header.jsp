<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->
<%@ taglib prefix="sec" uri="security_tld" %> <!-- http://www.springframework.org/security/tags" -->

<style>
	.travis-ci.dropdown-menu {width:320px !important;}
	.github.dropdown-menu {width:220px !important;}
	.form-search{width: 240px;margin-top: 4px;}
	.form-search input, .form-search .btn{height: 32px;}
	.form-search .btn{margin: 0 !important;}
	[class^="icon-"], [class*=" icon-"] {position:relative; top: -1px;}
</style>

<nav id="header" class="hidden-print navbar navbar-default" style="position: <c:out value='${position}' default='relative' />;">
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
				<li><a href="<c:url value='/article' />" class="article-list-pjax-href"><i class="icon-th-list"></i>博文列表</a></li>
				<li><a href="<c:url value='/editor' />">
					<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
						<i class="icon-pencil-squared"></i>编辑器
			        </sec:authorize>
			        <sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
						<i class="icon-pencil-squared"></i>写博客
			        </sec:authorize>
				</a></li>
				<li><a target="_blank" href="https://github.com/ManerFan/manerfan-blog"><i class="icon-github-circled"></i>GitHub</a></li>
				<li><a target="_blank" href="<c:url value='/about/author' />"><i class="icon-user-md"></i>关于作者</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right" style="margin-right: 15px !important;">
				<li>
					<form class="form-search" action="<c:url value='/article/search' />" method='GET' target="_blank">
						<div class="form-group input-group">
							<input type="text" class="form-control" name="kw" placeholder="全文检索">
							<span class="input-group-btn">
								<button type="submit" class="btn btn-default" type="button"><span class="glyphicon glyphicon-search"></span></button>
							</span>
						</div>
					</form>
				</li>
				<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
		            <li class="login"><a href="<c:url value='/login' />"><i class="glyphicon glyphicon-log-in"></i> 后台管理</a></li>
		        </sec:authorize>
		        <sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
					<li class="dropdown">
					    <a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false">
							<i class="glyphicon glyphicon-user"></i>
								<sec:authentication property="principal.username"/>
							<i class="caret"></i>
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