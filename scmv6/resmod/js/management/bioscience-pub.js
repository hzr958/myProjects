bioSciPub=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "生命科学-论文",
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
            data: ['生命科学-论文']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '生命科学-论文',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "生命科学-论文","value": 6,"symbolSize": 18,"category": "生命科学-论文","draggable": "true"},
              {"name": "MEGA5:使用最大似然估计，最大距离和最大简约法的分子进化遗传学分析","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "MEGA4:分子进化遗传学分析软件版本4.0","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "癌症的印记：新一代","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "MEGA6:分子进化遗传学分析版本6.0","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "定义因素的来自于老鼠胚胎和成人纤维原细胞的多能干细胞感应","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "PLINK:全基因组关联分析和基于人群的连接分析工具箱","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "化学晶体学中的结构确认","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "定义因素的来自于成人纤维原细胞的多能干细胞感应","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "微RNA：目标识别和监管职能","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "PHENIX:综合性的基于python的大分子结构解决方案系统","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "人类基因组短基因序列超快的存储高效的结盟","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "基于云计算的单核苷酸多态性搜索","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "黑鸭的特性和发展","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "JMODELTEST:对于系统发生模型求平均值","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "来自于人类体细胞的诱导多能干细胞系","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "矩阵弹性指导的细胞系详述","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "病原体识别和自然免疫","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "染色体修正和它们的功能","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "实验发展规格","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2},
              {"name": "七个常见疾病的14000个案例和3000个共享控制的全基因组关联研究","symbolSize": 3,"category": "生命科学-论文","draggable": "true","value": 2}],
links:[{"source": "生命科学-论文","target": "MEGA5:使用最大似然估计，最大距离和最大简约法的分子进化遗传学分析"}, 
       {"source": "生命科学-论文","target": "MEGA4:分子进化遗传学分析软件版本4.0"}, 
       {"source": "生命科学-论文","target": "癌症的印记：新一代"}, 
       {"source": "生命科学-论文","target": "MEGA6:分子进化遗传学分析版本6.0"}, 
       {"source": "生命科学-论文","target": "定义因素的来自于老鼠胚胎和成人纤维原细胞的多能干细胞感应"}, 
       {"source": "生命科学-论文","target": "PLINK:全基因组关联分析和基于人群的连接分析工具箱"}, 
       {"source": "生命科学-论文","target": "化学晶体学中的结构确认"},
       {"source": "生命科学-论文","target": "定义因素的来自于成人纤维原细胞的多能干细胞感应"},
       {"source": "生命科学-论文","target": "微RNA：目标识别和监管职能"},
       {"source": "生命科学-论文","target": "PHENIX:综合性的基于python的大分子结构解决方案系统"},
       {"source": "生命科学-论文","target": "人类基因组短基因序列超快的存储高效的结盟"}, 
       {"source": "生命科学-论文","target": "基于云计算的单核苷酸多态性搜索"}, 
       {"source": "生命科学-论文","target": "黑鸭的特性和发展"}, 
       {"source": "生命科学-论文","target": "JMODELTEST:对于系统发生模型求平均值"}, 
       {"source": "生命科学-论文","target": "来自于人类体细胞的诱导多能干细胞系"}, 
       {"source": "生命科学-论文","target": "矩阵弹性指导的细胞系详述"}, 
       {"source": "生命科学-论文","target": "病原体识别和自然免疫"},
       {"source": "生命科学-论文","target": "染色体修正和它们的功能"},
       {"source": "生命科学-论文","target": "实验发展规格"},
       {"source": "生命科学-论文","target": "七个常见疾病的14000个案例和3000个共享控制的全基因组关联研究"}],
       
   categories: [{'name':'MEGA5:使用最大似然估计，最大距离和最大简约法的分子进化遗传学分析'},
                {'name':'MEGA4:分子进化遗传学分析软件版本4.0'},
                {'name':'癌症的印记：新一代'},
                {'name':'MEGA6:分子进化遗传学分析版本6.0'},
                {'name':'定义因素的来自于老鼠胚胎和成人纤维原细胞的多能干细胞感应'},
                {'name':'PLINK:全基因组关联分析和基于人群的连接分析工具箱'},
                {'name':'化学晶体学中的结构确认'},
                {'name':'定义因素的来自于成人纤维原细胞的多能干细胞感应'},
                {'name':'微RNA：目标识别和监管职能'},
                {'name':'PHENIX:综合性的基于python的大分子结构解决方案系统'},
                {'name':'人类基因组短基因序列超快的存储高效的结盟'},
                {'name':'基于云计算的单核苷酸多态性搜索'},
                {'name':'黑鸭的特性和发展'},
                {'name':'JMODELTEST:对于系统发生模型求平均值'},
                {'name':'来自于人类体细胞的诱导多能干细胞系'},
                {'name':'矩阵弹性指导的细胞系详述'},
                {'name':'病原体识别和自然免疫'},
                {'name':'染色体修正和它们的功能'},
                {'name':'实验发展规格'},
                {'name':'七个常见疾病的14000个案例和3000个共享控制的全基因组关联研究'}],
 
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