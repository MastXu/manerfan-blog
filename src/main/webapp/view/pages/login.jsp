<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <title>登陆</title>
    
    <style>
        h1 {
            margin: 1em 0 !important;
        }
    
        div, span, h1, h2, h3, h4, h5, h6 {
            text-align: center !important;
            color: #8C8C8C !important;
        }
        
        .panel {
            display: none;
            position: absolute !important;
            width: 450px !important;
            top: 50% !important;
            left: 50% !important;
            margin-top: -160px;
            margin-left: -230px;
        }
        
        .glyphicon-cloud {
            top: 10px !important;
        }
        
        form div {
            position: relative !important;
            height: 42px !important;
            margin: 5px 0 !important;
        }
        
        form .form-control {
            position: absolute !important;
            top: 0 !important;
            padding-left: 50px !important;
        }
        
        form .glyphicon {
            position: absolute !important;
            z-index: 99 !important;
            left: 10px !important;
            font-size: 20px !important;
            top: 10px !important;
        }
        
        button[type=submit] {
            margin-top: 10px;
        }
    </style>
</head>
<body>

    <div class="panel panel-default">
        <div class="panel-body">
            <h1><span class="glyphicon glyphicon-cloud"></span> 登陆</h1>
			<c:if test="${not empty error}">
				<pre class="bg-danger text-danger">${error}</pre>
			</c:if>
			<c:if test="${not empty msg}">
				<pre class="bg-info text-info">${msg}</pre>
			</c:if>
			<form name="loginForm" action="<c:url value='/login-check' />" method='POST'>
	            <div>
		            <span class="glyphicon glyphicon-user"></span>
		            <input class="form-control" type='text' name='username'>
	            </div>
	            <div>
                    <span class="glyphicon glyphicon-lock"></span>
                    <input class="form-control" type='password' name='password'>
                </div>
                <%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
                <button type="submit" class="btn btn-primary btn-lg btn-block">登陆</button>
	        </form>
		</div>
	</div>
	
	<script>
		window.debug = false;
	    if (/(\?|&)debug($|&)/.test(location.search)) {
	        window.debug = true;
	    }
	</script>
	
	<script src="<c:url value="/view/js/require.js" />"></script>
    <script src="<c:url value="/view/js/main.js" />"></script>
    <script src="<c:url value="/view/js/login.js" />"></script>
</body>
</html>