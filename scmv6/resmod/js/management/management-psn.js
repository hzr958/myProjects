managementPsn=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "管理科学-人员",
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
            data: ['管理科学-人员']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '管理科学-人员',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "管理科学-人员","value": 6,"symbolSize": 18,"category": "管理科学-人员","draggable": "true"},
              {"name": "S Aral","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "E Brynjolfsson","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "KJ Boudreau","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "N Archak","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "H Servaes","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "J Berkowitz","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "BL Bayus","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "KJ Singleton","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "S Erat","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "A Goldfarb","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "GP Cachon","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "RB Adams","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "M Sun","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2},
              {"name": "J Zhang","symbolSize": 3,"category": "管理科学-人员","draggable": "true","value": 2}],
        links:[{"source": "管理科学-人员","target": "S Aral"}, 
               {"source": "管理科学-人员","target": "E Brynjolfsson"}, 
               {"source": "管理科学-人员","target": "KJ Boudreau"}, 
               {"source": "管理科学-人员","target": "N Archak"}, 
               {"source": "管理科学-人员","target": "H Servaes"}, 
               {"source": "管理科学-人员","target": "J Berkowitz"}, 
               {"source": "管理科学-人员","target": "BL Bayus"},
               {"source": "管理科学-人员","target": "KJ Singleton"},
               {"source": "管理科学-人员","target": "S Erat"},
               {"source": "管理科学-人员","target": "A Goldfarb"}, 
               {"source": "管理科学-人员","target": "GP Cachon"},
               {"source": "管理科学-人员","target": "RB Adams"},
               {"source": "管理科学-人员","target": "M Sun"},
               {"source": "管理科学-人员","target": "J Zhang"}],
       
        categories: [{'name':'S Aral'},
                     {'name':'E Brynjolfsson'},
                     {'name':'KJ Boudreau'},
                     {'name':'N Archak'},
                     {'name':'H Servaes'},
                     {'name':'J Berkowitz'},
                     {'name':'BL Bayus'},
                     {'name':'KJ Singleton'},
                     {'name':'S Erat'},
                     {'name':'A Goldfarb'},
                     {'name':'GP Cachon'},
                     {'name':'RB Adams'},
                     {'name':'M Sun'},
                     {'name':'J Zhang'}],
 
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