<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="resmod" value="/resmod" />
<title>前沿分析</title>
<script src="${resmod}/js/clusteringTest2.js"></script>
<script src="${resmod}/js/echarts.min.js"></script>
<script type="text/javascript">
var mId=${mId};//左侧菜单Id
var titleData = "工材科学";
var bottomData = ['机械工程', '建筑学', '金属材料','高分子材料','复合材料'];
var categoryData = [{'name':'机械工程'},
		{'name':'建筑学'},
		{'name':'金属材料'},
		{'name':'高分子材料'},
		{'name':'复合材料'}];

var chartData =[{"name": "工材科学","value": 27,"symbolSize": 20,"draggable": "true"},
			{"name": "机械工程","symbolSize": 18,"category": "机械工程","draggable": "TRUE","value":6},
			{"name": "建筑学","symbolSize": 18,"category": "建筑学","draggable": "TRUE","value":6},
			{"name": "金属材料","symbolSize": 18,"category": "金属材料","draggable": "TRUE","value":6},
			{"name": "高分子材料","symbolSize": 18,"category": "高分子材料","draggable": "TRUE","value":6},
			{"name": "复合材料","symbolSize": 18,"category": "复合材料","draggable": "TRUE","value":6},
			{"name": "机械加工","symbolSize": 3,"category": "机械工程","draggable": "TRUE","value":2},
			{"name": "机械制造基础","symbolSize": 3,"category": "机械工程","draggable": "TRUE","value":2},
			{"name": "耐磨性","symbolSize": 3,"category": "机械工程","draggable": "TRUE","value":2},
			{"name": "制造工艺","symbolSize": 3,"category": "机械工程","draggable": "TRUE","value":2},
			{"name": "数控机床","symbolSize": 3,"category": "机械工程","draggable": "TRUE","value":2},
			{"name": "机械设计","symbolSize": 3,"category": "机械工程","draggable": "TRUE","value":2},
			{"name": "机械自动化","symbolSize": 3,"category": "机械工程","draggable": "TRUE","value":2},
			{"name": "柔性制造系统","symbolSize": 3,"category": "机械工程","draggable": "TRUE","value":2},
			{"name": "材料检测","symbolSize": 3,"category": "建筑学","draggable": "TRUE","value":2},
			{"name": "质量控制","symbolSize": 3,"category": "建筑学","draggable": "TRUE","value":2},
			{"name": "纤维增强塑料","symbolSize": 3,"category": "建筑学","draggable": "TRUE","value":2},
			{"name": "分层防护","symbolSize": 3,"category": "建筑学","draggable": "TRUE","value":2},
			{"name": "新型建筑材料","symbolSize": 3,"category": "建筑学","draggable": "TRUE","value":2},
			{"name": "钛合金","symbolSize": 3,"category": "金属材料","draggable": "TRUE","value":2},
			{"name": "机械性能","symbolSize": 3,"category": "金属材料","draggable": "TRUE","value":2},
			{"name": "热处理","symbolSize": 3,"category": "金属材料","draggable": "TRUE","value":2},
			{"name": "金属基复合材料","symbolSize": 3,"category": "金属材料","draggable": "TRUE","value":2},
			{"name": "疲劳强度","symbolSize": 3,"category": "金属材料","draggable": "TRUE","value":2},
			{"name": "功能高分子材料","symbolSize": 3,"category": "高分子材料","draggable": "TRUE","value":2},
			{"name": "高聚物","symbolSize": 3,"category": "高分子材料","draggable": "TRUE","value":2},
			{"name": "聚合物","symbolSize": 3,"category": "高分子材料","draggable": "TRUE","value":2},
			{"name": "生物材料","symbolSize": 3,"category": "高分子材料","draggable": "TRUE","value":2},
			{"name": "碳纤维","symbolSize": 3,"category": "高分子材料","draggable": "TRUE","value":2},
			{"name": "导电高分子","symbolSize": 3,"category": "高分子材料","draggable": "TRUE","value":2},
			{"name": "玻璃纤维","symbolSize": 3,"category": "复合材料","draggable": "TRUE","value":2},
			{"name": "纳米复合材料","symbolSize": 3,"category": "复合材料","draggable": "TRUE","value":2},
			{"name": "环氧树脂","symbolSize": 3,"category": "复合材料","draggable": "TRUE","value":2},
			{"name": "冲击损伤","symbolSize": 3,"category": "复合材料","draggable": "TRUE","value":2},
			{"name": "聚丙烯","symbolSize": 3,"category": "复合材料","draggable": "TRUE","value":2},
			{"name": "结构健康监测","symbolSize": 3,"category": "复合材料","draggable": "TRUE","value":2}];

	var linkData =[{"source": "工材科学","target": "机械工程"},
			{"source": "工材科学","target": "建筑学"},
			{"source": "工材科学","target": "金属材料"},
			{"source": "工材科学","target": "高分子材料"},
			{"source": "工材科学","target": "复合材料"},
			{"source": "机械工程","target": "机械加工"},
			{"source": "机械工程","target": "机械制造基础"},
			{"source": "机械工程","target": "耐磨性"},
			{"source": "机械工程","target": "制造工艺"},
			{"source": "机械工程","target": "数控机床"},
			{"source": "机械工程","target": "机械设计"},
			{"source": "机械工程","target": "机械自动化"},
			{"source": "机械工程","target": "柔性制造系统"},
			{"source": "建筑学","target": "材料检测"},
			{"source": "建筑学","target": "质量控制"},
			{"source": "建筑学","target": "纤维增强塑料"},
			{"source": "建筑学","target": "分层防护"},
			{"source": "建筑学","target": "新型建筑材料"},
			{"source": "金属材料","target": "钛合金"},
			{"source": "金属材料","target": "机械性能"},
			{"source": "金属材料","target": "热处理"},
			{"source": "金属材料","target": "金属基复合材料"},
			{"source": "金属材料","target": "疲劳强度"},
			{"source": "高分子材料","target": "功能高分子材料"},
			{"source": "高分子材料","target": "高聚物"},
			{"source": "高分子材料","target": "聚合物"},
			{"source": "高分子材料","target": "生物材料"},
			{"source": "高分子材料","target": "碳纤维"},
			{"source": "高分子材料","target": "导电高分子"},
			{"source": "复合材料","target": "玻璃纤维"},
			{"source": "复合材料","target": "纳米复合材料"},
			{"source": "复合材料","target": "环氧树脂"},
			{"source": "复合材料","target": "冲击损伤"},
			{"source": "复合材料","target": "聚丙烯"},
			{"source": "复合材料","target": "结构健康监测"}];
                
$(document).ready(function() {
	clusterchart('main4',1,bottomData,categoryData,linkData,chartData,titleData);
});

</script>
