PsnsettingPwd.sendValidateEmail/**
 * 个人设置js
 */
var PsnsettingPwd  = PsnsettingPwd|| {} ;
PsnsettingPwd.initForm = function(){
	
	$("#changePwdForm").validate({
		errorPlacement : function(error, element) {//修改错误的显示位置. targetloca
			//checkbox换行显示
			if (element.is(':checkbox')) {
				$('<br/>').appendTo(element.parent());
				error.appendTo(element.parent());
			} else {
				/* error.appendTo(element.parent()); */
				error.appendTo(element.closest(".targetloca"));
			}
		},
		rules : {
			oldpassword : {
				required : true,
				minlength :6 ,
				maxlength :40 
			},
			newpassword : {
				required : true,
				minlength :6,
				maxlength :40 
			},
			renewpassword : {
				required : true,
				minlength :6,
				maxlength :40 ,
				equalTo:"#newpassword" 
			}
			
		},
		messages : {
			oldpassword : {
				required : PsnsettingPwd.noblank,
				minlength : PsnsettingPwd.lessthan
			},
			newpassword : {
				required : PsnsettingPwd.noblank,
				minlength :  PsnsettingPwd.lessthan
			},
			renewpassword : {
				required :  PsnsettingPwd.noblank,
				minlength : PsnsettingPwd.lessthan ,
				equalTo : PsnsettingPwd.incorrect
			}
		},
		 submitHandler: function(form) {
			 PsnsettingPwd.savePwd();
         },
	});
	
	//qq/微信绑定提示
	var occupy =  $(".page-box").attr("occupy") ;
	$(".page-box").attr("occupy","");
	switch (occupy) {
	case 'QQ':
		scmpublictoast(PsnsettingPwd.bindSuccess , 2000);
		break;
	case 'qqOccupy':
		scmpublictoast(PsnsettingPwd.qqHasBind , 2000);
		break;
	case 'WX':
		scmpublictoast(PsnsettingPwd.bindSuccess , 2000);
		break;
	case 'wxOccupy':
		scmpublictoast(PsnsettingPwd.wxHasBind , 2000);
		break;
	case 'repeatBind':
		scmpublictoast(PsnsettingPwd.repeatBind , 2000);
		break;

	default:
		break;
	}
	BaseUtils.removeParamFromUrl("occupy");
}

/**
 * 保存密码
 */
PsnsettingPwd.savePwd = function(){
	
	  if ( !$("#changePwdForm").valid()) {
	      return ;
	   }

	var paramt = {
			oldpassword :$("#oldpassword").val(),
			newpassword :$("#newpassword").val(),
			renewpassword :$("#renewpassword").val()
	} ;
	$.ajax({
		url : "/psnweb/psnsetting/ajaxsavepwd",
		type : 'post',
		dataType:'json',
		data:paramt,
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				scmpublictoast(data.msg , 2000);
			});
		},
		error: function(){
		}
	});	
};

/**
 * 编辑个人邮箱
 */
PsnsettingPwd.loadPsnEmail = function(){
	$("#psnsetting_account_email").click();
};

/**
 * 添加个人邮箱
 */
PsnsettingPwd.addPsnEmail = function(obj){
  
  PsnsettingBase.doHitMore(obj,2000);
  if($(".confirm-email__body-list").length>=10){
    scmpublictoast(PsnsettingPwd.emailSumLimit , 2000);
    return;
  }
	 var email = $("#add_psn_email_input").val().trim();
	 var RegEmail = /^[a-z0-9]+[a-z0-9_\-.]*@([a-z0-9][0-9a-z\-]*\.)+[a-z]{2,10}$/i;
	 if(email === ""){
		 scmpublictoast(PsnsettingPwd.emailBlank , 2000);
		 return ;
	 }
	 if(!RegEmail.test(email)){
		 scmpublictoast(PsnsettingPwd.emailIncorrect , 2000);
		 return ;
	 }
	
	var paramt ={email:email} ;
	$.ajax({
		url : "/psnweb/psnsetting/ajaxaddemail",
		type : 'post',
		dataType:'json',
		data:paramt,
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				if(data.result == "success"){
					scmpublictoast(  PsnsettingPwd.addSuccess, 2000);
					PsnsettingPwd.loadPsnEmail();
				}else{
					if(data.code == "email_exit"){
						scmpublictoast(PsnsettingPwd.existEmail , 2000);
					}else if(data.code == "email_use_other"){
						scmpublictoast(PsnsettingPwd.useByOther , 2000);
					}else{
						scmpublictoast(PsnsettingPwd.addFail , 2000);
					}
				}
			});
		},
		error: function(){
		}
	});	
};



/**
 * 重新加载个人邮箱列表
 */
PsnsettingPwd.reloadPsnEmail = function(){
	var paramt ={} ;
	$.ajax({
		url : "/psnweb/psnsetting/ajaxloademail",
		type : 'post',
		dataType:'html',
		data:paramt,
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				$(".confirm-email__body-item").remove();
				$(".confirm-email__body-header").remove();
				$(".confirm-email__footer").remove();
				$(".confirm-eamil__box").find(".confirm-email__header").after(data);
			});
		},
		error: function(){
		}
	});	
};

PsnsettingPwd.delPsnEmailConfirm = function(obj){
	 var screencallbackData={
			 obj:obj
	  };
	
	 var   tip =  PsnsettingPwd.delEmailTip;
	    var option={
	            'screentxt':tip ,
	            'screencallback':PsnsettingPwd.delPsnEmail,
	            'screencallbackData':screencallbackData
	   };
	    popconfirmbox(option)
	
};




/**
 * 删除个人邮箱
 */
PsnsettingPwd.delPsnEmail = function(option){
	
	var obj = option.obj
	var mailId=$(obj).closest(".confirm-email__body-list").attr("mailId");
	var paramt ={mailId:mailId} ;
	$.ajax({
		url : "/psnweb/psnsetting/ajaxdeleteemail",
		type : 'post',
		dataType:'json',
		data:paramt,
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				  if(data.result == "success"){
					  $(obj).closest(".confirm-email__body-list").remove();
					  scmpublictoast(PsnsettingPwd.deleteSuccess , 2000);
				  }else{
					  scmpublictoast(PsnsettingPwd.couldNotDelete , 2000);
				  }
			});
		},
		error: function(){
		}
	});	
};

/**
 * 发送确认邮件
 */
PsnsettingPwd.sendConfirmEmail = function(obj){
	var mailId=$(obj).closest(".confirm-email__body-list").attr("mailId");
	var paramt ={mailId:mailId} ;
	$.ajax({
		url : "/psnweb/psnsetting/ajaxsendconfirmemail",
		type : 'post',
		dataType:'json',
		data:paramt,
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				  if(data.result == "success"){
					  scmpublictoast(PsnsettingPwd.sentSuccess , 2000);
				  }else{
					  scmpublictoast(PsnsettingPwd.sentteFail , 2000);
				  }
			});
		},
		error: function(){
		}
	});	
};
/**
 * 发送验证的邮件
 * resent 再次发送
 */
PsnsettingPwd.sendValidateEmail = function(obj){
	var resend = $(obj).closest(".confirm-email__body-set").attr("resend");
	var mailId=$(obj).closest(".confirm-email__body-list").attr("mailId");
	var paramt ={mailId:mailId  , resend:resend} ;
	$.ajax({
		url : "/psnweb/psnset/ajaxsendvalidatememail",
		type : 'post',
		dataType:'json',
		data:paramt,
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				  if(data.result == true){
					  $(obj).closest(".confirm-email__body-set").attr("resend",true);
					  $("#psnsetting_account_email").click();
					  scmpublictoast(PsnsettingPwd.sentSuccess , 2000);
				  }else{
						scmpublictoast(PsnsettingPwd.sentteFail , 2000);
				  }
			});
		},
		error: function(){
		}
	});	
};


PsnsettingPwd.setFirstEmailConfirm = function(obj){
	 var screencallbackData={
			 obj:obj
	  };
	
	 var   tip =  PsnsettingPwd.setFirstEmailTip;
	    var option={
	            'screentxt':tip ,
	            'screencallback':PsnsettingPwd.showValidatePassword,
	            'screencallbackData':screencallbackData
	   };
	    popconfirmbox(option)
	
};

PsnsettingPwd.showValidatePassword = function (option) {
    var screencover = document.getElementsByClassName("background-screen_cover")[1];
    var targetcover = document.getElementsByClassName("input-verificationcode_container")[1];
    screencover.style.display="flex";
    targetcover.style.left = (document.body.offsetWidth - targetcover.offsetWidth)/2 + "px";
    setTimeout(function(){
        targetcover.style.bottom = (window.innerHeight - targetcover.offsetHeight)/2 + "px";
    },100);
    var obj = option.obj
    var mailId=$(obj).closest(".confirm-email__body-list").attr("mailId");
    $("#psn-password-validate-input").attr("mailId" ,mailId) ;
}

PsnsettingPwd.checkEmailHasUsed=function(obj){
  var mailId=$(obj).closest(".confirm-email__body-list").attr("mailId");
  $("#psn-password-validate-input").attr("mailId" ,mailId) ;
  var  paramt = { mailId:mailId};
  $.ajax({
    url : "/psnweb/psnsetting/ajaxvalidateemailhasused",
    type : 'post',
    dataType:'json',
    data:paramt,
    success : function(data) {
      PsnsettingBase.ajaxTimeOut(data,function(){
        if(data.result == "hasUsed"){
          scmpublictoast(PsnsettingPwd.emailHasUsed , 2000);
        }else {
          PsnsettingPwd.showNewValidatePassword(obj);
        }

      });
    },
    error: function(){
    }
  });
}

PsnsettingPwd.showNewValidatePassword = function (obj) {
  var screencover = document.getElementsByClassName("background-screen_cover")[1];
  var targetcover = document.getElementsByClassName("input-verificationcode_container")[1];
  screencover.style.display="flex";
  targetcover.style.left = (document.body.offsetWidth - targetcover.offsetWidth)/2 + "px";
  setTimeout(function(){
      targetcover.style.bottom = (window.innerHeight - targetcover.offsetHeight)/2 + "px";
  },100);
  var mailId=$(obj).closest(".confirm-email__body-list").attr("mailId");
  $("#psn-password-validate-input").attr("mailId" ,mailId) ;
}

PsnsettingPwd.hideValidatePassword = function(obj){

    $("#psn-password-validate-input").attr("mailId" ,"") ;
    $("#psn-password-validate-input").val("") ;
    var screencover = document.getElementsByClassName("background-screen_cover")[1];
    screencover.style.display="none";
};
PsnsettingPwd.validatePassword = function () {
	var password = $("#psn-password-validate-input").val();

	if($.trim(password) == ""){
        scmpublictoast(PsnsettingPwd.passwordNoblank , 2000);
		return ;
	}
   var  paramt = { oldpassword:password};
	$.ajax({
		url : "/psnweb/psnsetting/ajaxvalidateuser",
		type : 'post',
		dataType:'json',
		data:paramt,
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				if(data.result == "success" ){
					PsnsettingPwd.setFirstEmail();
					PsnsettingPwd.hideValidatePassword();
				}else {
					scmpublictoast(PsnsettingPwd.passwordError , 2000);
				}

			});
		},
		error: function(){
		}
	});


}

/**
 * 设置为首要邮件邮件/登录邮件
 */
PsnsettingPwd.setFirstEmail = function(){

    var mailId = $("#psn-password-validate-input").attr("mailId") ;
	var paramt ={mailId:mailId} ;
	$.ajax({
		url : "/psnweb/psnsetting/ajaxfirstemail",
		type : 'post',
		dataType:'json',
		data:paramt,
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				  if(data.result == "1" ){
					  PsnsettingPwd.loadPsnEmail();
					  scmpublictoast(PsnsettingPwd.setFirstMailSuccess , 2000);
				  }else if(data.result == "2"){
					  PsnsettingPwd.loadPsnEmail();
					  scmpublictoast(PsnsettingPwd.setFirstMailSuccessBuLoginFail , 2000);
				  }else if(data.result == "-1"){
					  //scmpublictoast(PsnsettingPwd.noValidateEmail , 2000);
				    $(".send_validate_"+mailId).click();
				  }else if(data.result == "-2"){
					  scmpublictoast(PsnsettingPwd.emailHasUsed , 2000);
				  }else{
					  scmpublictoast(PsnsettingPwd.operaFailAndTryAgainThen , 2000);
				  }
			});
		},
		error: function(){
		}
	});	
};
/**
 * 测试
 */
PsnsettingPwd.test = function(){
	
	alert("test") ;
}
/**
 * 测试
 */
PsnsettingPwd.forgetPwd = function(email){
	location.href="/oauth/pwd/forget/index?email="+email+"&returnUrl="+domainscm+"/psnweb/psnsetting/main?model=password"  ;
	
}
/**
 * 取消qq绑定
 */
PsnsettingPwd.unbindQQ = function(){
	$.ajax({
		url : "/psnweb/psnsetting/ajaxcancelqqbind",
		type : 'post',
		dataType:'json',
		data:{},
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				 if(data.result == "success"){
					 scmpublictoast(PsnsettingPwd.cancelBindSuccess  , 2000);
					 setTimeout( function(){
						$("#psnsetting_change_password").click();
					 }, 1000);
				 }else{
					 scmpublictoast(PsnsettingPwd.cancelBindFail , 2000);
				 }
			});
		},
		error: function(){
		}
	});	
	
}
/**
 * 取消微信绑定
 */
PsnsettingPwd.unbindWX = function(email){
	$.ajax({
		url : "/psnweb/psnsetting/ajaxcancelwxbind",
		type : 'post',
		dataType:'json',
		data:{},
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				 if(data.result == "success"){
					 scmpublictoast(PsnsettingPwd.cancelBindSuccess , 2000);
					 setTimeout( function(){
							$("#psnsetting_change_password").click();
						 }, 1000);
				 }else{
					 scmpublictoast(PsnsettingPwd.cancelBindFail , 2000);
				 }
			});
		},
		error: function(){
		}
	});	
	
};

PsnsettingPwd.showEmailValidateCodeBox =function(obj){
	var mailId = $(obj).closest(".confirm-email__body-list").attr("mailId");
	$("#psn-set-emall-validate").attr("mailId" ,mailId);
	var screencover = document.getElementsByClassName("background-screen_cover")[0];
	var targetcover = document.getElementsByClassName("input-verificationcode_container")[0];
    screencover.style.display="flex";
    targetcover.style.left = (document.body.offsetWidth - targetcover.offsetWidth)/2 + "px";
    setTimeout(function(){
       targetcover.style.bottom = (window.innerHeight - targetcover.offsetHeight)/2 + "px";
    },100)
    
};
PsnsettingPwd.hidderEmailValidateCodeBox = function(obj){
	$("#psn-set-emall-validate").attr("mailId","");
	$("#psn-set-emall-validate-input").val("");
	var screencover = document.getElementsByClassName("background-screen_cover")[0];
	 screencover.style.display="none";
};
PsnsettingPwd.sureEmailValidateCode = function(obj){
	BaseUtils.doHitMore(obj , 1000);
	var validateCode = $("#psn-set-emall-validate-input").val();
	if($.trim(validateCode) === ""){
		$("#validateAccountCode").html(validateCode);
		scmpublictoast(confirm_email.codeBlankTip,2000);
		return ;
	}
	if($.trim(validateCode).length != 6  ){
		$("#validateAccountCode").html(validateCode);
		scmpublictoast(confirm_email.codeLengthTip,2000);
		return ;
	}
	
	var mailId = $("#psn-set-emall-validate").attr("mailId");
	var params ={'validateCode':$.trim(validateCode)  ,'mailId':mailId} ;
	
	$.ajax({
		url : "/psnweb/psnseting/ajaxdovalidateemail",
		type : 'post',
		dataType:'json',
		data:params,
		success : function(data) {
			PsnsettingBase.ajaxTimeOut(data,function(){
				//0=未处理 ， 1验证成功 ， 9=验证码错误， 2=重新发送  3= 异常
                if(data.result === 1){
                	PsnsettingPwd.hidderEmailValidateCodeBox();
                	scmpublictoast(confirm_email.dealSuccess,2000);
                	$("#psnsetting_account_email").click();
                }else if(data.result === 9){
                	$("#psn-set-emall-validate-input").val("");
                	scmpublictoast(confirm_email.validateCodeError,2000);
                }else{
                	scmpublictoast(confirm_email.operatorException,2000);
                }
			});
		},
		error: function(){
		}
	});	
	
};

/**
 * 个人设置，检查邮件发送的时间，在60秒之内不能重复发送
 */

PsnsettingPwd.checkEmailSendDate = function(){
	$("#psnsetting_main_box").find(".set-container__right-content .confirm-email__body-list").each(function(){
		var $obj =$(this).find(".confirm-email__body-set");
		if($obj.attr("resend") =="true"){
			var delaySendDate =$obj.attr("delaySendDate");
			if( $.trim(delaySendDate) !=="" ){
				var delaySendDate = new Number(delaySendDate) ;
				if(delaySendDate <1){
					return ;
				}
				var destObj =$obj.find("span")[0] ;
				BaseUtils.doHitMore(destObj , delaySendDate*1000);
				$(destObj).attr("content",$(destObj).html());
				//$obj.attr("title",delaySendDate+PsnsettingPwd.canBeResend);
				$(destObj).html(PsnsettingPwd.canBeResend.replace("num" ,delaySendDate));
				$(destObj).css("cssText","color: #999 !important;")
				var interval =setInterval(function(){
					delaySendDate = delaySendDate -1;
					if(delaySendDate == 0){
						$(destObj).html($(destObj).attr("content"));
						$(destObj).css("color","");
						$(destObj).css("cursor","pointer");
						clearInterval(interval);
					}else{
						$(destObj).html(PsnsettingPwd.canBeResend.replace("num" ,delaySendDate));
					}
				}, 1000);
			}
		}
	});
};

PsnsettingPwd.showAddMobileNumBox = function (obj) {
    var data = {
        from:"pwdSet",
    }
    $.ajax({
        url : "/psnweb/mobilevaliate/ajaxgetmobile",
        type : 'post',
        dataType:'json',
        data:{"showMobileBox":true},
        success : function(result) {
            PsnsettingBase.ajaxTimeOut(result,function(){
                var opts = Object.assign(result,data);
                confirmmobile(opts);
            });
        },
        error: function(){
        }
    });

};

PsnsettingPwd.hideAddMobileNumBox = function (obj) {

};
/**
 * 绑定手机号
 */
PsnsettingPwd.bindMobileNum = function (obj) {

};
/**
 * 取消绑定手机号
 */
PsnsettingPwd.unBindMobileNum = function (obj) {
    var screencallbackData={
        obj:obj
    };

    var   tip =  PsnsettingPwd.sureUnbindMobileTip;
    var option={
        'screentxt':tip ,
        'screencallback':PsnsettingPwd.sureUnBindMobileNum,
        'screencallbackData':screencallbackData
    };
    popconfirmbox(option)
};

PsnsettingPwd.sureUnBindMobileNum = function (option){

    var paramt ={} ;
    $.ajax({
        url : "/psnweb/psnsetting/ajaxunbindmobilenum",
        type : 'post',
        dataType:'json',
        data:paramt,
        success : function(data) {
            PsnsettingBase.ajaxTimeOut(data,function(){
                if(data.result == "success"){
                    $("#psnsetting_change_password").click();
                    scmpublictoast(PsnsettingPwd.dealSuccess , 2000);
                }else{
                    scmpublictoast(PsnsettingPwd.dealFail , 2000);
                }
            });
        },
        error: function(){
        }
    });
}
