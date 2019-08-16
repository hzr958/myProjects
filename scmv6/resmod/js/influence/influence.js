var Influence  = Influence||{};
/**
 * 加载影响力页面
 */
Influence.main = function(){
	Influence.changeUrl("influence");
	$.ajax({
		url: "/psnweb/influence/ajaxmain",
		data: {
		    "isMyself": $("#isMySelf").val()
		},
		type: 'post',
		dataType: 'html',
		success: function(data){
			BaseUtils.ajaxTimeOut(data,function(){
				if($(".container__horiz").length>0){
					$(".container__horiz").replaceWith(data);	
				}else{
					$(".dev_select_tab").replaceWith(data);	//container__horiz带有样式，影响力用dev_select_tab这个
				}
			});
		},
		error: function(){
			
		}
	})
};
/**
 * 改变链接地址
 */
Influence.changeUrl = function(targetModule) {
	var json = {};
	var oldUrl = window.location.href;
	var index = oldUrl.lastIndexOf("module");
	var newUrl = window.location.href;
	if (targetModule != undefined && targetModule != "") {
		if (index < 0) {
			if(oldUrl.lastIndexOf("?")>0){
				newUrl = oldUrl + "&module=" + targetModule;
			}else{
				newUrl = oldUrl + "?module=" + targetModule;
			}
		} else {
			newUrl = oldUrl.substring(0, index) + "module=" + targetModule;
		}
	}
	window.history.replaceState(json, "", newUrl);
};


//人员统计数
Influence.getPsnStatistics = function(){
    var des3PsnId = $("#des3PsnId").val();
    $.ajax({
        url: '/psnweb/influence/ajaxstatistics',
        type: 'post',
        data: {
            "des3PsnId" : des3PsnId
        },
        dataType: "json",
        success: function(data){
            if(data.result == "success"){
                $("#influence_award_share_sum").html(data.shareAndAwardSum);
                $("#influence_visit_sum").html(data.visitSum);
                $("#psn_visit_sum").html(data.visitSum);
                $("#influence_download_sum").html(data.downLoadSum);
                $("#influence_cited_sum").html(data.citedSum);
                $("#influence_hindex").html(data.hindex);
                $("#psn_hindex").val(data.hindex);
                $(".psn_friend_sum").html(data.frdSum);
            }
        },
        erorr: function(data){
        }
    });
};
//单位阅读分布
Influence.getVisitIns = function(){
    var des3PsnId = $("#des3PsnId").val();
    $.ajax({
        url: '/psnweb/influence/ajaxvisitins',
        type: 'post',
        data: {
            "des3PsnId" : des3PsnId
        },
        dataType: "html",
        success: function(data){
            $("#visitinsDiv").html(data);
        },
        erorr: function(data){
        }
    });
};
//职称阅读分布
Influence.getVisitPos = function(){
    var des3PsnId = $("#des3PsnId").val();
    $.ajax({
        url: '/psnweb/influence/ajaxvisitpos',
        type: 'post',
        data: {
            "des3PsnId" : des3PsnId
        },
        dataType: "html",
        success: function(data){
            $("#visitposDiv").html(data);
        },
        erorr: function(data){
        }
    });
};
/**
 * 显示阅读人员分布图
 */
Influence.showVisitMap = function(){
	$.ajax({
		url: '/psnweb/influence/ajaxvisitmap',
		type: 'post',
		data: {"des3PsnId":$("#des3PsnId").val()},
		dataType: "json",
		success: function(data){
			if (data.result=="success") {
				var option = Influence.visitMapOption(data);
//			    Mydraw.draw(typeConst.MAP,option,"visitmap");
			}	
		},
		erorr: function(data){
		}
	});
};

/**
 * 显示阅读数折线图
 */
Influence.showLineMap = function(){
    var des3PsnId = $("#des3PsnId").val();
	$.ajax({
		url: '/psnweb/influence/ajaxreadline',
		type: 'post',
		data: {
		    "des3PsnId": des3PsnId
		},
		dataType: "json",
		success: function(data){
		    if(data.result == "success"){
		        var option = Influence.showLineOption(data);
//		        Mydraw.draw(typeConst.LINE,option,"linemap");
		    }
		},
		erorr: function(data){
		}
	});
};
/**
 * 论文引用趋势柱状图
 */
Influence.showBarMap = function(){
	$.ajax({
		url: '/psnweb/influence/ajaxpubcite',
		type: 'post',
		data: {"des3PsnId": $("#des3PsnId").val()},
		dataType: "json",
		success: function(data){
			BaseUtils.ajaxTimeOut(data,function(){
				if(data.result == "success"){
					if(data.hasCiteThead == "no") {
						$('.dev_cite_thread').hide();
					} else {
						$('.dev_pub_cite_total').html(data.citeSum);
						$('.dev_frd_total').html(data.frdSum);
						$('.dev_cite_rank').html(data.citeRank);
					    var max = (Math.ceil(data.yMaxVal/10) + 1) * 10;
	                    var min = Math.floor(data.yMinVal/10) - 1 < 0 ? Math.floor(data.yMinVal/10) * 10 : (Math.floor(data.yMinVal/10) - 1) * 10;
	                    var interval = (max - min) / 10;
	                    data["yMinVal"] = min;
	                    data["yMaxVal"] = max;
	                    data["interval"] = interval;
						Influence.showBarOption(data);
					}
				}
			});
		},
		erorr: function(data){
		}
	});
};
/**
 * Hindex
 */
Influence.showHindexMap = function(){
	$.ajax({
		url: '/psnweb/influence/ajaxhindex',
		type: 'post',
		data: {
		    "des3PsnId": $("#des3PsnId").val()
		},
		dataType: "json",
		success: function(data){
		    if(data.result == "success"){
		        var option = Influence.showHindexOption(data);
		    }
		    if(data.xAxisData == "" || data.yAxisData == ""){
		        $("#influence_hindex_div").hide();
		    }
		    $("#influence_hindex").text(data.hindex);
		    Influence.showHindexPub();
		},
		erorr: function(data){
		    $("#influence_hindex_div").hide();
		    Influence.showHindexPub();
		}
	});
};


/**
 * 阅读人员分布图的option
 */
Influence.visitMapOption = function(data){
	var dataList = data.dataList;
	var chinaRegion = data.chinaRegion;
	var datav = [];
	var datavZh = {};
	var datavEn = {};
	datav.push({name:"南海诸岛",itemStyle:{normal:{borderColor: '#A8A8A8',borderWidth:0.1}}});
	datavZh["南海诸岛"] = "南海诸岛: 0";
 	datavEn["南海诸岛"] = "The South China Sea Islands: 0";
 	for(var i=0;i<chinaRegion.length;i++){
 		var dataItem = chinaRegion[i];
 		var pattern = /(中国)?(黑龙江|内蒙古|.{2}).*/; //截取省份名称简称
 		datavZh[dataItem.zhName.replace(pattern,"$2")] = dataItem.zhName+": 0";//全国
 		datavEn[dataItem.zhName.replace(pattern,"$2")] = dataItem.enName+": 0";
 	}
    for(var i=0;i<dataList.length;i++){
   	   var dataItem = dataList[i];
   	   var pattern = /(中国)?(黑龙江|内蒙古|.{2}).*/; //截取省份名称简称
   	   datav.push({name:dataItem.provinceName.replace(pattern,"$2"), value:dataItem.visiCount});
   	   datavZh[dataItem.provinceName.replace(pattern,"$2")] = dataItem.provinceName+": "+dataItem.visiCount;//一小部分
   	   datavEn[dataItem.provinceName.replace(pattern,"$2")] = dataItem.provinceEnName+": "+dataItem.visiCount;
    }
	option = {
		    dataRange: {
		        orient: 'horizontal',
		        min: 0,
		        max: data.maxNum,
		        itemWidth:15,//值域控件item宽度
		        itemGap:1,//值域控件item间距
		        text:[Influence_globalization.more,Influence_globalization.less],           // 文本，默认为数值文本
		        splitNumber:5 //值域控件分块
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: function(data){
		        	if(locale=='zh_CN'){//lovale在main.jsp设置为了全局变量
		        		return datavZh[data.name];		        		
		        	}else{
		        		return datavEn[data.name];		        				        		
		        	}
                }
		    },		    
			toolbox: {
				show: true,
				left: 'right',
				top: 'center',
				padding: 15,
				feature: {
				    restore : {
				        title : Influence_globalization.refresh,
				    	//title : '还原',
				        
				    },
				}
			},
		    series : [
		        {
		            name: '阅读人员分布',
		            type: 'map',
		            mapType: 'china',
		            zoom:1.2,
		            clickable:false,
		        	roam: true,//缩放和漫游
		        	scaleLimit:{max:25, min:1.2},//缩放最大最小倍
		            mapLocation: {
		                x: 'center'
		            },
		            itemStyle:{
		                normal:{
		                	label:{show:false},
		                	borderColor: '#fff',
		                	areaColor: '#e3f3fb'
		                		
		                },
		                emphasis:{
		                	label:{show:false},
		                },		             

		            },
		            data:datav,
		        }
		        
		    ],
		    animation: false
		};
    if(document.getElementById("visitmap") != null){
        var myChart = echarts.init(document.getElementById("visitmap"), "walden");
        // 为echarts对象加载数据
        myChart.setOption(option);
    }

};
/**
 * 显示阅读访问趋势图
 */
Influence.showLineOption = function(data){
	option = {
	        tooltip : {  
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                },  
                padding: 5,  
                formatter: function(param){

//                    alert(JSON.stringify(param));

                    return (param[0].name +'<br/>'+Influence_globalization.reads+' : '+param[0].value);
                }
            },
            grid:{
                x:45,
                y:30,
                x2:locale == "zh_CN" ? 45 : 100,
                y2:25,
                borderWidth:1
            },
	        xAxis: {
	            splitLine:{show: false},
	            type: 'category',
	            boundaryGap: false,
	            name: Influence_globalization.time,
	            data: data.xAxisData.split(",")
	        },
	        yAxis: {
	            name: Influence_globalization.reads,
	            type: 'value',
	            minInterval: 1
	        },
	        series: [{
	            smooth: true,
	            data: data.yAxisData.split(","),
	            type: 'line',
	            areaStyle: {}
	        }]
	    }
    if(document.getElementById("linemap") != null){
        var myChart = echarts.init(document.getElementById("linemap"), "walden");
        // 为echarts对象加载数据
        myChart.setOption(option);
    }

};
/**
 * 论文引用趋势柱状图option
 */
Influence.showBarOption = function(data){
    var option = {
            tooltip: {
                show: false
            },
            grid:{
                x:45,
                y:30,
                x2:locale == "zh_CN" ? 45 : 100,
                y2:25,
                borderWidth:1
            },
            xAxis : [
                {
                	splitLine:{show: false},//去除网格线
                	name: Influence_globalization.year,
                    type : 'category',
                    data : data.xAxisData.split(",")
                }
            ],
            yAxis :
                {
                	name: Influence_globalization.cites,
                	minInterval: 1,
//                	max: (Math.ceil(data.yMaxVal/10) + 1) * 10 ,
//                	min: Math.floor(data.yMinVal/10) - 1 <= 0 ? Math.floor(data.yMinVal/10) * 10 : (Math.floor(data.yMinVal/10) - 1) * 10,
//                	interval:Math.ceil(data.yMaxVal/10) - Math.floor(data.yMinVal/10) <= 0 ? 1 : Math.ceil(data.yMaxVal/10) - Math.floor(data.yMinVal/10),
                	min: data.yMinVal,
                    max: data.yMaxVal,
                    interval: data.interval,
                    type : 'value'
                },
            series : [
                {
                    "type":"bar",
                    barMaxWidth:20,//最大宽度
                    "data":data.yAxisData.split(",")
                }
            ]
        };
    if(document.getElementById("barmap") != null ){
        var myChart = echarts.init(document.getElementById("barmap"), "walden");
        // 为echarts对象加载数据
        myChart.setOption(option);
    }

};
/**
 * Hindex的option
 */
Influence.showHindexOption = function(data){
    option = {
            tooltip : {  
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                },  
                padding: 5,  
                formatter: function(param){  

                    return (Influence_globalization.pubCitedSum + ' : '+param[0].value);  

                },  
            },
            grid:{
                x:45,
                y:45,
                x2:locale == "zh_CN" ? 60 : 100,
                y2:35,
                borderWidth:1
            },
            xAxis: {
                splitLine:{show: false},
                name: Influence_globalization.pubNo,
                type: 'category',
                boundaryGap: true,//刻度是否从0开始
                data: data.xAxisData.split(",")
            },
            yAxis: {
                name: Influence_globalization.pubCitedSum,
                type: 'value',
                minInterval: 1
            },
            series: [{
                smooth: true,
                data: data.yAxisData.split(","),
                type: 'line',
                markPoint: {
                    symbol: 'pin',
                    symbolRotate: data.hindex > 1 ? 0 : -90,
                    itemStyle:{  
                        normal:{
                            label:{  
                                show: true,  
                                position: 'inside',  
                                formatter: "H-index : {c}",
                                fontSize:12
                            }  
                        }  
                    },
                    data: [
                        {name: 'H-index', value : data.hindex, xAxis : (data.hindex == 0 ? null : data.xHindex), yAxis: data.yHindex}
                    ]
                    
                }
            }]
        }
    if(document.getElementById("hindexmap") != null ){
        var myChart = echarts.init(document.getElementById("hindexmap"), "walden");
        // 为echarts对象加载数据
        myChart.setOption(option);
    }

};


//获取hindex在联系人中的排名
Influence.getHindexRankingInFriends = function(){
    $.ajax({
        url: '/psnweb/influence/hindex/ajaxranking',
        type: 'post',
        data: {
            "des3PsnId": $("#des3PsnId").val()
        },
        dataType: "json",
        success: function(data){
            if(data.result == "success"){
                $("#psn_hindex_ranking").html(data.ranking);
            }
        },
        erorr: function(data){
        }
    });
};

//获取阅读数在联系人中的排名
Influence.getVisitRankingInFriends = function(){
    $.ajax({
        url: '/psnweb/influence/visit/ajaxranking',
        type: 'post',
        data: {
            "des3PsnId": $("#des3PsnId").val()
        },
        dataType: "json",
        success: function(data){
            if(data.result == "success"){
                $("#psn_visit_ranking").html(data.ranking);
            }
        },
        erorr: function(data){
        }
    });
};

//显示H-index提升推荐的成果列表
Influence.showHindexPub = function() {
    $.ajax({
        url: '/pub/influence/ajaxhindexlist',
        type: 'post',
        data: {
            "des3PsnId" : $("#des3PsnId").val(),
            "hIndex": $("#influence_hindex").text()
        },
        dataType: "html",
        success: function(data){
			BaseUtils.ajaxTimeOut(data,function(){
				$('#dev_pubindex_list').html("");
				$('#dev_pubindex_list').html(data);
			});
        }
    });
};

//检查更新hindex
Influence.checkHindex = function(){
    $.ajax({
        url: '/psnweb/outside/hindex/ajaxupdate',
        type: 'post',
        data: {
            "des3PsnId" : $("#des3PsnId").val()
        },
        dataType: "json",
        success: function(data){
            if(data.result == "success"){
                $("#influence_hindex").html(data.hindex);
            }
        },
        error: function(){}
    });
}
