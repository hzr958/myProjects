/**
 * 我的联系人相关js
 * @author lhd
 */
var Friend = Friend ? Friend : {};


Friend.moduleClick = function(module) {
	var arrLi = $("*[scm_friend_module='module__list']").find("li");
	arrLi.removeClass("item_selected");
	if(module == "myf"){//选择我的联系人模块
		//1.把联系人推荐模块隐藏
		$("*[scm_friend_module='rec']").hide();
		//2.为我的联系人模块添加选中样式
		arrLi.eq(0).addClass("item_selected");
		//3.显示我的联系人模块
		$("*[scm_friend_module='myf']").show();
		//4.加载我的联系人列表,先清空列表
		$('.dev_myfriend_list').html("");
		$(".searchbox__main>input").val("");
		Friend.friendList();
	} else {
		//1.把我的联系人模块隐藏
		$("*[scm_friend_module='myf']").hide();
		//2.为联系人推荐模块添加选中样式
		arrLi.eq(1).addClass("item_selected");
		//3.显示联系人推荐模块
		$("*[scm_friend_module='rec']").show();
		//4.加载联系人请求列表
		Friend.reqList();
		//5.加载联系人推荐列表
		Friend.recommendList();
	}
	addFormElementsEvents();
	Friend.changeUrl(module);
};


//改变url
Friend.changeUrl = function(targetModule) {
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

// 我的联系人列表
var $myfriend;
Friend.friendList = function() {
	const $filterlist = Array.from(document.getElementsByClassName("filter-list")).filter(function(x){
		return x.getAttribute("list-filter") === "myfriend";
	});
	var $filtersections = [];
	$filterlist.forEach(function(x){
		$filtersections = $filtersections.concat(Array.from(x.getElementsByClassName("filter-list__section")));
	});
//	const $region = $filtersections.filter(function(x){
//		return x.getAttribute("filter-section") === "regionId";
//	})[0];
	const $insitution = $filtersections.filter(function(x){
		return x.getAttribute("filter-section") === "insId";
	})[0];
//	const $regionValue = {
//		url: "/psnweb/friend/ajaxreg",
//		name: "regionName",
//		code: "regionId"
//	};
	const $insValue = {
		url: "/psnweb/friend/ajaxins",
		name: "name",
		code: "insId"
	};
	const $filterMap = new Map();
//	$filterMap.set($region, $regionValue);
	$filterMap.set($insitution, $insValue);
	
	$myfriend = window.Mainlist({
		name : "myfriend",
		listurl : "/psnweb/friend/ajaxlist",
		listdata : {},
		listcallback : function(xhr) {
			var titlelist = document.getElementsByClassName("filter-value__option");
			for(var i = 0; i < titlelist.length ;i++ ){
				titlelist[i].onmouseover =  function(){
					var text = this.innerHTML;
					this.setAttribute("title",text);
				}
			}
		},
		statsurl: "/psnweb/friend/ajaxfriendlistcallback",
		filtermap: $filterMap
	});
};

// 弹出操作
Friend.friendOpt = function(obj, e) {
	$('.dev_myfriend_list').find(".action-dropdown__list").removeClass("list_shown");
	$(obj).parent().find(".action-dropdown__list").addClass("list_shown");
	Friend.clickOtherHide(e, function() {
		$(obj).parent().find(".action-dropdown__list").removeClass("list_shown");
	});
};

// 点击其他地方事件
Friend.clickOtherHide = function(e, myFunction) {
	if (e && e.stopPropagation) {// 非IE
		e.stopPropagation();
	} else {// IE
		window.event.cancelBubble = true;
	}
	$(document).click(function() {
		myFunction();
	});
};

//删除联系人
Friend.delFriend = function(psnId, des3FriendId) {
  if (des3FriendId == null || des3FriendId == ""
    || typeof (des3FriendId) == "undefined") {
    return;
  } else {
    $.ajax({
      url : "/psnweb/friend/ajaxdel",
      type : "post",
      data : {
        "friendPsnIds" : des3FriendId
      },
      dataType : "json",
      success : function(data) {
        Friend.ajaxTimeOut(data, function(){
          if(data.result == "success"){
            scmpublictoast(friend_main.delFriendSuccess,1000);
            $myfriend.reloadCurrentPage();
            var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
            if($(".new-searchplugin_container")){
                $(".background-cover").remove();
                $(".new-searchplugin_container").remove();
             }
          }else{
            scmpublictoast(friend_main.delFriendError,1000);
            var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
            if($(".new-searchplugin_container")){
                $(".background-cover").remove();
                $(".new-searchplugin_container").remove();
             }
          }
        });
      },
      error : function(data) {
        scmpublictoast(friend_main.delFriendError,1000);
      }
    });
  }
}
//删除联系人弹框
Friend.ajaxDelFriend = function(psnId, des3FriendId) {
  smate.showTips._showNewTips(friend_main.confirmTips, friend_main.confirmTitle, "Friend.delFriend('"+psnId+"','"+des3FriendId+"')", "",
      friend_main.choose_confirm_btn, friend_main.choose_cancel_btn);
};

//弹出发现联系人框
Friend.findFriend = function(){
  BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function() {
    showDialog("find_friend_dialog");
    $("#addFriendBtn").attr("disabled", "disabled");
  }, 1);
};

//个人发送的联系人请求
Friend.sendaddfriend =function(isAll){
	$.ajax({
		url : "/psnweb/sendfriendreq/ajaxlist",
		type : "post",
		dataType : "html",
		data : {"isAll":isAll},
		success : function(data) {
			Friend.ajaxTimeOut(data,function(){
				if (isAll == 1) {
					$('.dev_sendlist_more').replaceWith(data);
				} else {
					$('.dev_reqorsend_list').html(data);
				}
			});
		}
	});
};

//取消发送联系人请求
Friend.cancelSendFriend =function(des3ReceiveId,receiveId){
	$.ajax({
		url:"/psnweb/friendreq/ajaxremove",
		type :"post",
		dateType:"json",
		data : {"des3TempPsnId" : des3ReceiveId},
		success : function(data) {
			Friend.ajaxTimeOut(data,function(){
				if(data.result=="success"){
					$("div#"+receiveId).remove();
				}
			});
		}
	})
};

// 超时处理
Friend.ajaxTimeOut = function(data, myfunction) {
	var toConfirm = false;
	if ('{"ajaxSessionTimeOut":"yes"}' == data) {
		toConfirm = true;
	}
	if (!toConfirm && data != null) {
		toConfirm = data.ajaxSessionTimeOut;
	}
	if (toConfirm) {
		jConfirm(friend_main.i18n_timeout, friend_main.i18n_tipTitle, function(r) {
			if (r) {
				document.location.href = window.location.href;
				return 0;
			}
		});
	} else {
		if (typeof myfunction == "function") {
			myfunction();
		}
	}
};

Friend.doHitMore = function(obj, time) {
	$(obj).attr("disabled", true);
	var click = $(obj).attr("onclick");
	if (click != null && click != "") {
		$(obj).attr("onclick", "");
	}
	setTimeout(function() {
		$(obj).removeAttr("disabled");
		if (click != null && click != "") {
			$(obj).attr("onclick", click);
		}
	}, time);
};

//邀请联系人
Friend.sendMail = function() {
	var mail = $.trim($('#search_psn_input').val());
	var reg=/[\u4e00-\u9fa5]/;//中文正则表达式，Email地址不能出现中文
    if (!common.isEmail(mail) || reg.test(mail)) {
    	scmpublictoast(friend_main.sendFail,1000);
        return;
    }
	$.ajax({
		url : "/psnweb/friend/ajaxsendmail",
		type : "post",
		dataType : "json",
		data : {"psnSendMail.email":mail},
		success : function(data) {
			Friend.ajaxTimeOut(data,function(){
				if (data.result == "success") {
					scmpublictoast(friend_main.sendSuccess,1000);
				} else if(data.result == "exists"){
					scmpublictoast(friend_main.exists,2000);
				}
			});
		}
	});
};

//发送站内信
Friend.sendMsg = function(des3PsnId) {
	window.location.href = "/dynweb/showmsg/msgmain?model=chatMsg&des3ChatPsnId="+des3PsnId;
};

//联系人推荐列表
var $recommendfriend;
Friend.recommendList = function() {
	const $filterlist = Array.from(document.getElementsByClassName("filter-list")).filter(function(x){
		return x.getAttribute("list-filter") === "recommendfriend";
	});
	var $filtersections = [];
	$filterlist.forEach(function(x){
		$filtersections = $filtersections.concat(Array.from(x.getElementsByClassName("filter-list__section")));
	});
	const $insitution = $filtersections.filter(function(x){
		return x.getAttribute("filter-section") === "insId";
	})[0];
	const $region = $filtersections.filter(function(x){
		return x.getAttribute("filter-section") === "regionId";
	})[0];
	const $scienceArea = $filtersections.filter(function(x){
		return x.getAttribute("filter-section") === "scienceAreaId";
	})[0];
	const $insValue = {
			url: "/psnweb/recommend/ajaxins",
			acURL: "/psnweb/ac/ajaxautoinstitution",
			name: "name",
			code: "insId"
	};
	const $regionValue = {
		url: "/psnweb/recommend/ajaxreg",
		acURL: "/psnweb/ac/ajaxautoregion",
		name: "regionName",
		code: "regionId"
	};
	const $saValue = {
			url: "/psnweb/recommend/ajaxsciencearea",
			name: "name",
			code: "scienceAreaId"
	};
	const $filterMap = new Map();
	$filterMap.set($region, $regionValue);
	$filterMap.set($insitution, $insValue);
	$filterMap.set($scienceArea, $saValue);
	
	$recommendfriend = window.Mainlist({
		name : "recommendfriend",
		listurl : "/psnweb/recommend/ajaxrecommendpsn",
		listdata : {},
		method : "scroll",
		listcallback : function(xhr) {
			var titlelist = document.getElementsByClassName("filter-value__option");
			for(var i = 0; i < titlelist.length ;i++ ){
				titlelist[i].onmouseover =  function(){
					var text = this.innerHTML;
					this.setAttribute("title",text);
				}
			}
		    $(".js_autocompletebox").parent(".filter-create__input").addClass("filter-create__input-center");
		},
		statsurl: "/psnweb/recommend/ajaxfriendlistcallback",
		filtermap: $filterMap
	});
};

//我的-联系人推荐列表-移除
Friend.remove = function(recommendPsnId, des3TempPsnId) {
	$.ajax({
		url : "/psnweb/recommend/ajaxremove",
		type : "post",
		dataType : "json",
		data : {"des3TempPsnId":des3TempPsnId},
		success : function(data) {
		    BaseUtils.ajaxTimeOut(data, function(){
		        if (data.result == "success") {
		            $('.dev_recommendlist_'+recommendPsnId).remove();
		            scmpublictoast(friend_main.removeSucess,1000);
		        } 
		    });
		}
	});
};

//新-联系人请求列表
Friend.reqList = function(isAll) {
	$.ajax({
		url : "/psnweb/friendreq/ajaxnewlist",
		type : "post",
		dataType : "html",
		data : {
			"isAll":isAll,
			"des3ReqPsnIds": $("#reqPsnIdsData").val()
		},
		success : function(data) {
			Friend.ajaxTimeOut(data,function(){
				$('.dev_reqorsend_list').html(data);
				if(isAll == 1){
					$('.dev_reqlist_more').hide();
				}
			});
		}
	});
};

//联系人请求列表-忽略操作
Friend.neglet = function(reqPsnId, des3TempPsnId) {
	$.ajax({
		url : "/psnweb/friendreq/ajaxneglet",
		type : "post",
		dataType : "json",
		data : {"des3TempPsnId":des3TempPsnId},
		success : function(data) {
			Friend.ajaxTimeOut(data,function(){
				if (data.result == "success") {
					$('.dev_reqlist_'+reqPsnId).remove();
				}
			});
		}
	});
};

//联系人请求列表-同意操作
Friend.agree = function(reqPsnId, des3TempPsnId) {
	$.ajax({
		url : "/psnweb/friendreq/ajaxaccept",
		type : "post",
		dataType : "json",
		data : {"des3ReqPsnIds":des3TempPsnId},
		success : function(data) {
			Friend.ajaxTimeOut(data,function(){
				if (data.result == "success") {
					$('.dev_reqlist_'+reqPsnId).remove();
				} else if(data.result == "overdue") {
					$('.dev_reqlist_'+reqPsnId).remove();
				} else if(data.result == "msg") {
					$('.dev_reqlist_'+reqPsnId).remove();
					scmpublictoast(friend_main.agreeMsg,1000);
				} else {
					scmpublictoast(data.result,1000);
				}
			});
		}
	});
};