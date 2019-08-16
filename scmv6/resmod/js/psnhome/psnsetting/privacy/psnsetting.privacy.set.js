/**
 * 
 */

 var PsnsettingPrivacy =  PsnsettingPrivacy || {};



/**
 * 初始化
 */
PsnsettingPrivacy.init = function(data){
	PsnsettingPrivacy.initpageclick();
	PsnsettingPrivacy.initSettings();
};


/*
 * 初始化页面设置数据
 */

PsnsettingPrivacy.initSettings=function(){
	var data = $("#initPrivateJson").val();
	$("#initPrivateJson").val("");
	 if(data==null){
		return; 
	 }
	 var dataObj=eval('('+data+')');//转换为json对象.
	 for(var i=0;i<dataObj.length;i++){
		 var pk=dataObj[i].pk;
		 //如果选择项为 谁能请求加我为联系人，其查看权限为 1-联系人，则调整查看权限为 2-仅自己.
		 if(pk.privacyAction=='reqAddFrd'){
			 if(dataObj[i].permission=='1'){
				 dataObj[i].permission=2;
			 }
		 }
		//如果选择项为 谁能看到我添加文献的动态，其查看权限为 0-任何人，则调整查看权限为 1-联系人.  2-仅自己.   , 4 为关注我的人
		 if(pk.privacyAction=='vMyLiter'){
			 if(dataObj[i].permission!='2'){
				 dataObj[i].permission=4;
			 }
		 }
		 var  permission = dataObj[i].permission ;
		 $("#"+pk.privacyAction).attr("code"  , permission);	 
		 //请求加为联系人特殊
		 if(pk.privacyAction === "reqAddFrd"){
			   if(permission =="1" || permission == "2"){
				   $("#"+pk.privacyAction).html(PsnsettingPrivacy.constData[3]);	 
			   }else{
				   $("#"+pk.privacyAction).html(PsnsettingPrivacy.constData[0]);	 
			   }
		 }else{
			 $("#"+pk.privacyAction).html(PsnsettingPrivacy.constData[permission]);	 
		 }
		 
	 }
};

/**
 * 初始化页面点击事件
 */
PsnsettingPrivacy.initpageclick = function (){
	
	
	var checklist = document.getElementsByClassName("set-privacy__list-item");
    var checbtn = document.getElementsByClassName("set-privacy__list-flag"); 
    var parlist = document.getElementsByClassName("set-privacy__list-content"); 
    for(var i=0; i < parlist.length;i++){
      parlist[i].onclick = function(e){
        if( this.querySelector(".set-privacy__selector-container").style.display=="block"){
          this.querySelector(".set-privacy__selector-container").style.display="none"
        }else{
          $(".set-privacy__selector-container").css("display" , "none");
          this.querySelector(".set-privacy__selector-container").style.display="block";
          if(e.currentTarget){
              if(e.stopPropagation){
                e.stopPropagation();
              }else{
                e.cancelBubble=true;
              }
            }

        }
      }
    }
   /* for(var i = 0 ;i < checbtn.length; i++){
      checbtn[i].onclick = function(e){
        if(this.closest(".set-privacy__list-content").querySelector(".set-privacy__selector-container").style.display == "block"){
          this.closest(".set-privacy__list-content").querySelector(".set-privacy__selector-container").style.display == "none";
        }else{
          this.closest(".set-privacy__list-content").querySelector(".set-privacy__selector-container").style.display == "block";
          if(e.currentTarget){
              if(e.stopPropagation){
                e.stopPropagation();
              }else{
                e.cancelBubble=true;
              }
          }
        }
      }
    }*/
    for(var i = 0;i < checklist.length; i++){
        checklist[i].onmousedown = function(){
          this.closest(".set-privacy__list-content").querySelector(".set-privacy__list-inner").innerHTML = this.innerHTML;
          $(this).closest(".set-privacy__list-content").find(".set-privacy__list-inner").attr("code" ,$(this).attr("item") );
          this.closest(".set-privacy__list-content").querySelector(".set-privacy__list-inner").style.color = "#333";
          this.closest(".set-privacy__list-content").querySelector(".set-privacy__selector-container").style.display = "none";
        }
    } 

    document.onclick = function(){
      for(var i =0;i < document.getElementsByClassName("set-privacy__selector-container").length;i++){
        document.getElementsByClassName("set-privacy__selector-container")[i].style.display = "none";
      }
    }
} ;

/**
* 提交修改设置请求.
*/
PsnsettingPrivacy.submitModify = function(obj) {
	PsnsettingBase.doHitMore(obj,3000);
	var colJson = PsnsettingPrivacy.collectDataJson();
	
	$.ajax({
		url : '/psnweb/psnsetting/privacy/ajaxsaveprivacyconfig',
		type : 'POST',
		dataType : 'json',
		data : {
			'userSettings.privacyConfig' : colJson
		},
		error : function(e) {
			scmpublictoast("网络异常" , 2000);
		},
		success : function(json) {
			PsnsettingBase.ajaxTimeOut(json,function(){
				if (json.action == "success") {
					scmpublictoast(PsnsettingPrivacy.savaSuccess , 2000);
				} else if (json.action == "failure") {
					scmpublictoast(PsnsettingPrivacy.savaFail , 2000);
				}
			});
			
		}
	});
};
/**
 * 获取封装JSon信息.
 */
PsnsettingPrivacy.collectDataJson = function() {
	var colJson = "";
	
	$(".set-privacy__list-inner").each(function(i){
		colJson = colJson + "{\"permission\":" + $(this).attr("code")
		+ ",\"pk\":\"privacyAction="+ $(this).attr("id") + "\"},";
	});
	colJson = "[" + colJson.substring(0, colJson.length - 1) + "]";
	return colJson;
};


