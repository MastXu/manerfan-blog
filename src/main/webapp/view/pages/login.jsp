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
	
    <title>登陆</title>
    
    <style>
        h1 {margin: 1em 0 !important;}
        div, span, pre,
        h1, h2, h3, h4, h5, h6 {
            font-family: 'Hiragino Sans GB', 'Microsoft YaHei', 微软雅黑, tahoma, arial, simsun, 宋体;
        }
        div, span, 
        h1, h2, h3, h4, h5, h6 {
            text-align: center !important;
            color: #999 !important;
        }
        .body {display: none;position: absolute !important;width: 450px !important;top: 50% !important;left: 50% !important;margin-top: -235px;margin-left: -230px;}
        .panel {padding: 5px 20px !important;}
        .glyphicon-cloud {top: 10px !important;}
        form div {position: relative !important;height: 42px !important;margin: 5px 0 !important;}
        form .form-control {position: absolute !important;top: 0 !important;padding-left: 50px !important;color: #333333;}
        form .glyphicon {position: absolute !important;z-index: 99 !important;left: 5px !important;font-size: 20px !important;top: 5px !important;}
        button[type=submit] {margin-top: 10px;}
		.msg-danger {background-color: rgba(232, 76, 61, 0.5) !important;}
		.msg-info {background-color: rgba(217, 237, 247, 0.5) !important;}
    </style>
</head>
<body class="not-print">
    <c:set var="position" value="fixed" scope="request"></c:set>
    <c:import url="nav/header.jsp" />
    
    <div class="body">
	    <h1><span class="icon-cloud"></span> 登陆</h1>
	    <c:if test="${not empty err}">
	        <pre class="msg-danger text-danger">${err}</pre>
	    </c:if>
	    <c:if test="${not empty info}">
	        <pre class="msg-info text-info">${info}</pre>
	    </c:if>
	    <div class="panel panel-default">
	        <div class="panel-body">
				<form name="loginForm" action="<c:url value='/login/check' />" method='POST'>
		            <div>
                        <span class="glyphicon icon-user"></span>
                        <input class="form-control" type='text' name='username' placeholder="用户名">
                    </div>
                    <div>
                        <span class="glyphicon icon-lock"></span>
                        <input class="form-control" type='password' id='password' placeholder="密码">
                        <input type='hidden' name='password'>
                        <input type='hidden' id='exponent' value='<c:out value="${exponent}" />'>
                        <input type='hidden' id='modulus' value='<c:out value="${modulus}" />'>
                    </div>
	                <%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
	                <button type="submit" class="btn btn-primary btn-lg btn-block">登陆</button>
	                <h6 style="margin-bottom: 0; color: #d82a1a !important;">暂不开放注册</h6>
		        </form>
			</div>
		</div>
	</div>
	
	<c:import url="nav/footer.jsp" />

	<script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
    <script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
    <script src="<c:url value="/view/js/login.js?v=${version}" />"></script>
</body>
</html>