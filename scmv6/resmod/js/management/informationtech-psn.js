infoTechPsn=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "信息科学-人员",
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
            data: ['信息科学-人员']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '信息科学-人员',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "信息科学-人员","value": 6,"symbolSize": 18,"category": "信息科学-人员","draggable": "true"},
              {"name": "V Venkatesh","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "H Chen","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "MK Sein","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "SB MacKenzie","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "PM Leonardi","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "S Gregor","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "HJ Smith","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "CM Ringle","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "D Gefen","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "S Mithas","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "F Bélanger","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "PP Tallon","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2},
              {"name": "S Elliot","symbolSize": 3,"category": "信息科学-人员","draggable": "true","value": 2}],
links:[{"source": "信息科学-人员","target": "V Venkatesh"}, 
       {"source": "信息科学-人员","target": "H Chen"}, 
       {"source": "信息科学-人员","target": "MK Sein"}, 
       {"source": "信息科学-人员","target": "SB MacKenzie"}, 
       {"source": "信息科学-人员","target": "PM Leonardi"}, 
       {"source": "信息科学-人员","target": "S Gregor"}, 
       {"source": "信息科学-人员","target": "V Venkatesh"},
       {"source": "信息科学-人员","target": "HJ Smith"},
       {"source": "信息科学-人员","target": "CM Ringle"},
       {"source": "信息科学-人员","target": "D Gefen"},
       {"source": "信息科学-人员","target": "S Mithas"}, 
       {"source": "信息科学-人员","target": "F Bélanger"}, 
       {"source": "信息科学-人员","target": "PP Tallon"}, 
       {"source": "信息科学-人员","target": "S Elliot"}],
       
   categories: [{'name':'V Venkatesh'},
                {'name':'H Chen'},
                {'name':'MK Sein'},
                {'name':'SB MacKenzie'},
                {'name':'PM Leonardi'},
                {'name':'S Gregor'},
                {'name':'V Venkatesh'},
                {'name':'HJ Smith'},
                {'name':'CM Ringle'},
                {'name':'D Gefen'},
                {'name':'S Mithas'},
                {'name':'F Bélanger'},
                {'name':'PP Tallon'},
                {'name':'S Elliot'}],
 
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