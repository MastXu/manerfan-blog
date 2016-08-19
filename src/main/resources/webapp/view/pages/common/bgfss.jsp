<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<style>
	#bg-container {position:fixed;width:100%;height:100%;z-index:0;background-color:#00C1FF;}
	#bg-output {opacity: 0.3;width: 100%;height: 100%;-webkit-transition: all 400ms ease-in-out;-ms-transition: all 400ms ease-in-out;-o-transition: all 400ms ease-in-out;-moz-transition: all 400ms ease-in-out;transition: all 400ms ease-in-out;}
</style>

<div id="bg-container" class="animated fadeIn">
	<div id="bg-output"></div>
</div>