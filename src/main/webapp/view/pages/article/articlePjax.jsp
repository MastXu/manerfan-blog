<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->
<%@ taglib prefix="sec" uri="security_tld" %> <!-- http://www.springframework.org/security/tags" -->

<c:if test="${pjax}" >
	<title><c:out value="${fn:substring(title,0,32)}" />·MBLOG</title>
</c:if>

<div class="panel panel-default article-panel">
	<div class="panel-heading text-overflow">
		<span><c:out value="${title}" /></span>
		<div class="pull-right">
			<sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
				<button data-uid="<c:out value='${uid}' />" type="button" class="btn btn-danger btn-xs btn-article-recycle"><i class="glyphicon glyphicon-trash"></i>删除</button>
				<a href="/editor/<c:out value='${uid}' />" type="button" class="btn btn-primary btn-xs btn-article-edit"><i class="glyphicon glyphicon-edit"></i>编辑</a>
			</sec:authorize>
		</div>
	</div>
	<div class="panel-body">
		<span style="margin: 0 0 5px 10px;display: block;">
			<small>
				<c:out value="${hits}" />人阅读
				<i class="icon-calendar"></i><fmt:formatDate value="${createTime}" pattern="yyyy-MM-dd HH:mm" />
			</small>
		</span>
		<category>
			<i class="glyphicon glyphicon-list"></i> <span>分类</span>
			<c:if test="${not empty categories}">
				<c:forEach var="category" items="${categories}">
					<a class="article-list-pjax-href" href="<c:url value='/article/category/${category.name}' />"><c:out value="${category.name}"/></a>
				</c:forEach>
			</c:if>	
		</category>
		<span class="callout-danger">版权声明：本文为博主原创文章，未经博主允许不得转载。</span>
		<summary><pre><b>摘要: </b><c:out value="${summary}" escapeXml="false"/></pre></summary>
		<article><c:out value="${content}" escapeXml="false"/></article>
		<c:import url='../share/bdshare.jsp' />
	</div>
</div>
<nav>
	<ul class="pager">
		<c:choose>
			<c:when test="${not empty articlePrev}">
				<li class="previous"><a class="article-content-pjax-href" href="<c:url value='/article/${articlePrev.uid}' />"><span aria-hidden="true">&larr;</span> <c:out value="${fn:substring(articlePrev.title,0,16)}" /></a></li>		
			</c:when>
			<c:otherwise>
				<li class="previous disabled"><a>已是第一篇</a></li>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${not empty articleNext}">
				<li class="next"><a class="article-content-pjax-href" href="<c:url value='/article/${articleNext.uid}' />"><c:out value="${fn:substring(articleNext.title,0,16)}" /> <span aria-hidden="true">&rarr;</span></a></li>		
			</c:when>
			<c:otherwise>
				<li class="next disabled"><a>已是最后一篇</a></li>
			</c:otherwise>
		</c:choose>
	</ul>
</nav>

<c:if test="${not empty morelikes}">
	<div class="panel panel-default">
		<div class="panel-heading">推荐文章</div>
		<div class="panel-body">
			<div class="list-group article-list">
			<c:forEach var="like" items="${morelikes}">
				<article class="list-group-item">
					<h4><a class="text-info" href="<c:url value='/article/${like.uid}' />"><c:out value="${like.title}" /></a></h4>
					<section title="<c:out value="${like.summary}" />"><c:out value="${like.summary}" /></section>
					<small>
						<i class="glyphicon glyphicon-calendar"></i> <fmt:formatDate value="${like.createTime}" pattern="yyyy-MM-dd HH:mm" />
						<i class="glyphicon glyphicon-heart-empty"></i> 相关度 <fmt:formatNumber value="${like.score}" pattern="##.##" minFractionDigits="2" ></fmt:formatNumber>
					</small>
				</article>
			</c:forEach>
			</div>
		</div>
	</div>
</c:if>
