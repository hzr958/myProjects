ecpaper=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "工材科学-论文",
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
            data: ['工材科学-论文']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '工材科学-论文',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

data:[{"name": "工材科学-论文","value": 6,"symbolSize": 18,"category": "工材科学-论文","draggable": "true"},
{"name": "石墨烯的增加","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "电化学电容器材料","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "石墨烯纳米片的可加工水分散体系","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "单层二硫化钼晶体管","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "单个气体分子吸附在石墨烯上的探测","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "30英寸石墨烯膜透明电极的卷对卷生产","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "改进的光伏器件等离子体","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "石墨烯生产的化学方法","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "作为癌症治疗新兴平台的纳米载体","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "使用纳米硅丝的高性能锂电池阳极","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "石墨烯和氧化石墨烯的合成，性能及应用","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "基于电浆纳米传感器的生物传感器","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "复杂热电材料","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "二维过渡金属硫化物的电子学和光电学","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "模拟体液在体内骨生物活性预测中有怎样的用处","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "太阳能电池体异质结中施主设计规则——10%的功率转换效率","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "有7.4%功率转换效率的聚合物太阳能电池体异质结的光明未来","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "可充电锂电池的挑战","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "石墨烯液相剥落的高产产品","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2},
{"name": "通过烷烃二硫酚处理实现窄带隙聚合物太阳能电池的效率提升","symbolSize": 3,"category": "工材科学-论文","draggable": "true","value": 2}],

links:[
{"source": "工材科学-论文","target": "石墨烯的增加"}, 
{"source": "工材科学-论文","target": "电化学电容器材料"}, 
{"source": "工材科学-论文","target": "石墨烯纳米片的可加工水分散体系"}, 
{"source": "工材科学-论文","target": "单层二硫化钼晶体管"}, 
{"source": "工材科学-论文","target": "单个气体分子吸附在石墨烯上的探测"}, 
{"source": "工材科学-论文","target": "30英寸石墨烯膜透明电极的卷对卷生产"}, 
{"source": "工材科学-论文","target": "改进的光伏器件等离子体"},
{"source": "工材科学-论文","target": "石墨烯生产的化学方法"},
{"source": "工材科学-论文","target": "作为癌症治疗新兴平台的纳米载体"},
{"source": "工材科学-论文","target": "使用纳米硅丝的高性能锂电池阳极"},
{"source": "工材科学-论文","target": "石墨烯和氧化石墨烯的合成，性能及应用"}, 
{"source": "工材科学-论文","target": "基于电浆纳米传感器的生物传感器"}, 
{"source": "工材科学-论文","target": "复杂热电材料"}, 
{"source": "工材科学-论文","target": "二维过渡金属硫化物的电子学和光电学"}, 
{"source": "工材科学-论文","target": "模拟体液在体内骨生物活性预测中有怎样的用处"}, 
{"source": "工材科学-论文","target": "太阳能电池体异质结中施主设计规则——10%的功率转换效率"}, 
{"source": "工材科学-论文","target": "有7.4%功率转换效率的聚合物太阳能电池体异质结的光明未来"},
{"source": "工材科学-论文","target": "可充电锂电池的挑战"},
{"source": "工材科学-论文","target": "石墨烯液相剥落的高产产品"},
{"source": "工材科学-论文","target": "通过烷烃二硫酚处理实现窄带隙聚合物太阳能电池的效率提升"}],

categories:[
{'name': '石墨烯的增加'}, 
{'name': '电化学电容器材料'}, 
{'name': '石墨烯纳米片的可加工水分散体系'}, 
{'name': '单层二硫化钼晶体管'}, 
{'name': '单个气体分子吸附在石墨烯上的探测'}, 
{'name': '30英寸石墨烯膜透明电极的卷对卷生产'}, 
{'name': '改进的光伏器件等离子体'},
{'name': '石墨烯生产的化学方法'},
{'name': '作为癌症治疗新兴平台的纳米载体'},
{'name': '使用纳米硅丝的高性能锂电池阳极'},
{'name': '石墨烯和氧化石墨烯的合成，性能及应用'}, 
{'name': '基于电浆纳米传感器的生物传感器'}, 
{'name': '复杂热电材料'}, 
{'name': '二维过渡金属硫化物的电子学和光电学'}, 
{'name': '模拟体液在体内骨生物活性预测中有怎样的用处'}, 
{'name': '太阳能电池体异质结中施主设计规则——10%的功率转换效率'}, 
{'name': '有7.4%功率转换效率的聚合物太阳能电池体异质结的光明未来'},
{'name': '可充电锂电池的挑战'},
{'name': '石墨烯液相剥落的高产产品'},
{'name': '通过烷烃二硫酚处理实现窄带隙聚合物太阳能电池的效率提升'}],
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