<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="c_tld" %><!-- http://java.sun.com/jsp/jstl/core -->
<%@ taglib prefix="fmt" uri="fmt_tld" %><!-- http://java.sun.com/jsp/jstl/functions -->
<%@ taglib prefix="fn" uri="fn_tld" %><!-- http://java.sun.com/jsp/jstl/fmt -->
<%@ taglib prefix="spring" uri="spring_tld" %> <!-- http://www.springframework.org/tags -->
<%@ taglib prefix="sec" uri="security_tld" %> <!-- http://www.springframework.org/security/tags" -->

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
					        <i class="icon-attention-circled"></i>offline
					    </div>
					</li>
					<li class="extension-buttons"></li>
				</ul>
				<ul class="nav pull-right title-container">
					<li><div class="working-indicator"></div></li>
					<li><a class="btn btn-success file-title-navbar" href="#"
						title="Rename document"> </a></li>
					<li><div class="input-file-title-container"><input type="text"
						class="col-sm-4 form-control hide input-file-title"
						placeholder="Document title" /></div></li>
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

	<div class="menu-panel">
		<button class="btn toggle-button" title="Menu">
			<img
				data-stackedit-src="menu-icon.png" width="24" height="24" />
		</button>
		<div class="panel-content">
			<div class="list-group">

				<a href="viewer" title="StackEdit Viewer"
					class="list-group-item">
					<i class="icon-resize-full"></i> StackEdit Viewer
				</a>

			</div>
			<div class="list-group">

				<a href="#" data-toggle="collapse" data-target=".collapse-synchronize"
					class="list-group-item">
					<div><i class="icon-refresh"></i> Synchronize</div>
					<small>Open, save, collaborate in the Cloud</small>
				</a>
				<div class="sub-menu collapse collapse-synchronize clearfix">
					<ul class="nav alert alert-danger show-already-synchronized">
						<li><div>"<span class="file-title"></span>" is already synchronized.</div></li>
						<li><a href="#" class="action-force-synchronization"><i class="icon-refresh"></i>
							Force synchronization</a></li>
	                    <li><a href="#" data-toggle="modal" data-target=".modal-manage-sync"
	                        class="action-reset-input"><i
	                        class="icon-refresh"></i> Manage synchronization</a></li>
	                </ul>
					<ul class="nav">
						<li><a href="#" class="action-sync-import-dialog-couchdb"><i
							class="icon-provider-couchdb"></i> Open from CouchDB <sup class="text-danger">new</sup></a></li>
						<li><a href="#" class="action-sync-export-dialog-couchdb"><i
					        class="icon-provider-couchdb"></i> Save on CouchDB <sup class="text-danger">new</sup></a></li>
						<li><a href="#" class="action-sync-import-dropbox"><i
							class="icon-provider-dropbox"></i> Open from Dropbox</a></li>
						<li><a href="#" class="action-sync-export-dialog-dropbox"><i
					        class="icon-provider-dropbox"></i> Save on Dropbox</a></li>
						<li><a href="#" class="submenu-sync-gdrive action-sync-import-gdrive"><i
							class="icon-provider-gdrive"></i> Open from Google Drive</a></li>
						<li><a href="#" class="submenu-sync-gdrive action-sync-export-dialog-gdrive"><i
							class="icon-provider-gdrive"></i> Save on Google Drive</a></li>
						<li><a href="#" class="submenu-sync-gdrivesec action-sync-import-gdrivesec"><i
							class="icon-provider-gdrive"></i> Open from Google Drive<br><small>(2nd account)</small></a></li>
						<li><a href="#" class="submenu-sync-gdrivesec action-sync-export-dialog-gdrivesec"><i
							class="icon-provider-gdrive"></i> Save on Google Drive<br><small>(2nd account)</small></a></li>
						<li><a href="#" class="submenu-sync-gdriveter action-sync-import-gdriveter"><i
							class="icon-provider-gdrive"></i> Open from Google Drive<br><small>(3rd account)</small></a></li>
						<li><a href="#" class="submenu-sync-gdriveter action-sync-export-dialog-gdriveter"><i
							class="icon-provider-gdrive"></i> Save on Google Drive<br><small>(3rd account)</small></a></li>
					</ul>
				</div>
				<a href="#" data-toggle="collapse" data-target=".collapse-publish-on"
					class="list-group-item">
					<div><i class="icon-upload"></i>Publish</div>
					<small>Export to the web</small>
				</a>
				<div class="sub-menu collapse collapse-publish-on clearfix">
					<ul class="nav alert alert-danger show-already-published">
	                    <li><div>"<span class="file-title"></span>" is already published.</div></li>
	                    <li><a href="#" class="action-update-publication"><i class="icon-upload"></i>
	                        Update publication</a></li>
	                    <li><a href="#" data-toggle="modal" data-target=".modal-manage-publish"
	                        class="action-reset-input"><i class="icon-upload"></i>
	                        Manage publication</a></li>
					</ul>
					<ul class="nav publish-on-provider-list">
					</ul>
				</div>
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
			</div>
			<ul class="nav">
				<li><a href="#" data-toggle="modal"
					data-target=".modal-settings" class="action-load-settings"><i
						class="icon-cog"></i> Settings</a></li>
				<li><a href="#" data-toggle="modal" data-target=".modal-about"><i
						class="icon-help-circled"></i> About</a></li>
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


<div class="modal fade modal-upload-gdrive">
</div>
<div class="modal fade modal-upload-gdrivesec">
</div>
<div class="modal fade modal-upload-gdriveter">
</div>


<div class="modal fade modal-autosync-gdrive">
</div>
<div class="modal fade modal-autosync-gdrivesec">
</div>
<div class="modal fade modal-autosync-gdriveter">
</div>


<div class="modal fade modal-upload-dropbox">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Save on Dropbox</h2>
			</div>
			<div class="modal-body">
				<p>
					This will save "<span class="file-title"></span>" to your <i
						class="icon-provider-dropbox"></i>
					<code>Dropbox</code>
					account and keep it synchronized.
				</p>
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-3 control-label"
							for="input-sync-export-dropbox-path">File path</label>
						<div class="col-sm-8">
							<input type="text" id="input-sync-export-dropbox-path"
								placeholder="/path/to/My Document.md" class="form-control">
							<span class="help-block"> File path is composed of both
								folder and filename. </span>
						</div>
					</div>
				</div>
				<blockquote>
					<b>Note:</b>
					<ul>
						<li>Dropbox file path does not depend on document title.</li>
						<li>The title of your document will not be synchronized.</li>
						<li>Destination folder must exist.</li>
						<li>Any existing file at this location will be overwritten.</li>
					</ul>
				</blockquote>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
				<a href="#" data-dismiss="modal"
					class="btn btn-primary action-sync-export-dropbox">OK</a>
			</div>
		</div>
	</div>
</div>


<div class="modal fade modal-download-couchdb">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">&times;</button>
                <h2 class="modal-title">Open from CouchDB</h2>
                <div class="form-horizontal list-mode">
                    <br>
                    <div class="form-group form-inline">
                        <label for="input-sync-import-couchdb-tag" class="col-sm-3 control-label">Filter by tag</label>
                        <select id="input-sync-import-couchdb-tag" class="col-sm-4 form-control">
                        </select>
                        <span class="col-sm-5">
                            <a class="btn btn-link action-add-tag"><i class="icon-tag"></i> Add
                            </a>
                            <a class="btn btn-link action-remove-tag"><i class="icon-tag"></i> Remove
                            </a>
                        </span>
                    </div>
                </div>
            </div>
            <div class="modal-body">
                <p class="msg-default-couchdb hide alert alert-danger"><i class="icon-attention"></i> <b>Careful:</b>
                    You're using the public CouchDB instance.
                    <b>Anybody can open, edit and delete your files there!</b> To setup your own CouchDB instance <a
                            target="blank" href="https://github.com/benweet/stackedit/blob/master/doc/couchdb-setup.md">click
                        here</a>.
                </p>

                <div class="form-horizontal byid-mode">
                    <div class="form-group">
                        <label for="input-sync-import-couchdb-documentid" class="col-sm-3 control-label">Document
                            ID</label>

                        <div class="col-sm-9">
                            <input id="input-sync-import-couchdb-documentid" class="form-control"
                                   placeholder="DocumentID">
                            <span class="help-block">Multiple IDs can be provided (space separated)</span>
                        </div>
                    </div>
                </div>
                    <ul class="list-mode nav nav-pills">
                        <li class="pull-right dropdown"><a href="#"
                                                           data-toggle="dropdown"><i class="icon-check"></i> Selection
                            <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="#" class="action-unselect-all"><i
                                        class="icon-check-empty"></i> Unselect all</a></li>
                                <li class="divider"></li>
                                <li><a href="#" class="action-delete-items"><i
                                        class="icon-trash"></i> Delete</a></li>
                            </ul>
                        </li>
                    </ul>
                    <p class="list-mode">
                    </p>
                    <div class="list-group document-list list-mode"></div>
                <div class="list-mode text-center">
                    <div class="please-wait"><b>Please wait...</b></div>
                    <div class="no-document"><b>No document.</b></div>
                    <button class="more-documents btn btn-link"><i class="icon-angle-double-down"></i> More documents!</button>
                </div>
                <p class="delete-mode hide">The following documents will be
                    removed from CouchDB:</p>

                <div class="delete-mode list-group selected-document-list hide"></div>
            </div>
            <div class="modal-footer">
                <a href="#" class="btn btn-default pull-left list-mode action-byid-mode">Open by ID...</a>
                <a href="#"
                   class="btn btn-default delete-mode action-cancel hide">Cancel</a>
                <a href="#"
                   class="btn btn-primary delete-mode action-delete-items-confirm hide">Delete</a>
                <a href="#" class="btn btn-default byid-mode action-cancel">Cancel</a>
                <a href="#" data-dismiss="modal"
                   class="btn btn-primary action-sync-import-couchdb byid-mode">Open</a>
                <a href="#" class="btn btn-default list-mode" data-dismiss="modal">Cancel</a>
                <a href="#" data-dismiss="modal"
                   class="btn btn-primary action-sync-import-couchdb list-mode">Open</a>
            </div>
        </div>
    </div>
</div>


<div class="modal fade modal-upload-couchdb">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">&times;</button>
                <h2 class="modal-title">Save on CouchDB</h2>
            </div>
            <div class="modal-body">
                <p class="msg-default-couchdb hide alert alert-danger"><i class="icon-attention"></i> <b>Careful:</b>
                    You're using the public CouchDB instance.
                    <b>Anybody can open, edit and delete your files there!</b> To setup your own CouchDB instance <a
                            target="blank" href="https://github.com/benweet/stackedit/blob/master/doc/couchdb-setup.md">click
                        here</a>.
                </p>

                <p>
                    This will save "<span class="file-title"></span>" to CouchDB and keep it synchronized.
                </p>
                <blockquote>
                    <p><b>Tip:</b> You can use a
                    <a href="http://jekyllrb.com/docs/frontmatter/"
                       target="_blank">YAML front matter</a> to specify tags for your document.</p>
                    <p>Alternatively, you can place comma separated tags in square brackets at the beginning of the document title.</p>
                </blockquote>
            </div>
            <div class="modal-footer">
                <a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
                <a href="#" data-dismiss="modal"
                   class="btn btn-primary action-sync-export-couchdb">OK</a>
            </div>
        </div>
    </div>
</div>


<div class="modal fade modal-manage-sync">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Synchronization</h2>
			</div>
			<div class="modal-body">
				<p>
					"<span class="file-title"></span>" is synchronized with the
					following location(s):
				</p>
				<div class="sync-list"></div>
				<blockquote>
					<p><b>Note:</b> Removing a synchronized location will not delete any
					file.</p>
				</blockquote>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-primary" data-dismiss="modal">Close</a>
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
					<div class="form-group modal-publish-ssh">
						<label class="col-sm-4 control-label" for="input-publish-ssh-host">Host</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-ssh-host"
								placeholder="hostname.or.ip" class="form-control"> <span
								class="help-block"> Host must be accessible publicly,
								unless you're hosting your own StackEdit instance.
							</span>
						</div>
					</div>
					<div class="form-group modal-publish-ssh">
						<label class="col-sm-4 control-label" for="input-publish-ssh-port">Port
							(optional)</label>
						<div class="col-sm-2">
							<input type="text" id="input-publish-ssh-port" placeholder="22"
								class="form-control">
						</div>
					</div>
					<div class="form-group modal-publish-ssh">
						<label class="col-sm-4 control-label"
							for="input-publish-ssh-username">Username</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-ssh-username"
								placeholder="username" class="form-control">
						</div>
					</div>
					<div class="form-group modal-publish-ssh">
						<label class="col-sm-4 control-label"
							for="input-publish-ssh-password">Password</label>
						<div class="col-sm-7">
							<input type="password" id="input-publish-ssh-password"
								placeholder="password" class="form-control">
						</div>
					</div>
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
					<div class="form-group modal-publish-gist">
						<label class="col-sm-4 control-label" for="input-publish-filename">Filename</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-filename"
								placeholder="filename" class="form-control">
						</div>
					</div>
					<div class="form-group modal-publish-gist">
						<label class="col-sm-4 control-label" for="input-publish-gist-id">Existing
							ID (optional)</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-gist-id"
								placeholder="GistID" class="form-control">
						</div>
					</div>
					<div class="form-group modal-publish-gist">
						<label class="col-sm-4 control-label"
							for="input-publish-gist-public">Public</label>
						<div class="col-sm-7">
							<div class="checkbox">
								<input type="checkbox" id="input-publish-gist-public"
									checked="checked" />
							</div>
						</div>
					</div>
					<div class="form-group modal-publish-blogger modal-publish-bloggerpage">
						<label class="col-sm-4 control-label"
							for="input-publish-blogger-url">Blog URL</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-blogger-url"
								placeholder="http://exemple.blogger.com/" class="form-control">
						</div>
					</div>
					<div class="form-group modal-publish-tumblr">
						<label class="col-sm-4 control-label"
							for="input-publish-tumblr-hostname">Blog hostname</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-tumblr-hostname"
								placeholder="exemple.tumblr.com" class="form-control">
						</div>
					</div>
					<div class="form-group modal-publish-wordpress">
						<label class="col-sm-4 control-label"
							for="input-publish-tumblr-hostname">WordPress site</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-wordpress-site"
								placeholder="exemple.wordpress.com" class="form-control">
							<span class="help-block"> <a target="_blank"
								href="http://jetpack.me/">Jetpack plugin</a> is required for
								self-hosted sites.
							</span>
						</div>
					</div>
					<div
						class="form-group modal-publish-blogger modal-publish-tumblr modal-publish-wordpress">
						<label class="col-sm-4 control-label" for="input-publish-postid">Update
							existing post ID (optional)</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-postid" placeholder="PostID"
								class="form-control">
						</div>
					</div>
					<div
						class="form-group modal-publish-bloggerpage">
						<label class="col-sm-4 control-label" for="input-publish-pageid">Update
							existing page ID (optional)</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-pageid" placeholder="PageID"
								class="form-control">
						</div>
					</div>
					<div class="form-group modal-publish-dropbox">
						<label class="col-sm-4 control-label"
							for="input-publish-dropbox-path">File path</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-dropbox-path"
								placeholder="/path/to/My Document.html" class="form-control">
							<span class="help-block"> File path is composed of both
								folder and filename. </span>
						</div>
					</div>
					<div class="form-group modal-publish-gdrive">
						<label class="col-sm-4 control-label"
							for="input-publish-gdrive-fileid">File ID (optional)</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-gdrive-fileid"
								placeholder="FileID" class="form-control"> <span
								class="help-block">If no file ID is supplied, a new file
								will be created in your Google Drive root folder. You can move
								the file afterwards within Google Drive.</span>
						</div>
					</div>
					<div class="form-group modal-publish-gdrive">
						<label class="col-sm-4 control-label"
							for="input-publish-gdrive-filename">Force filename
							(optional)</label>
						<div class="col-sm-7">
							<input type="text" id="input-publish-gdrive-filename"
								placeholder="Filename" class="form-control"> <span
								class="help-block">If no file name is supplied, the
								document title will be used.</span>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-4 control-label">Format</label>
						<div class="col-sm-7">
							<div class="radio">
								<label> <input type="radio" name="radio-publish-format"
									value="markdown"> Markdown
								</label>
							</div>
							<div class="radio">
								<label> <input type="radio" name="radio-publish-format"
									value="html"> HTML
								</label>
							</div>
							<div class="radio">
								<label> <input type="radio" name="radio-publish-format"
									value="template"> Template
								</label>
							</div>
						</div>
					</div>
					<div class="collapse publish-custom-template-collapse">
						<div class="form-group">
							<div class="col-sm-4"></div>
							<div class="col-sm-7">
								<div class="checkbox">
									<label> <input type="checkbox"
										id="checkbox-publish-custom-template"> Custom template
									</label> <a href="#" class="tooltip-template">(?)</a>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-4"></div>
							<div class="col-sm-7">
								<textarea class="form-control"
									id="textarea-publish-custom-template"></textarea>
							</div>
						</div>
					</div>
				</div>
				<blockquote class="front-matter-info modal-publish-blogger modal-publish-tumblr modal-publish-wordpress">
                    <p><b>Tip:</b> You can use a
                    <a href="http://jekyllrb.com/docs/frontmatter/"
                    target="_blank">YAML front matter</a> to specify the title and the tags/labels of your publication.</p>
                    <p><b>Interpreted variables:</b> <code>title</code>, <code>tags</code>, <code>published</code>, <code>date</code>.</p>
				</blockquote>
				<blockquote class="front-matter-info modal-publish-bloggerpage">
                    <p><b>Tip:</b> You can use a
                    <a href="http://jekyllrb.com/docs/frontmatter/"
                    target="_blank">YAML front matter</a> to specify the title of your page.</p>
                    <p><b>Interpreted variables:</b> <code>title</code>.</p>
				</blockquote>
				<blockquote class="url-info modal-publish-bloggerpage">
                    <p><b>About URL:</b> For newly created page , Blogger API will append a generated number to the url like <code>about-me-1234.html</code>, if you deeply care about your URL naming, you should first create the page on Blogger and then update them with StackEdit specifying the pageId when publishing.
                    </p>
                    <p><b>About page visibility:</b> Blogger API does not respect published status for pages.When publishing the page to Blogger, the page will be <strong>live</strong> but not added to the page listing. You should arrange the page listing from Blogger dashboard.
                    </p>
				</blockquote>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default" data-dismiss="modal">Cancel</a>
				<a href="#" data-dismiss="modal"
					class="btn btn-primary action-process-publish">OK</a>
			</div>
		</div>
	</div>
</div>


<div class="modal fade modal-manage-publish">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title">Publication</h2>
			</div>
			<div class="modal-body">
				<p>
					"<span class="file-title"></span>" is published on the following
					location(s):
				</p>
				<div class="publish-list"></div>
				<blockquote>
					<p><b>Note:</b> Removing a publish location will not delete the actual publication.</p>
				</blockquote>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-primary" data-dismiss="modal">Close</a>
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
								<label class="col-sm-4 control-label" for="input-settings-theme">Theme</label>
								<div class="col-sm-7">
									<select id="input-settings-theme" class="form-control">
									</select>
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


<div class="modal fade modal-add-google-drive-account">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<h2 class="modal-title">Add Google Drive account</h2>
			</div>
			<div class="modal-body">
				<p>To perform this request, you need to configure another Google Drive account in StackEdit.</p>
				<blockquote><b>Do you want to proceed?</b></blockquote>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-default action-remove-google-drive-state"
					data-dismiss="modal">No</a>
				<a href="#" class="btn btn-primary action-add-google-drive-account"
					data-dismiss="modal">Yes</a>
			</div>
		</div>
	</div>
</div>


<div class="modal fade modal-sponsor-only">
	<div class="modal-dialog">
		<div class="modal-content">

			<div class="modal-header">
				<h2 class="modal-title">Sponsor only!</h2>
			</div>
			<div class="modal-body">
				<p>This feature is restricted to sponsor users as it's a web service hosted on Amazon EC2.
                    Note that sponsoring StackEdit would cost you only $5/year.</p>
				<p>To see how a PDF looks <a target="_blank" href="/Welcome%20document.pdf">click here</a>.</p>
				<blockquote>
                    <p><b>Tip:</b> PDFs are fully customizable via Settings>Advanced>PDF template/options.</p>
                </blockquote>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn btn-primary"
					data-dismiss="modal">Close</a>
			</div>
		</div>
	</div>
</div>


<div id="dropboxjs" data-app-key="x0k2l8puemfvg0o"></div>
