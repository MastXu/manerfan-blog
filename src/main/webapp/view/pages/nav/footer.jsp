<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->
<%@ taglib prefix="sec" uri="security_tld" %> <!-- http://www.springframework.org/security/tags" -->

<footer id="footer" class="hidden-print text-center" style="position: <c:out value='${position}' default='relative' />;">
	<div class="social">
		<a href="https://github.com/ManerFan/manerfan-blog" target="_blank">
		  <i class="icon-github-circled"></i>
		</a>
	</div>
	Powered By <a target="_blank" href="http://www.manerfan.com">ManerFan</a>, All Rights Reserved.
	<br> 
	Licensed under an <a target="_blank" href="http://www.apache.org/licenses/LICENSE-2.0">Apache License 2.0</a>
</footer>