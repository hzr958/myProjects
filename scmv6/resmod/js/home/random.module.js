/**
 * 首页-右侧-随机显示 成果认领/全文认领/联系人请求/群组请求 模块js
 * @author lhd
 */
var Rm = Rm ? Rm : {};

var snsctx = "/scmwebsns";


//随机显示哪个模块 module为 1:成果认领  2:全文认领  3:联系人请求  4:群组请求 5:群组邀请
Rm.toModule = function(module) {
	var arr = [1,2,3,4,5];
	Rm.randomModule(arr,module);
};

//随机显示模块
Rm.randomModule = function(arr,module) {
	var item = Math.floor(Math.random()*arr.length);
	var r = arr[item];
	var url = "";
	if (module != undefined) {
		if (module == "1") {
			url = "/pub/mainmodule/ajaxgetdynpubconfirm";
		}
		if (module == "2") {
			url = "/pub/opt/ajaxgetrcmdpubfulltext2";
		}
	} else {
		if(r == 1) {
			url = "/pub/mainmodule/ajaxgetdynpubconfirm";
		}
		if(r == 2) {
			url = "/pub/opt/ajaxgetrcmdpubfulltext2";
		}
		if(r == 3) {
			url = "/psnweb/mainmodule/ajaxgetfriendreq";
		}
		if(r == 4) {
			url = "/groupweb/mainmodule/ajaxgetgrpreq";
		}
		if(r == 5) {
			url = "/groupweb/mainmodule/ajaxgetgrpinvite";
		}
	}
	$.ajax({
		url : url,
		type : "post",
		dataType : "html",
		data : {"module":module},
		success: function(data){
			BaseUtils.ajaxTimeOut(data,function(){
				$(".dev_displaymodule").html("");
				$(".dev_displaymodule").append(data);
				var width = $(".dev_displaymodule").width();
				$(".dev_displaymodule").find(".main-list__list-item__container-name").css("width",width-70);
				$(".dev_displaymodule").find(".main-list__list-item__container-duty").css("width",width-70);
				if (randomModule == "no") {
	          arr.splice(item,1);
	          if (arr.length > 0) {
	            Rm.randomModule(arr);
	          }
	      }
				
			});
		}
	});
};

//认领成果详情
Rm.rcmdDetail = function(des3RolPubId,insId) {
	var viewPubDetailUrl = "";
	$.ajax({
		url:snsctx + "/pubrolurl/ajaxPubViewUrl",
		type:"post",
		data:{"des3Id":des3RolPubId,"insId":insId},
		dataType:"json",
		async:false,
		success:function(data){
			if(data.result=="success"){
				viewPubDetailUrl = data.viewUrl;
			}
		},
		error:function(){
		}
	});
	if(viewPubDetailUrl != ""){
		Rm.forceOpen(viewPubDetailUrl);
	}
};

//打开认领成果详情
Rm.forceOpen=function(url) {
	var a = document.getElementById("open_linkxx");
	if (a == null || typeof (a) == "undefined") {
		a = document.createElement("a");
		a.setAttribute("target", "_blank");
		a.setAttribute("href", url);
		a.setAttribute("id", "open_linkxx");
		a.setAttribute("name", "open_linkxx");
		document.body.insertBefore(a, document.body.childNodes[0]);
	} else {
		a.setAttribute("href", url);
	}
	if (document.all) {
		a.click();
	} else {
		var evt = document.createEvent("MouseEvents");
		evt.initEvent("click", true, true);
		a.dispatchEvent(evt);
	}
};

//成果详情
Rm.pubDetail = function(des3Id) {
	var pram ={des3Id:des3Id};
	var params =  "des3Id="+encodeURIComponent(des3Id);
	var newwindow=window.open("about:blank");  
	$.ajax({
		url:"/pubweb/publication/ajaxview",
		type:"post",
		data:pram,
		dataType:"json",
		success:function(data){
			if(data.result==2){
				 newwindow.location.href=data.shortUrl;
			}
			if(data.result==1 || data.ajaxSessionTimeOut=='yes'){
				 newwindow.location.href="/pubweb/details/show?"+params+"&currentDomain=/pubweb&pubFlag=1";
			}else if(data.result==0){
				 newwindow.location.href="/pubweb/publication/wait?"+params;  
			}
		}
	});
};

//检查基准库成果是否存在
Rm.pdwhIsExist = function(des3PubId,func){
  $.ajax({
    url:"/pub/opt/ajaxpdwhisexists",
    type : 'post',
    dataType:"json",
    async : false,
    data:{"des3PubId":des3PubId},
    timeout : 10000,
    success: function(data){
      BaseUtils.ajaxTimeOut(data,function(){
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

//是我的成果
Rm.affirmConfirmPub = function(obj,pdwhPubId){
  var tip;
  if (window.locale == "en_US") {
    tip = "No matched record has been found";
  } else {
    tip = "未找到符合条件的记录";
  }
  Rm.pdwhIsExist(pdwhPubId,function(){
    if ($('#dev_homeconfirm_isall').val() == "one") {
      $(".dev_displaymodule").hide();
    } else {
      $("#dev_"+pdwhPubId).hide();
      var $parentNode = $("#dev_" + pdwhPubId).parent();
      var totalcount = $parentNode.attr("total-count");
      $parentNode.attr("total-count", totalcount - 1);
      if (totalcount - 1 == 0) {
        $parentNode.append('<div class="response_no-result">'+tip+'</div>');
      }
    }
    var params=[];
    params.push({"pubId":pdwhPubId});
    var postData={"jsonParams":JSON.stringify(params),"publicationArticleType":"1"};
    $.ajax({
      url: "/pub/opt/ajaxAffirmPubComfirm",
      type : "post",
      dataType : "json",
      data :{"pubId":pdwhPubId},
      success:function(data){
        BaseUtils.ajaxTimeOut(data,function(){
          if (data.result == "success") {
            if ($('#dev_homeconfirm_isall').val() == "one") {
              var oneText = $(obj).closest(".dev_Achieve-Claim_content").find(".pub-idx__main_add-tip").html();
              if (oneText == "check_box") {
                Rm.sendInviteFriend(pdwhPubId);
              }
              $(".dev_displaymodule").html("");
              $(".dev_displaymodule").show();
              Rm.toModule("1");
            } else {
              $('.dev_home_opt_confirm_count').val(Number($('.dev_home_opt_confirm_count').val())+1);
              pubconfirm.listdata.confirmCount = $('.dev_home_opt_confirm_count').val();
              var allText = $(obj).closest(".dev_home_pubconfirm_box").find(".pub-idx__main_add-tip").html();
              if (allText == "check_box") {
                Rm.sendInviteFriend(pdwhPubId);
              }
            }
          }
        });
      }
    });
  });
};
/*//是我的成果
Rm.affirmConfirmPub = function(obj,pdwhPubId) {
	$.ajax({
		url: "/pub/opt/ajaxAffirmPubComfirm",
		type : "post",
		dataType : "json",
		data :{"pubId":pdwhPubId},
		success:function(data){}
	});
};*/
//邀请合作者为联系人
Rm.sendInviteFriend = function(pdwhPubId) {
	$.ajax({
		url : "/psnweb/friend/ajaxinvitetofriend",
		type : "post",
		dataType : "json",
		data : {"pdwhPubId":pdwhPubId},
		success : function(data) {}
	});
};
//确认成果认领
//Rm.affirmComfirmPub = function(obj,pdwhPubId){
//	BaseUtils.doHitMore(obj,2000);
//	//先隐藏，再处理
//	if ($('#dev_homeconfirm_isall').val() == "one") {
//		$(".dev_displaymodule").hide();
//	} else {
//		$("#dev_"+pdwhPubId).hide();
//	}
//	$.ajax({
//		url: "/pub/opt/ajaxAffirmPubComfirm",
//		type : "post",
//		dataType : "json",
//		data :{"pubId":pdwhPubId},
//		success:function(data){
//			if (data.result == 'success') {
//				if ($('#dev_pubconfirm_isall').val() == "one") {
//					Pub.pubConfirm();
//				} else {
//					$('.dev_confirm_pub_count').val(Number($('.dev_confirm_pub_count').val())+1);
//					pubconfirm.listdata.confirmCount = $('.dev_confirm_pub_count').val();
//				}
//			}
//		}
//	});
//}
//// 忽略成果认领
//Rm.ignoreConfirmPub = function(obj,pdwhPubId) {
//	BaseUtils.doHitMore(obj,2000);
//	if ($('#dev_homeconfirm_isall').val() == "one") {
//        $(".dev_displaymodule").hide();
//    } else {
//        $("#dev_"+pdwhPubId).hide();
//    }
//	$.ajax({
//		url: "/pub/opt/ajaxIgnorePubComfirm",
//		type : "post",
//		dataType : "json",
//		data :{"pubId":pdwhPubId},
//		success:function(data){
//			if (data.result == 'success') {
//				if ($('#dev_pubconfirm_isall').val() == "one") {
//					Pub.pubConfirm();
//				} else {
//					$('.dev_confirm_pub_count').val(Number($('.dev_confirm_pub_count').val())+1);
//					pubconfirm.listdata.confirmCount = $('.dev_confirm_pub_count').val();
//				}
//			}
//		}
//	});
//}

//不是我的成果
Rm.ignoreConfirmPub = function(obj,pdwhPubId) {
    BaseUtils.doHitMore(obj,2000);
    Rm.pdwhIsExist(pdwhPubId,function(){
      BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
        if ($('#dev_homeconfirm_isall').val() == "one") {
          $(".dev_displaymodule").hide();
        } else {
          $("#dev_"+pdwhPubId).hide();
        }
        $.ajax({
          url: "/pub/opt/ajaxIgnorePubComfirm",
          type : "post",
          dataType : "json",
          data :{"pubId":pdwhPubId},
          success:function(data){
            BaseUtils.ajaxTimeOut(data,function(){
              if (data.result == 'success') {
                if ($('#dev_homeconfirm_isall').val() == "one") {
                  $(".dev_displaymodule").html("");
                  $(".dev_displaymodule").show();
                  Rm.toModule("1");
                } else {
                  $('.dev_home_opt_confirm_count').val(Number($('.dev_home_opt_confirm_count').val())+1);
                  pubconfirm.listdata.confirmCount = $('.dev_home_opt_confirm_count').val();
                }
              }
            });
          }
        });
      }, 1);
    });
};
//是我的全文
Rm.doConfirmPubft =function(obj,Id){
    BaseUtils.doHitMore(obj,2000);
    if ($('#dev_homefulltext_isall').val() == "one") {
        $(".dev_displaymodule").hide();
    } else {
        $("#dev_"+Id).hide();
    }
    $.ajax( {
        url : '/pub/opt/ajaxConfirmPubft',
        type : 'post',
        dataType : 'json',
        data : {'ids':Id},
        success : function(data) {
            BaseUtils.ajaxTimeOut(data,function(){
              if (data.status == "not_exist") {
                scmpublictoast("附件已删除", 2000);
              }
              if ($('#dev_homefulltext_isall').val() == "one") {
                $(".dev_displaymodule").html("");
                $(".dev_displaymodule").show();
                Rm.toModule("2");
              } else {
                $('.dev_home_opt_fulltext_count').val(Number($('.dev_home_opt_fulltext_count').val())+1);
                pubftconfirm.listdata.fulltextCount = $('.dev_home_opt_fulltext_count').val();
              }
            });
        }
    });
};

//不是我的全文
Rm.doRejectPubft =function(obj,Id){
    BaseUtils.doHitMore(obj,2000);
    var tip;
    if (window.locale == "en_US") {
      tip = "No matched record has been found";
    } else {
      tip = "未找到符合条件的记录";
    }
    if ($('#dev_homefulltext_isall').val() == "one") {
        $(".dev_displaymodule").hide();
    } else {
        $("#dev_"+Id).hide();
        var $parentNode = $("#dev_" + Id).parent();
        var totalcount = $parentNode.attr("total-count");
        $parentNode.attr("total-count", totalcount - 1);
        if (totalcount - 1 == 0) {
          $parentNode.append('<div class="response_no-result">'+tip+'</div>')
        }
    }
    $.ajax( {
        url : '/pub/opt/ajaxRejectPubft',
        type : 'post',
        dataType : 'json',
        data : {'ids' : Id},
        success : function(data) {
            BaseUtils.ajaxTimeOut(data,function(){
                if ($('#dev_homefulltext_isall').val() == "one") {
                    $(".dev_displaymodule").html("");
                    $(".dev_displaymodule").show();
                    Rm.toModule("2");
                } else {
                    $('.dev_home_opt_fulltext_count').val(Number($('.dev_home_opt_fulltext_count').val())+1);
                    pubftconfirm.listdata.fulltextCount = $('.dev_home_opt_fulltext_count').val();
                }
            });
        }
    });
};

//成果认领的全文下载
Rm.rcmdFulltextDownload = function(fdesId) {
	window.location.href = domainrol + "/scmwebrol/archiveFiles/fileDownload?fdesId="+fdesId;
};

//全文下载    废弃
/*Rm.fulltextDownload = function(fileId) {
	if(fileId != undefined && fileId != ""){
		window.location.href="/pubweb/fulltext/ajaxgetinfobyfileId?fulltextFileId="+fileId;
		return;
	}
};*/
//---------------------------------------------------------------------
//联系人请求列表-忽略操作
Rm.friend_neglet = function(des3TempPsnId,obj) {
  var tip;
  if (window.locale == "en_US") {
    tip = "No matched record has been found";
  } else {
    tip = "未找到符合条件的记录";
  }
	if(obj!=null){
		$(obj).closest(".main-list__item").remove();
	}else{
		//首页单条记录用
		cards =  $(".dev_displaymodule").find(".main-list__list");
		if(cards.length>1){//大于1条可以进行预加载
			cards.eq(0).hide();
			cards.eq(1).show();
			cards.eq(0).remove();
		}else{
			cards.eq(0).remove();
			//Rm.doReqFriend();
		}
	}
	$.ajax({
		url : "/psnweb/friendreq/ajaxneglet",
		type : "post",
		dataType : "json",
		data : {"des3TempPsnId":des3TempPsnId},
		success : function(data) {
		    BaseUtils.ajaxTimeOut(data, function(){
		        if(obj!=null){
		            $(obj).closest(".main-list__item").remove();
		            if($("*[list-main='reqfriend']").find(".main-list__item").length==0){
		              $("*[list-main='reqfriend']").append('<div class="response_no-result">'+tip+'</div>');
		              // Rm.reqFriendAllClose();
		            }
		        }else{
		            //首页单条记录用
		            if(cards.length>1){//大于1条可以进行预加载
		                Rm.doReqFriendbeforehand();
		            }
		        }
		    });
		}
	});
};
//联系人请求列表-同意操作
Rm.friend_agree = function(des3TempPsnId,obj) {
	var cards;
	var tip;
  if (window.locale == "en_US") {
    tip = "No matched record has been found";
  } else {
    tip = "未找到符合条件的记录";
  }
	if(obj!=null){//列表框用
		$(obj).closest(".main-list__item").remove();
	}else{//首页单条记录用
		cards =  $(".dev_displaymodule").find(".main-list__list");
		if(cards.length>1){//大于1条可以进行预加载
			cards.eq(0).hide();
			cards.eq(1).show();
			cards.eq(0).remove();
		}else{
			cards.eq(0).remove();
			//Rm.doReqFriend();
		}
	}
	$.ajax({
		url : "/psnweb/friendreq/ajaxaccept",
		type : "post",
		dataType : "json",
		data : {"des3ReqPsnIds":des3TempPsnId},
		success : function(data) {
		    BaseUtils.ajaxTimeOut(data, function(){
		        if(data.result == "msg") {
		            scmpublictoast("添加联系人失败，对方已取消联系人邀请。",1000);
		        }
		        if(obj!=null){//列表框用
		            $(obj).closest(".main-list__item").remove();
		            if($("*[list-main='reqfriend']").find(".main-list__item").length==0){
		              $("*[list-main='reqfriend']").append('<div class="response_no-result">'+tip+'</div>');
		                //Rm.reqFriendAllClose();
		            }
		        }else{//首页单条记录用
		            if(cards.length>1){//大于1条可以进行预加载
		                Rm.doReqFriendbeforehand();
		            }else{
                        // 没有数据，要隐藏模块
                        $(".dev_displaymodule").css("display","none");
                    }
		        }
		    });
		}
	});
};
Rm.doReqFriend = function(){
	$.ajax({
		url : "/psnweb/mainmodule/ajaxgetfriendreq",
		type : "post",
		dataType : "html",
		success: function(data){
			$(".dev_displaymodule").html("");
			$(".dev_displaymodule").append(data);
		}
	});
}
Rm.doReqFriendbeforehand = function(){
	$.ajax({
		url : "/psnweb/mainmodule/ajaxgetfriendreq",
		type : "post",
		dataType : "html",
		data:{
			"beforehand":1
		},
		success: function(data){
			$(".dev_displaymodule").find(".main-list__list:last").after(data);
		}
	});
}
//首页-联系人请求-查看全部
var ReqFriend;
Rm.reqFriendAll = function() {
	addFormElementsEvents();
	$('#dev_homeconfirm_isall').val("all");
	showDialog("dev_home_reqfriend_back");
	ReqFriend = window.Mainlist({
		name: "reqfriend",
		listurl: "/psnweb/mainmodule/ajaxgetfriendreq",
		listdata: {"isAll":"1"},
		method: "scroll",
		listcallback: function(xhr){}
	});
};
//关闭联系人列表
Rm.reqFriendAllClose = function(){
	hideDialog("dev_home_reqfriend_back");
	Rm.doReqFriend();
}
//处理群组申请（接受/忽略） disposeType 1=同意 ； 0=忽略
Rm.disposegrpApplication = function(disposeType,targetPsnIds,des3GrpId,obj){
	var cards;
	var tip;
	if (window.locale == "en_US") {
	  tip = "No matched record has been found";
  } else {
    tip = "未找到符合条件的记录";
  }
	if(obj!=null){
	  $(obj).closest(".main-list__item").remove();
	}else{
		cards =  $(".dev_displaymodule").find(".main-list__list");
		if(cards.length>1){//大于1条可以进行预加载
			cards.eq(0).hide();
			cards.eq(1).show();
			cards.eq(0).remove();
		}else{
			cards.eq(0).remove();
			//Rm.doReqGrp();
		}
	}
	$.ajax({
		url : '/groupweb/grpmember/ajaxdisposegrpapplication',
		type : 'post',
		dataType:'json',
		data:{
			'des3GrpId':des3GrpId,
			'targetPsnIds':targetPsnIds,
			'disposeType':disposeType
		},
		success : function(data) {
		    BaseUtils.ajaxTimeOut(data, function(){
		        var list = $("*[list-main='req_grp_list']");
		        if(obj!=null){
		            $(obj).closest(".main-list__item").remove();
		            if($("#has_ivite_grp_list").find(".main-list__item").length==0){
		              var $parentNode = $("#has_ivite_grp_list");
		              $parentNode.append('<div class="response_no-result">'+tip+'</div>');
		               // Rm.reqGrpAllClose();
		            }else{
		              Rm.reqGrpAllClose();
		            }
		        }else{
		            if(cards.length>1){//大于1条可以进行预加载
		                Rm.doReqGrpBeforehand();
		            }else{
                        // 没有数据，要隐藏模块
                        $(".dev_displaymodule").css("display","none");
                    }
		        }
		    });
		}
	}); 
}
//首页-群组申请-查看全部
var ReqGrp;
Rm.reqGrpAll = function() {
	addFormElementsEvents();
	$('#dev_homeconfirm_isall').val("all");
	showDialog("join_grp_invite_box");
	//统一用click事件
	$(".dev_grp_module_reg").click();
	/*showDialog("dev_home_reqgrp_back");
	ReqGrp = window.Mainlist({
		name: "req_grp_list",
		listurl: "/groupweb/mainmodule/ajaxgetgrpreq",
		listdata: {"isAll":"1"},
		method: "scroll",
		listcallback: function(xhr){}
	});*/
};
//加载群组请求
Rm.doReqGrp = function(){
	$.ajax({
		url : "/groupweb/mainmodule/ajaxgetgrpreq",
		type : "post",
		dataType : "html",
		success: function(data){
			$(".dev_displaymodule").html("");
			$(".dev_displaymodule").append(data);
			var width = $(".dev_displaymodule").width();
			$(".dev_displaymodule").find(".main-list__list-item__container-name").css("width",width-70);
			$(".dev_displaymodule").find(".main-list__list-item__container-duty").css("width",width-70);
		}
	});
}
//加载群组请求
Rm.doReqGrpBeforehand = function(){
	$.ajax({
		url : "/groupweb/mainmodule/ajaxgetgrpreq",
		type : "post",
		dataType : "html",
		data:{
			"beforehand":1
		},
		success: function(data){
			$(".dev_displaymodule").find(".main-list__list:last").after(data);
			var width = $(".dev_displaymodule").width();
			$(".dev_displaymodule").find(".main-list__list-item__container-name").css("width",width-70);
			$(".dev_displaymodule").find(".main-list__list-item__container-duty").css("width",width-70);
		}
	});
}
//关闭群组列表
Rm.reqGrpAllClose =  function(){
	try {
	hideDialog("dev_home_reqgrp_back");
	} catch (e) {
	}
	$("*[list-main='req_grp_list']").html("");
	Rm.doReqGrp();
}
//群组邀请
//处理群组申请（接受/忽略） disposeType 1=同意 ； 0=忽略 ---zzx-----
Rm.ivitegrpApplication = function(disposeType,targetdes3GrpId,obj){
	var cards;
	if(obj!=null){
		$(obj).closest(".main-list__item").remove();
	}else{
		cards =  $(".dev_displaymodule").find(".main-list__list");
		if(cards.length>1){//大于1条可以进行预加载
			cards.eq(0).hide();
			cards.eq(1).show();
			cards.eq(0).remove();
		}else{
			cards.eq(0).remove();
			//Rm.doiviteGrp();
		}
	}
	$.ajax({
		url : '/groupweb/grpmember/ajaxivitegrpapplication',
		type : 'post',
		dataType:'json',
		data:{
			'targetdes3GrpId':targetdes3GrpId,
			'disposeType':disposeType
		},
		success : function(data) {
		    BaseUtils.ajaxTimeOut(data, function(){
		        var list = $("*[list-main='ivite_grp_list']");
		        if(obj!=null){
		            $(obj).closest(".main-list__item").remove();
		            if(list.find(".main-list__item").length==0){
		                Rm.iviteGrpAllClose();
		            }
		        }else{
		            if(cards.length>1){//大于1条可以进行预加载
		                Rm.doiviteGrpBeforehand
		            }else{
		                // 没有数据，要隐藏模块
                        $(".dev_displaymodule").css("display","none");
                    }
		        }
		    });
		}
	});
}
Rm.iviteGrpAllClose = function(){
	try {
		hideDialog("dev_home_ivitegrp_back");
		} catch (e) {
		}
	$("*[list-main='ivite_grp_list']").html("");
	Rm.doiviteGrp();
}
Rm.doiviteGrp = function(){
	$.ajax({
		url : "/groupweb/mainmodule/ajaxgetgrpinvite",
		type : "post",
		dataType : "html",
		success: function(data){
			$(".dev_displaymodule").html("");
			$(".dev_displaymodule").append(data);
		}
	});
}
Rm.doiviteGrpBeforehand = function(){
	$.ajax({
		url : "/groupweb/mainmodule/ajaxgetgrpinvite",
		type : "post",
		dataType : "html",
		data:{
			"beforehand":1
		},
		success: function(data){
			$(".dev_displaymodule").find(".main-list__list:last").after(data);
		}
	});
}
var IviteGrp;
Rm.iviteGrpAll = function() {
	addFormElementsEvents();
	$('#dev_homeconfirm_isall').val("all");
	showDialog("join_grp_invite_box");
	$(".dev_grp_module_invite").click();
	/*showDialog("dev_home_ivitegrp_back");
	IviteGrp = window.Mainlist({
		name: "ivite_grp_list",
		listurl: "/groupweb/mainmodule/ajaxgetgrpinvite",
		listdata: {"isAll":"1"},
		method: "scroll",
		listcallback: function(xhr){}
	});*/
};
//------------------------------------------------------------------
//首页-成果认领-查看全部
var pubconfirm;
Rm.pubConfirmAll = function() {
    BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
        addFormElementsEvents();
        $('#dev_homeconfirm_isall').val("all");
        showDialog("dev_home_pubconfirm_back");
        if ($.trim($('#dev_home_fulltextCount').val()) < 1) {
            $('.dev_home_pubfulltext').hide();
        }
        pubconfirm = window.Mainlist({
            name: "pubconfirm",
            listurl: "/pub/mainmodule/ajaxgetdynpubconfirm",
            listdata: {
                "isAll":"1",
                "des3SearchPsnId" : $("#userId").val()
            },
            method: "scroll",
            listcallback: function(xhr){
                Rm.initCheckBox();
                //显示全文认领
                if ($('#hasPubfulltextConfirm').val() == "true") {
                    $('.dev_home_pubfulltext').show();
                }
            }
        });
    }, 1);
};

//成果认领与全文认领查看全部上的成果认领模块
Rm.toConfirmList = function() {
	$('.dev_home_fulltextList').html("");
	$('.dev_home_fulltextList').hide();
	$('.dev_home_confirmList').show();
	$('.dev_home_pubconfirm_back_add_tip').find(".pub-idx__main_add-tip").html("check_box");
	$('.dev_home_pubconfirm_back_add_tip').show();
	pubconfirm = window.Mainlist({
		name: "pubconfirm",
		listurl: "/pub/mainmodule/ajaxgetdynpubconfirm",
		listdata: {
			"isAll":"1",
			"des3SearchPsnId" : $("#userId").val()
		},
		method: "scroll",
		listcallback: function(xhr){
			Rm.initCheckBox();
		}
	});
};
//成果认领与全文认领查看全部上的全文认领模块
Rm.toFulltextList = function() {
	$('.dev_home_confirmList').html("");
	$('.dev_home_confirmList').hide();
	$('.dev_home_fulltextList').show();
	$('.dev_home_pubconfirm_back_add_tip').hide();
	pubftconfirm = window.Mainlist({
		name: "pubftconfirm",
		listurl: "/pub/opt/ajaxgetrcmdpubfulltext2",
		listdata: {"isAll":"1"},
		method: "scroll",
		listcallback: function(xhr){}
	});
};

//成果认领-关闭查看全部弹出层
Rm.closePubConfirmBack = function() {
	$('.dev_home_opt_confirm_count').val(0);
	$('#dev_homeconfirm_isall').val("one");
	hideDialog("dev_home_pubconfirm_back");
	//====/psnweb/timeout/ajaxtest 判断超时用
	$.ajax({
		url : "/psnweb/timeout/ajaxtest",
		type : "post",
		dataType : "json",
		data : {},
		success : function(data) {
			if(data.ajaxSessionTimeOut != "yes"){
				Rm.toModule("1");
				$('.dev_home_pubconfirm').click();
			}
		}
	});
};

//全文认领-查看全部
var pubftconfirm;
Rm.pubftConfirmAll = function() {
    BaseUtils.checkCurrentSysTimeout("/pub/ajaxtimeout", function(){
        addFormElementsEvents();
        $('#dev_homefulltext_isall').val("all");
        showDialog("dev_home_pubfulltext_back");
        $(".dev_home_pubfulltext_back_add_tip").hide();
        if ($.trim($('#dev_home_confirmCount').val()) < 1) {
            $('.dev_home_pubconfirm1').hide();
        }
        pubftconfirm = window.Mainlist({
            name: "pubftconfirm1",
            listurl: "/pub/opt/ajaxgetrcmdpubfulltext2",
            listdata: {"isAll":"1"},
            method: "scroll",
            listcallback: function(xhr){
                $("div[list-main='pubftconfirm1']").show();
            }
        });
    }, 1);
};

//全文认领-关闭查看全部弹出层
Rm.closePubftconfirmBack = function() {
	$('.dev_home_opt_fulltext_count').val(0);
	$('#dev_homefulltext_isall').val("one");
	hideDialog("dev_home_pubfulltext_back");
	//====/psnweb/timeout/ajaxtest 判断超时用
	$.ajax({
		url : "/psnweb/timeout/ajaxtest",
		type : "post",
		dataType : "json",
		data : {},
		success : function(data) {
			if(data.ajaxSessionTimeOut != "yes"){
				Rm.toModule("2");
				//$('.dev_home_pubfulltext1').click();
			}
		}
	});
};

//成果认领与全文认领查看全部上的成果认领模块
Rm.toConfirmList1 = function() {
	$('.dev_home_fulltextList1').html("");
	$('.dev_home_fulltextList1').hide();
	$('.dev_home_confirmList1').show();
	$('.dev_home_pubfulltext_back_add_tip').find(".pub-idx__main_add-tip").html("check_box");
	$('.dev_home_pubfulltext_back_add_tip').show();
	pubconfirm = window.Mainlist({
		name: "pubconfirm1",
		listurl: "/pub/mainmodule/ajaxgetdynpubconfirm",
		listdata: {
			"isAll":"1",
			"des3SearchPsnId" : $("#userId").val()
		},
		method: "scroll",
		listcallback: function(xhr){
			Rm.initCheckBox();
		}
	});
};

//成果认领与全文认领查看全部上的全文认领模块
Rm.toFulltextList1 = function() {
	$('.dev_home_confirmList1').html("");
	$('.dev_home_confirmList1').hide();
	$('.dev_home_fulltextList1').show();
	$('.dev_home_pubfulltext_back_add_tip').hide();
	pubftconfirm = window.Mainlist({
		name: "pubftconfirm1",
		listurl: "/pub/opt/ajaxgetrcmdpubfulltext2",
		listdata: {"isAll":"1"},
		method: "scroll",
		listcallback: function(xhr){}
	});
};

//查询群组邀请，请求的信息
Rm.showGrpInviteRegInfo = function(module) {
	$.ajax({
		url : "/groupweb/mygrp/ajaxFindGrpInviteRegInfo",
		type : "post",
		dataType : "json",
		data : {},
		success : function(data) {
			if(data.hasReqGrp === "0"){
				$(".dev_grp_module_reg").css("display","none");
			}
			if(data.hasIvite === "0"){
				$(".dev_grp_module_invite").css("display","none");
			}
			if(module == 'invite'){
				Rm.iviteGrpAll();
			}else if(module == 'reg'){
				Rm.reqGrpAll();
			}
		}
	});
};

//实现邀请我的合作者成为联系人的勾选取消效果
Rm.initCheckBox = function() {
	if(document.getElementsByClassName("pub-idx__main_add-tip")){
        var addlist = document.getElementsByClassName("pub-idx__main_add-tip");
        for(var i = 0; i < addlist.length; i++ ){
            addlist[i].onclick = function(){
                if(this.innerHTML!="check_box"){
                    this.innerHTML = "check_box";
                }else{
                    this.innerHTML = "check_box_outline_blank";
                }
            }
        }
    }
};
