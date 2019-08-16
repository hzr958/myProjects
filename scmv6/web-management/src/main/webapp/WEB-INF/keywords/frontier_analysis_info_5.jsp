<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="resmod" value="/resmod" />
<title>前沿分析</title>
<script src="${resmod}/js/clusteringTest2.js"></script>
<script src="${resmod}/js/echarts.min.js"></script>
<script type="text/javascript">
var mId=${mId};//左侧菜单Id
var titleData = "数理科学";
var bottomData = ['数学', '物理学', '哲学','教育学','天文学'];
var categoryData = [{'name':'数学'},
{'name':'物理学'},
{'name':'哲学'},
{'name':'教育学'},
{'name':'天文学'}];

var chartData =[{"name": "数理科学","value": 27,"symbolSize": 20,"draggable": "true"},
{"name": "数学","symbolSize": 13,"category": "数学","draggable": "TRUE","value":3},
{"name": "物理学","symbolSize": 13,"category": "物理学","draggable": "TRUE","value":3},
{"name": "哲学","symbolSize": 13,"category": "哲学","draggable": "TRUE","value":3},
{"name": "教育学","symbolSize": 13,"category": "教育学","draggable": "TRUE","value":3},
{"name": "天文学","symbolSize": 13,"category": "天文学","draggable": "TRUE","value":3},
{"name": "应用数学","symbolSize": 3,"category": "数学","draggable": "TRUE","value":2},
{"name": "数学建模","symbolSize": 3,"category": "数学","draggable": "TRUE","value":2},
{"name": "数学教育","symbolSize": 3,"category": "数学","draggable": "TRUE","value":2},
{"name": "数学史","symbolSize": 3,"category": "数学","draggable": "TRUE","value":2},
{"name": "实变函数","symbolSize": 3,"category": "数学","draggable": "TRUE","value":2},
{"name": "数理统计","symbolSize": 3,"category": "数学","draggable": "TRUE","value":2},
{"name": "概率论","symbolSize": 3,"category": "数学","draggable": "TRUE","value":2},
{"name": "量子力学","symbolSize": 3,"category": "物理学","draggable": "TRUE","value":2},
{"name": "原子物理学","symbolSize": 3,"category": "物理学","draggable": "TRUE","value":2},
{"name": "热力学与统计物理","symbolSize": 3,"category": "物理学","draggable": "TRUE","value":2},
{"name": "计算物理","symbolSize": 3,"category": "物理学","draggable": "TRUE","value":2},
{"name": "引力场","symbolSize": 3,"category": "物理学","draggable": "TRUE","value":2},
{"name": "超导物理","symbolSize": 3,"category": "物理学","draggable": "TRUE","value":2},
{"name": "数理逻辑","symbolSize": 3,"category": "哲学","draggable": "TRUE","value":2},
{"name": "方法论研究","symbolSize": 3,"category": "哲学","draggable": "TRUE","value":2},
{"name": "思维科学","symbolSize": 3,"category": "哲学","draggable": "TRUE","value":2},
{"name": "理性思维","symbolSize": 3,"category": "哲学","draggable": "TRUE","value":2},
{"name": "辩证法","symbolSize": 3,"category": "哲学","draggable": "TRUE","value":2},
{"name": "认识论","symbolSize": 3,"category": "哲学","draggable": "TRUE","value":2},
{"name": "教育思想","symbolSize": 3,"category": "教育学","draggable": "TRUE","value":2},
{"name": "教育哲学","symbolSize": 3,"category": "教育学","draggable": "TRUE","value":2},
{"name": "探究过程","symbolSize": 3,"category": "教育学","draggable": "TRUE","value":2},
{"name": "科学探究","symbolSize": 3,"category": "教育学","draggable": "TRUE","value":2},
{"name": "天体物理学","symbolSize": 3,"category": "天文学","draggable": "TRUE","value":2},
{"name": "宇宙学","symbolSize": 3,"category": "天文学","draggable": "TRUE","value":2},
{"name": "天体测量","symbolSize": 3,"category": "天文学","draggable": "TRUE","value":2},
{"name": "空间天文学","symbolSize": 3,"category": "天文学","draggable": "TRUE","value":2},
{"name": "中子星","symbolSize": 3,"category": "天文学","draggable": "TRUE","value":2},
{"name": "星际分子","symbolSize": 3,"category": "天文学","draggable": "TRUE","value":2}];

	var linkData =[{"source": "数理科学","target": "数学"},
{"source": "数理科学","target": "物理学"},
{"source": "数理科学","target": "哲学"},
{"source": "数理科学","target": "天文学"},
{"source": "数理科学","target": "教育学"},
{"source": "数学","target": "应用数学"},
{"source": "数学","target": "数学建模"},
{"source": "数学","target": "数学教育"},
{"source": "数学","target": "数学史"},
{"source": "数学","target": "实变函数"},
{"source": "数学","target": "数理统计"},
{"source": "数学","target": "概率论"},
{"source": "物理学","target": "量子力学"},
{"source": "物理学","target": "原子物理学"},
{"source": "物理学","target": "热力学与统计物理"},
{"source": "物理学","target": "计算物理"},
{"source": "物理学","target": "引力场"},
{"source": "物理学","target": "超导物理"},
{"source": "哲学","target": "数理逻辑"},
{"source": "哲学","target": "方法论研究"},
{"source": "哲学","target": "思维科学"},
{"source": "哲学","target": "理性思维"},
{"source": "哲学","target": "辩证法"},
{"source": "哲学","target": "认识论"},
{"source": "教育学","target": "教育思想"},
{"source": "教育学","target": "教育哲学"},
{"source": "教育学","target": "探究过程"},
{"source": "教育学","target": "科学探究"},
{"source": "天文学","target": "天体物理学"},
{"source": "天文学","target": "宇宙学"},
{"source": "天文学","target": "天体测量"},
{"source": "天文学","target": "空间天文学"},
{"source": "天文学","target": "中子星"},
{"source": "天文学","target": "星际分子"}];
                
$(document).ready(function() {
	clusterchart('main4',1,bottomData,categoryData,linkData,chartData,titleData);
});

</script>
