<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%-- http://www.blacktie.co/demo/kelvin --%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <meta name="author" content="ManerFan">
    <title>Maner·Fan</title>
    
	<link rel="stylesheet" href="<c:url value='/view/style/themes/default.css?v=${version}'/>" type="text/css">
	<link rel="stylesheet" href="<c:url value='/view/css/about/about.css?v=${version}'/>" type="text/css">
</head>
<body class="hidden-print">
    <c:set var="position" value="relative" scope="request"></c:set>
    <c:import url="../nav/header.jsp" />
    
    <div class="headerwrap">
		<div class="container">
			<div class="row text-center">
				<div class="col-lg-12">
					<h1>Maner · Fan</h1>
					<h3>Java Web 开发 | manerfan@foxmail.com</h3>
				</div>
				<div class="col-lg-12" style="margin-top: 60px;">
					<a href="<c:url value='/article' />" type="button" class="btn btn-info">进入博客</a>
				</div>
			</div>
		</div>
		<a href="#about" type="button" class="btn-scroll next btn btn-info"></a>
	</div>
	
	<div id="about" class="intro">
		<div class="container">
			<div class="row">
				<div class="col-lg-2 col-lg-offset-1">
					<h5>ABOUT</h5>
				</div>
				<div class="col-lg-6">
					<p>I'm web designer &amp; front-end developer with 7 years of professional experience. I'm interested in all kinds of visual communication, but my major focus is on designing web, mobile &amp; tablet interfaces. I also have skills in other fields like branding, icon design or web development.</p>
				</div>
				<div class="col-lg-3">
					<p><a href="http://www.blacktie.co/demo/kelvin/#"><i class="icon-file"></i></a> <sm>DOWNLOAD PDF</sm></p>
				</div>
			</div>
		</div>
		<a href="#education" type="button" class="btn-scroll next btn btn-info"></a>
	</div>
	
	<div id="education" class="container desc">
		<div class="row">
			<div class="col-lg-2 col-lg-offset-1">
				<h5>EDUCATION</h5>
			</div>
			<div class="col-lg-6">
				<p>
					<t>西安电子科技大学</t> 硕士<br>
					电路与系统 <br>
					<i>2.5 Years Course</i>
				</p>
			</div>
			<div class="col-lg-3">
				<p><sm>SEPT 2010 - APRIL 2013</sm></p>
			</div>

			<div class="col-lg-6 col-lg-offset-3">
				<p>
					<t>西安电子科技大学</t> 学士<br>
					电子信息工程 <br>
					<i>4 Years Course</i>
				</p>
			</div>
			<div class="col-lg-3">
				<p><sm>SEPT 2006 - SEPT 2010</sm></p>
			</div>
		</div>
		<br>
		<hr>
	</div>
	
	<div id="work" class="container desc">
		<div class="row">
			<div class="col-lg-2 col-lg-offset-1">
				<h5>WORK</h5>
			</div>
			<div class="col-lg-6">
				<p>
					<t>研发工程师</t><br>
					Sumavision Corp. <br>
				</p>
				<p><more>稍后补充</more></p>
			</div>
			<div class="col-lg-3">
				<p><sm>APRIL 2013 - CURRENT</sm></p>
			</div>
		</div>
		<br>
		<br>
		<a href="#skills" type="button" class="btn-scroll next btn btn-info"></a>
	</div>
	
	<div id="skills" class="intro">
		<div class="container">
			<div class="row">
				<div class="col-lg-2 col-lg-offset-1">
					<h5>SKILLS</h5>
				</div>
				<div class="col-lg-3">
					<div id="java" class="skills"></div>
					<p>Java</p>
					<br>
				</div>
				<div class="col-lg-3">
					<div id="springframework" class="skills"></div>
					<p>SpringFramework</p>
					<br>
				</div>
				<div class="col-lg-3">
					<div id="hibernate" class="skills"></div>
					<p>Hibernate</p>
					<br>
				</div>
				
				<div class="col-lg-3 col-lg-offset-3">
					<div id="mysql" class="skills"></div>
					<p>Mysql/H2DB</p>
					<br>
				</div>
				
				<div class="col-lg-3">
					<div id="html" class="skills"></div>
					<p>Html/CSS</p>
					<br>
				</div>
				
				<div class="col-lg-3">
					<div id="javascript" class="skills"></div>
					<p>JavaScript</p>
					<br>
				</div>
				
				<div class="col-lg-9 col-lg-offset-3">
					<p><more>
					待补充 ... ... ... ... ... ... ... ...
					</more></p>
				</div>
			</div>
		</div>
		<a href="#portfolio" type="button" class="btn-scroll next btn btn-info"></a>
	</div>
	
	<div id="portfolio" class="container desc">
		<div class="row">
				<div class="col-lg-2 col-lg-offset-1">
					<h5>PORTFOLIO</h5>
				</div>
				<div class="col-lg-6">
					<p><img class="img-responsive" src="<c:url value='/view/css/about/img/protfolio01.jpg' />" alt=""></p>
				</div>
				<div class="col-lg-3">
					<p>M(anerFan)Blog</p>
				<p>
					<more>
						MBLOG是一个主要面向个人网站搭建的博客系统，使用纯JAVA开发。<br>
						待补充 ... ... ...
						<br><br>
						<sm><i class="icon-tag"></i> develop</sm>
					</more>
				</p>
				</div>
		</div>
		<br>
		<br>
	</div>
    
	<c:import url="../nav/footer.jsp" />
	
	<script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
    <script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
    <script src="<c:url value="/view/js/about/author.js?v=${version}" />"></script>
</body>
</html>