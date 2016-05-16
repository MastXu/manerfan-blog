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
 * 首页
 * Created by ManerFan on 2016/1/26.
 */
require([
    "jquery",
    "echarts",
    "smoothscroll"
], function ($, echarts) {

    $(".btn-scroll").smoothScroll();

    /***********************
     * skills charts 初始化
     ************************/

    var javaOption = {
        series: [{
            type: 'pie',
            radius: ['65%', '80%'],
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: [{
                value: 80,
                itemStyle: {
                    normal: {
                        color: '#91c7ae'
                    }
                }
            }, {
                value: 20,
                itemStyle: {
                    normal: {
                        color: '#d48265'
                    }
                }
            }]
        }]
    };
    var javaChart = echarts.init(document.getElementById('java'));
    javaChart.setOption(javaOption);

    var springOption = {
        series: [{
            type: 'pie',
            radius: ['65%', '80%'],
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: [{
                value: 40,
                itemStyle: {
                    normal: {
                        color: '#91c7ae'
                    }
                }
            }, {
                value: 60,
                itemStyle: {
                    normal: {
                        color: '#d48265'
                    }
                }
            }]
        }]
    };
    var springChart = echarts.init(document.getElementById('springframework'));
    springChart.setOption(springOption);

    var hibernateOption = {
        series: [{
            type: 'pie',
            radius: ['65%', '80%'],
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: [{
                value: 60,
                itemStyle: {
                    normal: {
                        color: '#91c7ae'
                    }
                }
            }, {
                value: 40,
                itemStyle: {
                    normal: {
                        color: '#d48265'
                    }
                }
            }]
        }]
    };
    var hibernateChart = echarts.init(document.getElementById('hibernate'));
    hibernateChart.setOption(hibernateOption);

    var mysqlOption = {
        series: [{
            type: 'pie',
            radius: ['65%', '80%'],
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: [{
                value: 60,
                itemStyle: {
                    normal: {
                        color: '#91c7ae'
                    }
                }
            }, {
                value: 40,
                itemStyle: {
                    normal: {
                        color: '#d48265'
                    }
                }
            }]
        }]
    };
    var mysqlChart = echarts.init(document.getElementById('mysql'));
    mysqlChart.setOption(mysqlOption);

    var htmlOption = {
        series: [{
            type: 'pie',
            radius: ['65%', '80%'],
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: [{
                value: 70,
                itemStyle: {
                    normal: {
                        color: '#91c7ae'
                    }
                }
            }, {
                value: 30,
                itemStyle: {
                    normal: {
                        color: '#d48265'
                    }
                }
            }]
        }]
    };
    var htmlChart = echarts.init(document.getElementById('html'));
    htmlChart.setOption(htmlOption);

    var javascriptOption = {
        series: [{
            type: 'pie',
            radius: ['65%', '80%'],
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: [{
                value: 50,
                itemStyle: {
                    normal: {
                        color: '#91c7ae'
                    }
                }
            }, {
                value: 50,
                itemStyle: {
                    normal: {
                        color: '#d48265'
                    }
                }
            }]
        }]
    };
    var javascriptChart = echarts.init(document.getElementById('javascript'));
    javascriptChart.setOption(javascriptOption);
});
