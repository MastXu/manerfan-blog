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
        .list-group-item.list-category-item {width:250px;float:left;margin:5px;}
        .breadcrumb {float:right;margin:0;padding:0;}
        .manager-list .list-group-item {width:180px; height:180px; float:left; margin:5px;}
        .manager-list .list-group-item * {text-align: center;}
        .manager-list .list-group-item.dir-item a:not(.btn) {font-size: 5em;text-decoration:none;}
        .manager-list .list-group-item.dir-item span {width: 100%;display: block;margin-top: 10px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;}
        .manager-list .list-group-item.img-item img {max-width:150px;max-height:120px;display:block;margin:auto;cursor: pointer;}
        .manager-list .list-group-item.img-item pre {width: 100%;display: block;margin-bottom: 3px;padding: 0;}
        .manager-list-lg .list-group-item {height:210px;}
        .manager-list-lg .btn {margin:0;}
        .icon-archive:before {margin-right: 0.4em;}
        .img-dialog img {display:block;margin:auto;max-width:100%;}
        ._loading{display:none;position:fixed;top:0;width:100%;height:100%;z-index:999;background-color:#88a825;opacity:0.6;}
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
				    <li class="list-group-item" data-action="system-backup"><a href="#panel-settings">系统备份</a></li>
				    <li class="list-group-item" data-action="sysconfig"><a href="#panel-settings">系统设置</a></li>
				</ul>
			</div>
		</div>
		<div id="panel-settings" class="col-xs-12 col-sm-8 col-md-9 col-lg-9">
	        <div data-action="account-settings" class="panel panel-default">
	            <div class="panel-heading"><c:out value='${user.name}' /></div>
	            <div class="panel-body">
					<!-- 
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
					-->
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
	                <div class="list-group manager-list image-manager-list"></div>
	            </div>
	        </div>
	        <div data-action="system-backup" class="panel panel-default">
	            <div class="panel-heading">
	            	<span>系统备份</span>
	            </div>
	            <div class="panel-body">
	            	<div class="panel-group" id="systembackup-accordion" role="tablist" aria-multiselectable="true">
						<div class="panel panel-default">
							<div class="panel-heading" role="tab" id="hadding-sysbackup-download">
								<h4 class="panel-title">
									<a role="button" data-toggle="collapse"
										data-parent="#systembackup-accordion" href="#sysbackup-download"
										aria-expanded="true" aria-controls="sysbackup-download">
										备份下载
									</a>
								</h4>
							</div>
							<div id="sysbackup-download" class="panel-collapse collapse in"
								role="tabpanel" aria-labelledby="hadding-sysbackup-download">
								<div class="panel-body">
									<ol class="breadcrumb backup-breadcrumb" style="width:100%;margin-bottom:5px;"></ol>
									<div class="list-group manager-list manager-list-lg backup-manager-list"></div>
								</div>
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading" role="tab" id="hadding-sysbackup-setting">
								<h4 class="panel-title">
									<a class="collapsed" role="button" data-toggle="collapse"
										data-parent="#systembackup-accordion" href="#sysbackup-setting"
										aria-expanded="false" aria-controls="sysbackup-setting">
										备份设置
									</a>
								</h4>
							</div>
							<div id="sysbackup-setting" class="panel-collapse collapse"
								role="tabpanel" aria-labelledby="hadding-sysbackup-setting">
								<div class="panel-body">
									<div class="input-group pull-left col-xs-12 col-sm-12 col-md-5 col-lg-5">
										<span class="input-group-addon">每周</span>
										<select id="backup-week" class="form-control" style="cursor: pointer;">
											<option value="SUN" selected>周日</option>
											<option value="MON">周一</option>
											<option value="TUE">周二</option>
											<option value="WED">周三</option>
											<option value="THU">周四</option>
											<option value="FRI">周五</option>
											<option value="SAT">周六</option>
										</select>
									</div>
									<div class="input-group pull-right col-xs-12 col-sm-12 col-md-5 col-lg-5">
										<span class="input-group-addon">时间点</span>
										<select id="backup-hour" class="form-control" style="cursor: pointer;">
											<option value="0" >00:00</option>
											<option value="1" >01:00</option>
											<option value="2" >02:00</option>
											<option value="3" selected>03:00</option>
											<option value="4" >04:00</option>
											<option value="5" >05:00</option>
											<option value="6" >06:00</option>
											<option value="7" >07:00</option>
										</select>
									</div>
									
									<div class="input-group pull-left col-xs-12 col-sm-12 col-md-5 col-lg-5">
										<span class="input-group-addon">保留个数</span>
										<input type="number" id="backup-keep" maxlength="2" class="form-control" min="1" max="60" placeholder="12">
									</div>
									<button id="backup-immediately" type="button" class="btn btn-warning pull-right col-xs-12 col-sm-12 col-md-5 col-lg-5" data-loading-text="正在备份<i class='icon-spinner'></i>">立即备份</button>
									<button id="btn-backup-config" type="button" class="btn btn-primary btn-block" data-loading-text="保存中<i class='icon-spinner'></i>">确认</button>
								</div>
							</div>
						</div>
					</div>
	            </div>
	        </div>
	        <div data-action="sysconfig" class="panel panel-default">
	            <div class="panel-heading">
	            	<span>系统设置</span>
	            </div>
	            <div class="panel-body">
	            	<div class="panel panel-default email-config">
			            <div class="panel-heading">
			            	<span>邮箱设置</span>
			            	<a href="#" target="_blank" style="display:none;" id="email-login"></a>
			            	<span class="pull-right">
				            	<label for="email-sslenable">开启SSL</label>
				            	<input type="checkbox" id="email-sslenable" style="cursor: pointer;">
			            	</span>
			            	<button id="btn-email-clear" type="button" class="btn btn-link pull-right" style="padding: 0;">清除</button>
			            </div>
			            <div class="panel-body">
			            	<div class="input-group">
								<span class="input-group-addon">smtp地址</span>
								<input type="text" maxlength="64" class="form-control" id="email-host" placeholder="smtp.163.com">
							</div>
							<div class="input-group">
								<span class="input-group-addon">smtp端口</span>
								<input type="number" maxlength="5" class="form-control" id="email-port" min="1" max="65535" placeholder="25">
							</div>
							<div class="input-group">
								<span class="input-group-addon">登陆名&nbsp;&nbsp;</span>
								<input type="email" maxlength="32" class="form-control" id="email-name" placeholder="example@163.com">
							</div>
							<div class="input-group">
								<span class="input-group-addon">登陆密码</span>
								<input type="password" maxlength="32" class="form-control" id="email-password" placeholder="email password here">
							</div>
			            	<button id="btn-email-test" type="button" class="btn btn-warning btn-block" data-loading-text="正在验证<i class='icon-spinner'></i>">验证</button>
			            	<button id="btn-email-config" type="button" class="btn btn-primary btn-block" data-loading-text="保存中<i class='icon-spinner'></i>">确认</button>
			            </div>
			        </div>
			        <div class="panel panel-default duoshuo-config">
			            <div class="panel-heading">
			            	<span>多说设置</span>
			            	<a href="http://duoshuo.com/" target="_blank" class="glyphicon glyphicon-question-sign"></a>
			            	<button id="btn-duoshuo-clear" type="button" class="btn btn-link pull-right" style="padding: 0;">清除</button>
			            </div>
			            <div class="panel-body">
			            	<div class="input-group">
								<span class="input-group-addon">key</span>
								<input type="text" maxlength="16" class="form-control" id="duoshuo-key" placeholder="data-thread-key">
							</div>
							<div class="input-group">
								<span class="input-group-addon">url</span>
								<input type="text" maxlength="128" class="form-control" id="duoshuo-url" placeholder="data-url">
							</div>
							<button id="btn-duoshuo-config" type="button" class="btn btn-primary btn-block" data-loading-text="保存中<i class='icon-spinner'></i>">确认</button>
			            </div>
			        </div>
	            </div>
	        </div>
        </div>
	</div>

	<div class="modal fade img-dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title"></h4>
				</div>
				<div class="modal-body"><img></div>
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