chemistryIns=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "化学科学-机构",
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
            data: ['化学科学-机构']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '化学科学-机构',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "化学科学-机构","value": 6,"symbolSize": 18,"category": "化学科学-机构","draggable": "true"},
              {"name": "UNIV GOTTINGEN","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "UNIV MINNESOTA","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "LAWRENCE BERKELEY NATL LAB","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "MAX PLANCK SOCIETY","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "NORTHWESTERN UNIV","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "DEPT CHEM","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "CNRS","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "UNIV MUNSTER","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "JST","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "DALIAN UNIV TECHNOL","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "UNIV TEXAS AUSTIN","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "JOHANNES KEPLER UNIV LINZ","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "NATL CHIAO TUNG UNIV","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "TOKYO UNIV SCI","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "CSIC","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "TEXAS A&M UNIV COLLEGE STN","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "SAPIENZA UNIV ROME","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "UNIV MICHIGAN","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2},
              {"name": "SCRIPPS RES INST","symbolSize": 3,"category": "化学科学-机构","draggable": "true","value": 2}],
              
        links:[{"source": "化学科学-机构","target": "UNIV GOTTINGEN"}, 
               {"source": "化学科学-机构","target": "UNIV MINNESOTA"}, 
               {"source": "化学科学-机构","target": "LAWRENCE BERKELEY NATL LAB"}, 
               {"source": "化学科学-机构","target": "MAX PLANCK SOCIETY"}, 
               {"source": "化学科学-机构","target": "NORTHWESTERN UNIV"}, 
               {"source": "化学科学-机构","target": "DEPT CHEM"},
               {"source": "化学科学-机构","target": "CNRS"},
               {"source": "化学科学-机构","target": "UNIV MUNSTER"},
               {"source": "化学科学-机构","target": "JST"},
               {"source": "化学科学-机构","target": "DALIAN UNIV TECHNOL"}, 
               {"source": "化学科学-机构","target": "UNIV TEXAS AUSTIN"}, 
               {"source": "化学科学-机构","target": "JOHANNES KEPLER UNIV LINZ"}, 
               {"source": "化学科学-机构","target": "NATL CHIAO TUNG UNIV"}, 
               {"source": "化学科学-机构","target": "TOKYO UNIV SCI"}, 
               {"source": "化学科学-机构","target": "CSIC"}, 
               {"source": "化学科学-机构","target": "TEXAS A&M UNIV COLLEGE STN"},
               {"source": "化学科学-机构","target": "SAPIENZA UNIV ROME"},
               {"source": "化学科学-机构","target": "UNIV MICHIGAN"},
               {"source": "化学科学-机构","target": "SCRIPPS RES INST"}],
       
        categories: [{'name':'UNIV GOTTINGEN'},
                     {'name':'UNIV MINNESOTA'},
                     {'name':'LAWRENCE BERKELEY NATL LAB'},
                     {'name':'MAX PLANCK SOCIETY'},
                     {'name':'NORTHWESTERN UNIV'},
                     {'name':'DEPT CHEM'},
                     {'name':'CNRS'},
                     {'name':'UNIV MUNSTER'},
                     {'name':'JST'},
                     {'name':'DALIAN UNIV TECHNOL'},
                     {'name':'UNIV TEXAS AUSTIN'},
                     {'name':'JOHANNES KEPLER UNIV LINZ'},
                     {'name':'NATL CHIAO TUNG UNIV'},
                     {'name':'TOKYO UNIV SCI'},
                     {'name':'CSIC'},
                     {'name':'TEXAS A&M UNIV COLLEGE STN'},
                     {'name':'SAPIENZA UNIV ROME'},
                     {'name':'UNIV MICHIGAN'},
                     {'name':'SCRIPPS RES INST'}],
 
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