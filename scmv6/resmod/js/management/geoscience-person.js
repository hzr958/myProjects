earthPsn=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "地球科学-人员",
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
            data: ['地球科学-人员']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '地球科学-人员',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },
data:[{"name": "地球科学-人员","value": 6,"symbolSize": 18,"category": "地球科学-人员","draggable": "true"},
{"name": "DEE, DP","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "REIMER, PJ","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "TAYLOR, KE","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "HUFFMAN, GJ","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "WESTERLING, AL","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "ROCKSTROM, J","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "PEEL, MC","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "MEEHL, GA","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "SMITH, TM","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "MOSS, RH","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "RIENECKER, MM","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "HONG, SY","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "COLLINS, WD","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "KOTTEK, M","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "HELD, IM","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "GUENTHER, A","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "FRIEDLINGSTEIN, P","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "MESINGER, F","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "SAHA, S","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2},
{"name": "HALLQUIST, M","symbolSize": 3,"category": "地球科学-人员","draggable": "true","value": 2}],
links:[
{"source": "地球科学-人员","target": "DEE, DP"}, 
{"source": "地球科学-人员","target": "REIMER, PJ"}, 
{"source": "地球科学-人员","target": "TAYLOR, KE"}, 
{"source": "地球科学-人员","target": "HUFFMAN, GJ"}, 
{"source": "地球科学-人员","target": "WESTERLING, AL"}, 
{"source": "地球科学-人员","target": "ROCKSTROM, J"}, 
{"source": "地球科学-人员","target": "PEEL, MC"},
{"source": "地球科学-人员","target": "MEEHL, GA"},
{"source": "地球科学-人员","target": "SMITH, TM"},
{"source": "地球科学-人员","target": "MOSS, RH"},
{"source": "地球科学-人员","target": "RIENECKER, MM"}, 
{"source": "地球科学-人员","target": "HONG, SY"}, 
{"source": "地球科学-人员","target": "COLLINS, WD"}, 
{"source": "地球科学-人员","target": "KOTTEK, M"}, 
{"source": "地球科学-人员","target": "HELD, IM"}, 
{"source": "地球科学-人员","target": "GUENTHER, A"}, 
{"source": "地球科学-人员","target": "FRIEDLINGSTEIN, P"},
{"source": "地球科学-人员","target": "MESINGER, F"},
{"source": "地球科学-人员","target": "SAHA, S"},
{"source": "地球科学-人员","target": "HALLQUIST, M"}],

categories: [
{'name':'DEE, DP'},
{'name':'REIMER, PJ'},
{'name':'TAYLOR, KE'},
{'name':'HUFFMAN, GJ'},
{'name':'WESTERLING, AL'},
{'name':'ROCKSTROM, J'},
{'name':'PEEL, MC'},
{'name':'MEEHL, GA'},
{'name':'SMITH, TM'},
{'name':'MOSS, RH'},
{'name':'RIENECKER, MM'},
{'name':'HONG, SY'},
{'name':'COLLINS, WD'},
{'name':'KOTTEK, M'},
{'name':'HELD, IM'},
{'name':'GUENTHER, A'},
{'name':'FRIEDLINGSTEIN, P'},
{'name':'MESINGER, F'},
{'name':'SAHA, S'},
{'name':'HALLQUIST, M'}
],
 
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