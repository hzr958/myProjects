var confirmmobile = function(options){
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
    	            +'<span id="confirm_title">'+confirm_mobile.title_box+'</span>'
  	                +'<i class="list-results_close getConfirmnum_close"></i>'
  	                +'</div>'
  	                +'<div class="getConfirmnum_container-body">'
  	 	            +'<div class="getConfirmnum_container-body_title1" id="confirm_tip">'+confirm_mobile.tip01_box+'</div>'

			  	 	+'<div class="getConfirmnum_container-body_input" style="justify-content:flex-start;margin-left: 20px;margin-bottom: 10px;">'
		  	 		+'<span class="getConfirmnum_container-body_input-validateCode" style="width: 132px;text-align:right;">'+confirm_mobile.mobile+'</span>'
		  	 		+'<div class="getConfirmnum_container-body_input-area"  >'
  	 			    +'<input class="getConfirmnum_container-body_input-area_box" ;  maxlength="11" id="mobileNumber">'
		  	 		+'</div>'
		  	 		+'<div class="getConfirmnum_container-body_input-reset onsending-code_tophone" id="send_mobile_code" onclick="sendMobileCode(this);">'+confirm_mobile.send+'</div>'
  	 	            +'</div>'

                    +'<div class="getConfirmnum_container-body_input" style="justify-content: flex-start;margin-left: 20px;margin-bottom: 10px;">'
                    +'<span class="getConfirmnum_container-body_input-validateCode"  style="width: 132px;text-align:right;">'+confirm_mobile.validateCode+'</span>'
                    +'<div class="getConfirmnum_container-body_input-area"  >'
                    +'<input class="getConfirmnum_container-body_input-area_box" ;  maxlength="6" id="mobileValidateCode">'
                    +'</div>'
                    +'</div>'

				  	+'</div>'
				  	+'<div class="getConfirmnum_container-footer">'
			  	 	+'<div class="getConfirmnum_container-footer_content" from='+opts.from+' onclick="confirmValidateMobile(this);" >'+confirm_mobile.confirm+'</div>'
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
	   }
	    document.getElementsByClassName("onsending-code_tophone")[0].onmouseup = function(){
	        
	    }
	    if(opts.from != undefined && opts.from == "pwdSet"){
            //$("#confirm_title").html(confirm_mobile.title);
            //$("#confirm_tip").html(confirm_mobile.tip01);
        }
	    if(opts.mobileNumber != undefined && opts.mobileNumber != "" && opts.countdown>0){
	        $("#mobileNumber").val(opts.mobileNumber);
            var htmlContent = confirm_mobile.send;
            var time = opts.countdown;
            $("#send_mobile_code").html(time + "s");
            BaseUtils.doHitMore($("#send_mobile_code")[0] , time*1000) ;
            $("#send_mobile_code").addClass("onsending_code");
            var timeout = setInterval(function () {
                time = time -1;
                if(time == 0){
                    $("#send_mobile_code").html(confirm_mobile.sendAgain);
                    window.clearInterval(timeout)
                    $("#send_mobile_code").removeClass("onsending_code");
                }else{
                    if(!$("#send_mobile_code").hasClass("onsending_code")){
                        $("#send_mobile_code").addClass("onsending_code");
                    }
                    $("#send_mobile_code").html(time + "s");
                }
            },1000);
        }else if(opts.mobileNumber != undefined && opts.mobileNumber != ""){
            $("#mobileNumber").val(opts.mobileNumber);
            $("#send_mobile_code").html(confirm_mobile.sendAgain);
        }else{
            $("#send_mobile_code").html(confirm_mobile.send);
        }
    } 
};

function sendRecount(obj){
    
    var sendcode =  $("#mobileNumber").val().trim();
    var pattern=/^[1][3,4,5,7,8][0-9]{9}$/;
    if(pattern.test(sendcode)){
        if(!$(obj).hasClass("onsending_code")){
            $(obj).addClass("onsending_code");
        }
        var conttext = $(obj).html();
        var total = 59;
        setInterval(function(){
            if(total > 0){
                var cont = total - 1;
                total = cont;
                $(obj).html("请稍后" + cont + "s");
            }else if(total  == 0){
                $(obj).removeClass("onsending_code");
                $(obj).html(conttext);
                clearInterval();
            }
        },1000); 
    }; 
};

//验证账号邮箱是否已经确认过start
function checkMobileNumber(){
  $.ajax({
    url :"/oauth/ip" ,
    type : "post",
    dataType : "json",
    async:false ,
    data : {},
    success : function(data) {
      if(data.result == "1"){//手机号验证
        checkMobile();
      }else{//邮箱验证
        checkLoginEmail();
      }
    },
    error:function(){
    }
});
}

function checkMobile(){
  $.ajax({
    url:"/psnweb/mobilevaliate/ajaxisneed",
    type:'post',
    data:{},
    dataType : 'json',
    success: function(data){
        BaseUtils.ajaxTimeOut(data , function(){
            if(data.result === true){
                confirmmobile(data);
            }else{
                checkLoginEmail();
            }
        });
    },
    error:function(){
    }
});
}

function sendMobileCode (obj){
    var mobileNumber = $.trim( $("#mobileNumber").val() );
    if($.trim(mobileNumber) == ""){
        scmpublictoast(confirm_mobile.mobileNotBlank,1500);
        return ;
    }

    var isMobile = verifuMobileFormate(mobileNumber);
    if(!isMobile){
        return ;
    }

    if(!$(obj).hasClass("onsending_code")){
        $(obj).addClass("onsending_code");
    }
    //防重复点击
    BaseUtils.doHitMore(obj , 2000) ;
    var time = 60 ;
    var htmlContent = $(obj).html();
    $.ajax( {
        url :"/psnweb/psnsetting/ajaxsendmobilecode" ,
        type : "post",
        dataType : "json",
        data : {
            'mobileNumber':$.trim(mobileNumber)
        },
        success : function(data) {
            if(data.result == "success"){
                scmpublictoast(confirm_mobile.sendSuccess,1500);
                BaseUtils.doHitMore(obj , time*1000) ;
                var timeout = setInterval(function () {
                    time = time -1;
                    if(time == 0){
                        $(obj).html(confirm_mobile.sendAgain);
                        window.clearInterval(timeout)
                        $(obj).removeClass("onsending_code");
                    }else{
                        $(obj).html(time + "s");
                    }
                },1000);
            }else if(data.result == "exist"){
                $(obj).removeClass("onsending_code");
                $("#mobileNumber").val("");
                scmpublictoast(confirm_mobile.mobileIsUsed,2000);
            }else{
                $(obj).removeClass("onsending_code");
                scmpublictoast(confirm_mobile.sendError,1500);
            }
        }
    });

}


function confirmValidateMobile (obj){
    var mobileNumber = $.trim( $("#mobileNumber").val() );
    var mobileValidateCode = $.trim( $("#mobileValidateCode").val() );
    if(mobileNumber == ""){
        scmpublictoast(confirm_mobile.mobileNotBlank,1500);
        return ;
    }
    var isMobile = verifuMobileFormate(mobileNumber);
    if(!isMobile){
        return ;
    }
    if(mobileValidateCode == ""){
        scmpublictoast(confirm_mobile.codeNotBlank,1500);
        return ;
    }
    //防重复点击
    BaseUtils.doHitMore(obj , 2000) ;
    $.ajax( {
        url :"/psnweb/mobilevaliate/ajaxsuremobile" ,
        type : "post",
        dataType : "json",
        data : {
            'mobileNumber':$.trim(mobileNumber),
            'mobileValidateCode':mobileValidateCode
        },
        success : function(data) {
            if(data.result == "success"){
                $(".getConfirmnum_close").click();
                if($(obj).attr("from") == "pwdSet"){
                    $("#psnsetting_change_password").click();
                }
                scmpublictoast(confirm_mobile.verifySuccess,1500);
            }else if(data.result == "exist"){
                $("#mobileNumber").val("");
                scmpublictoast(confirm_mobile.mobileIsUsed,2000);
            }else if(data.result == "error"){
                scmpublictoast(confirm_mobile.systemError,1500);
            }else if(data.result == "mobileError"){
                scmpublictoast(confirm_mobile.codeError,1500);
            }else if(data.result == "codeInvalid"){
                scmpublictoast(confirm_mobile.codeExpired,1500);
            }else if(data.result == "codeError"){
                scmpublictoast(confirm_mobile.codeError,1500);
            }
        }
    });

}


function  verifuMobileFormate(mobileNumber){
    var pattern=/^[1][3,4,5,6,7,8,9][0-9]{9}$/;
    if(! pattern.test(mobileNumber)){
        scmpublictoast(confirm_mobile.mobileFromatError,1500);
        return  false;
    };
    return true ;
}





