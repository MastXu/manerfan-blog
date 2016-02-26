<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->
<%@ taglib prefix="sec" uri="security_tld" %> <!-- http://www.springframework.org/security/tags" -->

<div class="layout-wrapper-l1">
	<div class="layout-wrapper-l2" >
		<div class="navbar navbar-default">
			<div class="navbar-inner">
				<div class="nav left-space"></div>
				<div class="nav right-space pull-right"></div>
				<div class="buttons-dropdown dropdown">
		    		<div class="nav">
		        		<button class="btn btn-success" data-toggle="dropdown"
		            		title="Show buttons">
		            		<i class="icon-th-large"></i>
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
					<li class="wmd-button-group5 btn-group"></li>
				</ul>
				<ul class="nav left-buttons">
					<li class="wmd-button-group4 btn-group">
						<a class="btn btn-success button-open-discussion" title="Comments Ctrl/Cmd+M"><i class="icon-comment-alt"></i></a>
					</li>
				</ul>
				<ul class="nav pull-right right-buttons">
					<li class="offline-status hide">
					    <div class="text-danger">
					        <i class="icon-attention-circled"></i>离线
					    </div>
					</li>
					<li class="extension-buttons"></li>
				</ul>
				<ul class="nav pull-right title-container">
		            <li><div class="working-indicator"></div></li>
		            <li><a class="btn btn-success file-title-navbar" href="#"
		                title="重命名"> </a></li>
		            <li><div class="input-file-title-container"><input type="text"
		                class="col-sm-4 form-control hide input-file-title"
		                placeholder="标题" /></div></li>
                    <li><button type="button" class="btn btn-danger">发表博客</button></li>
		        </ul>
			</div>
		</div>
		<div class="layout-wrapper-l3">
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

	<div class="menu-panel"> <!-- 左侧侧拉面板 -->
		<button class="btn toggle-button" title="Menu">
			<img src="view/images/editor/menu-icon.png" width="24" height="24" />
		</button>
		<div class="panel-content">
			<div class="list-group">
				<a href="#" data-toggle="modal" data-target=".modal-manage-sharing"
					class="action-reset-input list-group-item">
					<div><i class="icon-link"></i>Sharing</div>
					<small>Share document links</small>
				</a>
			</div>
			<div class="list-group">
				<a data-toggle="modal"
					data-target=".modal-import-harddrive-markdown"
					class="list-group-item action-reset-input" href="#"><i
					class="icon-hdd"></i> Import from disk</a>
				<a href="#" data-toggle="collapse" data-target=".collapse-save-as"
					class="list-group-item"><i class="icon-hdd"></i> Export to disk</a>
				<div class="sub-menu collapse collapse-save-as clearfix">
					<ul class="nav">
						<li><a class="action-download-md" href="#"><i
					        class="icon-download"></i> As Markdown</a></li>
						<li><a class="action-download-html" href="#"><i
					        class="icon-download"></i> As HTML</a></li>
						<li><a class="action-download-template" href="#"><i
					        class="icon-download"></i> Using template</a></li>
						<li><a class="action-download-pdf" href="#"><i
					        class="icon-download"></i> As PDF <sup class="text-danger">sponsor</sup></a></li>
					</ul>
				</div>

	            <a data-toggle="modal" data-target=".modal-import-url"
	                class="list-group-item action-reset-input" href="#"><i
					class="icon-globe"></i> Import from URL</a>
	            <a data-toggle="modal"
					data-target=".modal-import-harddrive-html"
					class="list-group-item action-reset-input" href="#"><i
					class="icon-code"></i> Convert HTML to Markdown</a>
				<a data-toggle="modal" data-target=".modal-app-reset"
                    class="list-group-item action-reset-input" href="#"><i
                    class="icon-refresh"></i> Reset The Application.</a>
			</div>
		</div>
	</div>

	<div class="document-panel"> <!-- 右侧侧拉面板 -->
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
				<p>Please provide the image URL and an optional title:</p>
				<div class="input-group">
					<span class="input-group-addon"><i class="icon-picture"></i></span><input
						id="input-insert-image" type="text" class="col-sm-5 form-control"
						placeholder='http://example.com/image.jpg "optional title"' />
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default pull-left action-import-image-gplus"
					data-dismiss="modal"><i class="icon-provider-gplus"></i> Import
					from Google+</a> <a href="#" class="btn btn-default"
					data-dismiss="modal">Cancel</a> <a href="#"
					class="btn btn-primary action-insert-image" data-dismiss="modal">OK</a>
			</div>
		</div>
	</div>
</div>

<div class="modal fade modal-import-image">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Google+ image import</h2>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-7">
							<img>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"
							for="input-import-image-title">Title (optional)</label>
						<div class="col-sm-7">
							<input type="text" id="input-import-image-title"
								placeholder="Image title" class="form-control">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-4 control-label"
							for="input-import-image-size">Size limit (optional)</label>
						<div class="col-sm-7 form-inline">
							<input type="text" id="input-import-image-size" placeholder="0"
								class="col-sm-3 form-control"> px
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
				<a href="#" class="btn btn-primary action-import-image"
					data-dismiss="modal">OK</a>
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
				<h2 class="modal-title">Import from URL</h2>
			</div>
			<div class="modal-body">
				<p>Please provide a link to a Markdown document.</p>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-3 control-label" for="input-import-url">URL</label>
						<div class="col-sm-8">
							<input type="text" id="input-import-url"
								placeholder="http://www.abc.com/xyz.md" class="form-control">
						</div>
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

<div class="modal fade modal-import-harddrive-markdown">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Import from disk</h2>
			</div>
			<div class="modal-body">
				<p>Please select your Markdown files here:</p>
				<p>
					<input type="file" id="input-file-import-harddrive-markdown"
						multiple class="form-control" />
				</p>
				<p>Or drag and drop your Markdown files here:</p>
				<p id="dropzone-import-harddrive-markdown" class="drop-zone">Drop
					files here</p>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-primary" data-dismiss="modal">Close</a>
			</div>
		</div>
	</div>
</div>

<div class="modal fade modal-import-harddrive-html">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Convert HTML to Markdown</h2>
			</div>
			<div class="modal-body">
				<p>Please select your HTML files here:</p>
				<p>
					<input type="file" id="input-file-import-harddrive-html" multiple
						class="form-control" />
				</p>
				<p>Or drag and drop your HTML files here:</p>
				<p id="dropzone-import-harddrive-html" class="drop-zone">Drop
					files here</p>
				<p>Or insert your HTML code here:</p>
				<textarea id="input-convert-html" class="form-control"></textarea>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">Close</a> <a
					href="#" class="btn btn-primary action-convert-html"
					data-dismiss="modal">OK</a>
			</div>
		</div>
	</div>
</div>

<div class="modal fade modal-manage-sharing">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Sharing</h2>
			</div>
			<div class="modal-body">
                <p>Collaborate on "<span class="file-title"></span>" using the following link(s):</p>
            	<p class="msg-no-share-editor"><b>No sharing link yet!</b>
            	</p>
                <div class="share-editor-list"></div>
            	<blockquote>
                    <p><b>Note:</b> To collaborate on this document, just <a
                        href="#" class="action-sync-export-dialog-couchdb" data-dismiss="modal">save it on CouchDB</a>.
                    To collaborate via Google Drive or Dropbox, you have to share the file manually from Google Drive/Dropbox websites.</p>
            	</blockquote>
                <hr>
                <p>Share a read-only version of "<span class="file-title"></span>" using the following link(s):</p>
            	<p class="msg-no-share-viewer"><b>No sharing link yet!</b>
            	</p>
                <div class="share-viewer-list"></div>
            	<blockquote>
                    <p><b>Note:</b> To share a read-only version of this document, just <a
                        href="#" class="action-publish-gist" data-dismiss="modal">publish it as a Gist</a> in
                    Markdown format.</p>
            	</blockquote>
            	<blockquote>
            		<p><b>Tip:</b> You can open any markdown URL within StackEdit Viewer using <a
            			href="viewer#!url=https://raw.github.com/benweet/stackedit/master/README.md"
            			title="Sharing example"><code>viewer#!url=</code></a>.</p>
            	</blockquote>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-primary" data-dismiss="modal">Close</a>
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
