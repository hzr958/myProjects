earthPub=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "地球科学-论文",
	        subtext: "关键分析",
	        top: "top",
	        left: "center"
       },
        legend: [{  
            tooltip: { 
                show: true 
            },
            selectedMode: 'false', 
            bottom: 20, 
            data: ['地球科学-论文']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '地球科学-论文',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{
	"name": "地球科学-论文",
	"value": 6,
	"symbolSize": 18,
	"category": "地球科学-论文",
	"draggable": "true"
},{
	"name": "ERA-INTERIM再分析：数据协调系统的配置和性能",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "INTCAL09和MARINE09放射性碳年龄校准曲线，0-50,000年的年龄",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "CMIP5概述和实验设计",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "基于热带降雨测量卫星的多卫星降水分析：细尺度上类全球性的多年的组合传感器降水分析",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "INTCAL13和MARINE13放射性碳年龄校准曲线，0-50,000年的年龄",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "温暖和春季的提前到来增加了美国西部的森林火灾",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "人类的安全操作空间",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "更新的KOPPEN-GEIGER气候分类世界地图",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "气候变化研究的一个新纪元——世界气候研究计划CMIP3多模型数据集",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
    "name": "改进的美国国家海洋和大气管理局1880-2006年陆地海洋表明温度分析",
    "symbolSize": 3,
    "category": "地球科学-论文",
    "draggable": "true",
    "value": 2
},{
	"name": "气候变化研究和评估的新时代场景",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "美国国家航空和航天局研究和应用的现代史回顾性分析",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "夹带起沙治理的一个新的垂直扩展包",
	"symbolSize": 3,"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "社区气候系统模型版本3",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "KOPPEN-GEIGER气候分类世界地图的更新",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "全球变暖的水循环对策",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "基于大自然中气体和气雾剂排放模型的全球陆地异戊二烯排放的估计",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "气候碳循环反馈分析：来自于(CMIP)-M-4模型比较的结果",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "北美区域性再分析",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
},{
	"name": "美国国家环境预报中心气候森林系统再分析",
	"symbolSize": 3,
	"category": "地球科学-论文",
	"draggable": "true",
	"value": 2
}],
links:
[ 
{"source": "地球科学-论文","target": "ERA-INTERIM再分析：数据协调系统的配置和性能"}, 
{"source": "地球科学-论文","target": "INTCAL09和MARINE09放射性碳年龄校准曲线，0-50,000年的年龄"}, 
{"source": "地球科学-论文","target": "CMIP5概述和实验设计"}, 
{"source": "地球科学-论文","target": "基于热带降雨测量卫星的多卫星降水分析：细尺度上类全球性的多年的组合传感器降水分析"}, 
{"source": "地球科学-论文","target": "INTCAL13和MARINE13放射性碳年龄校准曲线，0-50,000年的年龄"}, 
{"source": "地球科学-论文","target": "温暖和春季的提前到来增加了美国西部的森林火灾"}, 
{"source": "地球科学-论文","target": "人类的安全操作空间"},
{"source": "地球科学-论文","target": "更新的KOPPEN-GEIGER气候分类世界地图"},
{"source": "地球科学-论文","target": "气候变化研究的一个新纪元——世界气候研究计划CMIP3多模型数据集"},
{"source": "地球科学-论文","target": "改进的美国国家海洋和大气管理局1880-2006年陆地海洋表明温度分析"},
{"source": "地球科学-论文","target": "气候变化研究和评估的新时代场景"}, 
{"source": "地球科学-论文","target": "美国国家航空和航天局研究和应用的现代史回顾性分析"}, 
{"source": "地球科学-论文","target": "夹带起沙治理的一个新的垂直扩展包"}, 
{"source": "地球科学-论文","target": "社区气候系统模型版本3"}, 
{"source": "地球科学-论文","target": "KOPPEN-GEIGER气候分类世界地图的更新"}, 
{"source": "地球科学-论文","target": "全球变暖的水循环对策"}, 
{"source": "地球科学-论文","target": "基于大自然中气体和气雾剂排放模型的全球陆地异戊二烯排放的估计"},
{"source": "地球科学-论文","target": "气候碳循环反馈分析：来自于(CMIP)-M-4模型比较的结果"},
{"source": "地球科学-论文","target": "北美区域性再分析"},
{"source": "地球科学-论文","target": "美国国家环境预报中心气候森林系统再分析"}
],
categories: [{
            'name':'ERA-INTERIM再分析：数据协调系统的配置和性能' 
          },{
            'name':'INTCAL09和MARINE09放射性碳年龄校准曲线，0-50,000年的年龄'
          },{
            'name':'CMIP5概述和实验设计'
          },{
            'name':'基于热带降雨测量卫星的多卫星降水分析：细尺度上类全球性的多年的组合传感器降水分析'
          },{
            'name':'INTCAL13和MARINE13放射性碳年龄校准曲线，0-50,000年的年龄'
          },{
            'name':'温暖和春季的提前到来增加了美国西部的森林火灾'
          },{
            'name':'人类的安全操作空间'
          },{
            'name':'更新的KOPPEN-GEIGER气候分类世界地图'
          },{
            'name':'气候变化研究的一个新纪元——世界气候研究计划CMIP3多模型数据集'
          },{
            'name':'改进的美国国家海洋和大气管理局1880-2006年陆地海洋表明温度分析'
          },{
            'name':'气候变化研究和评估的新时代场景'
          },{
            'name':'美国国家航空和航天局研究和应用的现代史回顾性分析'
          },{
            'name':'夹带起沙治理的一个新的垂直扩展包'
          },{
            'name':'社区气候系统模型版本3'
          },{
            'name':'KOPPEN-GEIGER气候分类世界地图的更新'
          },{
          	'name':'全球变暖的水循环对策'
          },{
            'name':'基于大自然中气体和气雾剂排放模型的全球陆地异戊二烯排放的估计'
          },{
            'name':'气候碳循环反馈分析：来自于(CMIP)-M-4模型比较的结果'
          },{
            'name':'北美区域性再分析'
          },{
            'name':'美国国家环境预报中心气候森林系统再分析'
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