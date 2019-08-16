chemistryPub=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "化学科学-论文",
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
            data: ['化学科学-论文']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '化学科学-论文',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "化学科学-论文","value": 6,"symbolSize": 18,"category": "化学科学-论文","draggable": "true"},
              {"name": "SHELX的简短历史","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "M06集团热化学，热化学动力学的非有机相互作用，激励状态和过渡元素的密度函数套件","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "使用长范围的分散度矫正进行半经验广义梯度近似型密度函数构建","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "移相器晶体软件","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "GROMACS 4：高效率，负载均衡和可扩展分子模拟算法","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "基于石墨烯的复合材料","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "通过膨胀石墨氧化物的化学还原进行基于石墨烯的纳米片合成","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "构建更好的电池","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "二氧化钛纳米材料的合成，性能，变型和应用","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "94要素H-PU的密度函数分散度矫正的一致和精确的参数化法","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "基于铁的层状超导体LA[O1-XFX]FEAS (X=0.05-0.12)","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "染料敏化太阳能电池","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "氧化石墨烯的化学过程","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "基于共轭聚合物的有机太阳能电池","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "使用基于钴(II/III)氧化还原电解质的卟啉敏化太阳能电池效率提升了12%","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "作为催化剂的金属有机框架","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "水分解的异质光催化剂材料","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "混合多孔物质的过去现在和将来","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "WSXM：一个扫描探针显微术软件和一个纳米技术工具","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2},
              {"name": "金属有机骨架中选择性气体吸附和分离","symbolSize": 3,"category": "化学科学-论文","draggable": "true","value": 2}],
        links:[{"source": "化学科学-论文","target": "SHELX的简短历史"}, 
               {"source": "化学科学-论文","target": "M06集团热化学，热化学动力学的非有机相互作用，激励状态和过渡元素的密度函数套件"}, 
               {"source": "化学科学-论文","target": "使用长范围的分散度矫正进行半经验广义梯度近似型密度函数构建"}, 
               {"source": "化学科学-论文","target": "移相器晶体软件"}, 
               {"source": "化学科学-论文","target": "GROMACS 4：高效率，负载均衡和可扩展分子模拟算法"}, 
               {"source": "化学科学-论文","target": "基于石墨烯的复合材料"}, 
               {"source": "化学科学-论文","target": "通过膨胀石墨氧化物的化学还原进行基于石墨烯的纳米片合成"},
               {"source": "化学科学-论文","target": "构建更好的电池"},
               {"source": "化学科学-论文","target": "二氧化钛纳米材料的合成，性能，变型和应用"},
               {"source": "化学科学-论文","target": "94要素H-PU的密度函数分散度矫正的一致和精确的参数化法"},
               {"source": "化学科学-论文","target": "基于铁的层状超导体LA[O1-XFX]FEAS (X=0.05-0.12)"}, 
               {"source": "化学科学-论文","target": "染料敏化太阳能电池"}, 
               {"source": "化学科学-论文","target": "氧化石墨烯的化学过程"}, 
               {"source": "化学科学-论文","target": "基于共轭聚合物的有机太阳能电池"}, 
               {"source": "化学科学-论文","target": "使用基于钴(II/III)氧化还原电解质的卟啉敏化太阳能电池效率提升了12%"}, 
               {"source": "化学科学-论文","target": "作为催化剂的金属有机框架"}, 
               {"source": "化学科学-论文","target": "水分解的异质光催化剂材料"},
               {"source": "化学科学-论文","target": "混合多孔物质的过去现在和将来"},
               {"source": "化学科学-论文","target": "WSXM：一个扫描探针显微术软件和一个纳米技术工具"},
               {"source": "化学科学-论文","target": "金属有机骨架中选择性气体吸附和分离"}],
       
        categories: [{'name':'SHELX的简短历史'},
                     {'name':'M06集团热化学，热化学动力学的非有机相互作用，激励状态和过渡元素的密度函数套件'},
                     {'name':'使用长范围的分散度矫正进行半经验广义梯度近似型密度函数构建'},
                     {'name':'移相器晶体软件'},
                     {'name':'GROMACS 4：高效率，负载均衡和可扩展分子模拟算法'},
                     {'name':'基于石墨烯的复合材料'},
                     {'name':'通过膨胀石墨氧化物的化学还原进行基于石墨烯的纳米片合成'},
                     {'name':'构建更好的电池'},
                     {'name':'二氧化钛纳米材料的合成，性能，变型和应用'},
                     {'name':'94要素H-PU的密度函数分散度矫正的一致和精确的参数化法'},
                     {'name':'基于铁的层状超导体LA[O1-XFX]FEAS (X=0.05-0.12)'},
                     {'name':'染料敏化太阳能电池'},
                     {'name':'氧化石墨烯的化学过程'},
                     {'name':'基于共轭聚合物的有机太阳能电池'},
                     {'name':'使用基于钴(II/III)氧化还原电解质的卟啉敏化太阳能电池效率提升了12%'},
                     {'name':'作为催化剂的金属有机框架'},
                     {'name':'水分解的异质光催化剂材料'},
                     {'name':'混合多孔物质的过去现在和将来'},
                     {'name':'WSXM：一个扫描探针显微术软件和一个纳米技术工具'},
                     {'name':'金属有机骨架中选择性气体吸附和分离'}],
 
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