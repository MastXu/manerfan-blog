<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<footer id="footer" class="text-center" style="position: <c:out value='${position}' default='relative' />;">
	<div class="social">
		<a href="https://github.com/pseudo-science/manerfan-blog" target="_blank">
		  <i class="icon-github-circled"></i>
		</a>
	</div>
	<a target="_blank" href="http://www.manerfan.com">ManerFan</a>, All Rights Reserved.
	<br> 
	Licensed under an
	<a target="_blank" href="http://www.apache.org/licenses/LICENSE-2.0">Apache	License 2.0</a>
</footer>