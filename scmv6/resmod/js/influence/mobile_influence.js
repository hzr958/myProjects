var MobileInfluence  = MobileInfluence||{};


MobileInfluence.bigVisitTrendImg = null;
MobileInfluence.bigCitedTrendImg = null;

// 人员统计数
MobileInfluence.getPsnStatistics = function(){
	// 这个不能删，app用
    var des3PsnId = $("#des3PsnId").val();
    // 这个不能删，app用
    $.ajax({
        url: '/psnweb/outside/mobile/ajaxstatistics',
        type: 'post',
        data: {
            "des3PsnId" : des3PsnId
        },
        dataType: "json",
        success: function(data){
            if(data.result == "success"){
                $("#influence_award_share_sum").html(data.shareAndAwardSum);
                $("#influence_visit_sum").html(data.visitSum);
                $("#influence_download_sum").html(data.downLoadSum);
                $("#influence_cited_sum").html(data.citedSum);
                $("#influence_hindex").html(data.hindex);
            }
        },
        erorr: function(data){
        }
    });
};


/**
 * 显示阅读数折线图
 */
MobileInfluence.showVisitTrend = function(){
    var des3PsnId = $("#des3PsnId").val();
    $.ajax({
        url: '/psnweb/outside/mobile/ajaxreadline',
        type: 'post',
        data: {
            "des3PsnId": des3PsnId
        },
        dataType: "json",
        success: function(data){
            if(data.result == "success"){
                MobileInfluence.showBigVisitTrendImg(data);
                MobileInfluence.showLineOption(data);
            }
        },
        erorr: function(data){
        }
    });
};


/**
 * 显示阅读访问趋势图
 */
MobileInfluence.showLineOption = function(data){
    option = {
            tooltip : {  
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                },  
                padding: 5,  
                formatter: function(param){
                    return (param[0].name +'<br/>被阅读数 : '+param[0].value);
                }
            },
            grid:{
                x:35,
                y:35,
                x2:45,
                y2:35,
                borderWidth:1
            },
            xAxis: {
                splitLine:{show: false},
                type: 'category',
                boundaryGap: false,
                name: "时间",
                data: data.xAxisData.split(",")
                
            },
            yAxis: {
                name: "阅读数",
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
    var myChart = echarts.init(document.getElementById("linemap"), "walden");         
    // 为echarts对象加载数据
    myChart.setOption(option);
};

// 画占一个屏幕大小的阅读趋势图
MobileInfluence.showBigVisitTrendImg = function(data){
  option = {
      tooltip : {
        axisPointer: {
          type: 'cross'
        },
        trigger: 'axis',
        formatter: "{b}<br/>被阅读数 : {c}",
        extraCssText:'transform: rotate(90deg)'
      },    
      xAxis: {
          name: "阅读数",
          type: 'value', //数据
          position :'top', //x 轴的位置【top bottom】
          nameRotate :-90, //坐标轴名字旋转，角度值。
          axisLabel :{  //坐标轴刻度标签的相关设置。
             rotate : 90, //刻度标签旋转的角度，
             formatter: '{value}'
             
          },
          minInterval: 1,
          scale: true, //是否是脱离 0 值比例
      },
      yAxis: {
          name: "时间",
          type: 'category',
          axisLine: {onZero: false},
          splitLine:{show: false},
          data: data.xAxisData.split(","),
          inverse :'true', //是否是反向坐标轴。
          nameRotate :-90, //坐标轴名字旋转，角度值。
          axisLabel :{
              rotate : -90
          },
      },
      series: [{
          data: data.yAxisData.split(","),
          type: 'line',
          smooth: true //是否平滑曲线显示
      }]
  };
    MobileInfluence.bigVisitTrendImg = echarts.init(document.getElementById("big_visit_trend_img"), "walden");         
    // 为echarts对象加载数据
    MobileInfluence.bigVisitTrendImg.setOption(option);
};

// 放大阅读趋势图
MobileInfluence.showBigVisitTrend = function(){
    $(".influence_module").hide();
    $("#big_visit_trend_img").show();
    $("#reduce_icon").show();
    if(MobileInfluence.bigVisitTrendImg != null){
        MobileInfluence.bigVisitTrendImg.resize();
    }
}

// 缩小阅读趋势图
MobileInfluence.reduceBigVisitTrend = function(){
    $(".influence_module").show();
    $("#big_visit_trend_img").hide();
    $("#reduce_icon").hide();
}

// 论文引用趋势柱状图
MobileInfluence.showBarMap = function(){
	$.ajax({
		url: '/psnweb/outside/mobile/ajaxpubcite',
		type: 'post',
		data: {"des3PsnId": $("#des3PsnId").val()},
		dataType: "json",
		success: function(data){
			if(data.result == "success"){
				if(data.hasCiteThead == "no") {
					$('.dev_cite_thread').hide();
				} else {
				    var max = (Math.ceil(data.yMaxVal/10) + 1) * 10;
                    var min = Math.floor(data.yMinVal/10) - 1 < 0 ? Math.floor(data.yMinVal/10) * 10 : (Math.floor(data.yMinVal/10) - 1) * 10;
                    var interval = (max - min) / 10;
                    data["yMinVal"] = min;
                    data["yMaxVal"] = max;
                    data["interval"] = interval;
					MobileInfluence.showBarOption(data);
					MobileInfluence.showBarOptionBig(data);
				}
			}
// BaseUtils.ajaxTimeOut(data,function(){
// });
		},
		erorr: function(data){
		}
	});
};

// 论文引用趋势柱状图option
MobileInfluence.showBarOption = function(data){
    var option = {
            tooltip: {
                show: false
            },
            grid:{
            	 x:35,
                 y:35,
                 x2:45,
                 y2:35,
                 borderWidth:1
            },
            xAxis : [
                {
                	splitLine:{show: false},// 去除网格线
                	name: "年份",
                    type : 'category',
                    data : data.xAxisData.split(",")
                }
            ],
            /*
             * yAxis : [ { name: "引用数", minInterval: 1, type : 'value' } ], series : [ {
             * "type":"bar", barMaxWidth:20,//最大宽度 "data":data.yAxisData.split(",") } ]
             */
            
            
            yAxis :
            {
                name: "引用数",
                minInterval: 1,
// max: Math.ceil(data.yMaxVal/10) * 10 ,
// min: Math.floor(data.yMinVal/10) * 10,
// interval:Math.ceil(data.yMaxVal/10) - Math.floor(data.yMinVal/10) <= 0 ? 1 :
// Math.ceil(data.yMaxVal/10) - Math.floor(data.yMinVal/10),
                min: data.yMinVal,
                max: data.yMaxVal,
                interval: data.interval,
                type : 'value'
            },
        series : [
            {
                "type":"bar",
                barMaxWidth:100,// 最大宽度
                "data":data.yAxisData.split(",")
            }
        ]
            
            
            
            
        };
	var myChart = echarts.init(document.getElementById("barmap"), "walden");         
    // 为echarts对象加载数据
    myChart.setOption(option);
};
// 放大的引用趋势柱状图option
MobileInfluence.showBarOptionBig = function(data){
	option = {
      tooltip : {
        show: false
      },    
      xAxis: {
          name: "引用数",
          type: 'value', //数据
          position :'top', //x 轴的位置【top bottom】
          nameRotate :-90, //坐标轴名字旋转，角度值。
          axisLabel :{  //坐标轴刻度标签的相关设置。
             rotate : 90, //刻度标签旋转的角度，
             formatter: '{value}'
             
          },
          minInterval: 1,
          min: data.yMinVal,
          max: data.yMaxVal,
          interval: data.interval,
          scale: true, //是否是脱离 0 值比例
      },
      yAxis: {
          name: "年份",
          type: 'category',
          axisLine: {onZero: false},
          splitLine:{show: false},
          data: data.xAxisData.split(","),
          inverse :'true', //是否是反向坐标轴。
          nameRotate :-90, //坐标轴名字旋转，角度值。
          axisLabel :{
              rotate : -90
          },
      },
      series: [{
          data: data.yAxisData.split(","),
          type: 'bar',
          barMaxWidth:20// 最大宽度
      }]
  };
	
	
	MobileInfluence.bigCitedTrendImg = echarts.init(document.getElementById("dev_cite_trend_big"), "walden");         
	// 为echarts对象加载数据
	MobileInfluence.bigCitedTrendImg.setOption(option);
};

// 点击放大引用趋势图
MobileInfluence.showBigCiteTrend = function() {
    $(".influence_module").hide();
    $("#dev_cite_trend_big").show();
    $(".dev_cite_small_icons").show();
    if(MobileInfluence.bigCitedTrendImg != null){
        MobileInfluence.bigCitedTrendImg.resize();
    }
};
// 点击隐藏放大的引用趋势图
MobileInfluence.hideBigCiteTrend = function() {
	$(".influence_module").show();
	$("#dev_cite_trend_big").hide();
	$(".dev_cite_small_icons").hide();
};
// 点击后退按钮 根据有无历史页面跳转
MobileInfluence.goBack=function(){
  if(window.history.length > 2){
    window.history.back();
  }else{
    location.href="/dynweb/mobile/dynshow";
  }
}
