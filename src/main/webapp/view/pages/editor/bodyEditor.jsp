<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->
<%@ taglib prefix="sec" uri="security_tld" %> <!-- http://www.springframework.org/security/tags" -->

<style>
	.navbar-title {width:100%;height:50px;position:relative;}
	.navbar-title ul {width:100%;height:100%;padding:4px 30px;position:relative;}
	.navbar-title ul li {float:left;}
	.navbar-title .title-inner {position:absolute;width:100%;padding-right:160px;}
</style>

<div class="navbar-title navbar-inner">
	<ul class="nav pull-right title-container">
		<sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
			<li style="float:right;"><button type="button" class="btn btn-danger" style="margin-top:1px;">发表博客</button></li>
		</sec:authorize>
		<li>
			<a class="btn btn-success file-title-navbar" href="#" title="Rename document"></a>
		</li>
		<li class="title-inner">
			<div class="input-file-title-container">
				<input type="text" class="col-sm-4 form-control hide input-file-title" placeholder="Document title" />
			</div>
		</li>
	</ul>
</div>
<div class="layout-wrapper-l1">
	<div class="layout-wrapper-l2">
		<div class="navbar navbar-default">
			<div class="navbar-inner">
				<div class="nav left-space"></div>
				<div class="nav right-space pull-right"></div>
				<div class="buttons-dropdown dropdown">
		    		<div class="nav">
		        		<button class="btn btn-success" data-toggle="dropdown"
		            		title="Show buttons">
		            		<i class="csdn-icon-th-large"></i>
		            	</button>
		    		    <div class="dropdown-menu">
		        		</div>
		        	</div>
				</div>
				<ul class="nav left-buttons">
					<li class="wmd-button-group1 btn-group"></li>
				</ul>
				<ul class="nav left-buttons">
					<li class="wmd-button-group2 btn-group"></li>
				</ul>
				<ul class="nav left-buttons">
					<li class="wmd-button-group3 btn-group"></li>
				</ul>
				<ul class="nav left-buttons">
					<li class="wmd-button-group4 btn-group"></li>
				</ul>
				<ul class="nav left-buttons">
					<li class="wmd-button-group5 btn-group">
						<a class="btn btn-success btn-blog-save" title="保存到草稿箱  Ctrl/Cmd+S">
							<i class="csdn-icon-disk"></i>
						</a>
						<a class="btn btn-success btn-blog-setting" title="文章设置">
							<i class="csdn-icon-doc-setting"></i>
						</a>
					</li>
				</ul>
				<ul class="nav left-buttons">
					<li class="wmd-button-group6 btn-group">
						<a class="btn btn-success btn-import-online" data-toggle="modal" data-target=".modal-import-url" title="线上导入">
							<i class="csdn-icon-link"></i>
						</a>
						<a class="btn btn-success btn-import" data-toggle="modal" data-target=".modal-import-harddrive-markdown" title="本地导入">
							<i class="csdn-icon-upload"></i>
						</a>
						<a class="btn btn-success btn-export" data-toggle="modal" data-target=".modal-export-harddrive" title="导出本地">
							<i class="csdn-icon-hdd"></i>
						</a>
					</li>
				</ul>
				<ul class="nav left-buttons">
					<li class="wmd-button-group7 btn-group">
						<a class="btn btn-success" id="wmd-help-button" title="语法帮助">
							<i class="csdn-icon-help"></i>
						</a>
					</li>
				</ul>
				<ul class="nav pull-right right-buttons">
					<li class="offline-status hide">
					    <div class="text-danger">
					        <i class="icon-attention-circled"></i>offline
					    </div>
					</li>
					<li class="extension-buttons"></li>
				</ul>
			</div>
		</div>
		<div class="layout-wrapper-l3">
			<!-- wmd-input 此DOM不要有折行，否则[ \t]等符号均会算到文章内 -->
			<pre id="wmd-input" class="form-control"><div class="editor-content" contenteditable=true></div><div class="editor-margin"></div></pre>
			<div class="preview-panel">
				<div class="layout-resizer layout-resizer-preview"></div>
				<div class="layout-toggler layout-toggler-navbar btn btn-info" title="Toggle navigation bar"><i class="icon-th"></i></div>
				<div class="layout-toggler layout-toggler-preview btn btn-info" title="Toggle preview"><i class="icon-none"></i></div>
				<div class="preview-container">
					<div id="preview-contents">
						<div id="wmd-preview" class="preview-content"></div>
					</div>
				</div>
			</div>
		</div>
		<div class="extension-preview-buttons">
			<div class="btn-group drag-me" title="Drag me!">
				<i class="icon-ellipsis-vert"></i>
			</div>
		</div>
	</div>
	<div id="wmd-button-bar" class="hide"></div>

	<div class="menu-panel">
		<button class="btn toggle-button" title="Menu">
			<img
				data-stackedit-src="menu-icon.png" width="24" height="24" />
		</button>
		<div class="panel-content">
			<div class="list-group">
				<a href="#" data-toggle="collapse" data-target=".collapse-publish-on"
					class="list-group-item">
					<div><i class="icon-upload"></i>Publish</div>
					<small>Export to the web</small>
				</a>
				<div class="sub-menu collapse collapse-publish-on clearfix">
					<ul class="nav publish-on-provider-list"></ul>
				</div>
			</div>
			<ul class="nav">
				<li><a href="#" data-toggle="modal"
					data-target=".modal-settings" class="action-load-settings"><i
						class="icon-cog"></i> Settings</a></li>
			</ul>
		</div>
	</div>

	<div class="document-panel">
		<button class="btn toggle-button" title="Select document Ctrl+[ Ctrl+]">
			<i class="icon-folder-open"></i>
		</button>
		<div class="search-bar clearfix">
			<ul class="nav">
				<li><a href="#" class="action-create-file"><i
						class="icon-file"></i> New document</a></li>
				<li><a href="#" class="action-remove-file-confirm"><i
						class="icon-trash"></i> Delete document</a></li>
				<li><a href="#" data-toggle="modal" data-target=".modal-document-manager"><i
						class="icon-layers"></i> Manage documents</a></li>
	        </ul>
			<div class="input-group">
				<span class="input-group-addon"><i class="icon-search"></i></span><input
					type="text" class="form-control" placeholder="Find document" />
			</div>
		</div>
		<div class="panel-content">
			<div class="list-group document-list"></div>
			<div class="list-group document-list-filtered hide"></div>
		</div>
	</div>
</div>

<div class="modal fade modal-document-manager">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Manage documents</h2>
			</div>
			<div class="modal-body">
				<div></div>
				<ul class="nav nav-pills document-list">
					<li class="pull-right dropdown"><a href="#"
						data-toggle="dropdown"><i class="icon-check"></i> Selection <b
							class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="#" class="action-select-all"><i
									class="icon-check"></i> Select all</a></li>
							<li><a href="#" class="action-unselect-all"><i
									class="icon-check-empty"></i> Unselect all</a></li>
							<li class="divider"></li>
							<li><a href="#" class="action-move-items"><i
									class="icon-forward"></i> Move to folder</a></li>
							<li><a href="#" class="action-delete-items"><i
									class="icon-trash"></i> Delete</a></li>
						</ul></li>
					<li class="pull-right"><a href="#"
						class="action-create-folder"> <i class="icon-folder"></i>
							Create folder
					</a></li>
					<li class="disabled"><a><i class="icon-file"></i> <span
							class="document-count"></span></a></li>
					<li class="disabled"><a><i class="icon-folder"></i> <span
							class="folder-count"></span></a></li>
				</ul>
				<div class="list-group document-list"></div>
				<p class="confirm-delete hide">The following documents will be
					deleted locally:</p>
				<p class="choose-folder hide">Please choose a destination
					folder:</p>
				<div class="list-group selected-document-list hide"></div>
				<div class="list-group select-folder-list hide"></div>
			</div>
			<div class="modal-footer">
				<a href="#"
					class="btn btn-default confirm-delete choose-folder action-cancel hide">Cancel</a>
				<a href="#"
					class="btn btn-primary confirm-delete action-delete-items-confirm hide">OK</a>
				<a href="#" class="btn btn-primary document-list"
					data-dismiss="modal">Close</a>
			</div>
		</div>
	</div>
</div>


<div class="modal fade modal-insert-link">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Hyperlink</h2>
			</div>
			<div class="modal-body">
				<p>Please provide the link URL and an optional title:</p>
				<div class="input-group">
					<span class="input-group-addon"><i class="icon-globe"></i></span><input
						id="input-insert-link" type="text" class="col-sm-5 form-control"
						placeholder='http://example.com/ "optional title"' />
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
				<a href="#" class="btn btn-primary action-insert-link"
					data-dismiss="modal">OK</a>
			</div>
		</div>
	</div>
</div>


<div class="modal fade modal-insert-image">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Image</h2>
			</div>
			<div class="modal-body">
				<sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
					<div class="file-upload">
						<div class="before-add">
							<div class="content">
								<h1>将博客插图拖拽到此区域</h1>
								<h4>若您的浏览器不支持文件拖拽，请点击此区域选择插图</h4>
							</div>
							<input id="imageupload" type="file" name="image" accept="image/*" > <!-- multiple -->
						</div>
						<div class="after-add" style="display: none;">
							<img id="preview"></img>
							<div class="content">
								<div class="file-progress">
									<h3 id="progress"></h3>
									<div id="progress-bar" class="progress-bar progress-bar-info progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="opacity: 0.5;"></div>
								</div>
								<button id="upload-image" type="button" class="btn btn-primary">上传</button>
							</div>
						</div>
					</div>
				</sec:authorize>
				<div>
					<sec:authorize access="hasAnyRole('ROLE_ADMIN,ROLE_USER')">
						<p>您也可以直接提供插图URL，并提供图片描述:</p>
					</sec:authorize>
					<sec:authorize access="hasRole('ROLE_ANONYMOUS')">
						<p>输入插图URL，并提供图片描述:</p>
					</sec:authorize>
					<div class="input-group">
						<span class="input-group-addon"><i class="icon-picture"></i></span><input
							id="input-insert-image" type="text" class="col-sm-5 form-control"
							placeholder='http://example.com/image.jpg "插图描述"' />
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default"	data-dismiss="modal">Cancel</a>
				<a href="#"	class="btn btn-primary action-insert-image" data-dismiss="modal">OK</a>
			</div>
		</div>
	</div>
</div>


<div class="modal fade modal-remove-file-confirm">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Delete</h2>
			</div>
			<div class="modal-body">
				<p>
					Are you sure you want to delete "<span class="file-title"></span>"?
				</p>
				<blockquote>
					<p><b>Note:</b> It won't delete the file on synchronized locations.</p>
				</blockquote>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
				<a href="#" class="btn btn-primary action-remove-file"
					data-dismiss="modal">Delete</a>
			</div>
		</div>
	</div>
</div>


<div class="modal fade modal-import-url">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">网络导入</h2>
			</div>
			<div class="modal-body">
				<p>填写Markdown文档URL地址</p>
				<div class="form-horizontal">
					<div class="form-group">
						<input type="text" id="input-import-url"
							placeholder="http://www.abc.com/xyz.md" class="form-control">
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
				<a href="#" data-dismiss="modal"
					class="btn btn-primary action-import-url">OK</a>
			</div>
		</div>
	</div>
</div>

<!-- 导入 -->
<div class="modal fade modal-import-harddrive-markdown">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h2 class="modal-title">从本机导入</h2>
			</div>
			<div class="modal-body">
				<p>请选择Markdown/HTML文件</p>
				<div class="file-upload">
					<div class="before-add" style="display: block !important">
						<div class="content" id="dropzone-import-harddrive-markdown">
							<h1>将Markdown/HTML文件拖拽到此区域</h1>
							<h4>若您的浏览器不支持文件拖拽，请点击此区域选择</h4>
						</div>
						<input id="input-file-import-harddrive-markdown" type="file" multiple >
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-primary" data-dismiss="modal">Close</a>
			</div>
		</div>
	</div>
</div>

<!-- 导出 -->
<div class="modal fade modal-export-harddrive">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h2 class="modal-title">导出到本地</h2>
			</div>
			<div class="modal-body">
				<ul class="nav">
					<li>
						<a class="action-download-md" > 
							<i class="csdn-icon-file-markdown"></i>
							<p>Markdown</p>
						</a>
					</li>
					<li>
						<a class="action-download-html">
							<i class="csdn-icon-file-html"></i>
							<p>仅内容HTML</p>
						</a>
					</li>
					<li>
						<a class="action-download-template">
							<i class="csdn-icon-file-html-t"></i>
							<p>带模板HTML</p>
						</a>
					</li>
					<!--<li><a class="action-download-pdf"><i class="icon-file-pdf"></i> <p>PDF文档</p></a></li>-->
				</ul>
			</div>
		</div>
	</div>
</div>

<div class="modal fade modal-publish">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">
					Publish on <span class="publish-provider-name"></span>
				</h2>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group modal-publish-github">
						<label class="col-sm-4 control-label"
							for="input-publish-github-repo">Repository</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-github-repo"
								placeholder="Repository name or URL" class="form-control">
						</div>
					</div>
					<div class="form-group modal-publish-github">
						<label class="col-sm-4 control-label"
							for="input-publish-github-branch">Branch</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-github-branch"
								placeholder="branch-name" class="form-control">
						</div>
					</div>
					<div class="form-group modal-publish-ssh modal-publish-github">
						<label class="col-sm-4 control-label"
							for="input-publish-file-path">File path</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-file-path"
								placeholder="path/to/file.md" class="form-control">
							<span class="help-block"> File path is composed of both
								folder and filename. </span>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
				<a href="#" data-dismiss="modal"
					class="btn btn-primary action-process-publish">OK</a>
			</div>
		</div>
	</div>
</div>

<div class="modal fade modal-settings">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Settings</h2>
				<ul class="nav nav-tabs">
					<li class="active"><a class="action-load-settings"
						href="#tabpane-settings-basic" data-toggle="tab">Basic</a></li>
					<li><a class="action-load-settings"
						href="#tabpane-settings-advanced" data-toggle="tab">Advanced</a></li>
					<li><a class="action-load-settings"
						href="#tabpane-settings-extensions" data-toggle="tab">Extensions</a></li>
					<li><a class="action-load-settings"
						href="#tabpane-settings-utils" data-toggle="tab">Utils</a></li>
				</ul>
			</div>
			<div class="modal-body">

				<div class="tab-content clearfix">
					<div class="tab-pane active" id="tabpane-settings-basic">
						<div class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-4 control-label">Layout orientation</label>
								<div class="col-sm-7">
									<div class="radio">
										<label> <input type="radio"
											name="radio-layout-orientation" value="horizontal">
											Horizontal
										</label>
									</div>
									<div class="radio">
										<label> <input type="radio"
											name="radio-layout-orientation" value="vertical">
											Vertical
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-4"></div>
								<div class="col-sm-7">
									<div class="checkbox">
									    <label>
										    <input type="checkbox" id="input-settings-markdown-extra" />
										    <b>Markdown Extra/GitHub Flavored Markdown</b> syntax
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-4"></div>
								<div class="col-sm-7">
									<div class="checkbox">
									    <label>
										    <input type="checkbox" id="input-settings-mathjax" />
										    <b>LaTeX mathematical expressions</b> using <code>$</code> and <code>$$</code> delimiters
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label"
									for="input-settings-gdrive-multiaccount">Google Drive multi-accounts
								</label>
								<div class="col-sm-7">
									<select id="input-settings-gdrive-multiaccount" class="form-control">
    									<option value="1">1 account</option>
    									<option value="2">2 accounts</option>
    									<option value="3">3 accounts</option>
									</select>
									<span class="help-block"><b>Please sign in first with Google.</b> Once linked with your Google accounts, changing account is not possible unless you reset the application.</span>
								</div>
							</div>
						</div>
					</div>
					<div class="tab-pane" id="tabpane-settings-advanced">
						<div class="form-horizontal">
							<div class="form-group">
								<label class="col-sm-4 control-label">Edit mode</label>
								<div class="col-sm-7">
									<div class="radio">
										<label> <input type="radio"
											name="radio-settings-edit-mode" value="ltr">
											Left-To-Right
										</label>
									</div>
									<div class="radio">
										<label> <input type="radio"
											name="radio-settings-edit-mode" value="rtl">
											Right-To-Left
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label">Editor's font style</label>
								<div class="col-sm-7">
									<div class="radio">
										<label> <input type="radio"
											name="radio-settings-editor-font-class" value="font-rich">
											Rich
										</label>
									</div>
									<div class="radio">
										<label> <input type="radio"
											name="radio-settings-editor-font-class" value="font-rich-monospaced">
											Rich Monospaced
										</label>
									</div>
									<div class="radio">
										<label> <input type="radio"
											name="radio-settings-editor-font-class" value="font-monospaced">
											Monospaced
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label"
									for="input-settings-font-size">Font size ratio</label>
								<div class="col-sm-8 form-inline">
									<input type="text" id="input-settings-font-size"
										class="form-control col-sm-2">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label"
									for="input-settings-max-width">Max width ratio</label>
								<div class="col-sm-8 form-inline">
									<input type="text" id="input-settings-max-width"
										class="form-control col-sm-2">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label"
									for="input-settings-cursor-focus">Cursor focus ratio</label>
								<div class="col-sm-8 form-inline">
									<input type="text" id="input-settings-cursor-focus"
										class="form-control col-sm-2">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label"
									for="input-settings-lazy-rendering">Lazy rendering <a
									href="#" class="tooltip-lazy-rendering">(?)</a>
								</label>
								<div class="col-sm-7">
									<div class="checkbox">
										<input type="checkbox" id="input-settings-lazy-rendering" />
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label"
									for="textarea-settings-default-content">Default content
									<a href="#" class="tooltip-default-content">(?)</a>
								</label>
								<div class="col-sm-7">
									<textarea id="textarea-settings-default-content"
										class="form-control"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label"
									for="textarea-settings-publish-template">Default
									template <a href="#" class="tooltip-template">(?)</a>
								</label>
								<div class="col-sm-7">
									<textarea id="textarea-settings-publish-template"
										class="form-control"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label"
									for="textarea-settings-pdf-template">PDF
									template <a href="#" class="tooltip-template">(?)</a>
								</label>
								<div class="col-sm-7">
									<textarea id="textarea-settings-pdf-template"
										class="form-control"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label" for="textarea-settings-pdf-options">PDF options
                                    <a href="#" class="tooltip-pdf-options">(?)</a>
                                </label>
								<div class="col-sm-7">
                                    <textarea id="textarea-settings-pdf-options"
                                              class="form-control"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label"
									for="input-settings-markdown-mime-type">Markdown MIME type
								</label>
								<div class="col-sm-7">
									<select id="input-settings-markdown-mime-type" class="form-control">
										<option value="text/plain">text/plain</option>
										<option value="text/x-markdown">text/x-markdown</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label">Permissions</label>
								<div class="col-sm-7">
									<div class="checkbox">
									    <label>
										    <input type="checkbox" id="input-settings-gdrive-full-access" />
										    Allow StackEdit to open any document in Google Drive
										</label> <span class="help-block">Existing authorization has to be revoked in
										<a href="https://www.google.com/settings/dashboard" target="_blank">Google Dashboard</a>
										for this change to take effect.</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-4"></div>
								<div class="col-sm-7">
									<div class="checkbox">
									    <label>
										    <input type="checkbox" id="input-settings-dropbox-full-access" />
										    Allow StackEdit to open any document in Dropbox
										</label> <span class="help-block">If unchecked, access will be restricted to folder
										<b>/Applications/StackEdit</b> for existing files.</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-4"></div>
								<div class="col-sm-7">
									<div class="checkbox">
									    <label>
										    <input type="checkbox" id="input-settings-github-full-access" />
										    Allow StackEdit to access private repositories in GitHub
										</label> <span class="help-block">Existing authorization has to be revoked in
										<a href="https://github.com/settings/applications" target="_blank">GitHub settings</a>
										for this change to take effect.</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label"
									for="input-settings-publish-commit-msg">GitHub commit message</label>
								<div class="col-sm-7">
									<input type="text" id="input-settings-publish-commit-msg"
										class="form-control">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-4 control-label"
									for="input-settings-couchdb-url">CouchDB URL</label>
								<div class="col-sm-7">
									<input type="text" id="input-settings-couchdb-url"
										class="form-control">
                                    <span class="help-block pull-right"><a
                                            target="blank"
                                            href="https://github.com/benweet/stackedit/blob/master/doc/couchdb-setup.md">Setup
                                        your own CouchDB...</a></span>
                                </div>
							</div>
						</div>
					</div>
					<div class="tab-pane" id="tabpane-settings-extensions">
						<div class="panel-group accordion-extensions"></div>
						<span class="help-block pull-right"><a target="_blank"
							href="https://github.com/benweet/stackedit/blob/master/doc/developer-guide.md#developer-guide">Create
								your own extension...</a></span>
					</div>
					<div class="tab-pane" id="tabpane-settings-utils">
						<div class="tab-pane-button-container">
							<a href="#" class="btn btn-block btn-default action-welcome-file"
								data-dismiss="modal"><i class="icon-help-circled"></i>
								Hello! document</a> <a href="#"
								class="btn btn-block btn-default action-welcome-tour"
								data-dismiss="modal"><i
								class="icon-help-circled"></i> Welcome tour</a>
						</div>
						<div class="tab-pane-button-container">
							<a href="#"
								class="btn btn-block btn-default action-import-docs-settings"><i
								class="icon-cog-alt"></i> Import docs & settings</a> <a href="#"
								class="btn btn-block btn-default action-export-docs-settings"
								data-dismiss="modal"><i class="icon-cog-alt"></i>
								Export docs & settings</a> <input type="file"
								id="input-file-import-docs-settings" class="hide">
						</div>
						<div class="tab-pane-button-container">
							<a href="#"
								class="btn btn-block btn-default action-default-settings"
								data-dismiss="modal"><i class="icon-wrench"></i>
								Load default settings</a> <a href="#" class="btn btn-block btn-default"
								data-dismiss="modal" data-toggle="modal"
								data-target=".modal-app-reset"><i
								class="icon-fire"></i> Reset application</a> <a target="_blank" href="recovery.html" class="btn btn-block btn-default"><i
								class="icon-medkit"></i> StackEdit recovery</a>
						</div>
					</div>
				</div>

			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
				<a href="#" class="btn btn-primary action-apply-settings"
					data-dismiss="modal">OK</a>
			</div>
		</div>
	</div>
</div>


<div class="modal fade modal-non-unique">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<h2 class="modal-title">Ooops...</h2>
			</div>
			<div class="modal-body">
				<p>StackEdit has stopped because another instance was running in
					the same browser.</p>
				<blockquote>
                    <p>If you want to reopen StackEdit, click on
					"Reload".</p>
                </blockquote>
			</div>
			<div class="modal-footer">
				<a href="javascript:window.location.reload();"
					class="btn btn-primary">Reload</a>
			</div>
		</div>
	</div>
</div>


<div class="modal fade modal-redirect-confirm">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<h2 class="modal-title">Redirection</h2>
			</div>
			<div class="modal-body">
			    <p class="redirect-msg"></p>
				<blockquote>
                    <p>Please click <b>OK</b> to proceed.</p>
                </blockquote>
			</div>
			<div class="modal-footer">
				<a class="btn btn-default" data-dismiss="modal">Cancel</a>
				<a class="btn btn-primary action-redirect-confirm" data-dismiss="modal">OK</a>
			</div>
		</div>
	</div>
</div>


<div class="modal fade modal-app-reset">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<h2 class="modal-title">Reset application</h2>
			</div>
			<div class="modal-body">
				<p>This will delete all your local documents.</p>
				<blockquote><b>Are you sure?</b></blockquote>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
				<a href="#" class="btn btn-primary action-app-reset"
					data-dismiss="modal">OK</a>
			</div>
		</div>
	</div>
</div>


<div class="modal fade modal-import-docs-settings">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<h2 class="modal-title">Import documents and settings</h2>
			</div>
			<div class="modal-body">
				<p>This will delete all existing local documents.</p>
				<blockquote><b>Are you sure?</b></blockquote>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
				<a href="#" class="btn btn-primary action-import-docs-settings-confirm"
					data-dismiss="modal">OK</a>
			</div>
		</div>
	</div>
</div>
