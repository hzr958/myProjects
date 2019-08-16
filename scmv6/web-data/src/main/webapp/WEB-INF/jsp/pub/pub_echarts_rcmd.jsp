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
var showList = ${showList};

function buildData(group,lang) {
	if (!$.isEmptyObject(showList)) {
    	var nodesList = [];
    	var linksList = [];
    	var keyAllArray = [];
    	var linksAllArray = [];
    	var values = showList;
    	var mapSize = buildNodeSize(values);
    	var k1Map = {};
    	var text = $.trim($('.dev_text').val());
    	var arr = text.split(";");
    	    
		for(var j in values) {
			//重复的key1,key2要去掉
			var k1 = dealKey(values[j].key1);
			var k2 = dealKey(values[j].key2);
			
			if(!k1Map[k1]){
				for(var x in arr){
					if(dealKey(arr[x])==k1){
						k1Map[k1]=arr[x];//node1按照用户输入的显示, 而node2则使用后台返回的数据显示即可
					}
				}
            }
			
			if ($.inArray(k1, keyAllArray) == -1) {//去掉重复的点(node1)
				keyAllArray.push(k1);
    			var node1 = {category:0, name: k1Map[k1], value : Math.ceil(Math.random()*10), symbolSize : mapSize[values[j].key1]};
    			nodesList.push(node1);
			}
			
			if($.inArray(k1+"|||"+k2, linksAllArray) == -1 && $.inArray(k2+"|||"+k1, linksAllArray) == -1){//去掉重复的线(link)
			    linksAllArray.push(k1+"|||"+k2);
	            linksAllArray.push(k2+"|||"+k1);
	            var source = {source : k1Map[k1], target : values[j].key2, weight : Math.ceil(Math.random()*10), name: values[j].key12Jaccard,
	                     itemStyle:{
	                            normal:{
	                                lineWidth:Math.pow(Math.log(2.7183+values[j].key12Jaccard*50),2),
	                                text:'',
	                                textColor:'#030303',
	                                textFont:'bold 1px verdana',
	                                textPosition:'inside',
	                                color:'#e0e1e0'
	                            }
	                        }
	            };
	            linksList.push(source);
			}
		}
		
	    for(var j in values) {
            //重复的key1,key2要去掉
	        var k2 = dealKey(values[j].key2);
            if ($.inArray(k2, keyAllArray) == -1) {//去掉重复的点(node2)
            	keyAllArray.push(k2);
                var node2 = {category:1, name: values[j].key2, value : Math.ceil(Math.random()*10), symbolSize : mapSize[values[j].key2]};
                nodesList.push(node2);
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
			  map[values[k].key1] = Number(map[values[k].key1]) + Number(values[k].key1Val);
		} else {
			  map[values[k].key1] = values[k].key1Val;
		} 
		if(Number(map[values[k].key2]) > 0) {
			  map[values[k].key2] = Number(map[values[k].key2]) + Number(values[k].key2Val);
		} else {
			  map[values[k].key2] = values[k].key2Val;
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
    var num = 40;
    var valArray = [];
    for ( var y in map2) {
    	if (num <= 15) {
			num = 15;
		}
    	if ($.inArray(map2[y], valArray) > -1) {
    		valArray.push(map2[y]);
    		map2[y] = num;
    	} else {
        	valArray.push(map2[y]);
    		map2[y] = num;
    		num -= 1;
    	}
    }
    return map2;
};

function dealKey(text) {
	return $.trim(text.replace(/\s+/, " ")).toLowerCase();
};

function doAnalysis(event) {
	search();
};

function search() {
    var text = $.trim($('.dev_text').val());
    var arr = text.split(";");
    if (arr.length > 20) {
		alert("关键词不能超过20个");
		return;
	}
    
    if (text != "") {
        window.location.href = "${domainscm}/dataweb/pub/pubrcmdview?devcode=1&keywords="+encodeURIComponent(text);
	}
//     var kw = "";
//     for ( var a in arr) {
//     	kw += encodeURIComponent(arr[a])+";";
// 	}
//     if (kw != "") {//encodeURIComponent()
//         window.location.href = "${domainscm}/dataweb/pub/pubrcmdview?keywords="+kw;
//     }
};

$(function(){
    $('.dev_text').keyup(function(event){
    	  if(event.keyCode ==13){
    		  search();
    	  }
    });
    var json = {};
    var newUrl = "${domainscm}/dataweb/pub/pubrcmdview";
    window.history.replaceState(json, "", newUrl);
	$('.dev_text').val("${keywords}");
	// 路径配置  
	require.config({
	    paths: {
	        echarts: '/resmod/js_v5/echarts'  
	    }  
	});
	if(!$.isEmptyObject(showList)) {
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
	        	        text: '成果关键词推荐图谱',
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
	        	           // name : null,
	        	            ribbonType: false,
	        	            categories : [
	        	                {
	        	                    name: ''
	        	                    ,
	        	                    itemStyle:{
	                                    normal:{
	                                        color:'#ADD8E6'
	                                    }
	                                }
	        	                },
	        	                {
	        	                    name: ''
	        	                    	,
	                                    itemStyle:{
	                                        normal:{
	                                            color:'#FAF0E6'
	                                        }
	                                    }
	        	                },
	        	                {
	        	                    name: ''
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
	        	    if (data.source !== undefined && data.target !== undefined) { //点击的是边
	        	        //var sourceNode = nodes.filter(function (n) {return n.name == data.source})[0];
	        	        //var targetNode = nodes.filter(function (n) {return n.name == data.target})[0];
	        	        //console.log("选中了边 " + sourceNode.name + ' -> ' + targetNode.name + ' (' + data.weight + ')');
	        	    } else { // 点击的是点
	        	       // console.log("选中了" + data.name + '(' + data.value + ')');
	        	    }
	        	}
	        	myChart.on(ecConfig.EVENT.CLICK, focus);

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
  <div style="display: flex; justify-content: flex-start; align-items: center;">
    <div style="height: 32px; width: 540px; display: flex; align-items: center; margin-left: 120px;">
      <input class="dev_text" type="text" placeholder="多个关键词以分号分隔，最多不超过二十个关键词"
        style="height: 95%; width: 98%; border: 1px solid #ccc; margin-left: 4px;" />
    </div>
    <div onclick="doAnalysis(event);"
      style="height: 32px; width: 72px; border-adius: 4px; margin-left: 8px; cursor: pointer; color: #fff; background: #288aed; border: 1px solid #288aed; display: flex; align-items: center; justify-content: center; font-size: 14px;">分析</div>
  </div>
  <c:if test="${msg == 'no'}">
    <div style="text-align: center;">
      <div>
        <h2>未找到相关图谱</h2>
      </div>
    </div>
  </c:if>
  <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
  <div id="main" style="width: 1000px; height: 750px;"></div>
</body>
</html>