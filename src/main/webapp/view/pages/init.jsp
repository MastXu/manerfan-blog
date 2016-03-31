<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->

<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" href="<c:url value='/view/style/themes/default.css?v=${version}'/>" type="text/css">

    <title>初始化</title>
    
    <style>
        h1 {margin: 1em 0 !important;}
        div, span, h1, h2, h3, h4, h5, h6, pre {font-family: 'Hiragino Sans GB', 'Microsoft YaHei', 微软雅黑, tahoma, arial, simsun, 宋体;}
		div, span, h1, h2, h3, h4, h5, h6 {text-align: center !important;color: #999 !important;}
		.body {display: none;position: absolute !important;width: 450px !important;top: 50% !important;left: 50% !important;margin-top: -250px;margin-left: -230px;}
        .panel {padding: 5px 20px !important;}
        .glyphicon-cloud {top: 10px !important;}
        form div {position: relative !important;height: 42px !important;margin: 5px 0 !important;}
        form .form-control {position: absolute !important;top: 0 !important;padding-left: 50px !important;color: #333333;}
        form .glyphicon {position: absolute !important;z-index: 99 !important;left: 10px !important;font-size: 20px !important;top: 10px !important;}
        button[type=submit] {margin-top: 10px;}
        .msg-danger {background-color: rgba(232, 76, 61, 0.5) !important;}
    </style>
</head>
<body class="hidden-print">
    <c:set var="position" value="fixed" scope="request"></c:set>
    <c:import url="nav/header.jsp" />
    
    <div class="body">
	    <h1><span class="glyphicon glyphicon-dashboard"></span> 初始化</h1>
	    <pre class="bg-info">该向导将引导您创建管理员用户并对系统做首次初始化</pre>
	    <c:choose>
	       <c:when  test="${not empty error}">
	           <pre class="msg-danger text-danger">${error}</pre>
	       </c:when>
	       <c:otherwise>
	           <pre class="msg-danger text-danger" style="display: none;"></pre>
	       </c:otherwise>   
	    </c:choose>
	    <div class="panel panel-default">
	        <div class="panel-body">
				<form name="loginForm" action="<c:url value='/init/check' />" method='POST'>
		            <div>
			            <span class="glyphicon glyphicon-user"></span>
			            <input class="form-control" type='text' name='name' placeholder="用户名">
		            </div>
		            <div>
	                    <span class="glyphicon glyphicon-lock"></span>
	                    <input class="form-control" type='password' id='password' placeholder="密码">
                        <input type='hidden' name='password'>
                        <input type='hidden' id='exponent' value='<c:out value="${exponent}" />'>
                        <input type='hidden' id='modulus' value='<c:out value="${modulus}" />'>
	                </div>
	                <div>
                        <span class="glyphicon glyphicon-envelope"></span>
                        <input class="form-control" type='text' name='email' placeholder="邮箱(可选)">
                    </div>
	                <button type="submit" class="btn btn-primary btn-lg btn-block">初始化</button>
		        </form>
			</div>
		</div>
	</div>
	
	<c:import url="nav/footer.jsp" />
	
	<script>
		window.debug = false;
	    if (/(\?|&)debug($|&)/.test(location.search)) {
	        window.debug = true;
	    }
	</script>
	
	<script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
    <script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
    <script src="<c:url value="/view/js/init.js?v=${version}" />"></script>
</body>
</html>