<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="resmod" value="/resmod" />
<title>前沿分析</title>
<script src="${resmod}/js/clusteringTest2.js"></script>
<script src="${resmod}/js/echarts.min.js"></script>
<script type="text/javascript">
var mId=${mId};//左侧菜单Id
var titleData = "管理科学";
var bottomData = ['工商管理', '管理科学与工程', '经济学'];
var categoryData = [{'name':'工商管理'},
{'name':'管理科学与工程'},
{'name':'经济学'}];

var chartData =[{"name": "管理科学","value": 27,"symbolSize": 20,"draggable": "true"},
{"name": "工商管理","symbolSize": 18,"category": "工商管理","draggable": "TRUE","value":3},
{"name": "管理科学与工程","symbolSize": 18,"category": "管理科学与工程","draggable": "TRUE","value":3},
{"name": "经济学","symbolSize": 18,"category": "经济学","draggable": "TRUE","value":3},
{"name": "成本管理","symbolSize": 3,"category": "工商管理","draggable": "TRUE","value":2},
{"name": "财务管理","symbolSize": 3,"category": "工商管理","draggable": "TRUE","value":2},
{"name": "现代企业","symbolSize": 3,"category": "工商管理","draggable": "TRUE","value":2},
{"name": "技术经济与技术管理","symbolSize": 3,"category": "工商管理","draggable": "TRUE","value":2},
{"name": "电子商务","symbolSize": 3,"category": "工商管理","draggable": "TRUE","value":2},
{"name": "公司治理","symbolSize": 3,"category": "工商管理","draggable": "TRUE","value":2},
{"name": "企业文化","symbolSize": 3,"category": "工商管理","draggable": "TRUE","value":2},
{"name": "科学管理","symbolSize": 3,"category": "工商管理","draggable": "TRUE","value":2},
{"name": "信息不对称","symbolSize": 3,"category": "工商管理","draggable": "TRUE","value":2},
{"name": "物流供应链","symbolSize": 3,"category": "管理科学与工程","draggable": "TRUE","value":2},
{"name": "管理信息系统","symbolSize": 3,"category": "管理科学与工程","draggable": "TRUE","value":2},
{"name": "健康信息系统","symbolSize": 3,"category": "管理科学与工程","draggable": "TRUE","value":2},
{"name": "社交网络","symbolSize": 3,"category": "管理科学与工程","draggable": "TRUE","value":2},
{"name": "大数据","symbolSize": 3,"category": "管理科学与工程","draggable": "TRUE","value":2},
{"name": "推荐系统","symbolSize": 3,"category": "管理科学与工程","draggable": "TRUE","value":2},
{"name": "信息过载","symbolSize": 3,"category": "管理科学与工程","draggable": "TRUE","value":2},
{"name": "互联网金融","symbolSize": 3,"category": "管理科学与工程","draggable": "TRUE","value":2},
{"name": "理论经济学","symbolSize": 3,"category": "经济学","draggable": "TRUE","value":2},
{"name": "应用经济学","symbolSize": 3,"category": "经济学","draggable": "TRUE","value":2},
{"name": "金融危机","symbolSize": 3,"category": "经济学","draggable": "TRUE","value":2},
{"name": "风险管理","symbolSize": 3,"category": "经济学","draggable": "TRUE","value":2},
{"name": "可持续发展","symbolSize": 3,"category": "经济学","draggable": "TRUE","value":2},
{"name": "货币政策","symbolSize": 3,"category": "经济学","draggable": "TRUE","value":2},
{"name": "制度变迁","symbolSize": 3,"category": "经济学","draggable": "TRUE","value":2},
{"name": "中小企业融资","symbolSize": 3,"category": "经济学","draggable": "TRUE","value":2},
{"name": "政策研究","symbolSize": 3,"category": "经济学","draggable": "TRUE","value":2}];

	var linkData =[{"source": "管理科学","target": "工商管理"},
{"source": "管理科学","target": "管理科学与工程"},
{"source": "管理科学","target": "经济学"},
{"source": "工商管理","target": "成本管理"},
{"source": "工商管理","target": "财务管理"},
{"source": "工商管理","target": "现代企业"},
{"source": "工商管理","target": "技术经济与技术管理"},
{"source": "工商管理","target": "电子商务"},
{"source": "工商管理","target": "公司治理"},
{"source": "工商管理","target": "企业文化"},
{"source": "工商管理","target": "科学管理"},
{"source": "工商管理","target": "信息不对称"},
{"source": "管理科学与工程","target": "物流供应链"},
{"source": "管理科学与工程","target": "管理信息系统"},
{"source": "管理科学与工程","target": "健康信息系统"},
{"source": "管理科学与工程","target": "社交网络"},
{"source": "管理科学与工程","target": "大数据"},
{"source": "管理科学与工程","target": "推荐系统"},
{"source": "管理科学与工程","target": "信息过载"},
{"source": "管理科学与工程","target": "互联网金融"},
{"source": "经济学","target": "理论经济学"},
{"source": "经济学","target": "应用经济学"},
{"source": "经济学","target": "金融危机"},
{"source": "经济学","target": "风险管理"},
{"source": "经济学","target": "可持续发展"},
{"source": "经济学","target": "货币政策"},
{"source": "经济学","target": "制度变迁"},
{"source": "经济学","target": "中小企业融资"},
{"source": "经济学","target": "政策研究"}];              
$(document).ready(function() {
	clusterchart('main4',1,bottomData,categoryData,linkData,chartData,titleData);
});

</script>
