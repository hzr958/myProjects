mathPub=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "数理科学-论文",
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
            data: ['数理科学-论文']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '数理科学-论文',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "数理科学-论文","value": 6,"symbolSize": 18,"category": "数理科学-论文","draggable": "true"},
              {"name": "通用参数模型里的联立推导","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "经验数据里的幂律分布","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "来自于不完全和不准确测量值的稳定信号恢复","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "自适应套素和它的预言性能","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "组变量回归的模型选择和估计","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "使用经验贝叶斯法微阵列表达数据的调整批次效应","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "张量分解和应用","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "Dantzig选择器：当P明显大于N时的统计估值","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "半参数广义线性模型的快速稳定的限制性最大似然估计和边缘似然估计","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "通过再加权的最小化提升稀疏性","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "LASSO变量选择和高维图形","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "图解LASSO的稀疏倒协方差估计","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "对于线性方程组大部分大的待定系统最小标准解决方案是最稀疏的解决方案","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "COSAMP:来自于不完全和不准确样本的迭代信号恢复","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "对于压缩感知的约束等距性和它的启示","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "无偏的递归分析：一个条件推断框架","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "多元分散体的基于距离的同质测试","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "严格合适的评分规则，预测和估计","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "通过凸优化完成精确矩阵","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2},
              {"name": "使用综合嵌入的拉普拉斯近似值的潜在高斯模型的近似贝叶斯推理","symbolSize": 3,"category": "数理科学-论文","draggable": "true","value": 2}],
links:[{"source": "数理科学-论文","target": "通用参数模型里的联立推导"}, 
       {"source": "数理科学-论文","target": "经验数据里的幂律分布"}, 
       {"source": "数理科学-论文","target": "来自于不完全和不准确测量值的稳定信号恢复"}, 
       {"source": "数理科学-论文","target": "自适应套素和它的预言性能"}, 
       {"source": "数理科学-论文","target": "组变量回归的模型选择和估计"}, 
       {"source": "数理科学-论文","target": "使用经验贝叶斯法微阵列表达数据的调整批次效应"}, 
       {"source": "数理科学-论文","target": "张量分解和应用"},
       {"source": "数理科学-论文","target": "Dantzig选择器：当P明显大于N时的统计估值"},
       {"source": "数理科学-论文","target": "半参数广义线性模型的快速稳定的限制性最大似然估计和边缘似然估计"},
       {"source": "数理科学-论文","target": "通过再加权的最小化提升稀疏性"},
       {"source": "数理科学-论文","target": "LASSO变量选择和高维图形"}, 
       {"source": "数理科学-论文","target": "图解LASSO的稀疏倒协方差估计"}, 
       {"source": "数理科学-论文","target": "对于线性方程组大部分大的待定系统最小标准解决方案是最稀疏的解决方案"}, 
       {"source": "数理科学-论文","target": "COSAMP:来自于不完全和不准确样本的迭代信号恢复"}, 
       {"source": "数理科学-论文","target": "对于压缩感知的约束等距性和它的启示"}, 
       {"source": "数理科学-论文","target": "无偏的递归分析：一个条件推断框架"}, 
       {"source": "数理科学-论文","target": "多元分散体的基于距离的同质测试"},
       {"source": "数理科学-论文","target": "严格合适的评分规则，预测和估计"},
       {"source": "数理科学-论文","target": "通过凸优化完成精确矩阵"},
       {"source": "数理科学-论文","target": "使用综合嵌入的拉普拉斯近似值的潜在高斯模型的近似贝叶斯推理"}],
       
   categories: [{'name':'通用参数模型里的联立推导'},
                {'name':'经验数据里的幂律分布'},
                {'name':'来自于不完全和不准确测量值的稳定信号恢复'},
                {'name':'自适应套素和它的预言性能'},
                {'name':'组变量回归的模型选择和估计'},
                {'name':'使用经验贝叶斯法微阵列表达数据的调整批次效应'},
                {'name':'张量分解和应用'},
                {'name':'Dantzig选择器：当P明显大于N时的统计估值'},
                {'name':'半参数广义线性模型的快速稳定的限制性最大似然估计和边缘似然估计'},
                {'name':'通过再加权的最小化提升稀疏性'},
                {'name':'LASSO变量选择和高维图形'},
                {'name':'图解LASSO的稀疏倒协方差估计'},
                {'name':'对于线性方程组大部分大的待定系统最小标准解决方案是最稀疏的解决方案'},
                {'name':'COSAMP:来自于不完全和不准确样本的迭代信号恢复'},
                {'name':'对于压缩感知的约束等距性和它的启示'},
                {'name':'无偏的递归分析：一个条件推断框架'},
                {'name':'多元分散体的基于距离的同质测试'},
                {'name':'严格合适的评分规则，预测和估计'},
                {'name':'通过凸优化完成精确矩阵'},
                {'name':'使用综合嵌入的拉普拉斯近似值的潜在高斯模型的近似贝叶斯推理'}],
 
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