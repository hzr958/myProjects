var smatemobile =function(){
    if(document.getElementsByClassName("bckground-cover").length==0){
        var innerele = '<div class="background-cover__content">请使用电脑端操作</div>';
        var newele = document.createElement("div");
        newele.className = "bckground-cover";  
        newele.innerHTML = innerele;
        document.getElementsByTagName("body")[0].appendChild(newele);
        var windheight = newele.offsetHeight;
        var windwidth = newele.offsetWidth;
        var windbottom = (windheight - 48)/2 + "px";
        var windleft = (windwidth - 240)/2 + "px";
        document.getElementsByClassName("background-cover__content")[0].style.left = windleft;
        document.getElementsByClassName("background-cover__content")[0].style.bottom = windbottom;
        setTimeout(function(){
            document.getElementsByClassName("background-cover__content")[0].style.bottom = "-64px";
            setTimeout(function(){document.getElementsByTagName("body")[0].removeChild(newele);},500);
            },1500);
    } 
};    

var Similarresults = function(options){
        var defaults = {
            targettxt: ""
        };
        var opts = Object.assign(defaults,options);
        /* 重复成果打不开的原因
         * 原代码： if(document.getElementsByClassName("bckground-cover").length==0){   存在bckground-cover样式，因此不可能为0
         * */
        if(document.getElementsByClassName("Similarresults-box").length==0){
            var innertext = '<div class="Similarresults-box">'
                +'<div class="Similarresults-header">'
                +'<div class="Similarresults-header_title" id="id_repeat_pub_box_title">'+opts.box_title+'</div>'
                +'<i class="list-results_close Similarresults-close"></i>'
                +'</div>'
                +'<div style="width:732px;height:1px;background-color:#ddd;margin-left:16px;opacity:0;" id="id_repeat_pub_top_line" ></div>'
                +'<div class="Similarresults-container">'
                +'<div class="Similarresults-body" id="id_repeat_pub_box"  style="min-height:150px;" >'
                +'</div>'
                
                +'<div class="Similarresults-footer_up-tool Similarresults-container__tool" id="id_keep_all"  style="margin-top:10px;display:none;" >'+homepage.repeatPubAllSave+'</div>'
                +'</div>'
                +'<div class="Similarresults-footer">'
               
                +'<div class="Similarresults-footer_up-tool" id="id_timer" style="margin-right:-5px;" ></div>'
                +'<div class="Similarresults-footer_down-tool">'
                +'<div class="go-up_group"  style="color:#999;">'+homepage.repeatPubLastRecord+'</div>'
                +'<div class="go-page_num" style="color:#999;" ><span class="page-Determine_num " id="id_repeat_pub_box_pageNo">1</span>/<span class="page-total_num" id="id_repeat_pub_box_pageSize">'+opts.pageSize+'</span></div>'
                +'<div class="go-down_group" style="color:#999;" >'+homepage.repeatPubNextRecord+'</div>'
                +'</div>'
                +'</div>'
                +'</div>';
            var newbox = document.createElement("div");
            newbox.className = "bckground-cover"; 
            document.getElementsByTagName("body")[0].appendChild(newbox);
            newbox.innerHTML = innertext;
            var windheight =  window.outerHeight;
            var windwidth = newbox.offsetWidth;
            var targetele = document.getElementsByClassName("Similarresults-box")[0];
            var windbottom = (windheight - targetele.offsetHeight)/2 + "px";
            var windleft = (windwidth - targetele.offsetWidth)/2 + "px";
            targetele.style.top = windbottom;
            targetele.style.left = windleft;     
            var closeele = document.getElementsByClassName("Similarresults-close")[0];
            var addnum = document.getElementsByClassName("go-down_group")[0];
            var minusnum = document.getElementsByClassName("go-up_group")[0];
            var deletelist = document.getElementsByClassName("confirm-email__body-iconlist");
            var totalnum = document.getElementsByClassName("page-total_num")[0];
            var deternum = document.getElementsByClassName("page-Determine_num")[0];
            
            window.onresize = function(){
                var windbottom = (window.outerHeight - targetele.offsetHeight)/2 + "px";
                var windleft = (newbox.offsetWidth - targetele.offsetWidth)/2 + "px";
                targetele.style.top = windbottom;
                targetele.style.left = windleft;
           }
            document.onkeydown = function(event){
                if(event.keyCode == 27){
                    event.stopPropagation();
                    event.preventDefault();
                    targetele.style.bottom = "-600px";
                    setTimeout(function(){
                        document.getElementsByTagName("body")[0].removeChild(newbox);
                    },700);
                    RepeatPub.closerepeatpubUI(event);
                }
            }
            closeele.onclick = function(e){
                targetele.style.bottom = "-600px";
                setTimeout(function(){
                    document.getElementsByTagName("body")[0].removeChild(newbox);
                },700);
                RepeatPub.closerepeatpubUI(e);
            }
            document.getElementById("id_keep_all").onclick = function(e){
            	RepeatPub.keepAll(e);
            }
            addnum.onclick = function(e){
                var deter = parseInt(deternum.innerHTML);
                var total = parseInt(totalnum.innerHTML);
                if((deter+1) <= total ){
                    RepeatPub.shownextpage(e);
                }
            }
            minusnum.onclick = function(e){
                var deter = parseInt(deternum.innerHTML);
                var total = parseInt(totalnum.innerHTML);
                if((deter-1) >= 1 ){
                    RepeatPub.showlastpage(e);
                }
            }
       
            
    } 
}

var resumetipbox =function(options){
    var defaults = {
        targettxt: ""
    };
    var opts = Object.assign(defaults,options);
    var newcontent = opts.targettxt;
    if(document.getElementsByClassName("resume-container").length==0){
    	if(locale=="en_US"){
    		var innerele = '<div class="resume-container">'
    			+'<div class="resume-container__header">'
    			+'<div class="resume-container__header-title">'+resumetip.cvList+'</div>'
    			+'<i class="list-results_close resume-container__close"></i>'
    			+'</div>'
    			+'<div class="resume-container__neck">'
    			+'<div class="resume-container__neck-create" onclick="PsnResume.createResume()">'+resumetip.createCv+'</div>'
    			+'</div>'
    			+'<div class="resume-container__body">'
    			
    			+'</div>'
    			+'</div>';
    	} else{
    		var innerele = '<div class="resume-container">'
    			+'<div class="resume-container__header">'
    			+'<div class="resume-container__header-title">'+resumetip.cvList+'</div>'
    			+'<i class="list-results_close resume-container__close"></i>'
    			+'</div>'
    			+'<div class="resume-container__neck">'
    			+'<div class="resume-container__neck-create" onclick="PsnResume.createResume()">'+resumetip.createCv+'</div>'
    			+'</div>'
    			+'<div class="resume-container__body">'
    			
    			+'</div>'
    			+'</div>';
    	}
        var newbox = document.createElement("div");
        newbox.className = "bckground-cover";
        newbox.innerHTML = innerele;
        document.getElementsByTagName("body")[0].appendChild(newbox); 
        document.getElementsByClassName("resume-container__body")[0].innerHTML = newcontent;
        var targetele = document.getElementsByClassName("resume-container")[0];
        var windbottom = (newbox.offsetHeight - targetele.offsetHeight)/2 + "px";
        var windleft = (newbox.offsetWidth - targetele.offsetWidth)/2 + "px";
        targetele.style.bottom = windbottom;
        targetele.style.left = windleft;
        var closeele = document.getElementsByClassName("resume-container__close")[0]; 
        var deltelist = document.getElementsByClassName("confirm-email__body-icon");
        closeele.onclick = function(){
        	setTimeout(function(){
	        	document.getElementsByClassName("resume-container")[0].style.bottom = "-640px";
	        	setTimeout(function(){document.getElementsByTagName("body")[0].removeChild(newbox);},300);
            },200);
        }
        document.onkeydown = function(event){
            if(event.keyCode == 27){
                event.stopPropagation();
                event.preventDefault();
                setTimeout(function(){
                    document.getElementsByClassName("resume-container")[0].style.bottom = "-640px";
                    setTimeout(function(){document.getElementsByTagName("body")[0].removeChild(newbox);},300);
                },200);
            }
        }
        window.onresize = function(){
            var windbottom = (newbox.offsetHeight - targetele.offsetHeight)/2 + "px";
            var windleft = (newbox.offsetWidth - targetele.offsetWidth)/2 + "px";
            targetele.style.bottom = windbottom;
            targetele.style.left = windleft;
        }
    } 
};   
function editpsninfor(){
	if(document.getElementsByClassName("bckground-cover").length==0){
	    var innerele='<div class="edit-container">'
	    +'<div class="edit-container_title">'
	    +'<div class="edit-container_title-content">个人信息</div>'
	    +'</div>'
	    +'<div class="edit-container_body">   '
	    +'<div class="edit-container_subling" >'
	    + '<div class="edit-container_item edit-container_position">'
	    +'<label class="edit-container_tip">姓名</label>'
	    +'<input class="edit-container_content" id="edit-name" maxlength="100">'
	    +'<div class="edit-container_item-tip">'
	    +'<span class="edit-container_item-icon">*</span>'
	    +'<span class="edit-container_item-tip_content">姓名不能为空</span>'
	    +'</div>'
	    +'</div>'
	    +'<div class="edit-container_item">'
	    +'<label class="edit-container_tip">职位</label>'
	    +'<input class="edit-container_content" maxlength="100">'
	    +'</div>'
	    +'</div>'
	    +'<div class="edit-container_subling">'
	    +'<div class="edit-container_item">'
	    +'<label class="edit-container_tip">单位</label>'
	    +'<input class="edit-container_content" maxlength="100">'
	    +'</div>'
	    +'<div class="edit-container_item">'
	    +'<label class="edit-container_tip">学位</label>'
	    +'<input class="edit-container_content" maxlength="100">'
	    +'</div>'
	    +'</div>'
	    +'</div>'
	    +'<div class="edit-container_footer">'
	    +'<div class="edit-container_footer-cancle">取消</div>'
	    +'<div class="edit-container_footer-confirm">确定</div>'
	    +'</div>'
	    +'</div>';
	    var newbox = document.createElement("div");
	    newbox.className = "bckground-cover";  
	    newbox.innerHTML = innerele;
	    document.getElementsByTagName("body")[0].appendChild(newbox);
	    var container = document.getElementsByClassName("bckground-cover")[0];
	    var content = document.getElementsByClassName("edit-container")[0];
	    var windheight = container.offsetHeight;
	    var windwidth = container.offsetWidth;
	    var innerheight = content.offsetHeight;
	    var innerwidth = content.offsetWidth;
	    content.style.bottom =(container.offsetHeight - innerheight)/2 + "px";
	    content.style.left = (container.offsetWidth - innerwidth)/2 + "px";
	    var inputlist = document.getElementsByClassName("edit-container_content"); 
	    var confirmele = document.getElementsByClassName("edit-container_footer-confirm")[0];
	    var concleele = document.getElementsByClassName("edit-container_footer-cancle")[0];
	    for(var i = 0; i < inputlist.length; i++){
	        inputlist[i].onfocus = function(){
	            this.closest(".edit-container_item").querySelector(".edit-container_tip").style.top="0px";
	            this.closest(".edit-container_item").querySelector(".edit-container_tip").style.color="#288aed";
	            this.closest(".edit-container_item").querySelector(".edit-container_tip").style.fontSize="12px";
	            this.closest(".edit-container_item").style.borderBottom="2px solid #288aed";
	        } 
	        inputlist[i].onblur = function(){
	            var inputtext = this.value.trim();
	            if(inputtext.length == 0){
	                this.closest(".edit-container_item").querySelector(".edit-container_tip").style.top="20px";
	                this.closest(".edit-container_item").querySelector(".edit-container_tip").style.color="#ccc";
	                this.closest(".edit-container_item").querySelector(".edit-container_tip").style.fontSize="14px";
	                this.closest(".edit-container_item").style.borderBottom="2px solid #ccc";
	            }else{
	                this.closest(".edit-container_item").querySelector(".edit-container_tip").style.top="0px";
	                this.closest(".edit-container_item").querySelector(".edit-container_tip").style.color="#ccc";
	                this.closest(".edit-container_item").style.borderBottom="2px solid #ccc";
	            }
	        }
	    }
	    window.onresize = function(){
	        var windbottom = (document.getElementsByClassName("bckground-cover")[0].offsetHeight - innerheight)/2 + "px";
	        var windleft = (document.getElementsByClassName("bckground-cover")[0].offsetWidth - innerwidth)/2 + "px";
	        content.style.bottom = windbottom;
	        content.style.left = windleft;
	    }
	    confirmele.onclick = function(){
	        if(document.getElementById("edit-name").value.trim().length==0){
	          document.getElementsByClassName("edit-container_item-tip")[0].style.display = "block";
	        }else{
	        setTimeout(function(){
	            document.getElementsByClassName("edit-container_item-tip")[0].style.display = "none";
	            document.getElementsByClassName("edit-container")[0].style.bottom = "-640px";
	            setTimeout(function(){document.getElementsByTagName("body")[0].removeChild(newbox);},500);
	        },500);
	        }
	    }
	    concleele.onclick = function(){
	        setTimeout(function(){
	            document.getElementsByClassName("edit-container")[0].style.bottom = "-640px";
	            setTimeout(function(){document.getElementsByTagName("body")[0].removeChild(newbox);},500);
	        },500);
	    }
	    }
	};
