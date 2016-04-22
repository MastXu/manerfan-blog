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
        .main-content #panel-settings>div:nth-child(n+2) {display: none;}
        .panel-heading .btn, .list-group .btn {float: right; margin-right: 5px;}
        .list-nav .list-group-item {visibility:hidden;}
        .list-group-item.list-category-item {width:220px;float:left;margin:5px;}
        .image-breadcrumb {float:right;margin:0;padding:0;}
        .image-manager-list .list-group-item {width:180px; height:180px;}
        .image-manager-list .list-group-item * {text-align: center;}
        .image-manager-list .list-group-item.dir-item a {font-size: 8em;text-decoration:none;}
        .image-manager-list .list-group-item.dir-item span {width: 100%;display: block;margin-top: 10px;}
        .image-manager-list .list-group-item img {max-width:100%;max-height:100%;}
        ._loading{display:none;position:absolute;top:0;width:100%;height:100%;z-index:999;background-color:#88a825;opacity:0.6;}
    </style>
</head>
<body>
    <c:import url="../nav/header.jsp" />
    
    <div class="main-content">
    	<div class="col-xs-12 col-sm-4 col-md-3 col-lg-3"> 
			<div class="panel panel-default">
				<div class="panel-heading">设置</div>
				<ul class="list-group list-nav">
				    <li class="list-group-item active" data-action="account-settings"><a href="#panel-settings">用户设置</a></li>
				    <li class="list-group-item" data-action="article-settings"><a href="#panel-settings">文章管理</a></li>
				    <li class="list-group-item" data-action="drafts-box"><a href="#panel-settings">草稿箱</a></li>
				    <li class="list-group-item" data-action="recycle-bin"><a href="#panel-settings">回收站</a></li>
				    <li class="list-group-item" data-action="category-settings"><a href="#panel-settings">分类管理</a></li>
				    <li class="list-group-item" data-action="image-manager"><a href="#panel-settings">图片管理</a></li>
				</ul>
			</div>
		</div>
		<div id="panel-settings" class="col-xs-12 col-sm-8 col-md-9 col-lg-9">
	        <div data-action="account-settings" class="panel panel-default">
	            <div class="panel-heading"><c:out value='${user.name}' /></div>
	            <div class="panel-body">
					<div class="panel-group" id="account-accordion" role="tablist" aria-multiselectable="true">
						<div class="panel panel-default">
							<div class="panel-heading" role="tab" id="hadding-account-info">
								<h4 class="panel-title">
									<a role="button" data-toggle="collapse"
										data-parent="#account-accordion" href="#collapse-account-info"
										aria-expanded="true" aria-controls="collapse-account-info">
										账户信息
									</a>
								</h4>
							</div>
							<div id="collapse-account-info" class="panel-collapse collapse in"
								role="tabpanel" aria-labelledby="hadding-account-info">
								<div class="panel-body">
									<div class="input-group">
										<span class="input-group-addon"><i class='icon-mail'></i>邮箱</span>
										<input type="email" maxlength="128" class="form-control" data-email="<c:out value='${user.email}' />" value="<c:out value='${user.email}' />" id="account-email" placeholder="your email here">
									</div>
									<button id="btn-account-info" type="button" class="btn btn-primary btn-block" data-loading-text="拼命加载中<i class='icon-spinner'></i>">确认</button>
								</div>
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading" role="tab" id="heading-account-password">
								<h4 class="panel-title">
									<a class="collapsed" role="button" data-toggle="collapse"
										data-parent="#account-accordion" href="#collapse-account-password"
										aria-expanded="false" aria-controls="collapse-account-password">
										重置密码
									</a>
								</h4>
							</div>
							<div id="collapse-account-password" class="panel-collapse collapse"
								role="tabpanel" aria-labelledby="heading-account-password">
								<div class="panel-body">
									<div class="input-group">
										<span class="input-group-addon">原密码</span>
										<input type="password" maxlength="16" class="form-control" id="account-org-password" placeholder="原密码">
									</div>
									<div class="input-group">
										<span class="input-group-addon">新密码</span>
										<input type="password" maxlength="16" class="form-control" id="account-new-password" placeholder="新密码">
									</div>
									<div class="input-group">
										<span class="input-group-addon">再确认</span>
										<input type="password" maxlength="16" class="form-control" id="account-cfm-password" placeholder="再次输入密码">
									</div>
									<button id="btn-account-password" type="button" class="btn btn-primary btn-block" data-loading-text="仔细验证中<i class='icon-spinner'></i>">确认</button>
								</div>
							</div>
						</div>
					</div>
				</div>
	        </div>
	        <div data-action="article-settings" class="panel panel-default">
	            <div class="panel-heading">文章管理</div>
	            <div class="panel-body">
	                <div class="list-group article-settings-list"></div>
	                <div class="pagination article-settings-pagination">
					    <a href="#" class="first" data-action="first">&laquo;</a>
					    <a href="#" class="previous" data-action="previous">&lsaquo;</a>
					    <input type="text" />
					    <a href="#" class="next" data-action="next">&rsaquo;</a>
					    <a href="#" class="last" data-action="last">&raquo;</a>
					</div>
	            </div>
	        </div>
	        <div data-action="drafts-box" class="panel panel-default">
	            <div class="panel-heading">草稿箱</div>
	            <div class="panel-body">
	                <div class="list-group drafts-box-list"></div>
	                <div class="pagination drafts-box-pagination">
					    <a href="#" class="first" data-action="first">&laquo;</a>
					    <a href="#" class="previous" data-action="previous">&lsaquo;</a>
					    <input type="text" />
					    <a href="#" class="next" data-action="next">&rsaquo;</a>
					    <a href="#" class="last" data-action="last">&raquo;</a>
					</div>
	            </div>
	        </div>
	        <div data-action="recycle-bin" class="panel panel-default">
	            <div class="panel-heading">回收站</div>
	            <div class="panel-body">
	                <div class="list-group recycle-bin-list"></div>
	                <div class="pagination recycle-bin-pagination">
					    <a href="#" class="first" data-action="first">&laquo;</a>
					    <a href="#" class="previous" data-action="previous">&lsaquo;</a>
					    <input type="text" />
					    <a href="#" class="next" data-action="next">&rsaquo;</a>
					    <a href="#" class="last" data-action="last">&raquo;</a>
					</div>
	            </div>
	        </div>
	        <div data-action="category-settings" class="panel panel-default">
	            <div class="panel-heading">分类管理</div>
	            <div class="panel-body">
	                <div class="list-group category-list"></div>
	            </div>
	        </div>
	        <div data-action="image-manager" class="panel panel-default">
	            <div class="panel-heading">
	            	<span>图片管理</span>
	            	<ol class="breadcrumb image-breadcrumb"></ol>
	            </div>
	            <div class="panel-body">
	                <div class="list-group image-manager-list"></div>
	            </div>
	        </div>
        </div>
	</div>
	
	<div class="hidden-print _loading">
	    <c:import url="../common/loading.jsp" />
    </div>
	
	<c:import url="../nav/footer.jsp" />
	
	<script src="<c:url value="/view/plugins/requirejs/require.js?v=${version}" />"></script>
    <script id="mainscript" data-version="<c:out value='${version}' />" src="<c:url value="/view/js/main.js?v=${version}" />"></script>
    <script src="<c:url value="/view/js/settings/settings.js?v=${version}" />"></script>
</body>
</html>