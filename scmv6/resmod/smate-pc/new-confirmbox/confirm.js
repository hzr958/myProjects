/**
 *   
 *  screenbox         需要加入弹框的对象  必填
 *  screentxt         要在弹框里显示的提示语  选填
 *  screencallback    回调函数
 *  screencallbackData  回调方法添加的参数
 */
var popconfirmbox = function(options){
    var defaults = {
        screentxt: "确认删除？",
        screencallback: "" ,
        screencallbackData: ""
    };
    var opts = Object.assign(defaults,options);
    var screencallback = opts.screencallback || function () {};
    var screencallbackData = opts.screencallbackData || {};
    var coverbox = document.createElement("div");
    coverbox.className = "confirm-container";
    
    if(!document.getElementById("confirm-box")){
    	
    	document.body.appendChild(coverbox);
    	if(locale=="en_US"){
    		var element ='<div class="confirm-box_title">Are you sure to delete?</div>'
    	    	+'<div class="confirm-box_footer">'
    	    	+'<div class="confirm-box_footer-cancle">Cancel</div>'
    	    	+'<div class="confirm-box_footer-confirm">Confirm</div>'
    	    	+'</div>';
    	}else{
    		var element ='<div class="confirm-box_title">确定要删除？</div>'
    	    	+'<div class="confirm-box_footer">'
    	    	+'<div class="confirm-box_footer-cancle">取消</div>'
    	    	+'<div class="confirm-box_footer-confirm">确认</div>'
    	    	+'</div>';
    	}
	    
	    var parntbox = document.createElement("div");
	    parntbox.className = "confirm-box";
	    parntbox.innerHTML = element;
	    coverbox.appendChild(parntbox);
	    /*opts.screenbox.appendChild(parntbox);*/
	    const textele = document.getElementsByClassName("confirm-box_title")[0];
	    const canclele = document.getElementsByClassName("confirm-box_footer-cancle")[0];
	    const confirmele = document.getElementsByClassName("confirm-box_footer-confirm")[0];
	    const allwidth = coverbox.clientWidth;
	    const allheight = coverbox.clientHeight;
	    var setwidth = (allwidth - 280)/2 + "px"; 
	    var setheight =  (allheight - 160)/2 + "px";
	    textele.innerHTML = opts.screentxt;
	    parntbox.style.left = setwidth;
	    parntbox.style.bottom = setheight;
	    function resize(){
	        const allwidth = coverbox.clientWidth;
	        const allheight = coverbox.clientHeight;
	        var setwidth = (allwidth - 280)/2 + "px"; 
	        var setheight =  (allheight - 160)/2 + "px";
	        textele.innerHTML = opts.screentxt;
	        parntbox.style.left = setwidth;
	        parntbox.style.bottom = setheight;
	    }
	    window.onresize = resize;
	    canclele.onclick = function(){
	        parntbox.style.bottom="-190px";
	        setTimeout(function(){
	        	coverbox.removeChild(parntbox);
	       	    coverbox.style.display = "none";
	       	    document.body.removeChild(coverbox);
	        } ,400);
	    };
	   
	    confirmele.onclick = function(){
	    	screencallback(screencallbackData);
            parntbox.style.bottom="-190px";
	        setTimeout(function(){
	        	coverbox.removeChild(parntbox);
	        	coverbox.style.display = "none";
	        	document.body.removeChild(coverbox);
	        } ,400);
	    }; 
    }
} 