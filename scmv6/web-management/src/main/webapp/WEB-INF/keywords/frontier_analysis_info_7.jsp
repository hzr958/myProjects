<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="resmod" value="/resmod" />
<title>前沿分析</title>
<script src="${resmod}/js/clusteringTest2.js"></script>
<script src="${resmod}/js/echarts.min.js"></script>
<script type="text/javascript">
var mId=${mId};//左侧菜单Id
var titleData = "医学科学";
var bottomData = ['基础医学', '中医学', '临床医学','中西医结合','公共卫生与预防','社会学'];
var categoryData = [{'name':'基础医学'},
{'name':'中医学'},
{'name':'临床医学'},
{'name':'中西医结合'},
{'name':'公共卫生与预防'},
{'name':'社会学'}];

var chartData =[{"name": "医学科学","value": 27,"symbolSize": 20,"draggable": "true"},
{"name": "基础医学","symbolSize": 13,"category": "基础医学","draggable": "TRUE","value":3},
{"name": "中医学","symbolSize": 13,"category": "中医学","draggable": "TRUE","value":3},
{"name": "临床医学","symbolSize": 13,"category": "临床医学","draggable": "TRUE","value":3},
{"name": "中西医结合","symbolSize": 13,"category": "中西医结合","draggable": "TRUE","value":3},
{"name": "公共卫生与预防","symbolSize": 13,"category": "公共卫生与预防","draggable": "TRUE","value":3},
{"name": "社会学","symbolSize": 13,"category": "社会学","draggable": "TRUE","value":3},
{"name": "免疫学","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "病理生理","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "实验研究","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "基础医学研究","symbolSize": 3,"category": "基础医学","draggable": "TRUE","value":2},
{"name": "青蒿素","symbolSize": 3,"category": "中医学","draggable": "TRUE","value":2},
{"name": "中医理论","symbolSize": 3,"category": "中医学","draggable": "TRUE","value":2},
{"name": "亚健康","symbolSize": 3,"category": "中医学","draggable": "TRUE","value":2},
{"name": "中医现代化","symbolSize": 3,"category": "中医学","draggable": "TRUE","value":2},
{"name": "中医药学","symbolSize": 3,"category": "中医学","draggable": "TRUE","value":2},
{"name": "临床治疗","symbolSize": 3,"category": "临床医学","draggable": "TRUE","value":2},
{"name": "临床实践","symbolSize": 3,"category": "临床医学","draggable": "TRUE","value":2},
{"name": "针刀疗法","symbolSize": 3,"category": "临床医学","draggable": "TRUE","value":2},
{"name": "内脏功能","symbolSize": 3,"category": "临床医学","draggable": "TRUE","value":2},
{"name": "临床流行病学","symbolSize": 3,"category": "临床医学","draggable": "TRUE","value":2},
{"name": "恶性肿瘤","symbolSize": 3,"category": "临床医学","draggable": "TRUE","value":2},
{"name": "循证医学","symbolSize": 3,"category": "中西医结合","draggable": "TRUE","value":2},
{"name": "医药学","symbolSize": 3,"category": "中西医结合","draggable": "TRUE","value":2},
{"name": "中西医结合治疗","symbolSize": 3,"category": "中西医结合","draggable": "TRUE","value":2},
{"name": "中西医结合研究","symbolSize": 3,"category": "中西医结合","draggable": "TRUE","value":2},
{"name": "流行病学","symbolSize": 3,"category": "公共卫生与预防","draggable": "TRUE","value":2},
{"name": "健康教育","symbolSize": 3,"category": "公共卫生与预防","draggable": "TRUE","value":2},
{"name": "卫生事业管理","symbolSize": 3,"category": "公共卫生与预防","draggable": "TRUE","value":2},
{"name": "医学模式","symbolSize": 3,"category": "公共卫生与预防","draggable": "TRUE","value":2},
{"name": "公共卫生","symbolSize": 3,"category": "公共卫生与预防","draggable": "TRUE","value":2},
{"name": "卫生保健","symbolSize": 3,"category": "公共卫生与预防","draggable": "TRUE","value":2},
{"name": "老年医学","symbolSize": 3,"category": "社会学","draggable": "TRUE","value":2},
{"name": "生活质量","symbolSize": 3,"category": "社会学","draggable": "TRUE","value":2},
{"name": "社会环境","symbolSize": 3,"category": "社会学","draggable": "TRUE","value":2},
{"name": "卫生服务","symbolSize": 3,"category": "社会学","draggable": "TRUE","value":2},
{"name": "医患关系","symbolSize": 3,"category": "社会学","draggable": "TRUE","value":2}];

	var linkData =[{"source": "医学科学","target": "基础医学"},
{"source": "医学科学","target": "中医学"},
{"source": "医学科学","target": "中西医结合"},
{"source": "医学科学","target": "临床医学"},
{"source": "医学科学","target": "公共卫生与预防"},
{"source": "医学科学","target": "社会学"},
{"source": "基础医学","target": "免疫学"},
{"source": "基础医学","target": "病理生理"},
{"source": "基础医学","target": "实验研究"},
{"source": "基础医学","target": "基础医学研究"},
{"source": "中医学","target": "青蒿素"},
{"source": "中医学","target": "中医理论"},
{"source": "中医学","target": "亚健康"},
{"source": "中医学","target": "中医现代化"},
{"source": "中医学","target": "中医药学"},
{"source": "临床医学","target": "临床治疗"},
{"source": "临床医学","target": "临床实践"},
{"source": "临床医学","target": "针刀疗法"},
{"source": "临床医学","target": "内脏功能"},
{"source": "临床医学","target": "临床流行病学"},
{"source": "临床医学","target": "恶性肿瘤"},
{"source": "中西医结合","target": "循证医学"},
{"source": "中西医结合","target": "医药学"},
{"source": "中西医结合","target": "中西医结合治疗"},
{"source": "中西医结合","target": "中西医结合研究"},
{"source": "公共卫生与预防","target": "流行病学"},
{"source": "公共卫生与预防","target": "健康教育"},
{"source": "公共卫生与预防","target": "卫生事业管理"},
{"source": "公共卫生与预防","target": "医学模式"},
{"source": "公共卫生与预防","target": "公共卫生"},
{"source": "公共卫生与预防","target": "卫生保健"},
{"source": "社会学","target": "老年医学"},
{"source": "社会学","target": "生活质量"},
{"source": "社会学","target": "社会环境"},
{"source": "社会学","target": "卫生服务"},
{"source": "社会学","target": "医患关系"}];
                
$(document).ready(function() {
	clusterchart('main4',1,bottomData,categoryData,linkData,chartData,titleData);
});

</script>
