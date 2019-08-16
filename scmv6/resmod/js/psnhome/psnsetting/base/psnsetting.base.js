/**
 * 个人设置js
 */
var PsnsettingBase  = PsnsettingBase|| {} ;
//改变url
PsnsettingBase.changeUrl = function(targetModule) {
	var json = {};
	var oldUrl = window.location.href;
	var index = oldUrl.lastIndexOf("model");
	var newUrl = window.location.href;
	if (targetModule != undefined && targetModule != "") {
		
		if (index < 0) {
			if(oldUrl.lastIndexOf("?")>0){
				newUrl = oldUrl + "&model=" + targetModule;
			}else{
				newUrl = oldUrl + "?model=" + targetModule;
			}
		} else {
			newUrl = oldUrl.substring(0, index) + "model=" + targetModule;
		}
	}
	window.history.replaceState(json, "", newUrl);
}
//---防止重复点击--zzx--
PsnsettingBase.doHitMore = function(obj,time){
	$(obj).attr("disabled",true);
	var click = $(obj).attr("onclick");
	if(click!=null&&click!=""){
		$(obj).attr("onclick","");
	}
	setTimeout(function(){
		$(obj).removeAttr("disabled");
		if(click!=null&&click!=""){
			$(obj).attr("onclick",click);
		}
	},time);
}
//超时处理
PsnsettingBase.ajaxTimeOut = function(data,myfunction){
	 var toConfirm=false;
		
		if('{"ajaxSessionTimeOut":"yes"}'==data){
			toConfirm = true;
		}
		if(!toConfirm&&data!=null){
			toConfirm=data.ajaxSessionTimeOut;
		}
		if(toConfirm){
			jConfirm(PsnsettingBase.timeOut, PsnsettingBase.tips, function(r) {
				if (r) {
					document.location.href=window.location.href;
					return 0;
				}
			});
			
		}else{
			if(typeof myfunction == "function"){
				myfunction();
			}
		}
};

/**
 * 弹出浮层
 * content 弹出框的内容
 * {content：content}
 */
PsnsettingBase.popupFloat= function( options  ){

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
    	
    	var element = opts.content ;

	    var parntbox = document.createElement("div");
	    parntbox.className = "confirm-eamil__box";
	    
	    var  headContent =  ' <div class="confirm-email__header"> '
	                       +'   <div class="confirm-email__header-title">'+PsnsettingBase.setEmailAdd+'</div> '
	                       +'   <i class="material-icons confirm-email__header-tip">close</i>  '
	                       +' </div> '  ;
	    element  = headContent +  element ;
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
	    function resize(){
            var allheight = document.getElementsByClassName("confirm-container")[0].clientHeight;
            var allwidth = document.getElementsByClassName("confirm-container")[0].clientWidth;
	        var setwidth = (allwidth - 642)/2 + "px"; 
	        var setheight =  (allheight - selfheight  + 2)/2 + "px";
	        document.getElementsByClassName("confirm-eamil__box")[0].style.left = setwidth;
	        document.getElementsByClassName("confirm-eamil__box")[0].style.bottom = setheight;
	    }
	    window.onresize = resize;
	    canclele.onclick = function(){
	        parntbox.style.bottom="-650px";
	        setTimeout(function(){
	        	coverbox.removeChild(parntbox);
	       	    coverbox.style.display = "none";
	       	    document.body.removeChild(coverbox);
	        } ,400);
	        $("#psnsetting_change_password").click();
	    };

	    for(var i = 0; i < inputlist.length; i++){
            inputlist[i].onfocus = function(){
            	this.closest(".confirm-email__footer-box").style.borderColor = "#288aed";
            }
            inputlist[i].onblur = function(){
            	this.closest(".confirm-email__footer-box").style.borderColor = "#ddd";
            }
	    };

	  /*  for(var i = 0; i < deletelist.length; i++){
            deletelist[i].onclick = function(){
            	this.closest(".confirm-email__body-item").removeChild(this.closest(".confirm-email__body-list"));
            }
	    };*/


    }

}