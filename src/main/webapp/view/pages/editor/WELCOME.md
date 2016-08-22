# 欢迎使用MBLOG编辑器!

本Markdown编辑器基于[StackEdit][1]修改而来，使用此编辑器将会给您带来全新的书写体验。

> Markdown 是一种轻量级标记语言，它允许人们使用易读易写的纯文本格式编写文档，然后转换成格式丰富的HTML页面。—— [维基百科][2]

**强烈建议您保留此文档，以便查阅相关语法。**

- **丰富的快捷键**
- **Markdown及其扩展语法**
- **代码高亮**
- **图片链接、图片上传**
- ***LaTex*数学公式**
- **UML序列图、流程图**
- **离线博客**
- **导入导出Markdown文件**

-------------------

## 快捷键

 - 加粗    `Ctrl + B` 
 - 斜体    `Ctrl + I` 
 - 引用    `Ctrl + Q`
 - 插入链接    `Ctrl + L`
 - 插入代码    `Ctrl + K`
 - 插入图片    `Ctrl + G`
 - 提升标题    `Ctrl + H`
 - 有序列表    `Ctrl + O`
 - 无序列表    `Ctrl + U`
 - 横线    `Ctrl + R`
 - 撤销    `Ctrl + Z`
 - 重做    `Ctrl + Y`
 
 ----------

 登陆后可使用
 
 - 保存草稿    `Ctrl + S`
 - 发布文章    `Ctrl + P`

----------

## Markdown 扩展

本编辑器支持 **粗体** *斜体* [链接](http://www.manerfan.com "mblog") 等基本格式，详细语法参考帮助<i class='icon-help-circled'></i>.

本编辑器支持 **Markdown Extra** ，具体请参考[Github][3].  

### 表格

**Markdown Extra** 表格语法:

Item     | Value
-------- | ---
Computer | $1600
Phone    | $12
Pipe     | $1

使用冒号定义对齐方式:

| 左对齐    | 右对齐 |  居中  |
| :------- | ----: | :---: |
| Computer | $1600 |  5    |
| Phone    | $12   |  12   |
| Pipe     | $1    |  234  |


### 定义列表

**Markdown　Extra**　列表语法:

项目１
项目２
:   定义 A
:   定义 B

项目３
:   定义 C
:   定义 D
	> 定义D内容

----------

无序列表

 - List 1
 - List 2
 - List 3
  - List 31
  - List 32

----------

有序列表

 1. List 1
 2. List 2
 3. List 3
  1. List 31
  2. List 32

----------

### 代码块

代码块语法遵循标准markdown代码，如：
``` java
public class CommonUtils {
    private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    public static Resource[] getResources(String searchPath) {
        if (!StringUtils.hasText(searchPath)) {
            return null;
        }

        try {
            return resourcePatternResolver.getResources(searchPath);
        } catch (IOException e) {
            MLogger.ROOT_LOGGER.error("Search Resources on [{}] Error.", searchPath, e);
            return null;
        }
    }
}
```

本编辑器默认采用 **Highlight.js** 进行代码高亮显示，详情请参考[Highlight][4].

----------

### 脚注

可以这样来生成一个脚注[^footnote].

  [^footnote]: 这里是 **脚注** 的 *内容*.

----------

### 目录

您可以使用 `[TOC]` 插入目录:

[TOC]

----------

### MathJax 数学公式

本编辑器使用MathJax渲染*LaTex* 数学公式，详见 [stackexchange](http://math.stackexchange.com/ "LaTex") 

 - 行内公式，可使用`$`标注 $\Gamma(n) = (n-1)!\quad\forall n\in\mathbb N$.
 - 块级公式，可使用`$$`进行标注
$$\Gamma(z) = \int_0^\infty t^{z-1}e^{-t}dt\,.$$

> **Note:** 更多内容请参考 [这里][5]


### UML 图

使用 `sequence` 渲染序列图:

```sequence
Alice->Bob: Hello Bob, how are you?
Note right of Bob: Bob thinks
Bob-->Alice: I am good thanks!
```

使用 `flow` 渲染流程图:

```flow
st=>start: Start
e=>end
op=>operation: My Operation
cond=>condition: Yes or No?

st->op->cond
cond(yes)->e
cond(no)->op
```

> 更多 **序列图** 信息请参考 [这里][6]
> 更多 **流程图** 信息请参考 [这里][7]

##浏览器兼容

目前，本编辑器对Chrome浏览器支持最为完整。建议大家使用较新版本的Chrome。

## 支持作者

您的支持是我最大的动力！

![您的支持是我最大的动力](/view/images/alipay-qr.jpg "您的支持是我最大的动力")

----------

  [1]: https://github.com/benweet/stackedit "StackEdit"
  [2]: https://zh.wikipedia.org/wiki/Markdown "维基百科"
  [3]: https://github.com/jmcmanus/pagedown-extra "Pagedown Extra"
  [4]: http://highlightjs.org/ "Highlightjs"
  [5]: http://meta.math.stackexchange.com/questions/5020/mathjax-basic-tutorial-and-quick-reference
  [6]: http://bramp.github.io/js-sequence-diagrams/
  [7]: http://adrai.github.io/flowchart.js/
