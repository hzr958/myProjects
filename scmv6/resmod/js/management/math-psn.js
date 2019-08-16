mathPsn=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "数理科学-人员",
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
            data: ['数理科学-人员']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '数理科学-人员',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "数理科学-人员","value": 6,"symbolSize": 18,"category": "数理科学-人员","draggable": "true"},
              {"name": "HOTHORN, T","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "CLAUSET, A","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "CANDES, EJ","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "ZOU, H","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "YUAN, M","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "JOHNSON, WE","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "KOLDA, TG","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "CANDES, E","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "WOOD, SN","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "MEINSHAUSEN, N","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "FRIEDMAN, J","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "DONOHO, DL","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "NEEDELL, D","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "ANDERSON, MJ","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "GNEITING, T","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "RUE, H","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "KARABOGA, D","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "CAI, JF","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2},
              {"name": "STUART, EA","symbolSize": 3,"category": "数理科学-人员","draggable": "true","value": 2}],
links:[{"source": "数理科学-人员","target": "HOTHORN, T"}, 
       {"source": "数理科学-人员","target": "CLAUSET, A"}, 
       {"source": "数理科学-人员","target": "CANDES, EJ"}, 
       {"source": "数理科学-人员","target": "ZOU, H"}, 
       {"source": "数理科学-人员","target": "YUAN, M"}, 
       {"source": "数理科学-人员","target": "JOHNSON, WE"}, 
       {"source": "数理科学-人员","target": "KOLDA, TG"},
       {"source": "数理科学-人员","target": "CANDES, E"},
       {"source": "数理科学-人员","target": "WOOD, SN"},
       {"source": "数理科学-人员","target": "MEINSHAUSEN, N"},
       {"source": "数理科学-人员","target": "FRIEDMAN, J"}, 
       {"source": "数理科学-人员","target": "DONOHO, DL"}, 
       {"source": "数理科学-人员","target": "NEEDELL, D"}, 
       {"source": "数理科学-人员","target": "ANDERSON, MJ"}, 
       {"source": "数理科学-人员","target": "GNEITING, T"}, 
       {"source": "数理科学-人员","target": "RUE, H"},
       {"source": "数理科学-人员","target": "KARABOGA, D"},
       {"source": "数理科学-人员","target": "CAI, JF"},
       {"source": "数理科学-人员","target": "STUART, EA"}],
       
   categories: [{'name':'HOTHORN, T'},
                {'name':'CLAUSET, A'},
                {'name':'CANDES, EJ'},
                {'name':'ZOU, H'},
                {'name':'YUAN, M'},
                {'name':'JOHNSON, WE'},
                {'name':'KOLDA, TG'},
                {'name':'CANDES, E'},
                {'name':'WOOD, SN'},
                {'name':'MEINSHAUSEN, N'},
                {'name':'FRIEDMAN, J'},
                {'name':'DONOHO, DL'},
                {'name':'NEEDELL, D'},
                {'name':'ANDERSON, MJ'},
                {'name':'GNEITING, T'},
                {'name':'RUE, H'},
                {'name':'KARABOGA, D'},
                {'name':'CAI, JF'},
                {'name':'STUART, EA'}],

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