/**
 * 重复成果js
 */
var RepeatPub =RepeatPub!=null?RepeatPub:{};
RepeatPub.index = 0;
RepeatPub.Second = 5;
/**
 * 重复成果显示title入口
 */
RepeatPub.ajaxshowrepeatpubmain=function(){
	BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
		var param={};
		$.ajax({
			url : '/pub/repeatpub/maintitle',
			type : 'post',
			dataType:'json',
			data:param,
			success : function(data) {
				BaseUtils.ajaxTimeOut(data,function(){
					if(data.result=="success"){
						$("#id_repeat_pub_title").show();
						$("#id_repeat_pub_msg").html(data.msg);
						$("#id_repeat_pub_dealwith").html(data.dealwith);
						$("#id_repeat_pub_title").attr("box_title",data.box_title).attr("count",data.count);
						RepeatPub.recordIdArr=data.records;
					}else{
						$("#id_repeat_pub_title").hide();
					}
				});
			}
		});
	});
	
}
/**
 * 显示重复成果列表页面
 */
RepeatPub.showrepeatpubUI = function(ev){
	BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
		if(ev){
			BaseUtils.doHitMore($(ev.currentTarget));
		}
		var count = $("#id_repeat_pub_title").attr("count");
		if(count>0&&typeof Similarresults=="function"){
			Similarresults({
				"box_title":$("#id_repeat_pub_title").attr("box_title"),
				"pageSize":count
			});
			// 只有一条数据时，下一组不可点
			if(RepeatPub.recordIdArr!=null && RepeatPub.recordIdArr.length>1){
				$(".go-down_group").css("color","#288aed");
			}
			RepeatPub.ajaxgetrepeatpublist();
		}	
	});
	
}
/**
 * 关闭重复成果列表页面
 */
RepeatPub.closerepeatpubUI = function(ev){
	BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
		if(ev){
			BaseUtils.doHitMore($(ev.currentTarget));
		}
		RepeatPub.ajaxshowrepeatpubmain();
	});
	
}



/**
 * 获取重复成果item信息
 */
RepeatPub.ajaxgetrepeatpublist = function(){
	BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
		RepeatPub.loadStateIco();
		if(RepeatPub.recordIdArr!=null){
			RepeatPub.ajaxGetRepeatPubPage(RepeatPub.recordIdArr[0]);
		}else{
			$("#id_repeat_pub_box").html("<div  class='main-list__list item_no-border'><div class='response_no-result'>"+homepage.noRecord+"</div></div>");
		}	
	});
}

/**
 * 请求重复成果信息
 */
RepeatPub.ajaxGetRepeatPubPage = function(des3RecordId){
	BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
		var param = {"des3RecordId":des3RecordId};
		$.ajax({
			url: "/pub/ajaxrepeatpublist",
			type: "post",
			dataType: "html",
			data: param,
			success: function(data){
				BaseUtils.ajaxTimeOut(data,function(){
					$("#id_repeat_pub_box").html(data);
					$("#id_keep_all").show();
					var itmes = $("#id_repeat_pub_box").find(".dev_repeatpub_item[dealstatus='0']");
					if(itmes!=null&&itmes.length>0){
						$("#id_keep_all").show();
						$("#id_keep_all").css("color","#288aed");
						// 控制高度
						if(itmes.length > 3){
							$("#id_keep_all").css("margin-top","0px");
							$("#id_keep_all").css("margin-right","16px");
							$(".Similarresults-footer").eq(0).css("margin-right","16px");
							
							// 显示自己画的线  id_repeat_pub_top_line，同时去除Similarresults-header的border-bottom属性 
							$("#id_repeat_pub_top_line").css("opacity","1");
							$(".Similarresults-header").eq(0).css("border-bottom","0px solid #ddd");
							// 最近更新时间的按钮
							$(".Similarresults-body_uploadtime").css("padding-right","6px");
						}else{
							$("#id_keep_all").css("margin-top","10px");
							$("#id_keep_all").css("margin-right","0px");
							$(".Similarresults-footer").eq(0).css("margin-right","0px");
							
							// 不显示自己画的线  id_repeat_pub_top_line，同时不去除Similarresults-header的border-bottom属性 
							$("#id_repeat_pub_top_line").css("opacity","0");
							$(".Similarresults-header").eq(0).css("border-bottom","1px solid #ddd");
							
							// 最近更新时间的按钮
							$(".Similarresults-body_uploadtime").css("padding-right","0px");
						}
					}else{
						// $("#id_keep_all").hide();
						$("#id_keep_all").css("color","#999");
					}
				});		
			}
		});
	});
}

/**
 * 下一页
 */
RepeatPub.shownextpage = function(ev){
	BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
		$("#id_keep_all").hide();
		if(ev){
			BaseUtils.doHitMore($(ev.currentTarget));
		}
		RepeatPub.loadStateIco();
		//TODO 发请求根据下一个组id获取下一个页面
		if(RepeatPub.recordIdArr != null){
			RepeatPub.index = Number($("#id_repeat_pub_box_pageNo").html())-1;
			// 关闭定时器
			RepeatPub.timerClose();
			if(RepeatPub.index < RepeatPub.recordIdArr.length-1){
				RepeatPub.ajaxGetRepeatPubPage(RepeatPub.recordIdArr[++RepeatPub.index]);
				$("#id_repeat_pub_box_pageNo").html(Number($("#id_repeat_pub_box_pageNo").html())+1);
				if(RepeatPub.index==RepeatPub.recordIdArr.length-1){
					$(".go-down_group").css("color","#999");
				}
				$(".go-up_group").css("color","#288aed");
			}
		}
		
	});
	
}
/**
 * 上一页
 */
RepeatPub.showlastpage = function(ev){
	BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
		$("#id_keep_all").hide();
		if(ev){
			BaseUtils.doHitMore($(ev.currentTarget));
		}
		RepeatPub.loadStateIco();
		//TODO 发请求根据上一个组id获取上一个页面
		if(RepeatPub.recordIdArr != null){
			RepeatPub.index = Number($("#id_repeat_pub_box_pageNo").html())-1;
			if(RepeatPub.index > 0){
				// 关闭定时器
				RepeatPub.timerClose();
				RepeatPub.ajaxGetRepeatPubPage(RepeatPub.recordIdArr[--RepeatPub.index]);
				$("#id_repeat_pub_box_pageNo").html(Number($("#id_repeat_pub_box_pageNo").html())-1);
				if(RepeatPub.index==0){
					$(".go-up_group").css("color","#999");
				}
				$(".go-down_group").css("color","#288aed");
			}
		}	
	});
}
/**
 *删除
 */
RepeatPub.itemdelete = function(ev){
	var $this = $(ev.currentTarget); 
	BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
		BaseUtils.doHitMore($this);

		var des3pubSameItemId = $this.closest(".dev_repeatpub_item").attr("des3pubSameItemId");	
		if(des3pubSameItemId==null||des3pubSameItemId==""){
			console.log("缺失重复成果记录id");
			return;
		}
		$this.closest(".dev_repeatpub_item").attr("dealStatus","2");
		$this.closest(".dev_repeatpub_item").find(".dev_del_btn").attr("onclick","").css("color","#999").css("border","#999").html(homepage.repeatpubdel);
		var items = $("#id_repeat_pub_box").find(".dev_repeatpub_item[dealStatus='0']");
		if(items!=null&&items.length==1){
			if(RepeatPub.recordIdArr != null&&RepeatPub.index < RepeatPub.recordIdArr.length-1){
				items.find(".dev_del_btn").attr("onclick","").css("color","#999").css("border","#999").html(homepage.repeatpubkeep);
				// RepeatPub.shownextpage();
				// 全部保留按钮置灰
				$("#id_keep_all").css("color","#999");
				RepeatPub.autoNextGroup();
			}
		}
		
		if(items!=null&&items.length==1){
			if(!(RepeatPub.recordIdArr != null&&RepeatPub.index < RepeatPub.recordIdArr.length-1)){
				//$(".Similarresults-close").click();
				items.find(".dev_del_btn").attr("onclick","").css("color","#999").css("border","#999").html(homepage.repeatpubkeep);
				// 全部保留按钮置灰
				$("#id_keep_all").css("color","#999");
			}
		}
		
		var param = {};
		param.des3pubSameItemId=des3pubSameItemId;
		$.ajax({
			url : '/pub/repeatpub/del',
			type : 'post',
			dataType:'json',
			data:param,
			success : function(data) {
				BaseUtils.ajaxTimeOut(data,function(){
					if(data.result=="success"){
						if(items!=null&&items.length==1){
							if(!(RepeatPub.recordIdArr != null&&RepeatPub.index < RepeatPub.recordIdArr.length-1)){
								//$(".Similarresults-close").click();
								// 全部保留按钮置灰
								$("#id_keep_all").css("color","#999");
							}
						}
					}
				});
			}
		});
		
	});
}
/**
 * 全部保留
 */
RepeatPub.keepAll = function(ev){
	BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
		// 防止重复点击
		if(ev){
			BaseUtils.doHitMore($(ev.currentTarget));
		}
		// 判断全部保留按钮是否为蓝色 #288aed  是的话方可操作
		if($("#id_keep_all").css("color") != "rgb(40, 138, 237)"){
			return;
		}
		var des3RecordId = $("#id_repeat_pub_box").find(".dev_repeatpub_item").eq(0).attr("des3RecordId");
		if(des3RecordId==null||des3RecordId==""){
			console.log("缺失重复成果记录组id");
			return;
		}
		if(RepeatPub.recordIdArr != null&&RepeatPub.index < RepeatPub.recordIdArr.length-1){
			var items = $("#id_repeat_pub_box").find(".dev_repeatpub_item[dealStatus='0']");
			items.find(".dev_del_btn").attr("onclick","").css("color","#999").css("border","#999").html(homepage.repeatpubkeep);
			// RepeatPub.shownextpage();
			// 全部保留按钮置灰
			$("#id_keep_all").css("color","#999");
			RepeatPub.autoNextGroup();
		}
		if(!(RepeatPub.recordIdArr != null&&RepeatPub.index < RepeatPub.recordIdArr.length-1)){
			var items = $("#id_repeat_pub_box").find(".dev_repeatpub_item[dealStatus='0']");
			items.find(".dev_del_btn").attr("onclick","").css("color","#999").css("border","#999").html(homepage.repeatpubkeep);
			// 全部保留按钮置灰
			$("#id_keep_all").css("color","#999");
		}
		var param = {};
		param.des3RecordId=des3RecordId;
		$.ajax({
			url : '/pub/repeatpub/keep',
			type : 'post',
			dataType:'json',
			data:param,
			success : function(data) {
				BaseUtils.ajaxTimeOut(data,function(){
					if(data.result=="success"){
					}
				});
			}
		});
		
	});
	
}
/**
 * 加载进程样式
 */
RepeatPub.loadStateIco = function(){
	$("#id_repeat_pub_box").doLoadStateIco({
		"style" : "height: 38px; width:38px; margin:auto ; margin-top:50px;",
		"status" : 1
	});
}

/**
 * 添加定时器:操作完本成果重复项目后5s自动进入下一组
 * 
 * 5s跳转
 * 
 */

RepeatPub.autoNextGroup = function(){
	$("#id_timer").css("color","#999");
	RepeatPub.Second=2;
	$("#id_timer").html(homepage.repeatPubAutoToRecord+"（3）");
	RepeatPub.interval = setInterval("RepeatPub.timerOpen()",1000);
}

/**
 * 定时器执行函数
 */
RepeatPub.timerOpen = function(){
	if(RepeatPub.Second <= 5 && RepeatPub.Second > 0){
		// 显示在界面上
		$("#id_timer").html(homepage.repeatPubAutoToRecord+"（"+(RepeatPub.Second--)+"）");
	} else if(RepeatPub.Second == 0){
		// 自动跳转下一组
		RepeatPub.shownextpage();
		// 关闭定时器
		RepeatPub.timerClose();
		// 清空显示框
		$("#id_timer").html("");
	}
}

/**
 * 清除定时器
 */
RepeatPub.timerClose = function(){
	window.clearInterval(RepeatPub.interval);
	// 清空显示框
	$("#id_timer").html("");
}


