var UserSearch = {};
UserSearch.ctx = "";
UserSearch.res = "";
UserSearch.resultSize = 0;
UserSearch.startIndex = 0;
UserSearch.searchSize = 0;
UserSearch.init = function(ctx,res,resultSize,startIndex,searchSize){
	
	UserSearch.ctx = ctx;
	UserSearch.res = res;
	UserSearch.resultSize = resultSize;
	UserSearch.startIndex = startIndex;
	UserSearch.searchSize = searchSize;
	
	if(UserSearch.resultSize > (UserSearch.startIndex + UserSearch.searchSize)){
		$("#us_more_psn_div").show();
	}
	$("#us_main_input").bind("focus",function(){
		$("#us_main_error").hide();
	});
	
	UserSearch.bindUserListBox();
};

UserSearch.searchPerson = function(type){
	var userInfo = $.trim($("#us_main_input").val());
	if(userInfo == ""){
		$("#us_main_error").html(input_eptinf).show();
		return false;
	}
	if(!type){
		$("#us_main_form").submit();
	}else{
		return true;
	}
};
UserSearch.searchMorePerson = function(){
	var searchName = $("#us_main_input").val();
	var searchKey = $("#us_main_input_key").val();
	var pageNo=$("#pageNo").val();

	$("#us_more_psn_link").hide();
	$("#us_more_psn_lodding").show();
		$.ajax( {
			url : '/psnweb/personsearch/ajaxshow',
			type : 'post',
			dataType:'html',
			cache: false,
			data : {'pageNo':parseInt(pageNo)+1,'searchName':searchName,'searchKey':searchKey},
			success : function(data) {
				$("#us_more_psn_link").show();
				$("#us_more_psn_lodding").hide();
				$("#person_search_tb>tbody").append(data);
				UserSearch.bindUserListBox();
				if(data.indexOf("showMore")<0 || data==''){
					$("#us_more_psn_div").remove();
				}
				$("#pageNo").val(parseInt(pageNo)+1);
			},error:function(){
				
			}
		});
	
};
UserSearch.bindUserListBox = function(){
	$(".search_info_box").bind("mouseover",function(){
		$(this).css("background","#eff6fc");
		$(this).find(".link_cheak").show();
	}).bind("mouseleave",function(){
		$(this).css("background","");
		$(this).find(".link_cheak").hide();
	});
	
	$(".addFriendClass").each(function(){
		$(this).thickbox({
			ctxpath:UserSearch.ctx,
			respath:UserSearch.res,
			parameters:{"d3d" : $(this).attr("des3Id")},
			type:"addRequests"
	 });
	});
	
};
//添加联系人请求回调函数
function add_friendReq_callBack(result,psnId){
	if(result){
	$.scmtips.show('success',inviteInf);
	$('#div_af_'+psnId).remove();
	}
};