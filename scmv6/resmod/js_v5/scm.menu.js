/**
 * 菜单皮肤页面JS.
 * @author MJG
 */
var Menu =
{
	"thisobj":null,
	"number" : null,
	"secondLeft" :1,
	"timer" : null,
	"SetTimer":function()// 主导航时间延迟的函数
	{
		for (var j = 1; j < 20; j++) {
			if (j == this.number) {
				if ($("#mm" + j) != false) {
					$("#mm" + this.number).attr("class","menuhover");
					$("#mb" + this.number).attr("class","");
					//$("#mb" + this.number).className = "";
				}
			} else {
				if ($("#mm" + j) != false) {
					$("#mm" + j).attr("class","");
					$("#mb" + j).attr("class","hide");
					//$("mb" + j).className = "hide";
				}
			}
		}
	},
	"CheckTime":function(_this)// 设置时间延迟后
	{
		_this.secondLeft--;
		if (_this.secondLeft == 0) {
			clearInterval(_this.timer);
			_this.SetTimer();
		}
	},
	"OnMouseLeft":function()// 主导航鼠标移出函数,清除时间函数
	{
		clearInterval(this.timer);
	},
	"showM":function(thisobj,Num)// 主导航鼠标滑过函数,带时间延迟
	{
		var _this = this;
		this.thisobj=thisobj;
		this.number = Num;
		this.secondLeft = 1;
		this.timer = setTimeout(function(){_this.CheckTime(_this);}, 100);
	},
	"quitNavShow":function(linkId){//显示退出按钮.
		var left=$("#"+linkId).offset().left;
		$(".quit-nav").css("left",left-141);
		$(".quit-nav").show();
	},
	"quitNavHide":function(){//隐藏退出按钮.
		$(".quit-nav").hide();
	},
	"reduction":function(activeMenuId){//还原为程序加载时的菜单显示样式.
		var thisobj=$("#"+activeMenuId);
		var num=Number(activeMenuId.substring(activeMenuId.length-1));
		var _this = this;
		_this.showM(thisobj,num);
	}
};


//nav 设置
var quitNavTimeout;

function quitNavShow(){
	//if(quitNavTimeout!=null&&typeof(quitNavTimeout)!="undefined"){
	//	clearTimeout(quitNavTimeout);
	//}
	//$("#work-reminds").hide();
	//触发本事件的a标签.
	var top = $("#link-setting").offset().top;    //获取a的居上位置
	//var left = $("#link-setting").offset().left;    //获取a的居左位置
	$(".quit-nav").css({"position":"absolute","z-index":999999});
	$(".quit-nav").fadeIn();
	var top2 = $(".quit-nav").offset().top;    //获取a的居上位置
}

function quitNavHide(){
	quitNavTimeout=setTimeout(function(){
		$(".quit-nav").fadeOut();
	},100);
}