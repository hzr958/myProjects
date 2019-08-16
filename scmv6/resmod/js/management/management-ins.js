managementIns=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "管理科学-机构",
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
            data: ['管理科学-机构']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '管理科学-机构',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "管理科学-机构","value": 6,"symbolSize": 18,"category": "管理科学-机构","draggable": "true"},
              {"name": "哈佛大学","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "伦敦商学院","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "欧洲工商管理学院（法国）","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "斯坦福大学","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "宾夕法尼亚大学","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "麻省理工学院","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "新加坡国立大学","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "香港大学","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "耶鲁大学","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "清华大学","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "香港城市大学","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "香港科技大学","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "多伦多大学","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2},
              {"name": "墨尔本大学","symbolSize": 3,"category": "管理科学-机构","draggable": "true","value": 2}],
              
          links:[{"source": "管理科学-机构","target": "哈佛大学"}, 
                     {"source": "管理科学-机构","target": "伦敦商学院"}, 
                     {"source": "管理科学-机构","target": "欧洲工商管理学院（法国）"}, 
                     {"source": "管理科学-机构","target": "斯坦福大学"}, 
                     {"source": "管理科学-机构","target": "宾夕法尼亚大学"}, 
                     {"source": "管理科学-机构","target": "麻省理工学院"}, 
                     {"source": "管理科学-机构","target": "新加坡国立大学"},
                     {"source": "管理科学-机构","target": "香港大学"},
                     {"source": "管理科学-机构","target": "耶鲁大学"},
                     {"source": "管理科学-机构","target": "清华大学"},
                     {"source": "管理科学-机构","target": "香港城市大学"}, 
                     {"source": "管理科学-机构","target": "香港科技大学"}, 
                     {"source": "管理科学-机构","target": "多伦多大学"},
                     {"source": "管理科学-机构","target": "墨尔本大学"}],
       
   categories: [{'name':'哈佛大学'},
                {'name':'伦敦商学院'},
                {'name':'欧洲工商管理学院（法国）'},
                {'name':'斯坦福大学'},
                {'name':'宾夕法尼亚大学'},
                {'name':'麻省理工学院'},
                {'name':'新加坡国立大学'},
                {'name':'香港大学'},
                {'name':'耶鲁大学'},
                {'name':'清华大学'},
                {'name':'香港城市大学'},
                {'name':'香港科技大学'},
                {'name':'多伦多大学'},
                {'name':'墨尔本大学'}],
 
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