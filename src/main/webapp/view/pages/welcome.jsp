<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->
<%@ taglib prefix="sec" uri="security_tld" %> <!-- http://www.springframework.org/security/tags" --> 

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <meta name="author" content="ManerFan">
    <title>ManerFan的博客</title>
    
	<link rel="stylesheet" href="<c:url value='/view/style/themes/default.css?v=${version}'/>" type="text/css">
	<style>
		body {position: absolute;width: 100%;height: 100%;}
		.jumbotron {position: relative;padding-top: 50px;padding-bottom: 85px;margin: -200px auto 0;top: 50%;width: 100%;}
	    .col {width: 126px;float: left;margin: 20px;}
	    .col.panel a {font-size: 3em;}
	    .col.panel a:hover, .col.panel a:focus {text-decoration: none;}
	    .col.panel .panel-footer {text-align: center;}
	    .animated:nth-child(1) {animation-delay:.2s;-webkit-animation-delay:.2s;}
	    .animated:nth-child(2) {animation-delay:.4s;-webkit-animation-delay:.4s;}
	    .animated:nth-child(3) {animation-delay:.6s;-webkit-animation-delay:.6s;}
	    .animated:nth-child(4) {animation-delay:.8s;-webkit-animation-delay:.8s;}
	    .animated:nth-child(5) {animation-delay:1s;-webkit-animation-delay:1s;}
	    .animated:nth-child(6) {animation-delay:1.2s;-webkit-animation-delay:1.2s;}
	</style>
</head>
<body>
    <c:set var="position" value="fixed" scope="request"></c:set>
    <c:import url="nav/header.jsp" />

	<div class="jumbotron">
		<div style="display: table; margin: auto;">
			<div class="panel panel-default col animated flipInX">
				<div class="panel-body"><a href="#" class="icon-th-list"></a></div>
				<div class="panel-footer">进入博客</div>
			</div>
			<div class="panel panel-default col animated flipInX">
				<div class="panel-body"><a href="<c:url value='/editor' />" class="icon-pencil-squared"></a></div>
				<div class="panel-footer">写博客</div>
			</div>
			<div class="panel panel-default col animated flipInX">
				<div class="panel-body"><a target="_blank" href="https://github.com/ManerFan/manerfan-blog" class="icon-github-circled"></a></div>
				<div class="panel-footer">GitHub</div>
			</div>
			<div class="panel panel-default col animated flipInX">
				<div class="panel-body"><a target="_blank" href="https://travis-ci.org/ManerFan" class="icon-gauge"></a></div>
				<div class="panel-footer">TravisCI</div>
			</div>
			<div class="panel panel-default col animated flipInX">
				<div class="panel-body"><a class="icon-book"></a></div>
				<div class="panel-footer">JAVADOC</div>
			</div>
			<div class="panel panel-default col animated flipInX">
				<div class="panel-body"><a class="icon-user-md"></a></div>
				<div class="panel-footer">关于我</div>
			</div>
			<!-- <div class="panel panel-default col">
				<div class="panel-body"><a href="mailto:manerfan@foxmail.com" class="icon-mail-alt"></a></div>
				<div class="panel-footer">EMAIL</div>
			</div>
			<div class="panel panel-default col">
				<div class="panel-body"><a class="icon-chat-empty" href="#" data-toggle="modal" data-target="#wx-modal"></a></div>
				<div class="panel-footer">微信</div>
			</div> -->
		</div>
	</div>

	<!-- 微信 -->
	<div class="modal fade bs-example-modal-sm" id="wx-modal" tabindex="-1" role="dialog">
		<div class="modal-dialog modal-sm" role="document">
			<div class="modal-content">
				<div class="modal-body" style="margin: auto; display: table;">
					<img src="<c:url value='/view/images/qt-winxin.png' />"></img>
				</div>
			</div>
		</div>
	</div>

	<c:import url="nav/footer.jsp" />
	
	<script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
	<script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
	<script>
		require(["jquery","bootstrap"]);
	</script>
</body>
</html>