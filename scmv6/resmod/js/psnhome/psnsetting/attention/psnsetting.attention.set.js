/**
 * 
 */

 var PsnsettingAttention =  PsnsettingAttention || {};
 
 
 
 
 /**
  * 初始化
  */
 PsnsettingAttention.init = function (){
	 PsnsettingAttention.LoadAttPersonList();
	 PsnsettingAttention.initFriendChipBox();
 };
 
 /**
  * 加载关注人列表
  */
 PsnsettingAttention.LoadAttPersonList = function (){
		$.ajax({
			url : "/psnweb/psnsetting/ajaxLoadAttPersonList",
			type : 'post',
			dataType:'html',
			data:{},
			success : function(data) {
				PsnsettingBase.ajaxTimeOut(data,function(){
					$("#attention_psn_list_id").html(data);
				});
			},
			error: function(){
			}
		});	
 };
 
 
 PsnsettingAttention.canelAttPersonConfirm = function(obj){
	 var screencallbackData={
			 obj:obj
	  };
	
	 var   tip =PsnsettingAttention.unfollowTip ;
	    var option={
	            'screentxt':tip ,
	            'screencallback':PsnsettingAttention.canelAttPerson,
	            'screencallbackData':screencallbackData
	   };
	    popconfirmbox(option)
	
};
 
 /**
  * 取消关注
  */
 PsnsettingAttention.canelAttPerson = function (option){
	 
	 var obj = option.obj
	 
	 var id = $(obj).closest(".set-attention__per-box").attr("attPersonId");
	 var  param = {
			 "userSettings.cancelId":id
	 }
		$.ajax({
			url : "/psnweb/psnsetting/ajaxCancelAttPerson",
			type : 'post',
			dataType:'json',
			data:param,
			success : function(data) {
				PsnsettingBase.ajaxTimeOut(data,function(){
					if (data.result == "success") {
						$(obj).closest(".set-attention__per-box").remove();
						 PsnsettingAttention.updateAttPersonCount();
						scmpublictoast(PsnsettingAttention.dealSuccess , 2000);
					} else  {
						scmpublictoast(PsnsettingAttention.dealFail , 2000);
					}
				});
			},
			error: function(){
			}
		});	
 };
 /**
  * 更新关注数
  */
 PsnsettingAttention.updateAttPersonCount = function (obj){
	 
	 var  param = {
	 }
	 $.ajax({
		 url : "/psnweb/psnsetting/ajaxAttPsnCount",
		 type : 'post',
		 dataType:'json',
		 data:param,
		 success : function(data) {
			 PsnsettingBase.ajaxTimeOut(data,function(){
				 if (data.action == "success") {
					  data.attCount
                       $("#attPsnCountId").html("("+data.attCount+PsnsettingAttention.peopleUnit+")")		;			
				 }
			 });
		 },
		 error: function(){
		 }
	 });	
 };

 
 PsnsettingAttention.initFriendChipBox = function (){
	 addFormElementsEvents();
	 var  options = {name:"chipcodeshare" }
	 window.ChipBox(options );
		
 };
 
 /**
  * 添加关注联系人
  */
 PsnsettingAttention.addAtttionFriend = function (){
	 var des3PsnIds = "";
	 if($("#grp_friends").find(".chip__box").length == 0 ){
		 scmpublictoast(PsnsettingAttention.notSelectFriend , 2000);
		 return ;
	 }
	 $("#grp_friends").find(".chip__box").each(function(i){
		 des3PsnIds  = des3PsnIds + $(this).attr("code") + ",";
	 })
	 des3PsnIds = des3PsnIds.substring(0,des3PsnIds.length-1);
	 if($.trim(des3PsnIds)==""){
		 scmpublictoast(PsnsettingAttention.notSelectFriend , 2000);
		 return ;
	 }
	 var  param = {
			 des3PsnIds :des3PsnIds 
	 }
	 $.ajax({
		 url : "/psnweb/psnsetting/ajaxSaveAttFrd",
		 type : 'post',
		 dataType:'json',
		 data:param,
		 success : function(data) {
			 PsnsettingBase.ajaxTimeOut(data,function(){
				 if (data.action == "success" || data.action == "doNothing") {
					 $("#psnsetting_attention").click();
					 scmpublictoast(PsnsettingAttention.dealSuccess , 2000);	
				 }else{
					 scmpublictoast(PsnsettingAttention.dealFail , 2000);	
				 }
			 });
		 },
		 error: function(){
		 }
	 });	
		
 };
 

//关注，查询人员构建的额外的参数
PsnsettingAttention.buildExtralParams = function(){
  var ids = "";
  $("#grp_friends").find(".chip__box").each(function() {
    var des3PsnId = $(this).attr("code");
    if (des3PsnId != "") {
      ids += "," + des3PsnId;
    }
  });
  if (ids != "") {
    ids = ids.substring(1);
  };
 	return {"fromAttention":true,"des3PsnId":ids};
 }