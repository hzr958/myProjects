ecperson=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "工材科学-人员",
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
            data: ['工材科学-人员']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '工材科学-人员',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },


data:[{"name": "工材科学-人员","value": 6,"symbolSize": 18,"category": "工材科学-人员","draggable": "true"},
{"name": "GEIM, AK","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "SIMON, P","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "LI, D","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "RADISAVLJEVIC, B","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "SCHEDIN, F","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "BAE, S","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "ATWATER, HA","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "PARK, S","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "PEER, D","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "CHAN, CK","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "ZHU, YW","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "ANKER, JN","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "SNYDER, GJ","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "WANG, QH","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "KOKUBO, T","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "SCHARBER, MC","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "LIANG, YY","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "GOODENOUGH, JB","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "HERNANDEZ, Y","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2},
{"name": "PEET, J","symbolSize": 3,"category": "工材科学-人员","draggable": "true","value": 2}],


links:[
{"source": "工材科学-人员","target": "GEIM, AK"}, 
{"source": "工材科学-人员","target": "SIMON, P"}, 
{"source": "工材科学-人员","target": "LI, D"}, 
{"source": "工材科学-人员","target": "RADISAVLJEVIC, B"}, 
{"source": "工材科学-人员","target": "SCHEDIN, F"}, 
{"source": "工材科学-人员","target": "BAE, S"}, 
{"source": "工材科学-人员","target": "ATWATER, HA"},
{"source": "工材科学-人员","target": "PARK, S"},
{"source": "工材科学-人员","target": "PEER, D"},
{"source": "工材科学-人员","target": "CHAN, CK"},
{"source": "工材科学-人员","target": "ZHU, YW"}, 
{"source": "工材科学-人员","target": "ANKER, JN"}, 
{"source": "工材科学-人员","target": "SNYDER, GJ"}, 
{"source": "工材科学-人员","target": "WANG, QH"}, 
{"source": "工材科学-人员","target": "KOKUBO, T"}, 
{"source": "工材科学-人员","target": "SCHARBER, MC"}, 
{"source": "工材科学-人员","target": "LIANG, YY"},
{"source": "工材科学-人员","target": "GOODENOUGH, JB"},
{"source": "工材科学-人员","target": "HERNANDEZ, Y"},
{"source": "工材科学-人员","target": "PEET, J"}],

categories:[
{'name': 'GEIM, AK'}, 
{'name': 'SIMON, P'}, 
{'name': 'LI, D'}, 
{'name': 'RADISAVLJEVIC, B'}, 
{'name': 'SCHEDIN, F'}, 
{'name': 'BAE, S'}, 
{'name': 'ATWATER, HA'},
{'name': 'PARK, S'},
{'name': 'PEER, D'},
{'name': 'CHAN, CK'},
{'name': 'ZHU, YW'}, 
{'name': 'ANKER, JN'}, 
{'name': 'SNYDER, GJ'}, 
{'name': 'WANG, QH'}, 
{'name': 'KOKUBO, T'}, 
{'name': 'SCHARBER, MC'}, 
{'name': 'LIANG, YY'},
{'name': 'GOODENOUGH, JB'},
{'name': 'HERNANDEZ, Y'},
{'name': 'PEET, J'}],

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