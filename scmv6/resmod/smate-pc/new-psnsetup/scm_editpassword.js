
var confirmemailbox = function(options){
    var defaults = {
        screencallback: "" ,
        screencallbackData: ""
    };
    var opts = Object.assign(defaults,options);

    var screencallback = opts.screencallback || function () {};
    var screencallbackData = opts.screencallbackData || {};
    
    
    if(document.getElementsByClassName("confirm-container").length==0){
    	var coverbox = document.createElement("div");
        coverbox.className = "confirm-container";
    	document.body.appendChild(coverbox);
    	var element = '<div class="confirm-email__header">' 
   	                +'<div class="confirm-email__header-title">设置邮件地址</div>'
   	  	            +'<i class="material-icons confirm-email__header-tip">close</i>'
   	                +'</div>'
   	                +'<div class="confirm-email__body">'
   	  	            +'<div class="confirm-email__body-header">'
   	  	 	        +'<div class="confirm-email__body-header__title">邮件地址</div>'
   	  	 	        +'<div class="confirm-email__body-header__func">操作</div>'
   	  	            +'</div>'
   	  	            +'<div class="confirm-email__body-item">'
   	  	 	        +'<div class="confirm-email__body-list" >'
	   	  	 	    +'<div class="confirm-email__body-list__address">llhirissz1@126.com</div>'
                    +'<div class="confirm-email__body-list__confirm"></div>'
	   	  	 	    +'<div class="confirm-email__body-list__funcbox">'
	   	  	 		+'<div class="confirm-email__body-set">发送确认邮件</div>'
	   	  	 		+'<div class="confirm-email__body-opera">设为首要邮件/登录帐号</div>'
	   	  	 	    +'</div>'
   	  	            +'</div>'

                    +'<div class="confirm-email__body-list">'
                    +'<div class="confirm-email__body-list__address">llhirissz1@126.com</div>'
                    +'<div class="confirm-email__body-list__confirm">已确认</div>'
                    +'<div class="confirm-email__body-list__funcbox">'
                    +'<div class="confirm-email__body-opera">设为首要邮件/登录帐号</div>'
                    +'<div class="confirm-email__body-icon"></div>'
                    +'</div>'
                    +'</div> '

                     +'<div class="confirm-email__body-list">'
                    +'<div class="confirm-email__body-list__address">llhirissz1@126.com</div>'
                    +'<div class="confirm-email__body-list__confirm">已确认</div>'
                    +'<div class="confirm-email__body-list__funcbox">'
                    +'<div class="confirm-email__body-opera">设为首要邮件/登录帐号</div>'
                    +'<div class="confirm-email__body-icon"></div>'
                    +'</div>'
                    +'</div> '

                     +'<div class="confirm-email__body-list">'
                    +'<div class="confirm-email__body-list__address">llhirissz1@126.com</div>'
                    +'<div class="confirm-email__body-list__confirm">已确认</div>'
                    +'<div class="confirm-email__body-list__funcbox">'
                    +'<div class="confirm-email__body-opera">设为首要邮件/登录帐号</div>'
                    +'<div class="confirm-email__body-icon"></div>'
                    +'</div>'
                    +'</div> '

                    +'<div class="confirm-email__body-list">'
                    +'<div class="confirm-email__body-list__address">llhirissz1@126.com</div>'
                    +'<div class="confirm-email__body-list__confirm">已确认</div>'
                    +'<div class="confirm-email__body-list__funcbox">'
                    +'<div class="confirm-email__body-opera">设为首要邮件/登录帐号</div>'
                    +'<div class="confirm-email__body-icon"></div>'
                    +'</div>'
                    +'</div> '


                    +'<div class="confirm-email__body-list">'
                    +'<div class="confirm-email__body-list__address">llhirissz1@126.com</div>'
                    +'<div class="confirm-email__body-list__confirm">已确认</div>'
                    +'<div class="confirm-email__body-list__funcbox">'
                    +'<div class="confirm-email__body-opera">设为首要邮件/登录帐号</div>'
                    +'<div class="confirm-email__body-icon"></div>'
                    +'</div>'
                    +'</div> '

                    +'<div class="confirm-email__body-list">'
                    +'<div class="confirm-email__body-list__address">llhirissz1@126.com</div>'
                    +'<div class="confirm-email__body-list__confirm">已确认</div>'
                    +'<div class="confirm-email__body-list__funcbox">'
                    +'<div class="confirm-email__body-opera">设为首要邮件/登录帐号</div>'
                    +'<div class="confirm-email__body-icon"></div>'
                    +'</div>'
                    +'</div> '

                    +'<div class="confirm-email__body-list">'
                    +'<div class="confirm-email__body-list__address">llhirissz1@126.com</div>'
                    +'<div class="confirm-email__body-list__confirm">已确认</div>'
                    +'<div class="confirm-email__body-list__funcbox">'
                    +'<div class="confirm-email__body-opera">设为首要邮件/登录帐号</div>'
                    +'<div class="confirm-email__body-icon"></div>'
                    +'</div>'
                    +'</div> '

                    +'<div class="confirm-email__body-list">'
                    +'<div class="confirm-email__body-list__address">llhirissz1@126.com</div>'
                    +'<div class="confirm-email__body-list__confirm">已确认</div>'
                    +'<div class="confirm-email__body-list__funcbox">'
                    +'<div class="confirm-email__body-opera">设为首要邮件/登录帐号</div>'
                    +'<div class="confirm-email__body-icon"></div> '
                    +'</div>'
                    +'</div>'
                    +'</div>'
                    +'<div class="confirm-email__footer">'
                    +'<div class="confirm-email__footer-newaddress">新增邮件地址:</div>'
                    +'<div class="confirm-email__footer-box"><input type="text" class="confirm-email__footer-input" placeholder="新增邮件地址"></div>'
                    +'<div class="confirm-email__footer-confirm">确定</div>'
   	  	            +'</div>'
   	                +'</div>';

	    var parntbox = document.createElement("div");
	    parntbox.className = "confirm-eamil__box";
	    parntbox.innerHTML = element;
	    coverbox.appendChild(parntbox);
	    const canclele = document.getElementsByClassName("confirm-email__header-tip")[0];
	    var inputlist = document.getElementsByClassName("confirm-email__footer-input");
	    var deletelist = document.getElementsByClassName("confirm-email__body-icon");
	    const allwidth = coverbox.clientWidth;
	    const allheight = coverbox.clientHeight;
	    var selfheight = parntbox.clientHeight;
	    var setwidth = (allwidth - 642)/2 + "px"; 
	    var setheight =  (allheight - selfheight  + 2)/2 + "px";
	    parntbox.style.left = setwidth;
	    parntbox.style.bottom = setheight;
        window.onresize = resize;
	    function resize(){
            var allheight = document.getElementsByClassName("confirm-container")[0].clientHeight;
            var allwidth = document.getElementsByClassName("confirm-container")[0].clientWidth;
	        var setwidth = (allwidth - 642)/2 + "px"; 
	        var setheight =  (allheight - selfheight  + 2)/2 + "px";
	        document.getElementsByClassName("confirm-eamil__box")[0].style.left = setwidth;
	        document.getElementsByClassName("confirm-eamil__box")[0].style.bottom = setheight;
	    }
	    canclele.onclick = function(){
	        parntbox.style.bottom="-650px";
	        setTimeout(function(){
	        	coverbox.removeChild(parntbox);
	       	    coverbox.style.display = "none";
	       	    document.body.removeChild(coverbox);
	        } ,400);
	    };

	    for(var i = 0; i < inputlist.length; i++){
            inputlist[i].onfocus = function(){
            	this.closest(".confirm-email__footer-box").style.borderColor = "#288aed";
            }
            inputlist[i].onblur = function(){
            	this.closest(".confirm-email__footer-box").style.borderColor = "#ddd";
            }
	    };

	    for(var i = 0; i < deletelist.length; i++){
            deletelist[i].onclick = function(){
            	this.closest(".confirm-email__body-item").removeChild(this.closest(".confirm-email__body-list"));
            }
	    };


    }
} 