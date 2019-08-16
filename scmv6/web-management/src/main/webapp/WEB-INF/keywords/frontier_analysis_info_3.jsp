<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="resmod" value="/resmod" />
<title>前沿分析</title>
<script src="${resmod}/js/clusteringTest2.js"></script>
<script src="${resmod}/js/echarts.min.js"></script>
<script type="text/javascript">
var mId=${mId};//左侧菜单Id
var titleData = "化学科学";
var bottomData = ['高分子化学', '超分子化学', '化学工程与技术','物理化学','有机化学','电化学'];
var categoryData = [{'name':'高分子化学'},
{'name':'超分子化学'},
{'name':'化学工程与技术'},
{'name':'物理化学'},
{'name':'有机化学'},
{'name':'电化学'}];

var chartData =[{"name": "化学科学","value": 27,"symbolSize": 20,"draggable": "true"},
{"name": "高分子化学","symbolSize": 13,"category": "高分子化学","draggable": "TRUE","value":4},
{"name": "超分子化学","symbolSize": 13,"category": "超分子化学","draggable": "TRUE","value":4},
{"name": "化学工程与技术","symbolSize": 13,"category": "化学工程与技术","draggable": "TRUE","value":4},
{"name": "物理化学","symbolSize": 13,"category": "物理化学","draggable": "TRUE","value":4},
{"name": "有机化学","symbolSize": 13,"category": "有机化学","draggable": "TRUE","value":4},
{"name": "电化学","symbolSize": 13,"category": "电化学","draggable": "TRUE","value":4},
{"name": "高分子材料","symbolSize": 3,"category": "高分子化学","draggable": "TRUE","value":2},
{"name": "聚合物","symbolSize": 3,"category": "高分子化学","draggable": "TRUE","value":2},
{"name": "高聚物","symbolSize": 3,"category": "高分子化学","draggable": "TRUE","value":2},
{"name": "离子交换与吸附","symbolSize": 3,"category": "高分子化学","draggable": "TRUE","value":2},
{"name": "高分子化合物","symbolSize": 3,"category": "高分子化学","draggable": "TRUE","value":2},
{"name": "自由基聚合","symbolSize": 3,"category": "高分子化学","draggable": "TRUE","value":2},
{"name": "吸附分离","symbolSize": 3,"category": "高分子化学","draggable": "TRUE","value":2},
{"name": "配位聚合物","symbolSize": 3,"category": "超分子化学","draggable": "TRUE","value":2},
{"name": "晶体结构","symbolSize": 3,"category": "超分子化学","draggable": "TRUE","value":2},
{"name": "杯芳烃","symbolSize": 3,"category": "超分子化学","draggable": "TRUE","value":2},
{"name": "环糊精","symbolSize": 3,"category": "超分子化学","draggable": "TRUE","value":2},
{"name": "自组装","symbolSize": 3,"category": "超分子化学","draggable": "TRUE","value":2},
{"name": "分子识别","symbolSize": 3,"category": "超分子化学","draggable": "TRUE","value":2},
{"name": "配位化学","symbolSize": 3,"category": "超分子化学","draggable": "TRUE","value":2},
{"name": "热稳定性","symbolSize": 3,"category": "化学工程与技术","draggable": "TRUE","value":2},
{"name": "表面活性剂","symbolSize": 3,"category": "化学工程与技术","draggable": "TRUE","value":2},
{"name": "表面张力","symbolSize": 3,"category": "化学工程与技术","draggable": "TRUE","value":2},
{"name": "萃取剂","symbolSize": 3,"category": "化学工程与技术","draggable": "TRUE","value":2},
{"name": "新材料","symbolSize": 3,"category": "化学工程与技术","draggable": "TRUE","value":2},
{"name": "离子液体","symbolSize": 3,"category": "物理化学","draggable": "TRUE","value":2},
{"name": "水溶液","symbolSize": 3,"category": "物理化学","draggable": "TRUE","value":2},
{"name": "物理化学特性","symbolSize": 3,"category": "物理化学","draggable": "TRUE","value":2},
{"name": "定量构效","symbolSize": 3,"category": "物理化学","draggable": "TRUE","value":2},
{"name": "分离纯化","symbolSize": 3,"category": "物理化学","draggable": "TRUE","value":2},
{"name": "纳米材料","symbolSize": 3,"category": "物理化学","draggable": "TRUE","value":2},
{"name": "加成反应","symbolSize": 3,"category": "有机化学","draggable": "TRUE","value":2},
{"name": "取代反应","symbolSize": 3,"category": "有机化学","draggable": "TRUE","value":2},
{"name": "有机化合物","symbolSize": 3,"category": "有机化学","draggable": "TRUE","value":2},
{"name": "有机溶剂","symbolSize": 3,"category": "有机化学","draggable": "TRUE","value":2},
{"name": "催化活性","symbolSize": 3,"category": "有机化学","draggable": "TRUE","value":2},
{"name": "光谱电化学","symbolSize": 3,"category": "电化学","draggable": "TRUE","value":2},
{"name": "修饰电极","symbolSize": 3,"category": "电化学","draggable": "TRUE","value":2},
{"name": "光电化学","symbolSize": 3,"category": "电化学","draggable": "TRUE","value":2},
{"name": "循环伏安","symbolSize": 3,"category": "电化学","draggable": "TRUE","value":2},
{"name": "导电聚合物","symbolSize": 3,"category": "电化学","draggable": "TRUE","value":2}];

	var linkData =[{"source": "化学科学","target": "高分子化学"},
{"source": "化学科学","target": "超分子化学"},
{"source": "化学科学","target": "化学工程与技术"},
{"source": "化学科学","target": "物理化学"},
{"source": "化学科学","target": "有机化学"},
{"source": "化学科学","target": "电化学"},
{"source": "高分子化学","target": "高分子材料"},
{"source": "高分子化学","target": "聚合物"},
{"source": "高分子化学","target": "高聚物"},
{"source": "高分子化学","target": "离子交换与吸附"},
{"source": "高分子化学","target": "高分子化合物"},
{"source": "高分子化学","target": "自由基聚合"},
{"source": "高分子化学","target": "吸附分离"},
{"source": "超分子化学","target": "配位聚合物"},
{"source": "超分子化学","target": "晶体结构"},
{"source": "超分子化学","target": "杯芳烃"},
{"source": "超分子化学","target": "环糊精"},
{"source": "超分子化学","target": "自组装"},
{"source": "超分子化学","target": "分子识别"},
{"source": "超分子化学","target": "配位化学"},
{"source": "化学工程与技术","target": "热稳定性"},
{"source": "化学工程与技术","target": "表面活性剂"},
{"source": "化学工程与技术","target": "表面张力"},
{"source": "化学工程与技术","target": "萃取剂"},
{"source": "化学工程与技术","target": "新材料"},
{"source": "物理化学","target": "离子液体"},
{"source": "物理化学","target": "水溶液"},
{"source": "物理化学","target": "物理化学特性"},
{"source": "物理化学","target": "定量构效"},
{"source": "物理化学","target": "分离纯化"},
{"source": "物理化学","target": "纳米材料"},
{"source": "有机化学","target": "加成反应"},
{"source": "有机化学","target": "取代反应"},
{"source": "有机化学","target": "有机化合物"},
{"source": "有机化学","target": "有机溶剂"},
{"source": "有机化学","target": "催化活性"},
{"source": "电化学","target": "光谱电化学"},
{"source": "电化学","target": "修饰电极"},
{"source": "电化学","target": "光电化学"},
{"source": "电化学","target": "循环伏安"},
{"source": "电化学","target": "导电聚合物"}];
                
$(document).ready(function() {
	clusterchart('main4',1,bottomData,categoryData,linkData,chartData,titleData);
});

</script>
