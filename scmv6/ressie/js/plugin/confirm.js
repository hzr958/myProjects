/**
 *   
 *  screenbox         需要加入弹框的对象  必填
 *  screentxt         要在弹框里显示的提示语  选填
 *  screencallback    “确认”回调函数
 *  screencallbackData  “确认”回调方法添加的参数
 *  screencancelcallback    “取消”回调函数
 *  screencancelcallbackData  “取消”回调方法添加的参数
 */
var popconfirmbox = function(options){
    var defaults = {
        screentxt: "确认删除？",
        screencallback: "" ,
        screencallbackData: "",
        screencancelcallback: "",
        screencancelcallbackData: ""
    };
    var opts = Object.assign(defaults,options);
    var screencallback = opts.screencallback || function () {};
    var screencancelcallback = opts.screencancelcallback || function () {};
    var screencallbackData = opts.screencallbackData || {};
    var screencancelcallbackData = opts.screencancelcallbackData || {};
    if(!(document.getElementsByClassName("confirm-container").length > 0)){
	    var coverbox = document.createElement("div");
	    coverbox.className = "confirm-container";
	    if(!document.getElementById("confirm-box")){
	    	document.body.appendChild(coverbox);
	    	var element ='<div class="confirm-box_title">Are you sure to delete?</div>'
	    	    	+'<div class="confirm-box_footer">'
	    	    	+'<div class="confirm-box_footer-cancle">取消</div>'
	    	    	+'<div class="confirm-box_footer-confirm">确认</div>'
	    	    	+'</div>';
	    	
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
		    parntbox.style.width = "300px";
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
		        screencancelcallback(screencancelcallbackData);
		    };
		   
		    confirmele.onclick = function(){
	            parntbox.style.bottom="-190px";
		        coverbox.removeChild(parntbox);
		        coverbox.style.display = "none";
		        document.body.removeChild(coverbox);
		        screencallback(screencallbackData);
		    }; 
	    }  
    } 
} 
