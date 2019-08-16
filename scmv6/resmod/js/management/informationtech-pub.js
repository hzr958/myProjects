infoTechPub=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "信息科学-论文",
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
            data: ['信息科学-论文']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '信息科学-论文',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },
            
        data:[{"name": "信息科学-论文","value": 6,"symbolSize": 18,"category": "信息科学-论文","draggable": "true"},
              {"name": "信息技术的消费者接受和使用","symbolSize": 3,"category": "信息科学-论文","draggable": "true","value": 2},
              {"name": "商业智能和分析：从大数据到大影响","symbolSize": 3,"category": "信息科学-论文","draggable": "true","value": 2},
              {"name": "行为设计研究","symbolSize": 3,"category": "信息科学-论文","draggable": "true","value": 2},
              {"name": "管理信息系统和行为研究中的测量和效度","symbolSize": 3,"category": "信息科学-论文","draggable": "true","value": 2},
              {"name": "最大化影响力的定位和展示设计研究","symbolSize": 3,"category": "信息科学-论文","draggable": "true","value": 2},
              {"name": "跨接定性和定量研究的鸿沟：信息系统中的混合研究方法指导","symbolSize": 3,"category": "信息科学-论文","draggable": "true","value": 2},
              {"name": "信息隐私研究","symbolSize": 3,"category": "信息科学-论文","draggable": "true","value": 2},
              {"name": "信息管理能力怎样影响公司的表现","symbolSize": 3,"category": "信息科学-论文","draggable": "true","value": 2},
              {"name": "数字时代的隐私","symbolSize": 3,"category": "信息科学-论文","draggable": "true","value": 2},
              {"name": "数字化商业策略","symbolSize": 3,"category": "信息科学-论文","draggable": "true","value": 2}],
links:[{"source": "信息科学-论文","target": "信息技术的消费者接受和使用"}, 
       {"source": "信息科学-论文","target": "商业智能和分析：从大数据到大影响"}, 
       {"source": "信息科学-论文","target": "行为设计研究"}, 
       {"source": "信息科学-论文","target": "管理信息系统和行为研究中的测量和效度"}, 
       {"source": "信息科学-论文","target": "最大化影响力的定位和展示设计研究"}, 
       {"source": "信息科学-论文","target": "跨接定性和定量研究的鸿沟：信息系统中的混合研究方法指导"}, 
       {"source": "信息科学-论文","target": "信息隐私研究"},
       {"source": "信息科学-论文","target": "信息管理能力怎样影响公司的表现"},
       {"source": "信息科学-论文","target": "数字时代的隐私"},
       {"source": "信息科学-论文","target": "数字化商业策略"}],
       
   categories: [{'name':'信息技术的消费者接受和使用'},
                {'name':'商业智能和分析：从大数据到大影响'},
                {'name':'行为设计研究'},
                {'name':'管理信息系统和行为研究中的测量和效度'},
                {'name':'最大化影响力的定位和展示设计研究'},
                {'name':'跨接定性和定量研究的鸿沟：信息系统中的混合研究方法指导'},
                {'name':'信息隐私研究'},
                {'name':'信息管理能力怎样影响公司的表现'},
                {'name':'数字时代的隐私'},
                {'name':'数字化商业策略'}],
 
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