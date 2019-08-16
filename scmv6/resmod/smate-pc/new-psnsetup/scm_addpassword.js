
var addpassconfirmbox = function(options){
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
    	var element = '<div class="add__pass-header">'
    	+'<div class="add__pass-header__title">合并帐号</div>'
			    +'<i class="material-icons add__pass-close">close</i>'
			    +'</div>'
			    +'<div class="add__pass-content">'
			    +'<div class="add__pass-content__tip">请输入需要被合并的帐号</div>'
			    +'<div class="add__pass-content__list">'
			    +'<span class="add__pass-content__list-title">登录帐号:</span>'
			    +'<input class="add__pass-content__list-input" type="text" placeholder="登录帐号">'
			    +'</div>'
			    +'<div  class="add__pass-content__list">'
			    +'<span class="add__pass-content__list-title" >密码:</span>'
			    +'<input class="add__pass-content__list-input" type="text" placeholder="密码">'
			    +'</div>'
			    +'</div>'
			    +'<div class="add__pass-footer">'
			    +'<div class="add__pass-cancle">取消</div>'
			    +'<div class="add__pass-comfir">增加</div>'
			    +'</div>';

	    var parntbox = document.createElement("div");
	    parntbox.className = "add__pass-container";
	    parntbox.innerHTML = element;
	    coverbox.appendChild(parntbox);
	    const canclele = document.getElementsByClassName("add__pass-close")[0];
	    const confirmele = document.getElementsByClassName("add__pass-cancle")[0];
	    var inputlist = document.getElementsByClassName("add__pass-content__list-input");
	    const allwidth = coverbox.clientWidth;
	    const allheight = coverbox.clientHeight;
	    var setwidth = (allwidth - 382)/2 + "px"; 
	    var setheight =  (allheight - 242)/2 + "px";
	    parntbox.style.left = setwidth;
	    parntbox.style.bottom = setheight;
   
        window.onresize = resize;
	    function resize(){
            var allheight = document.getElementsByClassName("confirm-container")[0].clientHeight;
            var allwidth = document.getElementsByClassName("confirm-container")[0].clientWidth;
	        var setwidth = (allwidth - 382)/2 + "px"; 
	        var setheight =  (allheight - 242)/2 + "px";
	        document.getElementsByClassName("add__pass-container")[0].style.left = setwidth;
	        document.getElementsByClassName("add__pass-container")[0].style.bottom = setheight;
	    }

	    canclele.onclick = function(){
	        parntbox.style.bottom="-260px";
	        setTimeout(function(){
	        	coverbox.removeChild(parntbox);
	       	    coverbox.style.display = "none";
	       	    document.body.removeChild(coverbox);
	        } ,400);
	    };
	   
	    confirmele.onclick = function(){
	    	screencallback(screencallbackData);
            parntbox.style.bottom="-260px";
	        setTimeout(function(){
	        	coverbox.removeChild(parntbox);
	        	coverbox.style.display = "none";
	        	document.body.removeChild(coverbox);
	        } ,400);
	    }; 

	    for(var i = 0; i < inputlist.length; i++){
            inputlist[i].onfocus = function(){
            	this.style.borderColor = "#288aed";
            }
            inputlist[i].onblur = function(){
            	this.style.borderColor = "#ddd";
            }
	    }


    }
} 