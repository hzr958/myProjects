clusterchart=function(target,mId,bottomData,categoryData,linkData,chartData,titleData){
	var targetelement = target;
  //var data = passdata;
  //var titleData = "信息科学";
	var subTitle = titleData+"-Clustering";
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
       title:{
	        text: titleData,
	        subtext: subTitle,
	        top: "top",
	        left: "center"
       },
      /*tooltip: {},*/
        legend: [{  //图例组件
            tooltip: { //图例的 tooltip 配置
                show: true //是否显示提示框组件
            },
            selectedMode: 'false', //图例选择的模式，控制是否可以通过点击图例改变系列的显示状态。默认开启图例选择，可以设成 false 关闭
            bottom: 20, // 距底部的距离
            data: bottomData
        }],
        animationDuration: 3000,  //本组件的动画时长
        animationEasingUpdate: 'quinticInOut', //动画的缓动效果
        series: [{      //系列列表
            name: titleData,//'香港城市大学',
            type: 'graph',
            layout: 'force',  //力引导布局
            force: {  //力引导布局相关配置
                repulsion: 150  // 节点排斥因子范围
            },
            data:chartData,
            links:linkData,
            categories: categoryData,
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