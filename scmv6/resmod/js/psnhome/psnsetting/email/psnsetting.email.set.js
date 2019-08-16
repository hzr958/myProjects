/**
 * 
 */

 var PsnsettingEmail =  PsnsettingEmail || {};



 PsnsettingEmail.init = function (){
	 PsnsettingEmail.ajaxListPsnMailSet();
	 PsnsettingEmail.initClickEvent();
 };
 
 /**
  * 初始化点击事件
  */
 PsnsettingEmail.initClickEvent=function(){
	 
	  var clicklist = document.getElementsByClassName("set-email__select-btn");
      var checklist = document.getElementsByClassName("selector-language__btn");
      for(var i = 0; i<clicklist.length;i++){
        clicklist[i].onclick = function(){
          if(this.classList.contains("set-email__selector-tip")){
            this.classList.remove("set-email__selector-tip");
            this.classList.add("set-email__tip-selected");
          }else{
            this.classList.remove("set-email__tip-selected");
            this.classList.add("set-email__selector-tip");
          }
        }
      }
      for(var i = 0; i < checklist.length; i++){
        checklist[i].onclick = function(){
          if(this.classList.contains("check_language")){
            document.getElementsByClassName("check_language-selected")[0].classList.add("check_language");
            document.getElementsByClassName("check_language-selected")[0].classList.remove("check_language-selected");
            this.classList.add("check_language-selected");
            this.classList.remove("check_language");
          }
      }
    };
 }
 
 /**
  * 初始化勾选框
  */
 PsnsettingEmail.ajaxListPsnMailSet = function(){
		var paramt = {
		};
		$.ajax({
			url : "/psnweb/psnsetting/ajaxListPsnMailSet",
			type : 'post',
			dataType:'json',
			data:paramt,
			success : function(data) {
				PsnsettingBase.ajaxTimeOut(data,function(){
					 if(data instanceof Array){
						 for(var i = 0 ; i<data.length ; i++){
							var mailTypeId =  data[i].mailTypeId  ;
							var   status = data[i].isReceive  ;
							 if(mailTypeId !=undefined){
								  $(".set-email__selector-list__box").each(function(){
									   if($(this).attr("code") == mailTypeId  &&  status == 1  ){
										   $(this).find(".set-email__select-btn").removeClass("set-email__selector-tip").addClass("set-email__tip-selected");
									   }
								  });
							 }else if(data[i].emailLanguageVersion!=undefined){
								 if(data[i].emailLanguageVersion === "zh_CN"){
								    $("#check_language_id").find("i[code='zh_CN']").removeClass("check_language");
								    $("#check_language_id").find("i[code='zh_CN']").addClass("check_language-selected");
								 }else{
									  $("#check_language_id").find("i[code='en_US']").removeClass("check_language");
									  $("#check_language_id").find("i[code='en_US']").addClass("check_language-selected");
								 }
							 }
							
						 }
					 }
				});
			},
			error: function(){
			}
		});	
 };
 
 
 /**
  * 保存邮件列表设置
  */
 PsnsettingEmail.ajaxSavePsnMailSet = function(obj){
	 PsnsettingBase.doHitMore(obj ,2000);
	 var ids="";
	 var languageVersion = $("#check_language_id").find(".check_language-selected").attr("code");
	 $(".set-email__selector-list__box").each(function(i){
		 if(   $(this).find(".set-email__tip-selected").length >0){
			 ids = ids+ $(this).attr("code")+","
		 }
	 });
	 if(ids !==""){
		 ids = ids.substring(0,ids.length-1);
	 }
		var paramt = {
				'psnMailSet.ids':ids ,
				'psnMailSet.languageVersion':languageVersion
		};
		$.ajax({
			url : "/psnweb/psnsetting/ajaxAddPsnMailSet",
			type : 'post',
			dataType:'json',
			data:paramt,
			success : function(data) {
				PsnsettingBase.ajaxTimeOut(data,function(){
					if(data.result === "success"){
						scmpublictoast(PsnsettingEmail.setSuccess , 2000);
					}else{
						scmpublictoast(PsnsettingEmail.setError, 2000);
					}
				});
			},
			error: function(){
			}
		});	
 }