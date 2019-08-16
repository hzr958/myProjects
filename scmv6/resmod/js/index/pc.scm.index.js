/**
 * 
 */

var ScmIndex = ScmIndex || {} ;

ScmIndex.changeModel = function (model , obj){
	switch (model) {
	case "login":
		$(obj).addClass("hover")
		$("#select_register_id").removeClass("hover")
		$("#login_form_ul").css("display","")
		$("#register_form_ul").css("display","none")
		break;
	case "register":
		$(obj).addClass("hover")
		$("#select_login_id").removeClass("hover")
		$("#login_form_ul").css("display","none")
		$("#register_form_ul").css("display","")
		break;

	default:
		break;
	}
	
}

ScmIndex.initCommonData = function(){
    var username=document.getElementById("username");
    var writepassword=document.getElementById("password");
  
    username.onblur=function (){
        if($.trim(this.value) ===""){
    		this.style.color='#ccc'
    		}
         }
    username.onkeyup=function(event){
    	if( $.trim(this.value) !=="" ) {
    		this.style.color='#333' 
    	 }
    	if(event.keyCode == 13){
    		if($.trim( $("#username").val() ) !== ""   && $.trim( $("#password").val() )  !=="" ){
    			$("#loginForm").submit();
    		}
    	};
    }
    writepassword.onblur=function (){if($.trim(this.value) ===""){this.style.color='#ccc'}}
    writepassword.onkeyup=function(event){
    	 if($.trim(this.value) !=="") { 
    		 this.style.color='#333' 
    	  }
    	 if(event.keyCode == 13){
     		if($.trim( $("#username").val() ) !== ""   && $.trim( $("#password").val() )  !== "" ){
     			$("#loginForm").submit();
     		}
     	};
    };
};

/**
 * 登陆请求. 
 * 如果记住登录 setcookie
 * 如果没选 判断所填帐号与目前kooie所存帐号是否相同
 * 相同 则清除
 */
ScmIndex.login=function(){
	ScmIndex.setCookie("LashLoginUser",$("#username").val(),7);  //记住帐号
};
/**
* 
*/
ScmIndex.initPageInfo=function(){
	if( $("#username").val()!="" && $("#password").val()!=null && $("#password").val()!="" ){
		$("#loginButton").click();
	}else{
		var c_username=ScmIndex.getCookie("LashLoginUser");
		$("#username").val(c_username);
		if($.trim(c_username) !== ""){
			$("#username").css("color" ,"#333");
		}
	}
	
}
ScmIndex.setCookie  = function (c_name,value,expiredays){
   var exdate=new Date();
   exdate.setDate(exdate.getDate()+expiredays);
   document.cookie=c_name+ "=" +escape(value)+((expiredays==null) ? "" : "; expires="+exdate.toGMTString())
}
 ScmIndex.getCookie = function(c_name){
	if (document.cookie.length>0){ 
	   c_start=document.cookie.indexOf(c_name + "=")
	   if (c_start!=-1){ 
	       c_start=c_start + c_name.length+1 
	       c_end=document.cookie.indexOf(";",c_start)
			if (c_end==-1) 
				c_end=document.cookie.length
			return unescape(document.cookie.substring(c_start,c_end))
	   } 
	}
   return ""
};

/**
 * 登录注册切换
 */
ScmIndex.newChangeModel = function (model , obj){
	switch (model) {
	case "login":
		$("#select_login_id").css("display","block");
		$("#select_register_id").css("display","none");
		break;
	case "register":
		$("#select_login_id").css("display","none");
		$("#select_register_id").css("display","block");
		var isPhoneCheck = $("#isPhoneCheck").val();
		if(isPhoneCheck == "2"){//非中国大陆就验证邮箱 
		  $("#phoneZhRegister").css("display","none");
		  $("#phoneEnRegister").css("display","none");
		  $("#mobileNumberDiv").css("display","none");
		  $("#mobileVerifyCodeDiv").css("display","none");
		}else{
		  $("#onlyEmailEnfirstName").css("display","none");
		  $("#onlyEmailEnlastName").css("display","none");
		  $("#onlyEmailZhfirstName").css("display","none");
      $("#onlyEmailZhlastName").css("display","none");
		}
		break;

	default:
		break;
	}
	
}
 