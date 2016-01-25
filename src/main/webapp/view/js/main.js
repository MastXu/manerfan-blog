// RequireJS configuration
/*global requirejs */

requirejs.config({

    baseUrl: "view",

    /**
     * The number of seconds to wait before giving up on loading a script.
     * Setting it to 0 disables the timeout. The default is 7 seconds.
     */
    waitSeconds: 0,

    /**
     * 从CommonJS包(package)中加载模块
     */
    packages: [
        {
            name: 'css', // 模块名
            location: 'plugins/require-css', // 相对路径
            main: 'css' // 主模块名（文件名）
        },
        {
            name: 'less',
            location: 'plugins/require-less',
            main: 'less'
        }
    ],

    /**
     * 配置模块ID与模块路径的映射
     *
     * 在使用require时，模块名与文件的对应关系如下
     *
     * 1.首先看requirejs.config()中paths是否配置了"path/module"这个配置项。
     * 如果配置了，"path/module"会被认为是一个普通的模块名。如果没有配置，那么执行第二步。
     *
     * 2.将"path/module"看成是文件系统上的路径，通过目录和文件名来确定需要加载的模块。
     * 如果仍然没有找到，那么requirejs会报错。
     */
    paths: {
        jquery: 'plugins/jquery/dist/jquery',
        underscore: 'plugins/underscore/underscore',
        crel: 'plugins/crel/crel',
        jgrowl: 'plugins/jgrowl/jquery.jgrowl',
        mousetrap: 'plugins/mousetrap/mousetrap',
        'mousetrap-record': 'plugins/mousetrap/plugins/record/mousetrap-record',
        toMarkdown: 'plugins/to-markdown/src/to-markdown',
        text: 'plugins/requirejs-text/text',
        mathjax: 'plugins/MathJax/MathJax.js?config=TeX-AMS_HTML',
        bootstrap: 'plugins/bootstrap/dist/js/bootstrap',
        requirejs: 'plugins/requirejs/require',
        'google-code-prettify': 'plugins/google-code-prettify/src/prettify',
        highlightjs: 'js/editor/libs/highlight/highlight.pack',
        'jquery-waitforimages': 'plugins/waitForImages/src/jquery.waitforimages',
        FileSaver: 'plugins/FileSaver/FileSaver',
        stacktrace: 'plugins/stacktrace/stacktrace',
        'requirejs-text': 'plugins/requirejs-text/text',
        'bootstrap-tour': 'plugins/bootstrap-tour/build/js/bootstrap-tour',
        'pagedown-extra': 'plugins/pagedown-extra/node-pagedown-extra',
        pagedownExtra: 'plugins/pagedown-extra/Markdown.Extra',
        pagedown: 'js/editor/libs/Markdown.Editor',
        'require-css': 'plugins/require-css/css',
        xregexp: 'plugins/xregexp/xregexp-all',
        yaml: 'plugins/yaml.js/bin/yaml',
        'yaml.js': 'plugins/yaml.js',
        'yaml-js': 'plugins/yaml.js/bin/yaml',
        css: 'plugins/require-css/css',
        'css-builder': 'plugins/require-css/css-builder',
        normalize: 'plugins/require-css/normalize',
        prism: 'plugins/prism/prism',
        'prism-core': 'plugins/prism/components/prism-core',
        MutationObservers: 'plugins/MutationObservers/MutationObserver',
        WeakMap: 'plugins/WeakMap/weakmap',
        rangy: 'plugins/rangy/rangy-core',
        'rangy-cssclassapplier': 'plugins/rangy/rangy-cssclassapplier',
        diff_match_patch: 'plugins/google-diff-match-patch-js/diff_match_patch',
        diff_match_patch_uncompressed: 'plugins/google-diff-match-patch-js/diff_match_patch_uncompressed',
        jsondiffpatch: 'plugins/jsondiffpatch/build/bundle',
        hammerjs: 'plugins/hammerjs/hammer',
        Diagram: 'plugins/js-sequence-diagrams/src/sequence-diagram',
        'diagram-grammar': 'plugins/js-sequence-diagrams/build/diagram-grammar',
        raphael: 'plugins/raphael/raphael',
        'flow-chart': 'plugins/flowchart/release/flowchart.amd-1.3.4.min',
        flowchart: 'plugins/flowchart/release/flowchart-1.3.4.min',
        monetizejs: 'plugins/monetizejs/src/monetize',
        'to-markdown': 'plugins/to-markdown/src/to-markdown',
        waitForImages: 'plugins/waitForImages/dist/jquery.waitforimages',
        MathJax: 'plugins/MathJax/MathJax',
        alertify: 'plugins/alertify.js/lib/alertify',
        
        /* 以下为自定义 */
        md5: 'plugins/jquery-md5/jquery.md5'
    },

    /**
     * 定义不支持AMD规范的插件及其依赖
     *
     * 模块名遵循上述paths注释内容
     * exports，与符合AMD规范模块中的define类似，将脚本中的哪个变量作为模块的返回（AMD规范使用define定义模块的返回）
     * deps，定义依赖，模块名遵循上述paths注释内容
     */
    shim: {
        underscore: {
            exports: '_'
        },
        mathjax: [
            'js/editor/libs/mathjax_init'
        ],
        jgrowl: {
            deps: [
                'jquery'
            ],
            exports: 'jQuery.jGrowl'
        },
        diff_match_patch_uncompressed: {
            exports: 'diff_match_patch'
        },
        jsondiffpatch: [
            'diff_match_patch_uncompressed'
        ],
        rangy: {
            exports: 'rangy'
        },
        'rangy-cssclassapplier': [
            'rangy'
        ],
        mousetrap: {
            exports: 'Mousetrap'
        },
        'yaml-js': {
            exports: 'YAML'
        },
        'prism-core': {
            exports: 'Prism'
        },
        'plugins/prism/components/prism-markup': [
            'prism-core'
        ],
        'js/editor/libs/prism-latex': [
            'prism-core'
        ],
        'js/editor/libs/prism-markdown': [
            'plugins/prism/components/prism-markup',
            'js/editor/libs/prism-latex'
        ],
        'bootstrap-record': [
            'mousetrap'
        ],
        toMarkdown: {
            deps: [
                'jquery'
            ],
            exports: 'toMarkdown'
        },
        stacktrace: {
            exports: 'printStackTrace'
        },
        FileSaver: {
            exports: 'saveAs'
        },
        MutationObservers: [
            'WeakMap'
        ],
        highlightjs: {
            exports: 'hljs'
        },
        'bootstrap-tour': {
            deps: [
                'bootstrap'
            ],
            exports: 'Tour'
        },
        bootstrap: [
            'jquery'
        ],
        'jquery-waitforimages': [
            'jquery'
        ],
        pagedown: [
            'js/editor/libs/Markdown.Converter'
        ],
        pagedownExtra: [
            'js/editor/libs/Markdown.Converter'
        ],
        'flow-chart': [
            'raphael'
        ],
        'diagram-grammar': [
            'underscore'
        ],
        Diagram: [
            'raphael',
            'diagram-grammar'
        ],
        
        /* 以下为自定义 */
        md5: [
            'jquery'
        ]
    }
});



