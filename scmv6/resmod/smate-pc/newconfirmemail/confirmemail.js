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
    	            +'<span>确认邮件地址</span>'
  	                +'<i class="list-results_close getConfirmnum_close"></i>'
  	                +'</div>'
  	                +'<div class="getConfirmnum_container-body">'
  	 	            +'<div class="getConfirmnum_container-body_title1">请先确认您的邮件地址,获得完整的科研之友服务.</div>'
  	 	            +'<div class="getConfirmnum_container-body_title2">'
		  	 		+'确认代码已发送至'
		  	 		+'<span>'
		  	 		+'<span class="getConfirmnum_container-body_email">XXXXX@qq.com</span>'
		  	 		+'<i class="getConfirmnum_container-body__icon-edit"></i>'
		  	 		+'</span>'
		  	 		+'中,请在下面输入邮件中的确认代码'
			  	 	+'</div>'
			  	 	+'<div class="getConfirmnum_container-body_input">'
		  	 		+'<span>验证码:</span>'
		  	 		+'<div class="getConfirmnum_container-body_input-area">'
  	 			    +'<div class="getConfirmnum_container-body_input-area_box" contenteditable="true"></div>'
		  	 		+'</div>'
		  	 		+'<div class="getConfirmnum_container-body_input-reset">重新发送验证码</div>'
  	 	            +'</div>'
				  	+'</div>'
				  	+'<div class="getConfirmnum_container-footer">'
			  	 	+'<div class="getConfirmnum_container-footer_content">确定</div>'
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
	    window.onresize = resize;
	    function resize(){
	    	var newwidth = coverbox.offsetWidth;
            var newheight = coverbox.offsetHeight;
	        var setwidth = (newwidth - selfwidth)/2 + "px"; 
	        var setheight =  (newheight - selfheight)/2 + "px";
	        parntbox.style.left = setwidth;
	        parntbox.style.bottom = setheight;
	    }
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
    }
}