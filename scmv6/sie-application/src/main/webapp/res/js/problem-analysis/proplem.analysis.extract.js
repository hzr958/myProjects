var Analysis = {};

var myInterval;
// 抽取关键词
Analysis.extractKeywords = function(){
	var title = $("#title").val();
	var summary = $("#summary").val();
	if(title == null || title == ""){
		scmpublictoast(analysis.titleShouldNotBeEmpty,1000);
		return;
	}
	if(summary == null || summary == ""){
		scmpublictoast(analysis.summaryShouldNotBeEmpty,1000);
		return;
	}
	$("#start_analysis").css("display","flex");
	$('#imp_pop_bg').css("display","block");
	$.ajax({
		url : '/application/problem/ajaxextract',
		type : 'post',
		dataType:'json',
		async:false,
		data:{
			"title":title,
			"summary":summary
		},
		success : function(data) {
			Analysis.ajaxTimeOut(true,data, function(){
		        if(data && data.result == "success"){		 
		        	$("#regForm").submit();
		        }else if(data && data.result == "none"){
		        	setTimeout(function(){
		        		$("#start_analysis").css("display","none");
		        		$('#imp_pop_bg').css("display","none");
					}, 500);
		        	setTimeout(function(){
		        		scmpublictoast(analysis.haveNoKeyword,1000);
					}, 300);
				}else{
					setTimeout(function(){
		        		$("#start_analysis").css("display","none");
		        		$('#imp_pop_bg').css("display","none");
					}, 100);
		        	setTimeout(function(){
		        		scmpublictoast(analysis.extractKeywordFail,1000);
					}, 100);
		        }
		    });
		},
		error: function(){
			$("#start_analysis").css("display","none");
	    	$('#imp_pop_bg').css("display","none");
			scmpublictoast(analysis.extractKeywordFail,1000);
		}
	});
};
/**
 * 删除关键词
 */
Analysis.deleteElement = function(obj){
	$(obj).closest(".analysis-importantkey_container-item").remove();
}

// 添加样式
Analysis.signSelectKw = function(obj){
	$(".analysis-importantkey_container-item").each(function(){
		$(this).removeClass("analysis-importantkey_container-item-border");
	});
	$(obj).addClass("analysis-importantkey_container-item-border");
	if($("#con_four_1").css("display")=="block"){
		var trendChart = echarts.init(document.getElementById('con_four_1'));
		trendChart.dispose();
		$("#trend_upload").css("display","block");
	}else if($("#con_four_2").css("display")=="block"){
		var trendChart = echarts.init(document.getElementById('con_four_2'));
		trendChart.dispose();
		$("#dis_upload").css("display","block");
	}else if ($("#con_four_3").css("display")=="block"){
		$("#con_four_3").html("");
		$("#researchers_upload").css("display","block");
	}else if ($("#con_four_4").css("display")=="block"){
		var trendChart = echarts.init(document.getElementById('con_four_4'));
		trendChart.dispose();
		$("#ins_upload").css("display","block");
	}
	$("#selectKw").val($(obj).text().trim());
	$("#trend_sign").val("false");
	$("#dis_sign").val("false");
	$("#psn_sign").val("false");
	$("#ins_sign").val("false");
	
}

Analysis.selectKw = function(obj){
	if($("#con_four_1").css("display")=="block"){
		Analysis.researchTrend(obj);
	}else if($("#con_four_2").css("display")=="block"){
		Analysis.relatedDis(obj);
	}else if ($("#con_four_3").css("display")=="block"){
		Analysis.researchers(obj);
	}else if ($("#con_four_4").css("display")=="block"){
		Analysis.institution(obj);
	}
}
Analysis.setTab = function(four,a,b){
	if(a == '1'){
		var inputSelectkeyword = $("#selectKw").val();
		var selectkeyword = $(".analysis-importantkey_container-item-border").text().trim();
		var flag = $("#trend_sign").val();
		setTimeout(setTab('four',1,4),100);
		if((selectkeyword == inputSelectkeyword) && !(flag=='false')){
			return;
		}
		var trendChart = echarts.init(document.getElementById('con_four_1'));
		trendChart.dispose();
		setTimeout(function(){
			$("#trend_upload").css("display","block");
		}, 100);
	}else if(a == '2'){
		var inputSelectkeyword = $("#selectKw").val();
		var selectkeyword = $(".analysis-importantkey_container-item-border").text().trim();
		var flag = $("#dis_sign").val();
		setTimeout(setTab('four',2,4),100);
		if(selectkeyword == inputSelectkeyword && !(flag=='false')){
			return;
		}
		var disChart = echarts.init(document.getElementById('con_four_2'));
		disChart.dispose();
		setTimeout(function(){
			$("#dis_upload").css("display","block");
		}, 100);
	}else if (a == '3'){
		var inputSelectkeyword = $("#selectKw").val();
		var selectkeyword = $(".analysis-importantkey_container-item-border").text().trim();
		var flag = $("#psn_sign").val();
		setTimeout(setTab('four',3,4),100);
		if(selectkeyword == inputSelectkeyword && !(flag=='false')){
			return;
		}
		$("#con_four_3").html("");
		setTimeout(function(){
			$("#researchers_upload").css("display","block");
		}, 100);
	}else if (a == '4'){
		var inputSelectkeyword = $("#selectKw").val();
		var selectkeyword = $(".analysis-importantkey_container-item-border").text().trim();
		var flag = $("#ins_sign").val();
		setTimeout(setTab('four',4,4),100);
		if((selectkeyword == inputSelectkeyword) && !(flag=='false')){
			return;
		}
		var insChart = echarts.init(document.getElementById('con_four_4'));
		insChart.dispose();
		setTimeout(function(){
			$("#ins_upload").css("display","block");
		}, 100);
	}
}
Analysis.researchTrend = function(obj){
	var inputSelectkeyword = $("#selectKw").val();
	var selectkeyword = $(".analysis-importantkey_container-item-border").text().trim();
	var flag = $("#trend_sign").val();
	if((selectkeyword == inputSelectkeyword) && !(flag=='false')){
		return;
	}
	if(inputSelectkeyword == ''){
		inputSelectkeyword = selectkeyword;
		$("#selectKw").val(inputSelectkeyword);
	}
	var trendChart = echarts.init(document.getElementById('con_four_1'));
	trendChart.dispose();
	// 科研趋势
	$.ajax({
		url : '/application/problem/ajaxtrend',
		type : 'post',
		dataType:'json',
		async:false,
		data:{
			"keyword":inputSelectkeyword},
		success : function(data) {
			Analysis.ajaxTimeOut(false, data, function(){
				if(data.result=="success"){
					if(data.resultMap.category.length == 0){
						$("#trend_upload").css("display","none");
					}else{
						Analysis.loadbarTrendEchart(data,trendChart);
						setTimeout(function(){
							$("#trend_upload").css("display","none");
						},100);
						$("#trend_sign").val("true");
					}
				}else{
					scmpublictoast(analysis.trendFail,1000);
				}
			});
		},
		error: function(){
			scmpublictoast(analysis.trendFail,1000);
		}
	});
}
Analysis.relatedDis = function(obj){
	var inputSelectkeyword = $("#selectKw").val();
	var selectkeyword = $(".analysis-importantkey_container-item-border").text().trim();
	var flag = $("#dis_sign").val();
	if(selectkeyword == inputSelectkeyword && !(flag=='false')){
		return;
	}
	if(inputSelectkeyword == ''){
		inputSelectkeyword = selectkeyword;
		$("#selectKw").val(inputSelectkeyword);
	}
	var disChart = echarts.init(document.getElementById('con_four_2'));
	disChart.dispose();
	// 相关学科
	$.ajax({
		url : '/application/problem/ajaxrelateddis',
		type : 'post',
		dataType:'json',
		async:false,
		data:{
			"keyword":inputSelectkeyword},
			success : function(data) {
				Analysis.ajaxTimeOut(false, data, function(){
					if(data.result=="success"){
						if(data.resultMap == null){
							$("#related_dis").css("display","block");
						}else{
							Analysis.loadbarDisEchart(data,disChart);
							setTimeout(function(){
								$("#dis_upload").css("display","none");
							},100);
							$("#dis_sign").val("true");
						}
					}else{
						scmpublictoast(analysis.disciplineFail,1000);
					}
				});
			},
			error: function(){
				scmpublictoast(analysis.disciplineFail,1000);
			}
	});
}
Analysis.researchers = function(obj){
	var inputSelectkeyword = $("#selectKw").val();
	var selectkeyword = $(".analysis-importantkey_container-item-border").text().trim();
	var flag = $("#psn_sign").val();
	if(selectkeyword == inputSelectkeyword && !(flag=='false')){
		return;
	}
	if(inputSelectkeyword == ''){
		inputSelectkeyword = selectkeyword;
		$("#selectKw").val(inputSelectkeyword);
	}
	$("#con_four_3").html("");
// setTimeout(function(){
// $("#researchers_upload").css("display","block");
// },100);
	// 相关学者
	$.ajax({
		url : '/application/problem/ajaxresearchers',
		type : 'post',
		dataType:'json',
		async:false,
		data:{
			"keyword":inputSelectkeyword
			},
		success : function(data) {
			Analysis.ajaxTimeOut(false, data, function(){
				if(data.result=="success"){
					var htmlContent = "<div class=\"big\"><div class=\"tagbox\" id=\"tagbox\">";
					var dataList = data.resultMap;
					console.log(dataList.length + "相关学者 有吗");
					if(dataList.length > 0){
						for(var i = 1;i <= dataList.length;i++){
							var name = dataList[i-1].psnName;
							if(name == null || name == ""){
								name = dataList[i-1].enPsnName;
							}
							var psnShortUrl = dataList[i-1].psnShortUrl;
							var target = "_blank";
							if(psnShortUrl == "###"){
								target = "_self";
							}
							if(i<11){
								htmlContent = htmlContent + "<a target='" + target + "' href='" + psnShortUrl + "' title='" + name + "' class='c" + i + "' style='left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:" + i + "'>" + name + "</a>";
							}else{
								htmlContent = htmlContent + "<a target='" + target + "' href='" + psnShortUrl + "' title='" + name + "' class='c" + (i-10) + "' style='left: 138.381px; top: 14.1595px; font-size: 12px; opacity: 0.459843; z-index:" + i + "'>" + name + "</a>";
							}
						}
						htmlContent = htmlContent + "</div></div>"
						$("#con_four_3").html(htmlContent);
						setTimeout(function(){
							$("#researchers_upload").css("display","none");
						},100);
						$("#psn_sign").val("true");
						// 解决转速为题，不断切换关键词，转速越来越快
						clearInterval(myInterval);
						myInterval = getWords();
					}else{
					  console.log(dataList.length + "相关学者 应该没有");
					  setTimeout(function(){
              $("#researchers_upload").css("display","none");
            },500);
						setTimeout(function(){
							$("#con_four_3").html($(".noresult-div").html());
							$("#con_four_3").children(".noresult").css("display","block");
						}, 500);
						$("#psn_sign").val("true");
					}
				}else{
					scmpublictoast(analysis.personFail,1000);
				}
			});
		},
		error: function(){
			scmpublictoast(analysis.personFail,1000);
		}
	});
}
Analysis.institution = function(obj){
	var inputSelectkeyword = $("#selectKw").val();
	var selectkeyword = $(".analysis-importantkey_container-item-border").text().trim();
	var flag = $("#ins_sign").val();
	if((selectkeyword == inputSelectkeyword) && !(flag=='false')){
		return;
	}
	if(inputSelectkeyword == ''){
		inputSelectkeyword = selectkeyword;
		$("#selectKw").val(inputSelectkeyword);
	}
	var insChart = echarts.init(document.getElementById('con_four_4'));
	insChart.dispose();
	// 相关单位
	$.ajax({
		url : '/application/problem/ajaxintitutions',
		type : 'post',
		dataType:'json',
		async:false,
		data:{
			"keyword":inputSelectkeyword
			},
		success : function(data) {
			Analysis.ajaxTimeOut(false, data, function(){
				if(data.result=="success"){
				  console.log(data.resultMap.category.length + "相关单位长度 有吗");
					if(data.resultMap.category.length == 0){
					  console.log(data.resultMap.category.length + "相关单位长度 应该没有");
						setTimeout(function(){
						  $("#ins_upload").css("display","none");
							$("#con_four_4").html($(".noresult-div").html());
							$("#con_four_4").children(".noresult").css("display","block");
						}, 500);
						$("#ins_sign").val("true");
					}else{
						Analysis.loadbarInstitutionEchart(data,insChart);
						setTimeout(function(){
							$("#ins_upload").css("display","none");
						},100);
						$("#ins_sign").val("true");
					}
				}else{
					scmpublictoast(analysis.institutionFail,1000);
				}
			});
		},
		error: function(){
			scmpublictoast(analysis.institutionFail,1000);
		}
	});
}




Analysis.loadbarTrendEchart = function(data,myChart){
	myChart = echarts.init(document.getElementById('con_four_1'));
	var option = {
			title : {
				text: '众多研究成果聚焦在此关键词有关的领域'
			},
			tooltip : {
				trigger: 'axis'
			},
			legend: {
				data:['论文','专利']
			},
			toolbox: {
				show : true
			},
			calculable : true,
			xAxis : [
				{
					type : 'category',
					name:'年份',
					data : []
				}
				],
				yAxis : [
					{
						type : 'value',
						name: '数量（项）',
					}
					],
					series : [
						{
							name:'论文',
							type:'bar',
							itemStyle :{
								color: '#54abe0'
							},
							data:[],
						},
						{
							name:'专利',
							type:'bar',
							itemStyle :{
								color: '#2882d8'
							},
							data:[],
						}
						]
	};
	option.xAxis[0].data = data.resultMap.category;   
	option.series[0].data = data.resultMap.pubSerisBar;   
	option.series[1].data = data.resultMap.patSerisBar;   
    myChart.setOption(option);
}

Analysis.loadbarInstitutionEchart = function(data,myChart){
	myChart.dispose();
	myChart = echarts.init(document.getElementById('con_four_4'));
	var option = {
			title: {
		        text: '众多科研机构从事此关键词相关领域的研究，以下为研究成果较多的科研单位'
		    },
		    grid: {
		        left: '2%',
		        containLabel: true
		    },
		    tooltip : {
    	        trigger: 'axis'
// triggerOn:'click'
    	    },
    	    toolbox: {
    	        show : true
    	    },
    	    calculable : true,
		    xAxis:  {
		        type: 'value',
		        name: '数量（项）'
		    },
		    yAxis: {
		        type: 'category',
		        name: '科研单位',
		        data: [],
		    },
		    series: [
		    	{
    	            name:'论文',
    	            type:'bar',
		            barMaxWidth : 30,
		            itemStyle :{
	 		            color: '#54abe0'
	 		         },
    	            data:[],
    	        }
		        
		    ]
		};
	var insNames = data.resultMap.category;
	$("#insNames").val(insNames);
	var insNameList = new Array();
	for(var i=0; i<insNames.length; i++){
		if(insNames[i].length > 12){
			insNameList.push(insNames[i].substring(0,12) + "...");
		}else{
			insNameList.push(insNames[i]);
		}
	}
    option.yAxis.data = insNameList;  
    option.series[0].data = data.resultMap.insPubSerisBar; 
    myChart.setOption(option);
    // 注释点击事件
    /*
     * myChart.on("click",function(params){ var insNameStr = $("#insNames").val(); insNameStr =
     * insNameStr.split(','); for(var i=0; i<insNameStr.length; i++){ var name = params.name;
     * if(name.indexOf("...") != -1){//存在... var subName = name.substring(0,name.length-3);
     * if(insNameStr[i].indexOf(subName) != -1){ window.location.href =
     * "/application/problem/toinshomepage?insNameStr=" + insNameStr[i]; } }else
     * if(insNameStr[i].indexOf(name) != -1){ window.location.href =
     * "/application/problem/toinshomepage?insNameStr=" + name; } } });
     */
}

Analysis.loadbarDisEchart = function(data,myChart){
	myChart = echarts.init(document.getElementById('con_four_2'));
	/*
   * var webkitDep = { nodes:[{category: 0, name: "", value: 5, id: 0}, //展示的节点 //category与关系网类别索引对应
   * //我的源数据中没有id属性，这里放出来的是目标数据，id是自动生成的 {category: 1, name: "爸", value: 3, id: 1}, {category: 1,
   * name: "妈", value: 1, id: 2}, {category: 1, name: "哥", value: 1, id: 3}, {category: 1, name:
   * "姐", value: 3, id: 4}, {category: 1, name: "我", value: 1, id: 5},
   * 
   * {category: 2, name: "我1", value: 1, id: 6}, {category: 2, name: "我2", value: 1, id: 7},
   * {category: 2, name: "我3", value: 1, id: 8}, {category: 2, name: "我4", value: 1, id: 9},
   * {category: 2, name: "我5", value: 1, id: 10}, {category: 2, name: "哥1", value: 1, id: 11},
   * {category: 2, name: "哥2", value: 1, id: 12}, {category: 2, name: "哥3", value: 1, id: 13},
   * {category: 2, name: "哥4", value: 1, id: 14}, {category: 2, name: "哥5", value: 1, id: 15} ],
   * links:[{source: 0, target: 1, value: 5}, //节点之间连接 //source起始节点，0表示第一个节点
   * //target目标节点，1表示与索引(id)为1的节点进行连接 {source: 0, target: 2, value: 5}, {source: 0, target: 3,
   * value: 5}, {source: 0, target: 4, value: 5}, {source: 0, target: 5, value: 50}, {source: 5,
   * target: 6, value: 5}, {source: 5, target: 7, value: 5}, {source: 5, target: 8, value: 5},
   * {source: 5, target: 9, value: 5}, {source: 5, target: 10, value: 5}, {source: 3, target: 11,
   * value: 5}, {source: 3, target: 12, value: 5}, {source: 3, target: 13, value: 5}, {source: 3,
   * target: 14, value: 5}, {source: 3, target: 15, value: 5} ] }
   */
	var webkitDep = { 
			// 展示的节点 //category与关系网类别索引对应 //我的源数据中没有id属性，这里放出来的是目标数据，id是自动生成的
			nodes:[data.resultMap.nodes],
			 // 节点之间连接 //source起始节点，0表示第一个节点 //target目标节点，1表示与索引(id)为1的节点进行连接
			links:[data.resultMap.links]
	}
	webkitDep.nodes = data.resultMap.nodes;	
	webkitDep.links = data.resultMap.links;	
	var option = { 
// legend: {
// x: 'left',//图例位置 //图例的名称 //此处的数据必须和关系网类别中name相对应
// data: webkitDep.categories.map(function (a) {
// return a.name;
// })
// },
			title: {
		        text: '根据知识图谱技术，此关键词所属学科，及其关联学科的热门关键词'
		        // subtext: '数据来自网络'
		    },
			series: [{ 
				type: 'graph', 
				layout: 'force', 
				// animation: false,
				label: { 
					normal: { 
						show:true, 
						position: 'right' 
					} 
				}, 
				itemStyle :{
 		            color: '#54abe0'
 		         },
				draggable: true, 
				force: { 
					layoutAnimation:true, 
					// xAxisIndex : 0, //x轴坐标 有多种坐标系轴坐标选项 //
					// yAxisIndex : 0, //y轴坐标
					gravity:0.03, // 节点受到的向中心的引力因子。该值越大节点越往中心点靠拢。
					edgeLength: 55, // 边的两个节点之间的距离，这个距离也会受 repulsion。[10, 50] 。值越小则长度越长
					repulsion: 50 // 节点之间的斥力因子。支持数组表达斥力范围，值越大斥力越大。
				}, 
				data: webkitDep.nodes.map(function (node, idx) { // node数据
					node.id = idx; 
					return node; 
				}), 
				categories: webkitDep.categories, // 关系网类别，可以写多组
				edges: webkitDep.links // link数据
			}] 
	}; 
	myChart.setOption(option);
}



Analysis.calMax =function(pubSerisbar,patSerisbar,prjSerisbar) {  
    var pubMax = pubSerisbar[0];  
    for ( var i = 1; i < pubSerisbar.length; i++) {// 求出一组数组中的最大值
        if (pubMax < pubSerisbar[i]) {  
        	pubMax = pubSerisbar[i];  
        }  
    }  
    var patMax = patSerisbar[0]; 
    for ( var i = 1; i < patSerisbar.length; i++) {// 求出一组数组中的最大值
    	if (patMax < patSerisbar[i]) {  
    		patMax = patSerisbar[i];  
    	}  
    }  
    var pubMaxInt = Math.ceil(pubMax / 10);// 向上取整
    var patMaxInt = Math.ceil(patMax / 10);// 向上取整
    var pubmaxval = pubMaxInt * 10;// 最终设置的最大值
    var patmaxval = patMaxInt * 10;// 最终设置的最大值
    return pubmaxval > patmaxval ? pubmaxval : patmaxval;// 输出最大值
};  

Analysis.back = function(){
	window.location.href = '/application/problem/analysis';  
}

// 新的超时处理，在当前页面弹出登录框
Analysis.ajaxTimeOut = function(flag,data,myfunction){
	 var toConfirm=false;
		
		if('{"ajaxSessionTimeOut":"yes"}'==data){
			toConfirm = true;
		}
		if(!toConfirm&&data!=null){
			toConfirm=data.ajaxSessionTimeOut;
		}
		if(toConfirm){
		    ScmLoginBox.showLoginToast();
		    if(flag){
		    	$("#start_analysis").css("display","none");
		    	$('#imp_pop_bg').css("display","none");
		    }
		}else{
			if(typeof myfunction == "function"){
				myfunction();
			}
		}
}