medicinePsn=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "医学科学-人员",
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
            data: ['医学科学-人员']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '医学科学-人员',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "医学科学-人员","value": 6,"symbolSize": 18,"category": "医学科学-人员","draggable": "true"},
              {"name": "JEMAL, A","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "SIEGEL, R","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "FERLAY, J","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "OGDEN, CL","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "EISENHAUER, EA","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "HOLICK, MF","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "LEVEY, AS","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "HODI, FS","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "LLOVET, JM","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "BUSTIN, SA","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "ESQUELA-KERSCHER, A","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "CONNOLLY, SJ","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "NEL, A","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "CALIN, GA","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "SIEGEL, RL","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "ALBERTI, KGMM","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "BRENNER, DJ","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "MOK, TS","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "MANTOVANI, A","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2},
              {"name": "SANDLER, A","symbolSize": 3,"category": "医学科学-人员","draggable": "true","value": 2}],
links:[{"source": "医学科学-人员","target": "JEMAL, A"}, 
       {"source": "医学科学-人员","target": "SIEGEL, R"}, 
       {"source": "医学科学-人员","target": "FERLAY, J"}, 
       {"source": "医学科学-人员","target": "OGDEN, CL"}, 
       {"source": "医学科学-人员","target": "EISENHAUER, EA"}, 
       {"source": "医学科学-人员","target": "HOLICK, MF"}, 
       {"source": "医学科学-人员","target": "LEVEY, AS"},
       {"source": "医学科学-人员","target": "HODI, FS"},
       {"source": "医学科学-人员","target": "LLOVET, JM"},
       {"source": "医学科学-人员","target": "BUSTIN, SA"},
       {"source": "医学科学-人员","target": "ESQUELA-KERSCHER, A"}, 
       {"source": "医学科学-人员","target": "CONNOLLY, SJ"}, 
       {"source": "医学科学-人员","target": "NEL, A"}, 
       {"source": "医学科学-人员","target": "CALIN, GA"}, 
       {"source": "医学科学-人员","target": "SIEGEL, RL"}, 
       {"source": "医学科学-人员","target": "ALBERTI, KGMM"}, 
       {"source": "医学科学-人员","target": "BRENNER, DJ"},
       {"source": "医学科学-人员","target": "MOK, TS"},
       {"source": "医学科学-人员","target": "MANTOVANI, A"},
       {"source": "医学科学-人员","target": "SANDLER, A"}],
       
   categories: [{'name':'JEMAL, A'},
                {'name':'SIEGEL, R'},
                {'name':'FERLAY, J'},
                {'name':'OGDEN, CL'},
                {'name':'EISENHAUER, EA'},
                {'name':'HOLICK, MF'},
                {'name':'LEVEY, AS'},
                {'name':'HODI, FS'},
                {'name':'LLOVET, JM'},
                {'name':'BUSTIN, SA'},
                {'name':'ESQUELA-KERSCHER, A'},
                {'name':'CONNOLLY, SJ'},
                {'name':'NEL, A'},
                {'name':'CALIN, GA'},
                {'name':'SIEGEL, RL'},
                {'name':'ALBERTI, KGMM'},
                {'name':'BRENNER, DJ'},
                {'name':'MOK, TS'},
                {'name':'MANTOVANI, A'},
                {'name':'SANDLER, A'}],
 
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