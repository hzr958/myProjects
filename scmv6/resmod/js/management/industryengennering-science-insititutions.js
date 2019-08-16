ecmecanism=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "工材科学-机构",
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
            data: ['工材科学-机构']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '工材科学-机构',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },
data:[
{"name": "工材科学-机构","value": 6,"symbolSize": 18,"category": "工材科学-机构","draggable": "true"},
{"name": "UNIV MANCHESTER","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "CNRS","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "UNIV CALIF LOS ANGELES","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "SWISS FED INST TECHNOL LAUSANNE","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "RADBOUD UNIV NIJMEGEN","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "AIST","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "UNIV TEXAS AUSTIN","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "CHILDRENS HOSP BOSTON","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "HITACHI HIGH TECHNOL AMER INC","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "NORTHWESTERN UNIV","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "CALTECH","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "MIT","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "CHUBU UNIV","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "KONARKA AUSTRIA","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "SOLARMER ENERGY INC","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "TRINITY COLL DUBLIN","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "UNIV CALIF SANTA BARBARA","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2},
{"name": "COLUMBIA UNIV","symbolSize": 3,"category": "工材科学-机构","draggable": "true","value": 2}],
links:[
{"source": "工材科学-机构","target": "UNIV MANCHESTER"}, 
{"source": "工材科学-机构","target": "CNRS"}, 
{"source": "工材科学-机构","target": "UNIV CALIF LOS ANGELES"}, 
{"source": "工材科学-机构","target": "SWISS FED INST TECHNOL LAUSANNE"}, 
{"source": "工材科学-机构","target": "RADBOUD UNIV NIJMEGEN"}, 
{"source": "工材科学-机构","target": "AIST"}, 
{"source": "工材科学-机构","target": "UNIV TEXAS AUSTIN"},
{"source": "工材科学-机构","target": "CHILDRENS HOSP BOSTON"},
{"source": "工材科学-机构","target": "HITACHI HIGH TECHNOL AMER INC"},
{"source": "工材科学-机构","target": "NORTHWESTERN UNIV"}, 
{"source": "工材科学-机构","target": "CALTECH"}, 
{"source": "工材科学-机构","target": "MIT"}, 
{"source": "工材科学-机构","target": "CHUBU UNIV"}, 
{"source": "工材科学-机构","target": "KONARKA AUSTRIA"}, 
{"source": "工材科学-机构","target": "SOLARMER ENERGY INC"},
{"source": "工材科学-机构","target": "TRINITY COLL DUBLIN"},
{"source": "工材科学-机构","target": "UNIV CALIF SANTA BARBARA"},
{"source": "工材科学-机构","target": "COLUMBIA UNIV"}],
categories: [
{'name':'UNIV MANCHESTER'},
{'name':'CNRS'},
{'name':'UNIV CALIF LOS ANGELES'},
{'name':'SWISS FED INST TECHNOL LAUSANNE'},
{'name':'RADBOUD UNIV NIJMEGEN'},
{'name':'AIST'},
{'name':'UNIV TEXAS AUSTIN'},
{'name':'CHILDRENS HOSP BOSTON'},
{'name':'HITACHI HIGH TECHNOL AMER INC'},
{'name':'NORTHWESTERN UNIV'},
{'name':'CALTECH'},
{'name':'MIT'},
{'name':'CHUBU UNIV'},
{'name':'KONARKA AUSTRIA'},
{'name':'SOLARMER ENERGY INC'},
{'name':'TRINITY COLL DUBLIN'},
{'name':'UNIV CALIF SANTA BARBARA'},
{'name':'COLUMBIA UNIV'}
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