var showrow= function(options){
    var openshow = document.getElementsByClassName("showrow");
    for(var i = 0; i < openshow.length; i++ ){                         //IE浏览器不支持foreach循环  
    	var judgeval = parseInt(window.getComputedStyle(openshow[i],null).getPropertyValue("height"));
	    openshow[i].onmouseup = function(e){
				var selectObj = window.getSelection();
				var selectext = selectObj.toString();			
				var savejudge = this.style.height;
				if(selectext === ""){
					if(savejudge!="auto"){
						this.style.height = "auto";             //隐藏缩略号
					}else{
						this.style.height = judgeval + "px";       //显示缩略号
				    } 
				}
	    }
    }											  
}

