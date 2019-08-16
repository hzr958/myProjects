infoTechIns=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "信息科学-机构",
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
            data: ['信息科学-机构']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '信息科学-机构',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "信息科学-机构","value": 6,"symbolSize": 18,"category": "信息科学-机构","draggable": "true"},
              {"name": "Univ Michigan","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Univ Wisconsin","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Indiana Univ","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Univ Texas","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Penn State Univ","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Univ IIIinois","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "City Univ Hong Kong","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Georgia State Univ","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Univ Arizona","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Carnegie Mellon Univ","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Univ Washington","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Univ Maryland","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Nati Univ Sinapore","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2},
              {"name": "Universal N Carolina","symbolSize": 3,"category": "信息科学-机构","draggable": "true","value": 2}],
links:[{"source": "信息科学-机构","target": "Univ Michigan"}, 
       {"source": "信息科学-机构","target": "Univ Wisconsin"}, 
       {"source": "信息科学-机构","target": "Indiana Univ"}, 
       {"source": "信息科学-机构","target": "Univ Texas"}, 
       {"source": "信息科学-机构","target": "Penn State Univ"}, 
       {"source": "信息科学-机构","target": "Univ IIIinois"}, 
       {"source": "信息科学-机构","target": "City Univ Hong Kong"},
       {"source": "信息科学-机构","target": "Georgia State Univ"},
       {"source": "信息科学-机构","target": "Univ Arizona"},
       {"source": "信息科学-机构","target": "Carnegie Mellon Univ"},
       {"source": "信息科学-机构","target": "Univ Washington"}, 
       {"source": "信息科学-机构","target": "Univ Maryland"}, 
       {"source": "信息科学-机构","target": "Nati Univ Sinapore"}, 
       {"source": "信息科学-机构","target": "Universal N Carolina"}],
       
   categories: [{'name':'Univ Michigan'},
                {'name':'Univ Wisconsin'},
                {'name':'Indiana Univ'},
                {'name':'Univ Texas'},
                {'name':'Penn State Univ'},
                {'name':'Univ IIIinois'},
                {'name':'City Univ Hong Kong'},
                {'name':'Georgia State Univ'},
                {'name':'Univ Arizona'},
                {'name':'Carnegie Mellon Univ'},
                {'name':'Univ Washington'},
                {'name':'Univ Maryland'},
                {'name':'Nati Univ Sinapore'},
                {'name':'Universal N Carolina'}],
 
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