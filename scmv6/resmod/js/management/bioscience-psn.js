bioSciPsn=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "生命科学-人员",
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
            data: ['生命科学-人员']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '生命科学-人员',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "生命科学-人员","value": 6,"symbolSize": 18,"category": "生命科学-人员","draggable": "true"},
              {"name": "TAMURA, K","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "HANAHAN, D","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "TAKAHASHI, K","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "PURCELL, S","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "SPEK, AL","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "BARTEL, DP","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "ADAMS, PD","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "LANGMEAD, B","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "EMSLEY, P","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "POSADA, D","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "YU, JY","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "ENGLER, AJ","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "AKIRA, S","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "KOUZARIDES, T","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "KABSCH, W","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "BURTON, PR","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "WINN, MD","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "YANG, ZH","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "CHEN, VB","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2},
              {"name": "DUNHAM, I","symbolSize": 3,"category": "生命科学-人员","draggable": "true","value": 2}],
links:[{"source": "生命科学-人员","target": "TAMURA, K"}, 
       {"source": "生命科学-人员","target": "HANAHAN, D"}, 
       {"source": "生命科学-人员","target": "TAKAHASHI, K"}, 
       {"source": "生命科学-人员","target": "PURCELL, S"}, 
       {"source": "生命科学-人员","target": "SPEK, AL"}, 
       {"source": "生命科学-人员","target": "BARTEL, DP"}, 
       {"source": "生命科学-人员","target": "ADAMS, PD"},
       {"source": "生命科学-人员","target": "LANGMEAD, B"},
       {"source": "生命科学-人员","target": "EMSLEY, P"},
       {"source": "生命科学-人员","target": "POSADA, D"},
       {"source": "生命科学-人员","target": "YU, JY"}, 
       {"source": "生命科学-人员","target": "ENGLER, AJ"}, 
       {"source": "生命科学-人员","target": "AKIRA, S"}, 
       {"source": "生命科学-人员","target": "KOUZARIDES, T"}, 
       {"source": "生命科学-人员","target": "KABSCH, W"}, 
       {"source": "生命科学-人员","target": "BURTON, PR"}, 
       {"source": "生命科学-人员","target": "WINN, MD"},
       {"source": "生命科学-人员","target": "YANG, ZH"},
       {"source": "生命科学-人员","target": "CHEN, VB"},
       {"source": "生命科学-人员","target": "DUNHAM, I"}],
       
   categories: [{'name':'TAMURA, K'},
                {'name':'HANAHAN, D'},
                {'name':'TAKAHASHI, K'},
                {'name':'PURCELL, S'},
                {'name':'SPEK, AL'},
                {'name':'BARTEL, DP'},
                {'name':'ADAMS, PD'},
                {'name':'LANGMEAD, B'},
                {'name':'EMSLEY, P'},
                {'name':'POSADA, D'},
                {'name':'YU, JY'},
                {'name':'ENGLER, AJ'},
                {'name':'AKIRA, S'},
                {'name':'KOUZARIDES, T'},
                {'name':'KABSCH, W'},
                {'name':'BURTON, PR'},
                {'name':'WINN, MD'},
                {'name':'YANG, ZH'},
                {'name':'CHEN, VB'},
                {'name':'DUNHAM, I'}],
 
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