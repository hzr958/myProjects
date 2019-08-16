scicluster=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
       title:{
	        text: "信息科学",
	        subtext: "各关系-Acring",
	        top: "top",
	        left: "center"
       },
        legend: [{  //图例组件
            tooltip: { //图例的 tooltip 配置
                show: true //是否显示提示框组件
            },
            selectedMode: 'false', //图例选择的模式，控制是否可以通过点击图例改变系列的显示状态。默认开启图例选择，可以设成 false 关闭
            bottom: 20, // 距底部的距离
            data: ['计算机科学与技术', '图书情报学', '物理学','推荐系统','信息融合','大数据','信息检索','信息论','信息科学技术']
        }],
        animationDuration: 3000,  //本组件的动画时长
        animationEasingUpdate: 'quinticInOut', //动画的缓动效果
        series: [{      //系列列表
            name: '信息科学',
            type: 'graph',
            layout: 'force',  //力引导布局
            force: {  //力引导布局相关配置
                repulsion: 50  // 节点排斥因子范围
            },

            data:[{
                  "name":"信息科学",
                  "symbolSize":22,
                  "draggable":"true",
                  "value":28
                },{
                  "name":"计算机科学与技术",
                  "symbolSize":12,
                  "draggable":"true",
                  "category":"计算机科学与技术",
                  "value":18
                },{
                  "name":"数据挖掘",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"计算机科学与技术",
                  "value":2
                },{
                  "name":"人工智能",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"计算机科学与技术",
                  "value":2
                },{
                  "name":"信息处理",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"计算机科学与技术",
                  "value":2
                },
                {
                  "name":"神经网络",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"计算机科学与技术",
                  "value":2
                },{
                  "name":"自然语言处理",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"计算机科学与技术",
                  "value":2
                },{
                  "name":"机器学习",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"计算机科学与技术",
                  "value":2
                },{
                  "name":"图书情报学",
                  "symbolSize":12,
                  "draggable":"true",
                  "category":"图书情报学",
                  "value":18
                },{
                  "name":"信息社会",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"图书情报学",
                  "value":2
                },{
                  "name":"信息资源",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"图书情报学",
                  "value":2
                },{
                  "name":"图书馆学",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"图书情报学",
                  "value":2
                },{
                  "name":"情报科学",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"图书情报学",
                  "value":2
                },{
                  "name":"物理学",
                  "symbolSize":13,
                  "draggable":"true",
                  "category":"物理学",
                  "value":2
                },{
                  "name":"光信息科学与技术",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"物理学",
                  "value":2
                },{
                  "name":"通信技术",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"物理学",
                  "value":19
                },{
                  "name":"光电子学",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"物理学",
                  "value":2
                },{
                  "name":"电子信息科学",
                  "symbolSize":5,
                  "draggable":"true",
                  "category":"物理学",
                  "value":2
                },{
                  "name":"推荐系统",
                  "symbolSize":12,
                  "draggable":"true",
                  "category":"推荐系统",
                  "value":13
                },{
                  "name":"信息采集",
                  "symbolSize":12,
                  "draggable":"true",
                  "category":"信息采集",
                  "value":12
                },{
                  "name":"信息融合",
                  "symbolSize":12,
                  "draggable":"true",
                  "category":"信息融合",
                  "value":14
                },{
                  "name":"大数据",
                  "symbolSize":12,
                  "draggable":"true",
                  "category":"大数据",
                  "value":15
                },{
                  "name":"信息检索",
                  "symbolSize":12,
                  "draggable":"true",
                  "category":"信息检索",
                  "value":16
                },{
                  "name":"信息论",
                  "symbolSize":12,
                  "draggable":"true",
                  "category":"信息论",
                  "value":17
                },{
                  "name":"信息科学技术",
                  "symbolSize":12,
                  "draggable":"true",
                  "category":"信息科学技术",
                  "value":17
                }],

    links:[{
            "source": "信息科学",
            "target": "计算机科学与技术"
          },{
            "source": "计算机科学与技术",
            "target": "数据挖掘"
          },{
            "source": "计算机科学与技术",
            "target": "人工智能"
          },{
            "source": "计算机科学与技术",
            "target": "信息处理"
          },{
            "source": "计算机科学与技术",
            "target": "神经网络"
          },{
            "source": "计算机科学与技术",
            "target": "自然语言处理"
          },{
            "source": "计算机科学与技术",
            "target": "机器学习"
          },{
            "source": "信息科学",
            "target": "图书情报学"
          },{
            "source": "图书情报学",
            "target": "信息社会"
          },{
            "source": "图书情报学",
            "target": "信息资源"
          },{
            "source": "图书情报学",
            "target": "图书馆学"
          },{
            "source": "图书情报学",
            "target": "情报科学"
          },{
            "source": "信息科学",
            "target": "物理学"
          },{
            "source": "物理学",
            "target": "电子信息科学"
          },{
            "source": "物理学",
            "target": "光电子学"
          },{
            "source": "物理学",
            "target": "通信技术"
          },{
            "source": "物理学",
            "target": "光信息科学与技术"
          },{
            "source": "信息科学",
            "target": "推荐系统"
          },{
            "source": "信息科学",
            "target": "信息采集"
          },{
            "source": "信息科学",
            "target": "信息融合"
          },{
            "source": "信息科学",
            "target": "大数据"
          },{
            "source": "信息科学",
            "target": "信息检索"
          },{
            "source": "信息科学",
            "target": "信息论"
          },{
            "source": "信息科学",
            "target": "信息科学技术"
          }],
   categories: [{
            'name':'计算机科学与技术'   
          },{
            'name':'图书情报学' 
          },{
            'name':'物理学'
          },{
            'name':'推荐系统'
          },{
            'name':'信息采集'
          },{
            'name':'信息融合'
          },{
            'name':'大数据'
          },{
            'name':'信息检索'
          },{
            'name':'信息论'
          },{
            'name':'信息科学技术'
          }],
 
          roam: true,
          label: {
              normal: {
                  show: true,
                  position: 'top',
              }
          },
          lineStyle: {
              normal: {
                  color: 'source',
                  curveness: 0,
                  type: "solid"
              }
          }
      }]
  };
  myChart.setOption(option);
}