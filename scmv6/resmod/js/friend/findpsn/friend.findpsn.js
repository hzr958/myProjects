var findPsn = findPsn ? findPsn : {};

//检索框绑定检索事件----IE有问题
findPsn.bindSearch = function(){
	$("#search_psn_input").bind("input propertychange", function(){
		$("#pageNo").val("1");
		$("#hasNextPage").val("true");
		$("#selectedPsnIds").val("");
		//用jquery选择器IE无效
		document.getElementById("find_psn_div").scrollTop = 0;
		//检索
		findPsn.ajaxfindpsn("");
	});
}
var searchPsnReq = null;
//检索人员
findPsn.ajaxfindpsn = function(searchType){
    if(searchPsnReq != null){
        searchPsnReq.abort();
    }
	var searchString = $.trim($("#search_psn_input").val());
	var currentPage = parseInt($("#pageNo").val());
	//检索框为空或没有下一页了
	if(searchString == ''){
		$("#find_psn_items").html("");
		$("#inviteDiv").hide();
		$("#inviteFrdDiv").hide();
		return;
	}
	if($("#hasNextPage").val() == "false"){
		return;
	}
	var isEmail = false;
	//判断是否是邮箱
	if(searchString.indexOf("@") > -1){
		isEmail = true
	}
	searchPsnReq = $.ajax({
		url: "/psnweb/findpsn/ajaxfind",
		type: "post",
		data: {
			"searchString": searchString,
			"isEmail" : isEmail,
			"page.pageNo": currentPage,
			"page.pageSize": 20
		},
		beforeSend : function () {
		  $("#inviteFrdDiv").hide();//隐藏没有符合记录提示
		  $("#inviteDiv").hide();
		  $("#find_psn_items").doLoadStateIco({
        status : 1
      });// 加载圈
		},
		dataType: "html",
		success: function(data){
			Friend.ajaxTimeOut(data, function(){
				if(data.indexOf("findNoPerson") !=-1){
				  $("#find_psn_items").find(".preloader").remove();// 隐藏加载圈
					$("#totalPageCount").remove();
					if("loadMore" == searchType){
						$("#find_psn_items").append(data);
					}else{
						$("#find_psn_items").html(data);
					}
					//若没匹配上人员则显示邀请人员按钮
					if(isEmail && $("#totalPageCount").val() <= 0){
						$("#emailNoteDiv").text($.trim($("#search_psn_input").val()));
						$("#inviteDiv").show();
					}else{
						$("#inviteDiv").hide();
					}
					//用户名检索提示
					if(isEmail==false && $("#totalPageCount").val() <= 0){
						$("#usrNoteDiv").text($.trim($("#search_psn_input").val()));
						$("#inviteFrdDiv").show();
					}else{
						$("#inviteFrdDiv").hide();
					}
					
					//如果是最后一页了（下一页大于总页数）
					var nextPage = currentPage + 1;
					if(nextPage > parseInt($("#totalPageCount").val())){
						$("#hasNextPage").val("false");
					}
					$("#pageNo").val(nextPage);
				}else{
					$("#totalPageCount").remove();//remove不会移除该元素本身，不能用移除来判断列表是否为空
					if("loadMore" == searchType){
						$("#find_psn_items").append(data);
					}else{
						$("#find_psn_items").html(data);
					}
					//若没匹配上人员则显示邀请人员按钮
					if(isEmail && $("#totalPageCount").val() <= 0){
						$("#emailNoteDiv").text($.trim($("#search_psn_input").val()));
						$("#inviteDiv").show();
					}else{
						//移除提示弹窗
						//scmpublictoast(friend_main.findNoPsn,1000);
						$("#inviteDiv").hide();
					}
					//用户名检索提示
					if(isEmail==false && $("#totalPageCount").val() <= 0){
						$("#usrNoteDiv").text($.trim($("#search_psn_input").val()));
						$("#inviteFrdDiv").show();
					}else{
						//移除提示弹窗
						//scmpublictoast(friend_main.findNoPsn,1000);
						$("#inviteFrdDiv").hide();
					}
					
					//如果是最后一页了（下一页大于总页数）
					var nextPage = currentPage + 1;
					if(nextPage > parseInt($("#totalPageCount").val())){
						$("#hasNextPage").val("false");
					}
					$("#pageNo").val(nextPage);
				}
				//每次检索都要把添加按钮致灰
				/*$("#addFriendBtn").attr("disabled", "disabled");*/
				//移除上一页检索结果中的总页数
				
				
			});
		},
		error: function(){
//			scmpublictoast(friend_main.opError,1000);
		}
	});
}

//绑定滚动加载
findPsn.listScroll = function(){
	$("#find_psn_div").on("scroll", function(){
		$listParent = document.getElementById('find_psn_div');
		//判断列表是否滚动到底部
		if($listParent.scrollHeight - $listParent.scrollTop - $listParent.clientHeight <= 10){
			findPsn.ajaxfindpsn("loadMore");
		}
	});
}

//隐藏
findPsn.hideFindBox = function(boxId){
	hideDialog(boxId);
	$("#find_psn_items").html("");
	$("#search_psn_input").val("");
	$('#emailNoteDiv').text("");
	$("#inviteDiv").hide();
	$("#inviteFrdDiv").hide();
	
}
//添加联系人
findPsn.addFriend =function(reqPsnId){
	var reqPsnId=$("#selectedPsnIds").val();
	$.ajax({
		url:'/psnweb/friend/ajaxaddfriend',
		type:'post',
		data:{'des3Id':reqPsnId},
		dataType:'json',
		timeout:10000,
		success:function(data){
			
		if (data.result == "true") {
			scmpublictoast(friend_main.addFriend,1000);
		} else{
			scmpublictoast(data.msg,1000);
		}
		    
		},
		error:function(){
			scmpublictoast("网络异常",1000);
		}
	});
};

//单个添加联系人
findPsn.addOneFriend =function(recommendPsnId, reqPsnId,obj){
	//防止重复点击
	if(obj){
		BaseUtils.doHitMore(obj,1000);
	}
	$.ajax({
		url:'/psnweb/friend/ajaxaddfriend',
		type:'post',
		data:{'des3Id':reqPsnId},
		dataType:'json',
		timeout:10000,
		success:function(data){
			Friend.ajaxTimeOut(data, function(){
				if (data.result == "true") {
					$('.dev_recommendlist_'+recommendPsnId).remove();
					scmpublictoast(friend_main.addFriend,1000);
				} else{
					scmpublictoast(data.msg,1000);
				}
			});
		}
	});
};
//其他的地方添加联系人
findPsn.addFriendOther =function(reqPsnId){
	console.info(reqPsnId);
	findPsn.checkPsn(reqPsnId,function(){
	  if(reqPsnId){
	    $.ajax({
	      url:'/psnweb/friend/ajaxaddfriend',
	      type:'post',
	      data:{'des3Id':reqPsnId},
	      dataType:'json',
	      timeout:10000,
	      success:function(data){
	        findPsn.ajaxTimeOut(data, function(){
	          if (data.result == "true") {
	            scmpublictoast(friend_main.addFriend,1000);
	          } else{
	            scmpublictoast(data.msg,1000);
	          }
	        });
	      },
	      error:function(){
	        scmpublictoast("网络异常",1000);
	      }
	    });
	  }
	});
};

/**
 * 检查人员是否存在
 */
findPsn.checkPsn = function(reqPsnId,func) {
  $.ajax({
    url : '/psnweb/friend/ajaxcheckfriend',
    type : 'post',
    data : {
      "des3Id" : reqPsnId
    },
    dataType : "json",
    success : function(data) {
      if (data.status == "isDel") {
        scmpublictoast(friend_main.noPsn, 2000);
      } else {
        func();
      }
    }
  });
}

//选中或取消选中人员
findPsn.selectedPsn = function(obj){
	$this = $(obj);
	var selectedIds = $("#selectedPsnIds").val();
	var des3PsnId = $this.attr("des3PsnId");
	if($this.attr("isSelected") == "false"){
		$this.find(".selected_find_psn").addClass("psn_chosen");
		if(selectedIds == ""){
			selectedIds += des3PsnId;
		}else{
			selectedIds += "," + des3PsnId;
		}
		$this.attr("isSelected", "true");
		$("#addFriendBtn").removeAttr("disabled");
	}else{
		$this.find(".selected_find_psn").removeClass("psn_chosen");
		if(selectedIds != "" && selectedIds.indexOf(des3PsnId) > -1){
			if(selectedIds.indexOf(des3PsnId) == 0){
				if(selectedIds.indexOf(",") > -1){
					selectedIds = selectedIds.replace(des3PsnId + ",", "");
				}else{
					selectedIds = selectedIds.replace(des3PsnId, "");
				}
			}else{
				selectedIds = selectedIds.replace("," + des3PsnId, "");
			}
		}
		$this.attr("isSelected", "false");
	}
	//禁用或启用添加联系人按钮
	if(selectedIds != ""){
		$("#addFriendBtn").removeAttr("disabled");
	}else{
		$("#addFriendBtn").attr("disabled", "disabled");
	}
	$("#selectedPsnIds").val(selectedIds);
}
findPsn.ajaxTimeOut = function(data, myfunction) {
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
				var url =  window.location.href;
				if(url.indexOf("project/view")){//站外的不会自动跳转登录
					url = "/oauth/index?service=" + encodeURIComponent(url);
				}
				document.location.href = url;
				return 0;
			}
		});
	} else {
		if (typeof myfunction == "function") {
			myfunction();
		}
	}
};
