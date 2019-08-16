
/**
 * 访问、赞、评论等统计
 */
var smate = smate ? smate : {};
smate.statistics = smate.statistics ? smate.statistics:{};

/**
 * 图表统计
 */
smate.statistics.frendImg = function(id,url,jsonParame){
	$.ajax({
		url:snsctx+url,
		type:"post",
		dataType:"json",
		data:jsonParame,
		async:false,
		success:function(dataJson){
			var chart;
		        chart = new Highcharts.Chart({
		            chart: {
		                renderTo: id,
		                type: 'line'
		            },
		            title: {
		                text: ''
		            },
		            subtitle: {
		                text: ''
		            },
		            xAxis: {
		                categories: dataJson.dateJson
		            },
		            yAxis: {
		                title: {
		                    text: ''
		                }
		            },
		            tooltip: {
		                formatter: function() {
		                        return this.y;
		                }
		            },
		            legend: {
		               enabled: false
		            },
		            series: [{
		                name: '',
		                data: dataJson.countJson
		            }]
		        });
		},
		error:function(){
		}
	});
};

//获取标注图标.
function getIconSrc(iconWidth) {
	var src = "";
	if (iconWidth == 10)
		src = resmod + "/images/map/marker_flag4.gif";
	else if (iconWidth == 15)
		src = resmod + "/images/map/marker_flag3.gif";
	else if (iconWidth == 20)
		src = resmod + "/images/map/marker_flag2.gif";
	else if (iconWidth == 25)
		src = resmod + "/images/map/marker_flag1.gif";
	else
		src = resmod + "/images/map/marker_flag0.gif";
	return src;
}

// 获取标注图标大小.
function getIconWidth(pubCount, maxCount) {
	var padCount = parseInt(maxCount) / 2;
	var width = 10;
	var count = parseInt(pubCount);
	if (count != 0) {
		if (count < padCount)
			width = 10;
		else 
			width = 15;
	}
	return width;
}

//打开信息内容.
function openInf(myOverlay, id, offsetTop, offsetLeft, topPadding, leftPadding, msgContent, count) {
	var content = "<table><tr><td><div style='width:100%;'>"
			+ "<div>"
			+ "<label style='width:100%;text-align:left;font-size:12px;'>"
			+ msgContent
			+ count
			+ "</label>"
			+ "</td></tr></table>";
	myOverlay.openInfoWindow(offsetTop, offsetLeft, topPadding,
			leftPadding, content);
}

smate.statistics.addMarker = function(map,markerArr,msgContent){
	//自定义覆盖物
	var maxCount = 0;
	for ( var t = 0; t < markerArr.length; t++) {
		var json = markerArr[t];
		var currentCount = parseInt(json.count);
		if (currentCount > maxCount)
			maxCount = currentCount;
	}
	
	for ( var i = 0; i < markerArr.length; i++) {
		var json = markerArr[i];
		var p0 = json.xAxis;
		var p1 = json.yAxis;
		var point = new BMap.Point(p0, p1);
		var iconWidth = getIconWidth(json.count, maxCount);
		var myOverlay = new MyOverlay(point, iconWidth, getIconSrc(iconWidth),
				"div_mark_" + i, json.name,110);

		map.addOverlay(myOverlay);
		
		(function() {
			var index = i;
			var count = json.count;
			var _myOverlay = myOverlay;
			var _point = point;
			var _title = json.name;
			var _zIndex;
			_myOverlay.addEventListener("click", function(e) {
			});
			_myOverlay
					.addEventListener(
							"mouseover",
							function(e) {
								_zIndex = document
										.getElementById('div_mark_' + index).style.zIndex;
								$('#div_mark_' + index).css('zIndex', 10000);
								openInf(_myOverlay, index, e.pageY, e.pageX, 0,
										0,msgContent,count);
							});
			_myOverlay.addEventListener("mouseout", function(e) {
				$('#div_mark_' + index).css('zIndex', _zIndex);
				_myOverlay.closeInfoWindow();
			});
		})();
	}
}

/**
 * 百度地图分布图
 */
smate.statistics.mapCoordinate = function(id,url,msgContent,jsonParame){
	$.ajax({
		url:snsctx+url,
		type:"post",
		dataType:"json",
		data:jsonParame,
		success:function(dataJson){
			var map = new BMap.Map(id);
			map.enableScrollWheelZoom();//启用地图滚轮放大缩小
			map.centerAndZoom(new BMap.Point(112.195025,34.127123), 4); //中心点
			if(dataJson.length> 0){
				statistics.addMarker(map,dataJson,msgContent);
			}
			var ctrl_nav = new BMap.NavigationControl({anchor:BMAP_ANCHOR_TOP_LEFT,type:BMAP_NAVIGATION_CONTROL_ZOOM});
			map.addControl(ctrl_nav);
		},
		error:function(){
		}
	});
};

smate.statistics.addVistRecord = function(vistPsnId,actionKey,actionType){
	if (vistPsnId != null && vistPsnId != "" && actionKey != null && actionKey != "" && actionType != null && actionKey != "") {
		$.ajax({
			url:snsctx+"/statistics/addVistRecord",
			type:"post",
			dataType:"json",
			data:{"vistPsnId":vistPsnId,"actionKey":actionKey,"actionType":actionType},
			success:function(data){
			
			},
			error:function(){
			}
		});
	}
};

smate.statistics.addReadRecord = function(readPsnId,actionKey,actionType,readType){
	$.ajax({
		url:snsctx+"/statistics/addReadRecord",
		type:"post",
		dataType:"json",
		async:false,
		data:{"readPsnId":readPsnId,"actionKey":actionKey,"actionType":actionType,"readType":readType},
		success:function(data){
		
		},
		error:function(){
		}
	});
};

//更新成果影响力数据
smate.statistics.updatePsnEffect = function(readPsnId){
	$.ajax({
		url:snsctx+"/statistics/updatePsnEffect",
		type:"post",
		dataType:"json",
		data:{"readPsnId":readPsnId},
		success:function(data){
		
		},
		error:function(){
		}
	});
};

//分享添加阅读记录专用的
smate.statistics.addShareReadRecord = function(actionKey,readType){
	var postData = {"actionKey":actionKey,"readType":readType};
	$.ajax({
		url:snsctx+"/statistics/addShareReadRecord",
		type:"post",
		dataType:"json",
		data:postData,
		success:function(data){
		
	},
	error:function(){
	}
	});
};
	
smate.statistics.addDCRecord = function(psnId,actionKey,actionType,countType,resNodeId){
	var postData = {"actionKey":actionKey,"actionType":actionType,"countType":countType};
	if(typeof psnId!="undefined"&&psnId != null){
		postData.psnId = psnId;
	}
	if(typeof resNodeId!="undefined"&&resNodeId != null){
		postData.resNodeId = resNodeId;
	}
	$.ajax({
		url:snsctx+"/statistics/addDCRecord",
		type:"post",
		dataType:"json",
		data:postData,
		success:function(data){
			
		},
		error:function(){
		}
	});
};

//分享专用的
smate.statistics.addShareDCRecord = function(actionKey,countType){
	var postData = {"actionKey":actionKey,"countType":countType};
	$.ajax({
		url:snsctx+"/statistics/addShareDCRecord",
		type:"post",
		dataType:"json",
		data:postData,
		success:function(data){
		
	},
	error:function(){
	}
	});
};

