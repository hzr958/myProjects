mathIns=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "数理科学-机构",
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
            data: ['数理科学-机构']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '数理科学-机构',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "数理科学-机构","value": 6,"symbolSize": 18,"category": "数理科学-机构","draggable": "true"},
              {"name": "NOVARTIS","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "CARNEGIE MELLON UNIV","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "CALTECH","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "UNIV MINNESOTA","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "GEORGIA INST TECHNOL","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "DANA FARBER CANC CTR","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "SANDIA NATL LAB","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "UNIV BATH","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "SWISS FED INST TECHNOL ZURICH","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "STANFORD UNIV","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "UNIV CALIF SYSTEM","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "UNIV ERLANGEN NUREMBERG","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "UNIV AUCKLAND","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "UNIV WASHINGTON","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "NA-CTR RECH ECON & STAT","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "ERCIYES UNIV","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "JOHNS HOPKINS UNIV","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "STATE UNIV SYS FLORIDA","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "NEW YORK UNIV","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2},
              {"name": "MIT","symbolSize": 3,"category": "数理科学-机构","draggable": "true","value": 2}],
links:[{"source": "数理科学-机构","target": "NOVARTIS"}, 
       {"source": "数理科学-机构","target": "CARNEGIE MELLON UNIV"}, 
       {"source": "数理科学-机构","target": "CALTECH"}, 
       {"source": "数理科学-机构","target": "UNIV MINNESOTA"}, 
       {"source": "数理科学-机构","target": "GEORGIA INST TECHNOL"}, 
       {"source": "数理科学-机构","target": "DANA FARBER CANC CTR"}, 
       {"source": "数理科学-机构","target": "SANDIA NATL LAB"},
       {"source": "数理科学-机构","target": "UNIV BATH"},
       {"source": "数理科学-机构","target": "SWISS FED INST TECHNOL ZURICH"},
       {"source": "数理科学-机构","target": "STANFORD UNIV"},
       {"source": "数理科学-机构","target": "UNIV CALIF SYSTEM"}, 
       {"source": "数理科学-机构","target": "UNIV ERLANGEN NUREMBERG"}, 
       {"source": "数理科学-机构","target": "UNIV AUCKLAND"}, 
       {"source": "数理科学-机构","target": "UNIV WASHINGTON"}, 
       {"source": "数理科学-机构","target": "NA-CTR RECH ECON & STAT"}, 
       {"source": "数理科学-机构","target": "ERCIYES UNIV"}, 
       {"source": "数理科学-机构","target": "JOHNS HOPKINS UNIV"},
       {"source": "数理科学-机构","target": "STATE UNIV SYS FLORIDA"},
       {"source": "数理科学-机构","target": "NEW YORK UNIV"},
       {"source": "数理科学-机构","target": "MIT"}],
       
   categories: [{'name':'NOVARTIS'},
                {'name':'CARNEGIE MELLON UNIV'},
                {'name':'CALTECH'},
                {'name':'UNIV MINNESOTA'},
                {'name':'GEORGIA INST TECHNOL'},
                {'name':'DANA FARBER CANC CTR'},
                {'name':'SANDIA NATL LAB'},
                {'name':'UNIV BATH'},
                {'name':'SWISS FED INST TECHNOL ZURICH'},
                {'name':'STANFORD UNIV'},
                {'name':'UNIV CALIF SYSTEM'},
                {'name':'UNIV ERLANGEN NUREMBERG'},
                {'name':'UNIV AUCKLAND'},
                {'name':'UNIV WASHINGTON'},
                {'name':'NA-CTR RECH ECON & STAT'},
                {'name':'ERCIYES UNIV'},
                {'name':'JOHNS HOPKINS UNIV'},
                {'name':'STATE UNIV SYS FLORIDA'},
                {'name':'NEW YORK UNIV'},
                {'name':'MIT'}],
 
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