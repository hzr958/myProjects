<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="resmod" value="/resmod" />
<title>前沿分析</title>
<script src="${resmod}/js/clusteringTest2.js"></script>
<script src="${resmod}/js/echarts.min.js"></script>
<script type="text/javascript">
var mId=${mId};//左侧菜单Id
var titleData = "地球科学";
var bottomData = ['地质学', '地球物理学', '地理学','地质资源与地质工程','测绘科学与技术'];
var categoryData = [{'name':'地质学'},
{'name':'地球物理学'},
{'name':'地理学'},
{'name':'地质资源与地质工程'},
{'name':'测绘科学与技术'}];

var chartData =[{"name": "地球科学","value": 27,"symbolSize": 20,"draggable": "true"},
{"name": "地质学","symbolSize": 13,"category": "地质学","draggable": "TRUE","value":4},
{"name": "地球物理学","symbolSize": 13,"category": "地球物理学","draggable": "TRUE","value":4},
{"name": "地理学","symbolSize": 13,"category": "地理学","draggable": "TRUE","value":4},
{"name": "地质资源与地质工程","symbolSize": 13,"category": "地质资源与地质工程","draggable": "TRUE","value":4},
{"name": "测绘科学与技术","symbolSize": 13,"category": "测绘科学与技术","draggable": "TRUE","value":4},
{"name": "工程地质学","symbolSize": 3,"category": "地质学","draggable": "TRUE","value":2},
{"name": "构造地质学","symbolSize": 3,"category": "地质学","draggable": "TRUE","value":2},
{"name": "成矿作用","symbolSize": 3,"category": "地质学","draggable": "TRUE","value":2},
{"name": "深部过程","symbolSize": 3,"category": "地质学","draggable": "TRUE","value":2},
{"name": "矿床地质特征","symbolSize": 3,"category": "地质学","draggable": "TRUE","value":2},
{"name": "成矿流体","symbolSize": 3,"category": "地质学","draggable": "TRUE","value":2},
{"name": "斑岩铜矿","symbolSize": 3,"category": "地质学","draggable": "TRUE","value":2},
{"name": "地震学","symbolSize": 3,"category": "地球物理学","draggable": "TRUE","value":2},
{"name": "地震活动性","symbolSize": 3,"category": "地球物理学","draggable": "TRUE","value":2},
{"name": "地震预报","symbolSize": 3,"category": "地球物理学","draggable": "TRUE","value":2},
{"name": "地壳结构","symbolSize": 3,"category": "地球物理学","draggable": "TRUE","value":2},
{"name": "地震影响","symbolSize": 3,"category": "地球物理学","draggable": "TRUE","value":2},
{"name": "勘探地球物理","symbolSize": 3,"category": "地球物理学","draggable": "TRUE","value":2},
{"name": "全球环境变化","symbolSize": 3,"category": "地理学","draggable": "TRUE","value":2},
{"name": "生态系统","symbolSize": 3,"category": "地理学","draggable": "TRUE","value":2},
{"name": "生物圈","symbolSize": 3,"category": "地理学","draggable": "TRUE","value":2},
{"name": "地球信息科学","symbolSize": 3,"category": "地理学","draggable": "TRUE","value":2},
{"name": "海岸带","symbolSize": 3,"category": "地理学","draggable": "TRUE","value":2},
{"name": "数字地球","symbolSize": 3,"category": "地理学","draggable": "TRUE","value":2},
{"name": "工程地质勘查","symbolSize": 3,"category": "地质资源与地质工程","draggable": "TRUE","value":2},
{"name": "地质灾害","symbolSize": 3,"category": "地质资源与地质工程","draggable": "TRUE","value":2},
{"name": "水文地质","symbolSize": 3,"category": "地质资源与地质工程","draggable": "TRUE","value":2},
{"name": "三维地质建模","symbolSize": 3,"category": "地质资源与地质工程","draggable": "TRUE","value":2},
{"name": "地质资源","symbolSize": 3,"category": "地质资源与地质工程","draggable": "TRUE","value":2},
{"name": "矿产资源","symbolSize": 3,"category": "地质资源与地质工程","draggable": "TRUE","value":2},
{"name": "资源潜力","symbolSize": 3,"category": "地质资源与地质工程","draggable": "TRUE","value":2},
{"name": "遥感技术","symbolSize": 3,"category": "地质资源与地质工程","draggable": "TRUE","value":2},
{"name": "地理信息系统","symbolSize": 3,"category": "测绘科学与技术","draggable": "TRUE","value":2},
{"name": "对地观测系统","symbolSize": 3,"category": "测绘科学与技术","draggable": "TRUE","value":2},
{"name": "全球定位系统","symbolSize": 3,"category": "测绘科学与技术","draggable": "TRUE","value":2},
{"name": "遥感与地理信息系统","symbolSize": 3,"category": "测绘科学与技术","draggable": "TRUE","value":2},
{"name": "数字化测绘技术","symbolSize": 3,"category": "测绘科学与技术","draggable": "TRUE","value":2}];

	var linkData =[{"source": "地球科学","target": "地质学"},
{"source": "地球科学","target": "地球物理学"},
{"source": "地球科学","target": "地理学"},
{"source": "地球科学","target": "地质资源与地质工程"},
{"source": "地球科学","target": "测绘科学与技术"},
{"source": "地质学","target": "工程地质学"},
{"source": "地质学","target": "构造地质学"},
{"source": "地质学","target": "成矿作用"},
{"source": "地质学","target": "深部过程"},
{"source": "地质学","target": "矿床地质特征"},
{"source": "地质学","target": "成矿流体"},
{"source": "地质学","target": "斑岩铜矿"},
{"source": "地球物理学","target": "地震学"},
{"source": "地球物理学","target": "地震活动性"},
{"source": "地球物理学","target": "地震预报"},
{"source": "地球物理学","target": "地壳结构"},
{"source": "地球物理学","target": "地震影响"},
{"source": "地球物理学","target": "勘探地球物理"},
{"source": "地理学","target": "全球环境变化"},
{"source": "地理学","target": "生态系统"},
{"source": "地理学","target": "生物圈"},
{"source": "地理学","target": "地球信息科学"},
{"source": "地理学","target": "海岸带"},
{"source": "地理学","target": "数字地球"},
{"source": "地质资源与地质工程","target": "工程地质勘查"},
{"source": "地质资源与地质工程","target": "地质灾害"},
{"source": "地质资源与地质工程","target": "水文地质"},
{"source": "地质资源与地质工程","target": "三维地质建模"},
{"source": "地质资源与地质工程","target": "地质资源"},
{"source": "地质资源与地质工程","target": "矿产资源"},
{"source": "地质资源与地质工程","target": "资源潜力"},
{"source": "地质资源与地质工程","target": "遥感技术"},
{"source": "测绘科学与技术","target": "地理信息系统"},
{"source": "测绘科学与技术","target": "对地观测系统"},
{"source": "测绘科学与技术","target": "全球定位系统"},
{"source": "测绘科学与技术","target": "遥感与地理信息系统"},
{"source": "测绘科学与技术","target": "数字化测绘技术"}];
$(document).ready(function() {
	clusterchart('main4',1,bottomData,categoryData,linkData,chartData,titleData);
});

</script>
