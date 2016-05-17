/*
 * ManerFan(http://www.manerfan.com). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * <pre>
 * ======================================================
 * InitialiseController 
 * {@link com.manerfan.blog.webapp.InitialiseController}
 * 
 * /init                page    初始化页面
 * /init/check          ajax    检测并初始化用户数据
 * ======================================================
 * LoginController 
 * {@link com.manerfan.blog.webapp.LoginController}
 * 
 * /login               page    登陆页面
 * /login/publickey     ajax    获取RSA公钥
 * ======================================================
 * SpringSecuritConfiguration 
 * {@link com.manerfan.spring.configuration.SpringSecuritConfiguration}
 * 
 * /login/check         ajax    登陆验证
 * /logout              page    登出
 * ======================================================
 * WelcomeController 
 * {@link com.manerfan.blog.webapp.WelcomeController}
 * 
 * /                    page    首页
 * ======================================================
 * EditorController 
 * {@link com.manerfan.blog.webapp.editor.EditorController}
 * 
 * /editor              page    写文章页面
 * /editor/{fileId}     page    编辑文章页面
 * /editor/fileImport   ajax    导入文章
 * ======================================================
 * SettingsController 
 * {@link com.manerfan.blog.webapp.settings.SettingsController}
 * 
 * /settings            page    设置页面
 * ======================================================
 * UserController 
 * {@link com.manerfan.blog.webapp.user.UserController}
 * 
 * /user/modify/email   ajax    修改用户邮箱
 * /user/modify/passwd  ajax    修改用户密码
 * ======================================================
 * ArticleOprController
 * {@link com.manerfan.blog.webapp.article.ArticleOprController}
 * 
 * /article/publish                 ajax    发布文章/保存草稿
 * /article/content/{uid}/{type}    ajax    获取文章内容
 * /article/delete/{uid}            ajax    删除文章
 * /article/update/state            ajax    修改文章状态
 * ======================================================
 * ArticleViewController
 * {@link com.manerfan.blog.webapp.article.ArticleViewController}
 * 
 * /article/{uid}                       page    查看文章
 * 
 * /article                             page    按创建时间浏览文章列表
 * /article/category/{name}             page    按文章分类浏览文章列表
 * /article/search/{key}                page    按关键词搜索文章列表
 * /article/archive/{year}/{month}      page    按归档日期浏览文章列表
 * /article/timeline                    page    时间线方式浏览文章列表
 * 
 * /article/list                        ajax    根据文章状态，按照创建时间降序排序，分页查询
 * /article/category/list/{name}        ajax    根据文章分类，按照创建时间降序排序，分页查询
 * /article/archive/list/{year}/{month} ajax    根据归档时间，按照创建时间降序排序，分页查询
 * 
 * /article/archive/hots/{top}          ajax    获取最近的top个归档数据
 * /article/archive/list/all            ajax    获取所有的归档数据
 * 
 * /article/hit/hots/{top}              ajax    获取浏览次数最多的top个文章列表
 * ======================================================
 * CategoryController
 * {@link com.manerfan.blog.webapp.article.CategoryController}
 * 
 * /article/category/hots/{top}         ajax    获取使用最多的前top个分类数据
 * /article/category/list/all           ajax    获取所有分类数据
 * 
 * /article/category/delete/{name}      ajax    删除分类
 * /article/category/rename/{oldName}   ajax    重命名分类
 * 
 * ======================================================
 * ImageController
 * {@link com.manerfan.blog.webapp.article.ImageController}
 * 
 * /article/image/upload                ajax    上传图片
 * /article/image/{dispos}/{name}       ajax    下载/浏览 图片
 * /article/image/delete/{name}         ajax    删除图片
 * /article/image/list                  ajax    浏览图片目录第一层 文件夹(年)
 * /article/image/list/{year}           ajax    浏览图片目录第二层 文件夹(月)
 * /article/image/list/{year}/{month}   ajax    浏览图片目录第三层 图片
 * ======================================================
 * AboutController
 * {@link com.manerfan.blog.webapp.about.AboutController}
 * 
 * /about/author    page    关于作者页面
 * ======================================================
 * </pre>
 *
 * @author ManerFan 2016年5月17日
 */
package com.manerfan.blog.webapp;