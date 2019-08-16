managementPub=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "管理科学-论文",
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
            data: ['管理科学-论文']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '管理科学-论文',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "管理科学-论文","value": 6,"symbolSize": 18,"category": "管理科学-论文","draggable": "true"},
              {"name": "搜索成本对产品销售集中性的影响","symbolSize": 3,"category": "管理科学-论文","draggable": "true","value": 2},
              {"name": "通过病毒性产品设计创造社会传染,网络中同行影响力的随机试验","symbolSize": 3,"category": "管理科学-论文","draggable": "true","value": 2},
              {"name": "创新竞赛中的激励和问题不确定性","symbolSize": 3,"category": "管理科学-论文","draggable": "true","value": 2},
              {"name": "通过挖掘消费者评论得出产品特征的定价权","symbolSize": 3,"category": "管理科学-论文","draggable": "true","value": 2},
              {"name": "企业社会责任对公司价值的影响, 顾客意识的作用","symbolSize": 3,"category": "管理科学-论文","draggable": "true","value": 2},
              {"name": "风险价值法模型的评估","symbolSize": 3,"category": "管理科学-论文","draggable": "true","value": 2},
              {"name": "众包新产品想法,戴尔想法风暴社区的分析","symbolSize": 3,"category": "管理科学-论文","draggable": "true","value": 2},
              {"name": "隐私规则和在线广告","symbolSize": 3,"category": "管理科学-论文","draggable": "true","value": 2},
              {"name": "善意的谎言","symbolSize": 3,"category": "管理科学-论文","draggable": "true","value": 2},
              {"name": "快时尚价值, 快速反应，提升设计和消费者行为策略","symbolSize": 3,"category": "管理科学-论文","draggable": "true","value": 2}],
              
        links:[{"source": "管理科学-论文","target": "搜索成本对产品销售集中性的影响"},
               {"source": "管理科学-论文","target": "通过病毒性产品设计创造社会传染,网络中同行影响力的随机试验"},
               {"source": "管理科学-论文","target": "创新竞赛中的激励和问题不确定性"},
               {"source": "管理科学-论文","target": "通过挖掘消费者评论得出产品特征的定价权"},
               {"source": "管理科学-论文","target": "企业社会责任对公司价值的影响, 顾客意识的作用"},
               {"source": "管理科学-论文","target": "风险价值法模型的评估"},
               {"source": "管理科学-论文","target": "众包新产品想法,戴尔想法风暴社区的分析"},
               {"source": "管理科学-论文","target": "隐私规则和在线广告"},
               {"source": "管理科学-论文","target": "善意的谎言"},
               {"source": "管理科学-论文","target": "快时尚价值, 快速反应，提升设计和消费者行为策略"}],
       
        categories: [{'name':'搜索成本对产品销售集中性的影响'},
                     {'name':'通过病毒性产品设计创造社会传染,网络中同行影响力的随机试验'},
                     {'name':'创新竞赛中的激励和问题不确定性'},
                     {'name':'通过挖掘消费者评论得出产品特征的定价权'},
                     {'name':'企业社会责任对公司价值的影响, 顾客意识的作用'},
                     {'name':'风险价值法模型的评估'},
                     {'name':'众包新产品想法,戴尔想法风暴社区的分析'},
                     {'name':'隐私规则和在线广告'},
                     {'name':'善意的谎言'},
                     {'name':'快时尚价值, 快速反应，提升设计和消费者行为策略'}],
 
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