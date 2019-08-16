var Pubdetails = Pubdetails?Pubdetails:{};

Pubdetails.heightDdjustment = function(){
	var detail_top = $(".detail-main__main").offset().top;
	if(detail_top<10){
		$(".detail-main__main").css("margin-top","50px");
	}
};
Pubdetails.initBuidName = function(){
	$(".detail-pub__cognize-item[status='1']").mouseenter(function() {
		var $this = $(this);
		$(".detail-pub__cognize-toast").slideUp();
		$this.find(".detail-pub__cognize-toast").stop(true).slideDown();	
/*		var $b = $this.find(".dev_b_span");
		$b.show();*/
		}).mouseleave(function() {
			var $this = $(this);
			$this.find(".detail-pub__cognize-toast").stop(true).slideUp();
		$("#showPurchase").slideUp();
		});
/*	$(".detail-pub__cognize-toast_container-footer").click(function(){
		var $this = $(this);
		var $b = $this.closest(".detail-pub__cognize-toast_container-footer").find(".dev_b_span");
		$b.hide();
		$this.closest(".detail-pub__cognize-toast").find(".detail-pub__cognize-toast_container:gt(1)").slideToggle();
	});*/
}
Pubdetails.sendMsg =function(des3PsnId,obj){
	window.open("/dynweb/showmsg/msgmain?model=chatMsg&des3ChatPsnId="+des3PsnId);
}
//单个添加联系人
Pubdetails.addOneFriend =function(reqPsnId,obj){
	//防止重复点击
	if(obj){
		BaseUtils.doHitMore(obj,100);
	}
	$.ajax({
		url:'/psnweb/friend/ajaxaddfriend',
		type:'post',
		data:{'des3Id':reqPsnId},
		dataType:'json',
		timeout:10000,
		success:function(data){
			Pubdetails.ajaxTimeOut(data, function(){
				if (data.result == "true") {
					scmpublictoast(pubdetails.addFriendSuccess,1000);
				} else{
					scmpublictoast(data.msg,1000);
				}
			});
		}
	});
};


//发表评论
Pubdetails.comment =function(des3PubId){
	var replyContent=$.trim($("#pubComment").find("textarea[name$='comments']").val());
	$.ajax({
		url :'/pubweb/comment/ajaxaddpubreply',
		type :'post',
		data : {"des3PubId" :des3PubId,"replyContent" :replyContent,"articleType":1},
		dataType : "json",
		success:function(data){
			Pubdetails.ajaxTimeOut(data , function(){
				/*Pubdetails.getComment(des3PubId);*/
				Pubdetails.getCommentList(des3PubId);
				Pubdetails.getCommentNumber(des3PubId);
				$("#pubComment").find("textarea[name$='comments']").val("");
				$("#pubCommnetBtn").attr("disabled", "disabled");
				
			});
		}
		
	});
}
/*//初始化评论列表
Pubdetails.getComment =function(des3PubId){
	$.ajax({
		url :'/pubweb/pubdetails/comment',
		type :'post',
		data : {"des3PubId" :des3PubId},
		dataType : "html",
		success:function(data){
			$("#pubCommentList").html("");
			$("#pubCommentList").append(data);
		}
		
	});
}*/
//初始化评论列表
Pubdetails.getCommentList =function(des3PubId){ 
	window.Mainlist({
		name:"pubCommentList",
		listurl: "/pubweb/pubdetails/comment",
		listdata: {'des3PubId':des3PubId},
		listcallback: function(){
			addFormElementsEvents(document.getElementById("pubComment"));
		},
		method: "scroll",
	})
	
}
//初始化基准库的评论列表
Pubdetails.getPdwhCommentList =function(des3PubId){ 
	window.Mainlist({
		name:"pubPdwhCommentList",
		listurl: "/pubweb/pubdetails/commentpdwh",
		listdata: {'des3PubId':des3PubId},
		listcallback: function(){
			addFormElementsEvents(document.getElementById("pubComment"));
		},
		method: "scroll",
	})
	
}
//检查基准库成果是否存在
Pubdetails.pdwhIsExist = function(des3PubId,func){
  $.ajax({
    url:"/pub/opt/ajaxpdwhisexists",
    type : 'post',
    dataType:"json",
    async : false,
    data:{"des3PubId":des3PubId},
    timeout : 10000,
    success: function(data){
      Pub.ajaxTimeOut(data,function(){
        if(data.result == 'success'){
          func();
        } else{
          scmpublictoast("成果不存在", 1000);
        }
      });
    },
    error: function(data){
      scmpublictoast("系统出错", 1000);
    }
  });
}

//基准库发表评论
Pubdetails.pdwhComment =function(des3PubId,dbId){
  Pubdetails.pdwhIsExist(des3PubId,function(){
    var replyContent=$.trim($("#pubComment").find("textarea[name$='comments']").val());
    $.ajax({
      url :'/pubweb/comment/ajaxaddcommentpdwh',
      type :'post',
      data : {"des3PubId" :des3PubId,"replyContent" :replyContent,"dbid" :dbId},
      dataType : "json",
      success:function(data){
        Pubdetails.ajaxTimeOut(data , function(){
          Pubdetails.getPdwhCommentList(des3PubId);
          Pubdetails.getPdwhCommentNumber(des3PubId);
          $("#pubComment").find("textarea[name$='comments']").val("");
          $("#pubCommnetBtn").attr("disabled", "disabled");
          
        });
      }
      
    });
  });
}
//添加评论数
Pubdetails.getCommentNumber =function(des3PubId){
	$.ajax({
		url :'/pubweb/pubdetails/commentnumber',
		type :'post',
		data : {"des3PubId" :des3PubId},
		dataType : "json",
		success:function(data){
			Pubdetails.ajaxTimeOut(data , function(){
				if(data=='0'){
					$("#pubTotalComent").text(pubdetails.commentTips);
				}else{
					$("#pubTotalComent").text(pubdetails.commentTips+'('+data+')');
				}
				
			});
		}
		
	});
}
//基准库加载评论数
Pubdetails.getPdwhCommentNumber =function(des3PubId){
	$.ajax({
		url :'/pubweb/pubdetails/ajaxpdwhcommentnumber',
		type :'post',
		data : {"des3PubId" :des3PubId},
		dataType : "json",
		success:function(data){
			Pubdetails.ajaxTimeOut(data , function(){
				if(data=='0'||data ==null){
					$("#pubTotalComent").text(pubdetails.commentTips);
				}else{
					$("#pubTotalComent").text(pubdetails.commentTips+'('+data+')');
					$("#pubTotalComentOp").text(pubdetails.comment+'('+data+')');
				}
				
			});
		}
		
	});
}
//取消评论
Pubdetails.cancelComment =function(){
    $("#pubCommnetBtn").hide();
    $("#pubCommnetCancle").hide();
    $("#pubComment").find("textarea[name$='comments']").val("");
}
//点击相关评论
Pubdetails.relatedComment =function(des3PubId){
	Pubdetails.getCommentList(des3PubId);
	Pubdetails.getCommentNumber(des3PubId);
	$("#pubComment").find("textarea[name$='comments']").val("");
	$("#pubCommnetBtn").attr("disabled", "disabled");
}
//基准库点击相关评论
Pubdetails.relatedPdwhComment =function(des3PubId){
	Pubdetails.getPdwhCommentList(des3PubId);
	Pubdetails.getPdwhCommentNumber(des3PubId);
	$("#pubComment").find("textarea[name$='comments']").val("");
	$("#pubCommnetBtn").attr("disabled", "disabled");
}
//收藏功能
Pubdetails.collection =function(pubId,ownerId){
	var params=[];
	params.push({"pubId":pubId,"ownerId":ownerId});
	var postData={"jsonParams":JSON.stringify(params),"articleType":"2"};
	$.ajax({
		url :'/pubweb/pubdetails/ajaxtomyliteratrue',
		type :'post',
		data : postData,
		dataType : "json",
		success:function(data){
			Pubdetails.ajaxTimeOut(data , function(){
				 if(data.result=="success"){
					   scmpublictoast(pubdetails.CollectionSuccess,2000);
				   }else{
					   scmpublictoast(pubdetails.CollectionFail,2000);
				   }
				
			});
		}
		
	});
}

//站外-操作-统一提示登录
Pubdetails.outsideTip = function() {
	$.ajax({
		url : "/psnweb/timeout/ajaxtest",
		type : "post",
		dataType : "json",
		success : function(data) {
			Pubdetails.ajaxTimeOut(data,function(){});
		}
	});
};

//超时处理
Pubdetails.ajaxTimeOut = function(data,myfunction){
	var toConfirm=false;
	if(data.ajaxSessionTimeOut == 'yes'){
		toConfirm = true;
	}
	if(!toConfirm&&data!=null){
		toConfirm=data.ajaxSessionTimeOut;
	}
	if(toConfirm){
		jConfirm(pubdetails.i18n_timeout, pubdetails.i18n_tipTitle, function(r) {
			if (r) {
				var url = window.location.href;
				if (url.indexOf("/pubweb/outside/pdwhdetails") > 0) {
					document.location.href = url.replace("/pubweb/outside/pdwhdetails","/pubweb/details/showpdwh");
					return 0;
				}
				document.location.href = "/oauth/index?sys=SNS&service=" + encodeURIComponent(url);
			}
		});
	}else{
		if(typeof myfunction == "function"){
			myfunction();
		}
	}
};

/**
 * 添加阅读记录
 */
Pubdetails.addReadRecord = function(){
	$.ajax({
		url :'/pubweb/statistics/ajaxaddreadrecord',
		type :'post',
		data : {
			"actionDes3Key": $("#des3PubId").val(),
			"readPsnDes3Id": $("#ownerPsnId").val(),
			"actionType" : 1,
			"readType": "detailRead"
		},
		dataType : "json",
		success:function(data){
			
		},
		error: function(){
			
		}
		
	});
}


/**
 * 添加访问记录
 */
Pubdetails.addVistRecord = function(vistPsnDes3Id,actionDes3Key,actionType){
	if (vistPsnDes3Id != null && vistPsnDes3Id != "" && actionDes3Key != null && actionDes3Key != "" && actionType != null && actionType != "") {
		$.ajax({
			url: "/psnweb/outside/addVistRecord",
			type:"post",
			dataType:"json",
			data:{"vistPsnDes3Id":vistPsnDes3Id,"actionDes3Key":actionDes3Key,"actionType":actionType},
			success:function(data){
			
			},
			error:function(){
			}
		});
	}
}

/**
 * 阅读记录添加后，更新人员成果影响力
 */
Pubdetails.updatePsnEffect = function(){
	$.ajax({
		url:"/pubweb/statistics/updatepsneffect",
		type:"post",
		dataType:"json",
		data:{"readPsnDes3Id": $("#ownerPsnId").val()},
		success:function(data){
		
		},
		error:function(){
		}
	});
}
Pubdetails.rcmFulltext = function(){
	$.ajax({
		url:"/pubweb/psn/ajaxgetpubdetailrcmdpubft",
		type:"post",
		data:{"des3PubId":$("#des3PubId").val(),
			  "ownerDes3PsnId":$("#ownerPsnId").val()
			  },
		dataType:"html",
		success:function(data){
			$("#rcmdPubft").html(data);
	           if (document.getElementsByClassName("confirm-fulltext")[0]) {
				   var goheight = document.getElementsByClassName("bckground-cover")[0].offsetHeight;
		           var gowidth = document.getElementsByClassName("bckground-cover")[0].offsetWidth;
		           var setheight = document.getElementsByClassName("confirm-fulltext")[0].offsetHeight;
		           var setwidth = document.getElementsByClassName("confirm-fulltext")[0].offsetWidth;
		           document.getElementsByClassName("confirm-fulltext")[0].style.bottom = (goheight - setheight)/2 + "px"; 
		           document.getElementsByClassName("confirm-fulltext")[0].style.left = (gowidth - setwidth)/2 + "px"; 
		           document.getElementsByClassName("confirm-fulltext_header-close")[0].onclick = function(){
		               document.getElementsByClassName("confirm-fulltext")[0].style.bottom = - setheight + "px";
		               setTimeout(function(){
		                   document.getElementsByClassName("bckground-cover")[0].removeChild(document.getElementsByClassName("confirm-fulltext")[0]);
		                   document.getElementById("rcmdPubft").removeChild(document.getElementsByClassName("bckground-cover")[0]);
		               }, 500);
		           }
			}
		},
		error:function(){
			
		}
	});
}
//是我的全文成果
Pubdetails.doConfirmPubft =function(Id){
	closeRcmdFulltext();
	$.ajax( {
		url : '/pubweb/pubftrcmd/ajaxConfirmPubft',
		type : 'post',
		dataType : 'json',
		data : {'ids':Id},
		success : function(data) {
			Pub.ajaxTimeOut(data,function(){
				if (data.result == "success") {
					scmpublictoast(pubdetails.rcmdft_success, 2000);
				}else{
					scmpublictoast(pubdetails.rcmdft_error, 2000);
				}
			});
		},
		error:function(){
			scmpublictoast(pubdetails.optFailed, 2000);
		}
	});
};

//不是我的全文成果
Pubdetails.doRejectPubft =function(Id){
	closeRcmdFulltext();
	$.ajax( {
		url : '/pubweb/pubftrcmd/ajaxRejectPubft',
		type : 'post',
		dataType : 'json',
		data : {'ids' : Id},
		success : function(data) {
			Pub.ajaxTimeOut(data,function(){
				if (data.result == "success") {
					scmpublictoast(pubdetails.optSuccess, 2000);
					window.location.reload();
				}else{
					scmpublictoast(pubdetails.optFailed, 2000);
				}
			});
		},
		error:function(){
			scmpublictoast(pubdetails.optFailed, 2000);
		}
	});
};
/**
 * 个人成果请求全文
 * @author houchuanjie
 */
Pubdetails.requestFullText = function requestFullText(des3RecvPsnId, des3PubId){
	if(des3RecvPsnId && des3PubId){
		$.post("/pubweb/fulltext/req/ajaxadd", {'des3RecvPsnId': des3RecvPsnId, 'des3PubId': des3PubId, 'pubType': 'sns'}, function(result){
			Pubdetails.ajaxTimeOut(result , function(){
				if(result.status == "success"){	//全文请求保存成功
					scmpublictoast(pubdetails.req_success, 2000);
				}else{
					scmpublictoast(pubdetails.req_error, 2000);
				}
			});
		},"json");
	}else{
		scmpublictoast(pubdetails.req_param_error, 2000);
	}
	
	
}
/**
 * 基准库成果请求全文
 * @author houchuanjie
 */
Pubdetails.requestPdwhFullText = function(des3PubId){
	if(des3PubId){
		$.post("/pubweb/fulltext/req/ajaxadd", {'des3PubId': des3PubId, 'pubType': 'pdwh'}, function(result){
			Pubdetails.ajaxTimeOut(result , function(){
				if(result.status == "success"){
					scmpublictoast(pubdetails.req_success, 2000);
				}else{
					scmpublictoast(pubdetails.req_error, 2000);
				}
			});
		},"json");
	}else{
		scmpublictoast(pubdetails.req_param_error, 2000);
	}
}
/**
 * 基准库收藏成果 publicationArticleType
 */
Pubdetails.importPdwh =function (pubId){
	var params=[];
	params.push({"pubId":pubId});
	var postData={"jsonParams":JSON.stringify(params),"publicationArticleType":"2"};
	$.ajax({
		url:"/pubweb/ajaxpublication/import/pdwh",
		type:"post",
		dataType:"json",
		data : postData,
		success:function(data){
			Pubdetails.ajaxTimeOut(data , function(){
				 if(data.result==true){
					   scmpublictoast(pubdetails.CollectionSuccess,2000);
				   }else{
					   scmpublictoast(pubdetails.CollectionFail,2000);
				   }
				
			});
		
		},
		error:function(){
		}
	});
	
}
//替代原有的基准库收藏
Pubdetails.ajaxSavePdwh = function(des3PubId){
	$.ajax({ 
		url:"/pubweb/ajaxsavePaper",
		type:"post",
		dataType:"json",
		data:{"des3PubId":des3PubId,"pubDb":"PDWH"},
		success:function(data){
			Pubdetails.ajaxTimeOut(data, function(){
				if(data && data.result){
					if(data.result == "success"){
						scmpublictoast(pubdetails.CollectionSuccess,1000);
					}else if(data.result == "exist"){
						scmpublictoast(pubdetails.pubIsSaved,1000);
					}else if(data.result == "isDel"){
						scmpublictoast(pubdetails.pubIsDeleted,1000);
					}else{
						scmpublictoast(pubdetails.CollectionFail,1000);
					}
				}else{
					scmpublictoast(pubdetails.CollectionFail,1000);
				}
			});
		}
	});
}
/**
 * 基准库收藏成果 publicationArticleType
 *//*
Pubdetails.importPdwh =function (des3PubId,obj){
	if(obj){
		BaseUtils.doHitMoreAndchangF(obj,500,"Pubdetails.delCollectedPub('"+des3PubId+"',this);");
	}
	$.ajax({
		url:"/pubweb/ajaxsavePaper",
		type:"post",
		dataType:"json",
		data : {"des3PubId":des3PubId,"pubDb":"PDWH"},
		success:function(data){
			BaseUtils.ajaxTimeOut(data , function(){
				if(data && data.result){
					if(data.result == "success"){
						$(obj).find(".spirit-icon__favorite-outline")
						.removeClass("spirit-icon__favorite-outline").addClass("spirit-icon__favorite");
			            $(obj).find("span").text(pubdetails.unsave);
						scmpublictoast(pubdetails.CollectionSuccess,1000);
					}else if(data.result == "exist"){
						scmpublictoast(pubdetails.pubIsSaved,1000);
					}else if(data.result == "isDel"){
						scmpublictoast(pubdetails.noPub,1000);
					}else{
						scmpublictoast(pubdetails.CollectionFail,1000);
					}
				}else{
					scmpublictoast(pubdetails.CollectionFail,1000);
				}
				
			});
		
		},
		error:function(){
		}
	});
	
}
//基准库成果-取消收藏

Pubdetails.delCollectedPub = function(pubId,obj){
	if(obj){
		BaseUtils.doHitMoreAndchangF(obj,500,"Pubdetails.importPdwh('"+pubId+"',this);");
	}
	$.ajax({
		url:"/pubweb/ajaxdelCollectedPub",
		type:'post',
		data:{"des3PubId":pubId,"pubDb":"PDWH"},
		success:function(data){
		    BaseUtils.ajaxTimeOut(data, function(){
		        if(data && data.result == "success"){
					$(obj).find(".spirit-icon__favorite").removeClass("spirit-icon__favorite").addClass("spirit-icon__favorite-outline");
		            $(obj).find("span").text(pubdetails.save);
		            scmpublictoast(pubdetails.optSuccess,1000);	
		        }else{
		            scmpublictoast(pubdetails.optFailed,1000);
		        }
		    });
		}
	});
}
*/
//替代原有的个人库收藏
Pubdetails.ajaxSaveSns = function(des3PubId){
	$.ajax({ 
		url:"/pubweb/ajaxsavePaper",
		type:"post",
		dataType:"json",
		data:{"des3PubId":des3PubId,"pubDb":"SNS"},
		success:function(data){
			Pubdetails.ajaxTimeOut(data, function(){
				if(data && data.result){
					if(data.result == "success"){
						scmpublictoast(pubdetails.CollectionSuccess,1000);
					}else if(data.result == "exist"){
						scmpublictoast(pubdetails.pubIsSaved,1000);
					}else if(data.result == "isDel"){
						scmpublictoast(pubdetails.pubIsDeleted,1000);
					}else{
						scmpublictoast(pubdetails.CollectionFail,1000);
					}
				}else{
					scmpublictoast(pubdetails.CollectionFail,1000);
				}
			});
		}
	});
}


/**
 * 添加基准库成果阅读记录
 */
Pubdetails.addPdwhPubReadRecord = function(){
	$.ajax({
		url :'/pubweb/readrecord/ajaxadd',
		type :'post',
		data : {
			"actionDes3Key": $("#des3PubId").val(),
			"readPsnDes3Id": $("#ownerPsnId").val(),
			"actionType" : 1,
			"readType": "detailRead"
		},
		dataType : "json",
		success:function(data){
		    Pubdetails.findPdwhPubStatistics($("#des3PubId").val(), $("#dbid").val());
		},
		error: function(){
		    Pubdetails.findPdwhPubStatistics($("#des3PubId").val(), $("#dbid").val());
		}
		
	});
}

//获取基准库成果操作统计数
Pubdetails.findPdwhPubStatistics = function(desId,dbid){
	$.ajax({
		url :'/pubweb/pdwh/ajaxstatistics',
		type :'post',
		data : {
			"des3Id": desId,
			"dbid":dbid
		},
		dataType : "json",
		success:function(data){
			Pubdetails.ajaxTimeOut(data , function(){
				 if(data.result=="success"){
					 $("#cited_count_span").html(data.citedCount);
					 $("#read_count_span").html(data.readCount);
				 }else{
					 $("#cited_count_span").html(0);
					 $("#read_count_span").html(0);
				 }
				 //改为先添加阅读记录再获取统计数，不然第一次PDWH_PUB_STATISTICS没有值时获取的统计数不对
//				 Pubdetails.addPdwhPubReadRecord();
			});
		},
		error: function(){
			$("#cited_count_span").html(0);
			$("#read_count_span").html(0);
//			Pubdetails.addPdwhPubReadRecord();
		}
	});
}

//个人库-这是我的成果
Pubdetails.impMyPubConfirm = function(nodeId,pubId,ownerId) {
	var screencallbackData={
			'nodeId': nodeId,
			'pubId' : pubId,
			'ownerId' : ownerId
	};
	var option={
			'screentxt':pubdetails.confirm_pub,
			'screencallback':Pubdetails.impMyPub,
			'screencallbackData':screencallbackData
	};
	popconfirmbox(option);
};


//个人库成果-导入到我的成果库
Pubdetails.impMyPub = function(callbackData) {
	var params=[];
	params.push({"nodeId":callbackData.nodeId,"pubId":callbackData.pubId,"ownerId":callbackData.ownerId,"ispubview":"true"});
	var postData={"jsonParams":JSON.stringify(params),"articleType":"1"};
		$.ajax({
			url:"/pubweb/publication/ajaximporttomypub",
			type:"post",
			dataType: "json",
			data:postData,
			success:function(data){
				Pubdetails.ajaxTimeOut(data , function(){
					if (data != null && data.result) {
						if (data.result == "success") {
							  scmpublictoast(data.msg,1000);
						} else if(data.result == "dup"){
							scmpublictoast(pubdetails.confirm_dup,2000);
						} else {
							 scmpublictoast(data.msg,2000);
						}
					} else {
						 scmpublictoast("网络错误",2000);
					}
				});
			}
		});	
};


Pubdetails.showMobileLoginTips = function(targetUrl){
    Smate.confirm("提示", "系统超时或未登录，你要登录吗？", function(){
        window.location.href= "/oauth/mobile/index?sys=wechat&service=" + encodeURIComponent(targetUrl);  
    },["确定", "取消"]);
}





//移动端个人库成果-导入到我的成果库
Pubdetails.impMyPubMobile = function(callbackData, targetUrl) {
	var params=[];
	params.push({"nodeId":callbackData.nodeId,"pubId":callbackData.pubId,"ownerId":callbackData.ownerId,"ispubview":"true"});
	var postData={"jsonParams":JSON.stringify(params),"articleType":"1"};
		$.ajax({
			url:"/pubweb/publication/ajaximporttomypub",
			type:"post",
			dataType: "json",
			data:postData,
			success:function(data){
				var toConfirm=false;
				if(data.ajaxSessionTimeOut == 'yes'){
					toConfirm = true;
				}
				if(!toConfirm&&data!=null){
					toConfirm=data.ajaxSessionTimeOut;
				}
				if(toConfirm){
					Pubdetails.showMobileLoginTips(targetUrl);
				}else{
					if (data != null && data.result) {
						if (data.result == "success") {
							  scmpublictoast(data.msg,1000);
						} else if(data.result == "dup"){
							scmpublictoast(pubdetails.confirm_dup,2000);
						} else {
							 scmpublictoast(data.msg,2000);
						}
					} else {
						 scmpublictoast("网络错误",2000);
					}
				}
			}
		});	
};



//基准库-这是我的成果
Pubdetails.importMyPubPdwhConfirm = function (pubId){
	var screencallbackData={'pubId' : pubId};
	var option={
			'screentxt':pubdetails.confirm_pub,
			'screencallback':Pubdetails.importMyPubPdwh,
			'screencallbackData':screencallbackData
	};
	popconfirmbox(option);
};

//基准库成果-导入到我的成果库
Pubdetails.importMyPubPdwh = function (callbackData){
	var params=[];
	params.push({"pubId":callbackData.pubId});
	var postData={"jsonParams":JSON.stringify(params),"publicationArticleType":"1"};
	$.ajax({
		url:"/pubweb/publication/ajaximportpdwh",
		type:"post",
		dataType:"json",
		data : postData,
		success:function(data){
			Pubdetails.ajaxTimeOut(data , function(){
				 if(data != null && data.result){
					 if (data.result == "success") {
						 scmpublictoast(pubdetails.importSuccess,2000);
					} else if(data.result == "dup") {
						 scmpublictoast(pubdetails.confirm_dup,2000);
					} else {
						scmpublictoast(pubdetails.importFail,2000);
					}
				   } else {
					   scmpublictoast(pubdetails.importFail,2000);
				   }
			});
		},
		error : function() {
			scmpublictoast(pubdetails.importFail,2000);
		}
	});
};
//成果编辑
Pubdetails.pubEdit = function(des3PubId) {
	var forwardUrl = "/pubweb/publication/edit?des3Id="+encodeURIComponent(des3PubId)+"&backType=4";
	var data = {"forwardUrl":forwardUrl};
	$.ajax({
		url : '/pubweb/ajaxforwardUrlRefer',
		//url : '/pubweb/forwardUrl',
		type : 'post',
		dataType:'json',
		data : data,
		success : function(data) {
			Pubdetails.ajaxTimeOut(data , function(){
				window.location.href=data.forwardUrl; 
			});
		},
	});
};
Pubdetails.changeLocale =function(locale,title){
	if(locale=="en_US"){
		$("#detail_pub_main_en").css("display","block");
		if(title!=""){
			$("#check_language_zh").css("display","block");
		}
		$("#detail_pub_main_zh").css("display","none");
		$("#check_language_en").css("display","none");
	}else{
		$("#detail_pub_main_zh").css("display","block");
		$("#check_language_zh").css("display","none");
		if(title!=""){
			$("#check_language_en").css("display","block");
		}
		$("#detail_pub_main_en").css("display","none");
	}
}

//其他类似全文
Pubdetails.fulltextList = function(des3PubId) {
	Pubdetails.checkTimeout(function(){
		$.ajax({
			url : '/pubweb/details/ajaxsimilarlist',
			type : 'post',
			dataType:'html',
			data : {"des3PubId":des3PubId},
			success : function(data) {
				Pubdetails.ajaxTimeOut(data , function(){
					var option={
							'data':data
					};
					scmconfirmbox(option);
				});
			},
		});
	});
};

//pdwh其他类似全文
Pubdetails.pdwhFulltextList = function(des3PubId) {
	Pubdetails.checkTimeout(function(){
		$.ajax({
			url : '/pubweb/details/ajaxpdwhsimilarlist',
			type : 'post',
			dataType:'html',
			data : {"des3PubId":des3PubId},
			success : function(data) {
				Pubdetails.ajaxTimeOut(data , function(){
					var option={
							'data':data
					};
					scmconfirmbox(option);
				});
			},
		});
	});
};

//检查是否超时
Pubdetails.checkTimeout = function(myfunction){
	$.ajax({
		url: "/dynweb/ajaxtimeout",
		dataType:"json",
		type:"post",
		data: {},
		success: function(data){
			Pubdetails.ajaxTimeOut(data,function(){
				if(typeof myfunction == "function"){
					myfunction();
				}
			});
		}
	});
};