
var scmpcnewtip =function(options){
    var defaults = {
        targettitle:"",      /*标题*/
        targetcllback:"",    /*回调函数*/
        targetfooter:"1",     /*是否隐藏底部*/
        targettxt: "",       /*显示的文本内容*/
        targetkey:"close",   //是否默认执行回调函数
        targetcllbackData:"", //回调函数参数
        targetconfirmeBefore:"",  /*回调函数*/
        targetWidth:"0",	/*弹框长度*/
        targetHeight:"0"	/*弹框高度*/
    };
    var opts = $.extend({},defaults, options);////material-icons
    if(document.getElementsByClassName("bckground-layer").length==0){
        var inputele = '<div class="self-adaption_container">'
                +'<div class="self-adaption_header">'
                +'<p class="self-adaption_header_content">'
                +'<span class="self-adaption_header-title"></span>'
                +'<i class="self-mask-close self-adaption_header-close"></i>'
                +'</p>'
                +'</div>'
                +'<div class="self-adaption_body"></div>'
                +'<div class="self-adaption_footer">'
                +'<div class="self-adaption_footer-cancle">取消</div>'
                +'<div class="self-adaption_footer-confirm">确认</div>'
                +'</div>'
                +'</div>';

        var newbox = document.createElement("div");
        newbox.className = "bckground-layer";  
        newbox.innerHTML = inputele;
        document.getElementsByTagName("body")[0].appendChild(newbox);
        var container = document.getElementsByClassName("bckground-layer")[0];
        var content = document.getElementsByClassName("self-adaption_container")[0];
        var closetarget = document.getElementsByClassName("self-adaption_header-close")[0];
        var closeele = document.getElementsByClassName("self-adaption_footer-cancle")[0];
        var confirmele = document.getElementsByClassName("self-adaption_footer-confirm")[0];
        var headertitle = document.getElementsByClassName("self-adaption_header-title")[0];
        var bodycontent = document.getElementsByClassName("self-adaption_body")[0];
        headertitle.innerHTML = opts.targettitle;
        bodycontent.innerHTML = opts.targettxt;
        if(document.getElementsByClassName("sie_personnnel_checkbox-title_sub-remark").length>0){
		 	 var heightlist = document.getElementsByClassName("sie_personnnel_checkbox-title_sub-remark");
			 for(var i = 0;i < heightlist.length; i++){
				 heightlist[i].closest(".sie_personnnel_checkbox-item").querySelector(".sie_personnnel_checkbox-title_sub-time").style.height = heightlist[i].offsetHeight + "px";
			 }
        }
        if(opts.targetfooter != 1){
        	document.getElementsByClassName("self-adaption_footer")[0].style.display="none";
        }
        if(opts.targetWidth!=0){
        	content.style.width=opts.targetWidth + "px";
        }
        if(opts.targetHeight!=0){
        	content.style.height=opts.targetHeight + "px";
        }
        var windheight = container.offsetHeight;
        var windwidth = container.offsetWidth;
        var innerheight = content.offsetHeight;
        var innerwidth = content.offsetWidth;
        var windbottom = (windheight - innerheight)/2 + "px";
        var windleft = (windwidth - innerwidth)/2 + "px";
        content.style.bottom = windbottom;
        content.style.left = windleft;
        if(opts.targetkey == "on"){
        	 opts.targetcllback(opts.targetcllbackData);
        }
        closeele.onclick = function(){
            content.style.bottom = "-640px";
            setTimeout(function(){document.getElementsByTagName("body")[0].removeChild(container);},300);
        }
        closetarget.onclick = function(){
                content.style.bottom = "-640px";
                setTimeout(function(){document.getElementsByTagName("body")[0].removeChild(container);},300);
        }
        confirmele.onclick = function(){
        	if(opts.targetconfirmeBefore && typeof(opts.targetconfirmeBefore) == "function"){         		
                if(!opts.targetconfirmeBefore()){
                	return;
                };
            } 
            content.style.bottom = "-640px";
            try{
                if(opts.targetcllback && typeof(opts.targetcllback) == "function"){ 
                    opts.targetcllback(opts.targetcllbackData);
                }
            }catch(e){
                console.log("方法不存在");
            }
            document.getElementsByTagName("body")[0].removeChild(container);
        }
        window.onresize = function(){
        	if((document.getElementsByClassName("self-adaption_container").length!=0)&&(document.getElementsByClassName("bckground-layer").length!=0)){
            	var content = document.getElementsByClassName("self-adaption_container")[0];
                var windbottom = (document.getElementsByClassName("bckground-layer")[0].offsetHeight - content.offsetHeight)/2 + "px";
                var windleft = (document.getElementsByClassName("bckground-layer")[0].offsetWidth - content.offsetWidth)/2 + "px";
                content.style.bottom = windbottom;
                content.style.left = windleft;
        	}

        }
        document.onkeydown=function(event){
            var e = event || window.event || arguments.callee.caller.arguments[0];
            if(e && e.keyCode==27){ // 按 Esc 
            	setTimeout(function(){
                    content.style.bottom = "-640px";
                    setTimeout(function(){document.getElementsByTagName("body")[0].removeChild(container);},300);
                    },300);
              }
        }; 
    } 
}; 
