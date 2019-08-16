<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EDGE, chrome=1" />
<meta http-equiv="content-style-type" content="text/css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>科研之友</title>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/echarts/echarts.js"></script>
<script type="text/javascript">
var showMap = ${showMap};
var selectGroups = [];
var selectNum = [];
for(var i in showMap){
	selectGroups.push(i);
	selectNum.push(i.substr(5));
}
selectNum.sort();//排序  <因为学部都为个位数,所以适用>
selectGroups.sort();//排序

function buildData(group,lang) {
	if (!$.isEmptyObject(showMap)) {
    	var nodesList = [];
    	var linksList = [];
    	var keyArray = [];
    	var values = [];
    	if (group == null) {//默认最小组
    		group = selectGroups[0];
    	} else {
    		group = selectGroups[group];
    	}
    	if (lang == null) {//默认英文关键词
    		values = showMap[group]["en_US"];
    		if (values == null) {
    			values = showMap[group]["zh_CN"];
    		}
    	} else {
    		values = showMap[group][lang];
    		if (values == null) {
    			values = showMap[group]["en_US"];
    		}
    	}
    	if (values != null) {
    		var mapSize = buildNodeSize(values);
    		for(var j in values) {
    			//重复的key1,key2要去掉
    			if ($.inArray(values[j].key1, keyArray) == -1) {
    				keyArray.push(values[j].key1);		
    				var node1 = {category:0, name: values[j].key1, value : Math.ceil(Math.random()*10), symbolSize : mapSize[values[j].key1]};
    				nodesList.push(node1);
    			}
    			if ($.inArray(values[j].key2, keyArray) == -1) {
    				keyArray.push(values[j].key2);
    				var node2 = {category:0, name: values[j].key2, value : Math.ceil(Math.random()*10), symbolSize : mapSize[values[j].key2]};
    				nodesList.push(node2);
    			}
    			var source = {source : values[j].key1, target : values[j].key2, weight : Math.ceil(Math.random()*10), name: values[j].val,
    			   	 itemStyle:{
    						normal:{
    							lineWidth:Math.pow(Math.log(2.7183+values[j].val*100),2),
    							text:'',
    							textColor:'#030303',
    							textFont:'bold 1px verdana',
    							textPosition:'inside',
    							color:'#288aed'
    						}
    					}
    			};
    			linksList.push(source);
    		}
    	}
    	var dataJson = {"nodesArray":nodesList,"linksArray":linksList};
    	return dataJson;
	}
};
//======================
function rebuildObjVal(obj) {
    var val=[],key;
    for (key in obj) {
        if (Object.prototype.hasOwnProperty.call(obj,key)) {
            val.push(obj[key]);
        }
    }
    return val;
};
//======================
function buildNodeSize(values) {
	var map= {};
	for(var k in values) {
		if (Number(map[values[k].key1]) > 0) {
			  map[values[k].key1] = Number(map[values[k].key1]) + Number(values[k].val);
		} else {
			  map[values[k].key1] = values[k].val;
		} 
		if(Number(map[values[k].key2]) > 0) {
			  map[values[k].key2] = Number(map[values[k].key2]) + Number(values[k].val);
		} else {
			  map[values[k].key2] = values[k].val;
		}
	}
    //===================================
    if (!Object.values) {//兼容 IE 不支持 Object.values
        var result = rebuildObjVal(map).sort();
    } else {
         var result = Object.values(map).sort();
    }
    result.reverse();
    var map2 = {};
    for (var r=0; r < result.length; r++) {
        for ( var m in map) {
            if (map[m] == result[r]) {
                map2[m] = result[r];
            }
        }
    }
    //圆圈最大值
    var num = 30;
    var valArray = [];
    for ( var y in map2) {
    	if ($.inArray(map2[y], valArray) > -1) {
    		valArray.push(map2[y]);
    		map2[y] = num;
    	} else {
        	valArray.push(map2[y]);
    		map2[y] = num;
    		num -= 1.5;
    	}
    }
    return map2;
};

function selectGroup() {
	var num = $('#dev_select_group').val();
	var group = findIndex(selectNum, num);
	var lang = $('#dev_select_lang').val();
	var dataArray = buildData(group,lang);
	pubEcharts(dataArray);
};

function selectLang() {
	var num = $('#dev_select_group').val();
	var group = findIndex(selectNum, num);
	var lang = $('#dev_select_lang').val();
	pubEcharts(buildData(group,lang));
};

function findIndex(arr,num){
	 var pos = arr.indexOf(num,pos);
	 if(pos === -1) {
		 return 0;
	 } else {
		 return pos;
	 }
};

$(function(){
	// 路径配置  
	require.config({
	    paths: {
	        echarts: '/resmod/js_v5/echarts'  
	    }  
	});
	if (!$.isEmptyObject(showMap)) {
    	//初始展示
    	pubEcharts(buildData());
	}
});

function pubEcharts(dataArray) {
	 // 使用  
	 require(
	    [  
	        'echarts',  
	        'echarts/chart/force', // 使用力导向布局图就加载force模块，按需加载  
	        'echarts/chart/chord'
	    ],  
	    function (ec) {
	        // 基于准备好的dom，初始化echarts图表  
	        var myChart = ec.init(document.getElementById('main'));   
	          
	        var option = {
	        	    title : {
	        	        text: '成果关键词知识图谱',
	        	        subtext: '数据来自科研之友',
	        	        x:'right',
	        	        y:'bottom'
	        	    },
	        	    tooltip : {
	        	        trigger: 'item',
	        	        formatter: '{a} : {b}'
	        	    },
	        	    toolbox: {
	        	        show : true,
	        	        feature : {
	        	            restore : {show: true},
	        	            magicType: {show: true, type: ['force', 'chord']},
	        	            saveAsImage : {show: true}
	        	        }
	        	    },
// 	        	    legend: {
// 	        	        x: 'left',
// 	        	        data:['家人','朋友']
// 	        	    },
	        	    series : [
	        	        {
	        	            type:'force',
	        	            name : "强度",
	        	            ribbonType: false,
	        	            categories : [
	        	                {
	        	                    name: '人物'
	        	                },
	        	                {
	        	                    name: '家人'
	        	                },
	        	                {
	        	                    name:'朋友'
	        	                }
	        	            ],
	        	            itemStyle: {
	        	                normal: {
	        	                    label: {
	        	                        show: true,
	        	                        textStyle: {
	        	                            color: '#333'
	        	                        }
	        	                    },
	        	                    nodeStyle : {
	        	                        brushType : 'both',
	        	                        borderColor : 'rgba(255,215,0,0.4)',
	        	                        borderWidth : 1
	        	                    },
	        	                    linkStyle: {
	        	                        type: 'curve'
	        	                    }
	        	                },
	        	                emphasis: {
	        	                    label: {
	        	                        show: false
	        	                        // textStyle: null      // 默认使用全局文本样式，详见TEXTSTYLE
	        	                    },
	        	                    nodeStyle : {
	        	                        //r: 30
	        	                    },
	        	                    linkStyle : {}
	        	                }
	        	            },
	        	            useWorker: false,
	        	            minRadius : 15,
	        	            maxRadius : 25,
	        	            gravity: 1.1,
	        	            scaling: 1.1,
	        	            roam: 'move',
	        	            nodes: dataArray.nodesArray,
	        	            links : dataArray.linksArray
	        	        }
	        	    ]
	        	};
	        	var ecConfig = require('echarts/config');
	        	function focus(param) {
	        	    var data = param.data;
	        	    var links = option.series[0].links;
	        	    var nodes = option.series[0].nodes;
	        	    if (
	        	        data.source !== undefined
	        	        && data.target !== undefined
	        	    ) { //点击的是边
	        	        var sourceNode = nodes.filter(function (n) {return n.name == data.source})[0];
	        	        var targetNode = nodes.filter(function (n) {return n.name == data.target})[0];
	        	        console.log("选中了边 " + sourceNode.name + ' -> ' + targetNode.name + ' (' + data.weight + ')');
	        	    } else { // 点击的是点
	        	        console.log("选中了" + data.name + '(' + data.value + ')');
	        	    }
	        	}
	        	myChart.on(ecConfig.EVENT.CLICK, focus)

	        	/* myChart.on(ecConfig.EVENT.FORCE_LAYOUT_END, function () {
	        	    console.log(myChart.chart.force.getPosition());
	        	}); */
	            // 为echarts对象加载数据   
	            myChart.setOption(option); 
	    }  
	);
};
</script>
</head>
<body>
  <c:if test="${msg == 'no'}">
    <div style="text-align: center;">
      <div>
        <h2>未找到相关图谱</h2>
      </div>
      <div>
        <h4>可以查看以下例子:</h4>
        <a style="text-decoration: none;" href="${domainscm}/dataweb/pub/chartview?des3PubId=VyILa6ETLBw%3D">例子1</a>&nbsp;
        <a style="text-decoration: none;" href="${domainscm}/dataweb/pub/chartview?des3PubId=yVJRmOXnEZE%3D">例子2</a>&nbsp;
        <a style="text-decoration: none;" href="${domainscm}/dataweb/pub/chartview?des3PubId=q7qvSRQb5ys%3D">例子3</a>
      </div>
    </div>
  </c:if>
  <div style="display: flex; justify-content: flex-start; align-items: center;">
    <s:if test="groupList.size() > 1">
      <div class="dev_select_group_div" style="width: 200px;">
        选择学部:&nbsp;&nbsp; <select id="dev_select_group" onchange="selectGroup();">
          <s:iterator value="groupList" var="group">
            <option value="${group}">
              <c:if test="${group == 1}">农业</c:if>
              <c:if test="${group == 2}">科学</c:if>
              <c:if test="${group == 3}">人文社科</c:if>
              <c:if test="${group == 4}">经济管理</c:if>
              <c:if test="${group == 5}">工程</c:if>
              <c:if test="${group == 6}">信息科技</c:if>
              <c:if test="${group == 7}">医药卫生</c:if>
            </option>
          </s:iterator>
        </select>
      </div>
    </s:if>
    <c:if test="${zhAndEn == true}">
      <div class="dev_select_lang_div" style="float: right;">
        选择语言:&nbsp;&nbsp; <select id="dev_select_lang" onchange="selectLang();">
          <option value="en_US">英文</option>
          <option value="zh_CN">中文</option>
        </select>
      </div>
    </c:if>
  </div>
  <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
  <div id="main" style="width: 1000px; height: 750px;"></div>
</body>
</html>