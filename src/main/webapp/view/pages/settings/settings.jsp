<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->
<%@ taglib prefix="sec" uri="security_tld" %> <!-- http://www.springframework.org/security/tags" -->

<!DOCTYPE html>
<html>
<head>
    <title>设置</title>
    
    <link rel="stylesheet" href="<c:url value='/view/style/themes/default.css?v=${version}'/>" type="text/css">
    <style>
        .main-content #panel-settings>div {
            display: none;
        }
    </style>
</head>
<body>
    <c:import url="../nav/header.jsp" />
    
    <div class="main-content">
		<div class="panel panel-default col-xs-12 col-sm-4 col-md-3 col-lg-3">
			<div class="panel-heading">设置</div>
			<ul class="list-group">
			    <li class="list-group-item active" data-action="account-profile"><a href="#panel-settings">用户简介</a></li>
			    <li class="list-group-item" data-action="account-settings"><a href="#panel-settings">用户设置</a></li>
			</ul>
		</div>
		<div class="col-sm-1 col-md-1 col-lg-1"></div>
		<div id="panel-settings" class="col-xs-12 col-sm-7 col-md-8 col-lg-8">
			<div data-action="account-profile" class="panel panel-default" style="display: block;">
	            <div class="panel-heading">用户简介</div>
	            <div class="panel-body">
	                
	            </div>
	        </div>
	        <div data-action="account-settings" class="panel panel-default">
	            <div class="panel-heading">用户设置</div>
	            <div class="panel-body">
	                
	            </div>
	        </div>
        </div>
	</div>
	
	<c:import url="../nav/footer.jsp" />

	<script>
		window.debug = false;
	    if (/(\?|&)debug($|&)/.test(location.search)) {
	        window.debug = true;
	    }
	</script>
	
	<script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
    <script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
    <script src="<c:url value="/view/js/settings/settings.js?v=${version}" />"></script>
</body>
</html>