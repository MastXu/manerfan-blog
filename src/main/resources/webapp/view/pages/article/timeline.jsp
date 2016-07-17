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
	<link rel="stylesheet" href="<c:url value='/view/css/timeline/blueprint/blueprint.css?v=${version}'/>" type="text/css">
    <title>时间线·MBLOG</title>
</head>
<body>
    <c:set var="position" value="relative" scope="request"></c:set>
    <c:import url="../nav/header.jsp" />
    
    <div class="article-content">
    	<div class="col-xs-12 col-sm-12  col-md-12 col-lg-6">
    		<h2 class="container-fluid text-primary">归档</h2>
    		<c:if test="${not empty archives}">
    			<ul class="cbp_tmtimeline">
    				<c:forEach items="${archives}" var="archive">
    					<li>
							<time class="cbp_tmtime"><span>归档 ${archive.num} 篇</span> <span><a href="<c:url value='/article/archive/${archive.date}' />">${archive.date}</a></span></time>
							<div class="cbp_tmicon"></div>
							<div class="cbp_tmlabel">
								<p>${archive.date} 归档${archive.num}篇</p>
							</div>
						</li>
    				</c:forEach>
				</ul>
    		</c:if>
	    </div>
	    
	    <div class="col-xs-12 col-sm-12  col-md-12 col-lg-6">
	    	<h2 class="container-fluid text-primary">分类</h2>
	    	<c:if test="${not empty categories}">
    			<ul class="cbp_tmtimeline">
    				<c:forEach items="${categories}" var="category">
    					<li>
							<time class="cbp_tmtime"><span>收录 ${category.num} 篇</span> <span><a href="<c:url value='/article/category/${category.name}' />">${category.name}</a></span></time>
							<div class="cbp_tmicon"></div>
							<div class="cbp_tmlabel">
								<p>${category.name} 收录${category.num}篇</p>
							</div>
						</li>
    				</c:forEach>
				</ul>
    		</c:if>
	    </div>
    </div>
    
	<c:import url="../nav/footer.jsp" />
</body>
</html>