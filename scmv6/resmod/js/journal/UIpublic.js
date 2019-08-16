// JavaScript Document


// 显示隐藏元素 参数为对象ID 
/*function displayObj(obj){
	$(obj).style.display=="none"?$(obj).style.display="":$(obj).style.display="none";
}*/


//顶部提示帮助信息
function tips_display(arrow){
	_arrow=document.getElementById('tips_arrow');
	_sam_ico=document.getElementById("sam_ico");
	_top_tips_cont=document.getElementById("top_tips_cont");	
	if(_top_tips_cont.style.display=="none"){
		_sam_ico.style.display="none";
		_top_tips_cont.style.display="";
		_arrow.innerHTML="<a href='#'>5</a>"
	}else{
		_sam_ico.style.display="";
		_top_tips_cont.style.display="none";
		_arrow.innerHTML="<a href='#'>6</a>"
	}
}

//禁止事件冒泡
function cancleEventUp(oevent){
	if(document.all) window.event.cancelBubble=true;
	else oevent.stopPropagation();
}
  function stopBubble(e) {
        //如果提供了事件对象，则这是一个非IE浏览器
        if ( e && e.stopPropagation )
            //因此它支持W3C的stopPropagation()方法
             e.stopPropagation();
        else
            //否则，我们需要使用IE的方式来取消事件冒泡
             window.event.cancelBubble = true;
     }


/*$(document).ready(function(){
	//表格奇偶列及over效果					   
//	$('._tr_over tr:not([th]):odd').addClass('odd');
//	$('._tr_over tr:not([th]):even').addClass('even');
	$('._tr_over tr:not([th])').hover(
		  function () {
			$(this).addClass("over");
		  },
		  function () {
			$(this).removeClass("over");
	 });
})*/

/*function openFloatDiv(but,div){ 
		var obj = document.getElementById(div);
		$(".selectdiv").hide();
		obj.style.display="block";
		obj.style.top = $(but).offset().top-obj.offsetHeight+"px";
		obj.style.left = $(but).offset().left+"px";
}*/
function openFloatDiv(but,div){ 
		var obj = document.getElementById(div);
		$(".selectdiv").hide();
		obj.style.display="block";
		obj.style.top = $(but).offset().top+22;
		obj.style.left = $(but).offset().left+"px";
}
function cloesFloatDiv(div){
		document.getElementById(div).style.display="none";
}