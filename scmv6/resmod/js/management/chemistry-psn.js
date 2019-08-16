chemistryPsn=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "化学科学-人员",
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
            data: ['化学科学-人员']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '化学科学-人员',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "化学科学-人员","value": 6,"symbolSize": 18,"category": "化学科学-人员","draggable": "true"},
              {"name": "SHELDRICK, GM","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "ZHAO, Y","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "MCCOY, AJ","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "HESS, B","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "STANKOVICH, S","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "ARMAND, M","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "CHEN, X","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "GRIMME, S","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "KAMIHARA, Y","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "HAGFELDT, A","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "DREYER, DR","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "GUNES, S","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "YELLA, A","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "LEE, J","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "KUDO, A","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "FEREY, G","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "HORCAS, I","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "LI, JR","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2},
              {"name": "HUBER, GW","symbolSize": 3,"category": "化学科学-人员","draggable": "true","value": 2}],
        links:[{"source": "化学科学-人员","target": "SHELDRICK, GM"}, 
               {"source": "化学科学-人员","target": "ZHAO, Y"}, 
               {"source": "化学科学-人员","target": "MCCOY, AJ"}, 
               {"source": "化学科学-人员","target": "HESS, B"}, 
               {"source": "化学科学-人员","target": "STANKOVICH, S"}, 
               {"source": "化学科学-人员","target": "ARMAND, M"},
               {"source": "化学科学-人员","target": "CHEN, X"},
               {"source": "化学科学-人员","target": "GRIMME, S"},
               {"source": "化学科学-人员","target": "KAMIHARA, Y"},
               {"source": "化学科学-人员","target": "HAGFELDT, A"}, 
               {"source": "化学科学-人员","target": "DREYER, DR"}, 
               {"source": "化学科学-人员","target": "GUNES, S"}, 
               {"source": "化学科学-人员","target": "YELLA, A"}, 
               {"source": "化学科学-人员","target": "LEE, J"}, 
               {"source": "化学科学-人员","target": "KUDO, A"}, 
               {"source": "化学科学-人员","target": "FEREY, G"},
               {"source": "化学科学-人员","target": "HORCAS, I"},
               {"source": "化学科学-人员","target": "LI, JR"},
               {"source": "化学科学-人员","target": "HUBER, GW"}],
       
        categories: [{'name':'SHELDRICK, GM'},
                     {'name':'ZHAO, Y'},
                     {'name':'GRIMME, S'},
                     {'name':'MCCOY, AJ'},
                     {'name':'HESS, B'},
                     {'name':'STANKOVICH, S'},
                     {'name':'ARMAND, M'},
                     {'name':'CHEN, X'},
                     {'name':'GRIMME, S'},
                     {'name':'KAMIHARA, Y'},
                     {'name':'HAGFELDT, A'},
                     {'name':'DREYER, DR'},
                     {'name':'GUNES, S'},
                     {'name':'YELLA, A'},
                     {'name':'LEE, J'},
                     {'name':'KUDO, A'},
                     {'name':'FEREY, G'},
                     {'name':'HORCAS, I'},
                     {'name':'LI, JR'},
                     {'name':'HUBER, GW'}],
 
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