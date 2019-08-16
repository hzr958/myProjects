var confirmemail = function(options){
    var defaults = {
		data: ""
	};
    var opts = Object.assign(defaults,options); 
    var newcontent = opts.data;
    if(document.getElementsByClassName("getConfirmnum_container").length==0){
        var coverbox = document.createElement("div");
        coverbox.className = "bckground-cover";
    	document.body.appendChild(coverbox);
    	var element ='<div class="getConfirmnum_container-header">'    	
    	            +'<span>'+confirm_email.title+'</span>'
  	                +'<i class="list-results_close getConfirmnum_close"></i>'
  	                +'</div>'
  	                +'<div class="getConfirmnum_container-body">'
  	 	            +'<div class="getConfirmnum_container-body_title1">'+confirm_email.tip01+'</div>'
  	 	            +'<div class="getConfirmnum_container-body_title2">'
		  	 		+confirm_email.tip02
		  	 		+'<span>'
		  	 		+'<div class="getConfirmnum_container-body_email" id="validateAccountEmail"  oldEmail="'+opts.newEmail+'"   >'+opts.newEmail+'</div>'
		  	 		/*+'<span class="getConfirmnum_container-body_email" id="validateAccountEmail" >'+opts.newEmail+'</span>'*/
		  	 		+'<i class="getConfirmnum_container-body__icon-edit material-icons" canEdit=true >edit</i>'
		  	 		+'</span>'
		  	 		+'<span class="getConfirmnum_container-body__icon-detaile">'+ confirm_email.tip03 +'</span>'
			  	 	+'</div>'
			  	 	+'<div class="getConfirmnum_container-body_input">'
		  	 		+'<span class="getConfirmnum_container-body_input-validateCode">'+confirm_email.validateCode+'</span>'
		  	 		+'<div class="getConfirmnum_container-body_input-area"  >'
  	 			    +'<input class="getConfirmnum_container-body_input-area_box" ;  maxlength="6" id="validateAccountCode">'
		  	 		+'</div>'
		  	 		+'<div class="getConfirmnum_container-body_input-reset" onclick="resendValidateAccount(this);">'+confirm_email.resend+'</div>'
  	 	            +'</div>'
				  	+'</div>'
				  	+'<div class="getConfirmnum_container-footer">'
			  	 	+'<div class="getConfirmnum_container-footer_content"  onclick="confirmValidateAccount(this);" >'+confirm_email.confirm+'</div>'
			  	    +'</div>';
        var parntbox = document.createElement("div");
        parntbox.className = "getConfirmnum_container";
        parntbox.innerHTML = element;
        coverbox.appendChild(parntbox);
        var allwidth = coverbox.offsetWidth;
        var allheight = coverbox.offsetHeight;
        const selfwidth = parntbox.clientWidth;
        const selfheight = parntbox.clientHeight;
        var setwidth = (allwidth - selfwidth)/2 + "px"; 
	    var setheight =  (allheight - selfheight)/2 + "px";
	    parntbox.style.left = setwidth;
	    parntbox.style.bottom = setheight;
	    function resize(){
	    	var newwidth = coverbox.offsetWidth;
            var newheight = coverbox.offsetHeight;
	        var setwidth = (newwidth - selfwidth)/2 + "px"; 
	        var setheight =  (newheight - selfheight)/2 + "px";
	        parntbox.style.left = setwidth;
	        parntbox.style.bottom = setheight;
	    }
	    window.onresize = resize;
	    if(document.getElementsByClassName("getConfirmnum_close")){
            document.getElementsByClassName("getConfirmnum_close")[0].onclick = function(){
            	parntbox.style.bottom= - selfheight + "px";
				setTimeout(function(){
		        	coverbox.removeChild(parntbox);
		        	coverbox.style.display = "none";
		        	document.body.removeChild(coverbox);
				} ,500);
            }
	    }
	    if(document.getElementsByClassName("getConfirmnum_container-body__icon-edit")){
	           document.getElementsByClassName("getConfirmnum_container-body__icon-edit")[0].onclick = function(){
	              if(this.innerHTML=="edit" && $(this).attr("canEdit") === "true"){
	            	  $("#validateAccountEmail").attr("contenteditable" ,"true");
	            	  this.innerHTML="done";
	            	  this.closest(".getConfirmnum_container-body_title2").querySelector(".getConfirmnum_container-body_email").classList.add("getConfirmnum_container-body_bottom");
	            	  this.style.background="#1265cf";
	            	  this.style.color="#fff"  ;
	            	  this.closest(".getConfirmnum_container-body_title2").querySelector(".getConfirmnum_container-body_email").focus();
	              }
	           }
	           document.getElementsByClassName("getConfirmnum_container-body_email")[0].onblur=function(){
	        	   $("#validateAccountEmail").attr("contenteditable" ,false);
	        	   this.classList.remove("getConfirmnum_container-body_bottom");
	        	   var obj =  document.getElementsByClassName("getConfirmnum_container-body__icon-edit")[0] ;
	        	   obj.innerHTML="edit";
	        	   $(obj).attr("canEdit","false")
	        	   //重置可编辑状态
	        	   setTimeout(function() {
	        		   $(obj).attr("canEdit","true")
				  }, 400);
	        	   obj.style.background="#f4f4f4";
	        	   obj.style.color="rgba(0,0,0,0.24)" 
	        	   //检查邮箱
	        	   editEmail();
	        	  
	           };
	          
	   }
    }
};


//验证账号邮箱是否已经确认过start
function checkAccountEmail(){
    //检查手机账号 ,不需要邮箱验证了 2018-12-21-ajb
    checkMobileNumber();

};

//判断邮箱是否验证
function checkLoginEmail() {
    $.ajax({
         url:"/psnweb/accountvaliate/ajaxisneed",
         type:'post',
         data:{},
         dataType : 'json',
         success: function(data){
        	 BaseUtils.ajaxTimeOut(data , function(){
                 if(data.result === true){
                       confirmemail(data);
           			var delaySendDate =data.delaySendDate;
           			delayTimeResendEmail(delaySendDate);
                 }else{

                 }
        	 });
         },
         error:function(){
         }
     });
}

function delayTimeResendEmail( delaySendDate){
	if( $.trim(delaySendDate) !=="" ){
			var delaySendDate = new Number(delaySendDate) ;
			if(delaySendDate <1 ){
				return ;
			}
			var destObj =$(".getConfirmnum_container-body_input-reset")[0] ;
			BaseUtils.doHitMore(destObj , delaySendDate*1000);
			$(destObj).attr("content",$(destObj).html());
			//$obj.attr("title",delaySendDate+PsnsettingPwd.canBeResend);
			$(destObj).html(confirm_email.canBeResend.replace("num" ,delaySendDate));
			$(destObj).css("cssText","color: #999 !important;")
			$(destObj).css("cursor","default");
			var interval =setInterval(function(){
				delaySendDate = delaySendDate -1;
				if(delaySendDate == 0){
					$(destObj).html($(destObj).attr("content"));
					$(destObj).css("color","");
					$(destObj).css("cursor","pointer");
					clearInterval(interval);
				}else{
					$(destObj).html(confirm_email.canBeResend.replace("num" ,delaySendDate));
				}
			}, 1000);
		}
}


//编辑邮箱
function editEmail(){
	var  email  = $("#validateAccountEmail").html();
	email =  $.trim(email) ;
	//去除html标签
	email = email.replace( /<[^>]*>/g, "");
	//去掉特殊字符
	email = email.replace( /&nbsp;+|[" "]+/g, "");
	$("#validateAccountEmail").html(email);
	var  oldEmail  = $("#validateAccountEmail").attr("oldEmail");
	if( oldEmail === email ){
		return ;
	}
	if(email ===""){
		scmpublictoast(confirm_email.emailNotBlank,2000);
		$("#validateAccountEmail").html(oldEmail)
		return ;
	}
	if(!/^[a-z0-9A-Z_]+[a-z0-9_\-.]*@([a-z0-9A-Z_]+\.)+[a-zA-Z_]{2,10}$/i.test(email)){
		scmpublictoast(confirm_email.mailboxIncorrect,2000);
		return ;
	}
	if(email.length>50){
		scmpublictoast(confirm_email.emailToLong,2000);
		return ;
	}
	var params = {'newEmail':email} ;
	 $.ajax({
         url:"/psnweb/accountvalidate/ajaxeditloginname",
         type:'post',
         data:params,
         dataType : 'json',
         success: function(data){   
        	 BaseUtils.ajaxTimeOut(data ,function(){
        		//1 = 更新成功， 2=邮箱被占用 ， 3格式不正确 ，  4异常
                 if(data.result === 1){
                	 $("#validateAccountEmail").attr("oldEmail" ,email );
                	 scmpublictoast(confirm_email.emailChange+email ,2000); 
                 }else if(data.result === 2){
                	 scmpublictoast(confirm_email.emailIsUsed+email ,2000); 
                 }else if(data.result === 3){
                	 scmpublictoast(confirm_email.accountIncorrect+email ,2000); 
                 }else{
                	 scmpublictoast(confirm_email.emailError+email ,2000);  
                 }
        	 });
        	 
         },
         error:function(){
         }
     });
};


//重新发送验证码
function  resendValidateAccount(obj){
	BaseUtils.doHitMore(obj , 2000);
	var params ={'newEmail':$("#validateAccountEmail").html()} ;
	$.ajax({
        url:"/psnweb/accountvalidate/ajaxresendaccountconfirmemail",
        type:'post',
        data:params,
        dataType : 'json',
        success: function(data){   
        	BaseUtils.ajaxTimeOut(data,function(){
        		 if(data.result === true){
        			 delayTimeResendEmail(data.delaySendDate);
                 	scmpublictoast(confirm_email.resendSuccess,2000);
                 }else{
                 	scmpublictoast(confirm_email.resendFail,2000);
                 }
        	})
        },
        error:function(){
        }
    });
};
//确认验证码
function  confirmValidateAccount(obj){
	
	BaseUtils.doHitMore(obj , 1000);
	var validateCode = $("#validateAccountCode").val();
	//除去空格
	//validateCode = validateCode.replace( /&nbsp;+|\\s+/g, "");
	//除去html标签
	//validateCode = validateCode.replace( /<[^>]*>/g, "");
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
	var params ={'validateCode':$.trim(validateCode)} ;
	$.ajax({
        url:"/psnweb/accountvalidate/ajaxdovalidteforcode",
        type:'post',
        data:params,
        dataType : 'json',
        success: function(data){   
        	
        	BaseUtils.ajaxTimeOut(data , function(){
        		//0=未处理 ， 1验证成功 ， 9=验证码错误， 2=重新发送  3= 异常
                if(data.result === 1){
                	scmpublictoast(confirm_email.dealSuccess,2000);
                	$(".getConfirmnum_close").click();
                }else if(data.result === 9){
                	scmpublictoast(confirm_email.validateCodeError,2000);
                }else{
                	scmpublictoast(confirm_email.operatorException,2000);
                }
        	});
        	    
        },
        error:function(){
        }
    });
}

/**
 * 截取输入的字符长度
 */
function truncContentLength(obj , length){
	var content =$(obj).html();
	//输入的是空格或者html标签
	if(!/^$nbsp;*\\s*(<[^>]*>)$/i.test(email)){
		return
	}
	
	if(content.length >length){
		content = content.replace( /[\D]+/g, "");
		$(obj).html(content.substring(0 ,length ));
		scmpublictoast(confirm_email.codeLengthTip,2000);
	}
}










