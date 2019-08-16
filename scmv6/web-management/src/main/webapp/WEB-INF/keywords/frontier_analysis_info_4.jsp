<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="resmod" value="/resmod" />
<title>前沿分析</title>
<script src="${resmod}/js/clusteringTest2.js"></script>
<script src="${resmod}/js/echarts.min.js"></script>
<script type="text/javascript">
var mId=${mId};//左侧菜单Id
var titleData = "生命科学";
var bottomData = ['生物学', '基础医学','兽医学','水产','植物学'];
var categoryData = [{'name':'生物学'},
{'name':'基础医学'},
{'name':'兽医学'},
{'name':'水产'},
{'name':'植物学'}];

var chartData =[{"name": "生命科学","value": 27,"symbolSize": 20,"draggable": "true"},
{"name": "生物学","symbolSize": 13,"category": "生物学","draggable": "TRUE","value":3},
{"name": "基础医学","symbolSize": 13,"category": "基础医学","draggable": "TRUE","value":3},
{"name": "兽医学","symbolSize": 13,"category": "兽医学","draggable": "TRUE","value":3},
{"name": "水产","symbolSize": 13,"category": "水产","draggable": "TRUE","value":3},
{"name": "植物学","symbolSize": 13,"category": "植物学","draggable": "TRUE","value":3},
{"name": "分子生物学研究","symbolSize": 3,"category": "生物学","draggable": "TRUE","value":2},
{"name": "保护生物学","symbolSize": 3,"category": "生物学","draggable": "TRUE","value":2},
{"name": "系统生物学","symbolSize": 3,"category": "生物学","draggable": "TRUE","value":2},
{"name": "基因组","symbolSize": 3,"category": "生物学","draggable": "TRUE","value":2},
{"name": "基因表达","symbolSize": 3,"category": "生物学","draggable": "TRUE","value":2},
{"name": "繁育系统","symbolSize": 3,"category": "生物学","draggable": "TRUE","value":2},
{"name": "生物技术","symbolSize": 3,"category": "生物学","draggable": "TRUE","value":2},
{"name": "发育生物学","symbolSize": 3,"category": "生物学","draggable": "TRUE","value":2},
{"name": "生物化学","symbolSize": 3,"category": "生物学","draggable": "TRUE","value":2},
{"name": "细胞生物学","symbolSize": 3,"category": "生物学","draggable": "TRUE","value":2},
{"name": "胚胎发育","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "细胞凋亡","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "基因型","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "生长因子","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "细胞因子","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "分子生物学","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "基因突变","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "染色体","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "抑癌基因","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "单克隆抗体","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "序列分析","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "大肠杆菌","symbolSize": 3,"category": "兽医学","draggable": "TRUE","value":2},
{"name": "血清型","symbolSize": 3,"category": "兽医学","draggable": "TRUE","value":2},
{"name": "病毒粒子","symbolSize": 3,"category": "兽医学","draggable": "TRUE","value":2},
{"name": "结构蛋白","symbolSize": 3,"category": "兽医学","draggable": "TRUE","value":2},
{"name": "禽流感病毒","symbolSize": 3,"category": "兽医学","draggable": "TRUE","value":2},
{"name": "人工繁殖","symbolSize": 3,"category": "水产","draggable": "TRUE","value":2},
{"name": "渔业生物学","symbolSize": 3,"category": "水产","draggable": "TRUE","value":2},
{"name": "性腺发育","symbolSize": 3,"category": "水产","draggable": "TRUE","value":2},
{"name": "繁殖生物学","symbolSize": 3,"category": "水产","draggable": "TRUE","value":2},
{"name": "组织培养","symbolSize": 3,"category": "植物学","draggable": "TRUE","value":2},
{"name": "培养基","symbolSize": 3,"category": "植物学","draggable": "TRUE","value":2},
{"name": "快速繁殖","symbolSize": 3,"category": "植物学","draggable": "TRUE","value":2},
{"name": "植株再生","symbolSize": 3,"category": "植物学","draggable": "TRUE","value":2},
{"name": "生根培养基","symbolSize": 3,"category": "植物学","draggable": "TRUE","value":2}];

	var linkData =[{"source": "生命科学","target": "生物学"},
{"source": "生命科学","target": "基础医学"},
{"source": "生命科学","target": "兽医学"},
{"source": "生命科学","target": "水产"},
{"source": "生命科学","target": "植物学"},
{"source": "生物学","target": "分子生物学研究"},
{"source": "生物学","target": "保护生物学"},
{"source": "生物学","target": "系统生物学"},
{"source": "生物学","target": "基因组"},
{"source": "生物学","target": "基因表达"},
{"source": "生物学","target": "繁育系统"},
{"source": "生物学","target": "生物技术"},
{"source": "生物学","target": "发育生物学"},
{"source": "生物学","target": "生物化学"},
{"source": "生物学","target": "细胞生物学"},
{"source": "基础医学","target": "胚胎发育"},
{"source": "基础医学","target": "细胞凋亡"},
{"source": "基础医学","target": "基因型"},
{"source": "基础医学","target": "生长因子"},
{"source": "基础医学","target": "细胞因子"},
{"source": "基础医学","target": "分子生物学"},
{"source": "基础医学","target": "基因突变"},
{"source": "基础医学","target": "染色体"},
{"source": "基础医学","target": "抑癌基因"},
{"source": "基础医学","target": "单克隆抗体"},
{"source": "基础医学","target": "序列分析"},
{"source": "兽医学","target": "大肠杆菌"},
{"source": "兽医学","target": "血清型"},
{"source": "兽医学","target": "病毒粒子"},
{"source": "兽医学","target": "结构蛋白"},
{"source": "兽医学","target": "禽流感病毒"},
{"source": "水产","target": "人工繁殖"},
{"source": "水产","target": "渔业生物学"},
{"source": "水产","target": "性腺发育"},
{"source": "水产","target": "繁殖生物学"},
{"source": "植物学","target": "组织培养"},
{"source": "植物学","target": "培养基"},
{"source": "植物学","target": "快速繁殖"},
{"source": "植物学","target": "植株再生"},
{"source": "植物学","target": "生根培养基"}];
                
$(document).ready(function() {
	clusterchart('main4',1,bottomData,categoryData,linkData,chartData,titleData);
});

</script>
