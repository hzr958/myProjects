
var typeConst={//画图的js名称枚举
		BAR:"bar",
		CHORD:"chord",
		EVENTRIVER:"eventRiver",
		FORCE:"force",
		FUNNEL:"funnel",
		GAUGE:"gauge",
		HEATMAP:"heatmap",
		K:"k",
		LINE:"line",
		MAP:"map",
		PIE:"pie",
		RADAR:"radar",
		SCATTER:"scatter",
		TREE:"tree",
		TREEMAP:"treemap",
		VENN:"venn",
		WORDCLOUD:"wordCloud"
};
var Mydraw = Mydraw || function(){};
Mydraw._instance=null;
Mydraw.initpath = function(){// 路径配置
	require.config({
		paths: {
			echarts: '/resmod/js_v5/echarts'
		}
	});
};
/**
 * type: 传typeConst枚举
 * option：图形的option参数，是对应的图片设置参数
 * domIdStr：画图的节点
 */
Mydraw.draw = function(type,option,domIdStr){//静态方法
	if(!Mydraw._instance){//配置路径只执行一次
		Mydraw.initpath();
		Mydraw._instance={};
	}
	require(
			[
				'echarts',
				'echarts/chart/'+type // 使用柱状图就加载bar模块，按需加载
			],
			function (ec) {
				// 基于准备好的dom，初始化echarts图表
				var myChart = ec.init(document.getElementById(domIdStr));         
				// 为echarts对象加载数据 
				myChart.setOption(option); 
			}
	);
}

