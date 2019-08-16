var deletenum=0;
var infor = [];
scmpublictoast=function(text,staytime){
    if (infor.length === 0){
        infor.push({"text":text,"staytime":staytime});
        opentoast();
    }else{
        infor.push({"text":text,"staytime":staytime});
    }
}
var opentoast=function(){
    var textmessage = infor[deletenum].text;
    var textTime = infor[deletenum].staytime;   
	    var toast_container= document.getElementsByClassName("toast_container")[0]; //取得弹框外表层
		if(toast_container == null ){                // 判断弹框外表层是否存在
			var div = document.createElement("div"); //创建弹框外表层
			div.className="toast_container";   
			document.getElementsByTagName("body")[0].appendChild(div);} //添加到页面最底部
			if(document.getElementsByClassName("toast_section").length==0){
		        var dav = document.createElement("div");                //创建弹框内内容层
				dav.className = "toast_section";
				document.getElementsByClassName("toast_container")[0].appendChild(dav); //将弹框内容层添加到弹框外表层中
				var timeshow = parseFloat(window.getComputedStyle(dav,null).getPropertyValue("transition-duration"))*1000;
                var toast_message = document.getElementsByClassName("toast_section")[0]; //取得弹框内容层
                toast_message.innerHTML = textmessage;
                setTimeout(function(){
                	//toast_message.style.bottom= "10px";  //将弹框的底边设置为0px(向上弹出)
                	toast_message.style.transform="translateY(-100px)";
                },0);
			    setTimeout(function(){
			    	toast_message.style.transform="translateY(100px)";
					//toast_message.style.bottom="-80px"; //将弹框的底边设置为-80px(向下收回)
					setTimeout(function(){
					toast_message.remove();   //将弹框的内容层删除
					++deletenum;              //将计数加一
					if (deletenum<infor.length){
					    opentoast();    
					}else{                    //没有要显示的内容时，清空数组与计数为下一次显示做准备
    	                infor.splice(0,infor.length);
    	                deletenum=0;
                        }   //回调函数
				   },timeshow)
			    },textTime+timeshow);
			}
}
