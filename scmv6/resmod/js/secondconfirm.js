var secondtip=function(text,callback){
	if(document.getElementById("dev_jconfirm")==null){ //判断是否存在信息提示框(用id的原因是同类名的dialogs_box太多)
		var dav='<div class="dialogs__box" id="dev_jconfirm" dialog-id="dev_jconfirm_ui" style="width: 192.906px; visibility: visible; height: 104px; top: 129px; left: 855.047px; opacity: 1; display: none;">'
		    +'<div class="dialogs__modal_text"></div>'
            +'<div class="dialogs__modal_actions">'
            +'<button class="button_main button_dense button_primary">确认</button>'
	        +'<button class="button_main button_dense button_delete">取消</button>'
	        +'</div>';
	    var dtv = document.createElement("div");
        dtv.className="background_cover";
	    dtv.innerHTML=dav;
	    document.getElementsByTagName("body")[0].append(dtv);
	    document.getElementsByClassName("dialogs__modal_text")[0].textContent = text;//设置显示信息
	    var pageskin = document.getElementsByClassName("background_cover")[0].getElementsByClassName("dialogs__box")[0];
	};
    /*显示浮层并弹出提示框*/
        pageskin.style.display="block";
    /*监控信息提示框里的两个按钮点击事件*/   
    document.getElementsByClassName("button_primary")[0].addEventListener("click",function(){
    	callback();
        setTimeout(function(){  
            dtv.parentElement.removeChild(dtv);  //删除已经隐藏的信息提示框
        },0);
    });
    document.getElementsByClassName("button_delete")[0].addEventListener("click",function(){
        setTimeout(function(){ 
        	dtv.parentElement.removeChild(dtv);
        },0);
    });
} 