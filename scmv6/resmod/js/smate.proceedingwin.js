function progressWin(resCtx){
	
	this.resctx = "";
	if(resCtx){
		this.resctx = resCtx;
	}
	this.bgObj = null;
	this.msgObj = null;
	
	this.openWin = function(content){
	    //var bordercolor = "#336699"; // 提示窗口的边框颜色 
		var iWidth = Math.max(document.body.clientHeight, document.documentElement.clientWidth); 
		//ljj
//		var iHeight = Math.max(document.body.scrollHeight, document.documentElement.clientHeight;
		var iHeight=document.body.scrollHeight+document.documentElement.clientHeight;
		var bHeight =document.documentElement.scrollTop+document.documentElement.clientHeight;
		var bWidth =document.documentElement.scrollLeft+document.documentElement.clientWidth;
		this.bgObj = document.createElement("div");
		var bgFrame = document.createElement("iframe");
		bgFrame.style.cssText = "position:absolute;left:0px;top:0px;width:"+iWidth+"px;height:"+iHeight+
								"px;filter:Alpha(Opacity=30);opacity:0.3;background-color:#000000;z-index:99991;";
		this.bgObj.style.cssText = "position:absolute;left:0px;top:0px;width:"+iWidth+"px;height:"+iHeight+
		                      "px;filter:Alpha(Opacity=30);opacity:0.3;background-color:#000000;z-index:99991;";
		document.body.appendChild(this.bgObj); 
		this.bgObj.appendChild(bgFrame); 
		
		this.msgObj = document.createElement("div");
		this.msgObj.style.cssText = "position:absolute;font:11px '宋体';top:"+bHeight/2+"px;left:"+bWidth/2+
		                       "px;width:auto;height:auto;text-align:center;line-height:22px;margin:10px,padding:10px;z-index:99992;";
		document.body.appendChild(this.msgObj);
		
		var imgObj = document.createElement("div");
		this.msgObj.appendChild(imgObj);
		imgObj.style.cssText = "margin-right:10px;float:left;";
		imgObj.innerHTML = "<img src='"+this.resctx+"/images/proceedingWin/spinner.gif' width='25px' height='25px' style='border:none;'/>";
		
		var waitObj = document.createElement("div");
		this.msgObj.appendChild(waitObj);
		waitObj.style.cssText = "float:left;";
		if(content){
			waitObj.innerHTML = content;
		}else{
			waitObj.innerHTML = "Please wait.....";
		}
	};
	
	this.closeWin = function(){
		if(this.bgObj && this.msgObj){
			document.body.removeChild(this.bgObj);
			document.body.removeChild(this.msgObj);
			this.bgObj = null;
			this.msgObj = null;
		}
	};
}