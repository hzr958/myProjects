medicinePub=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "医学科学-论文",
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
            data: ['医学科学-论文']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '医学科学-论文',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "医学科学-论文","value": 6,"symbolSize": 18,"category": "医学科学-论文","draggable": "true"},
              {"name": "全球癌症统计","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "2010年癌症统计","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "2008年癌症统计","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "2009年癌症统计","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "2012年癌症统计","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "2008年全世界癌症负担的估计","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "2013年癌症统计","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "2014年癌症统计","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "基于1999-2004年数据的美国超重和肥胖的流行","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "新的实体瘤疗效评价标准：修正的实体瘤疗效评价标准指导方针版本1.1","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "2007年癌症统计","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "维生素D缺乏症","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "估计肾小球滤过率的新方程式","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "使用易普利姆玛药的黑色素瘤转移性住院患者改进的幸存","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "用索拉非尼治疗晚期肝癌细胞","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "2006年癌症统计","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "MIQE指导方针：实时定量聚合酶链反应实验出版物的最少信息","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "ONCOMIRS——微RNA在癌症中的作用","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "心房颤动住院患者的达比加群酯药和华法令阻凝剂的对抗","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2},
              {"name": "纳米级材料的有毒可能性","symbolSize": 3,"category": "医学科学-论文","draggable": "true","value": 2}],
links:[{"source": "医学科学-论文","target": "全球癌症统计"}, 
       {"source": "医学科学-论文","target": "2010年癌症统计"}, 
       {"source": "医学科学-论文","target": "2008年癌症统计"}, 
       {"source": "医学科学-论文","target": "2009年癌症统计"}, 
       {"source": "医学科学-论文","target": "2012年癌症统计"}, 
       {"source": "医学科学-论文","target": "2008年全世界癌症负担的估计"}, 
       {"source": "医学科学-论文","target": "2013年癌症统计"},
       {"source": "医学科学-论文","target": "2014年癌症统计"},
       {"source": "医学科学-论文","target": "基于1999-2004年数据的美国超重和肥胖的流行"},
       {"source": "医学科学-论文","target": "新的实体瘤疗效评价标准：修正的实体瘤疗效评价标准指导方针版本1.1"},
       {"source": "医学科学-论文","target": "2007年癌症统计"}, 
       {"source": "医学科学-论文","target": "维生素D缺乏症"}, 
       {"source": "医学科学-论文","target": "估计肾小球滤过率的新方程式"}, 
       {"source": "医学科学-论文","target": "使用易普利姆玛药的黑色素瘤转移性住院患者改进的幸存"}, 
       {"source": "医学科学-论文","target": "用索拉非尼治疗晚期肝癌细胞"}, 
       {"source": "医学科学-论文","target": "2006年癌症统计"}, 
       {"source": "医学科学-论文","target": "MIQE指导方针：实时定量聚合酶链反应实验出版物的最少信息"},
       {"source": "医学科学-论文","target": "ONCOMIRS——微RNA在癌症中的作用"},
       {"source": "医学科学-论文","target": "心房颤动住院患者的达比加群酯药和华法令阻凝剂的对抗"},
       {"source": "医学科学-论文","target": "纳米级材料的有毒可能性"}],
       
   categories: [{'name':'全球癌症统计'},
                {'name':'2010年癌症统计'},
                {'name':'2008年癌症统计'},
                {'name':'2009年癌症统计'},
                {'name':'2012年癌症统计'},
                {'name':'2008年全世界癌症负担的估计'},
                {'name':'2013年癌症统计'},
                {'name':'2014年癌症统计'},
                {'name':'基于1999-2004年数据的美国超重和肥胖的流行'},
                {'name':'新的实体瘤疗效评价标准：修正的实体瘤疗效评价标准指导方针版本1.1'},
                {'name':'2007年癌症统计'},
                {'name':'维生素D缺乏症'},
                {'name':'估计肾小球滤过率的新方程式'},
                {'name':'使用易普利姆玛药的黑色素瘤转移性住院患者改进的幸存'},
                {'name':'用索拉非尼治疗晚期肝癌细胞'},
                {'name':'2006年癌症统计'},
                {'name':'MIQE指导方针：实时定量聚合酶链反应实验出版物的最少信息'},
                {'name':'ONCOMIRS——微RNA在癌症中的作用'},
                {'name':'心房颤动住院患者的达比加群酯药和华法令阻凝剂的对抗'},
                {'name':'纳米级材料的有毒可能性'}],
 
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