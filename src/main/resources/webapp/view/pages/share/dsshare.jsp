<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="ds-share flat" data-thread-key="<c:out value="${uid}" />"
	data-title="<c:out value="${title}" />"
	data-images="https://avatars2.githubusercontent.com/u/16587866"
	data-content="<c:out value="${summary}" escapeXml="false"/>"
	data-url="http://www.manerfan.com/article/<c:out value="${uid}" />">
	<div class="ds-share-inline">
		<ul class="ds-share-icons-16">
			<li data-toggle="ds-share-icons-more"><a class="ds-more" href="javascript:void(0);">分享到：</a></li>
			<li><a class="ds-weibo" href="javascript:void(0);" data-service="weibo">微博</a></li>
			<li><a class="ds-qzone" href="javascript:void(0);" data-service="qzone">QQ空间</a></li>
			<li><a class="ds-qqt" href="javascript:void(0);" data-service="qqt">腾讯微博</a></li>
			<li><a class="ds-wechat" href="javascript:void(0);" data-service="wechat">微信</a></li>
		</ul>
		<div class="ds-share-icons-more"></div>
	</div>
</div>