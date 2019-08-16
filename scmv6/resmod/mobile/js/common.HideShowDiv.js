/**
 * author:ajb
 */
var common = common ?common :{} ;
scrollDirect  = function (fn ,class_name) {
	
	var beforeScrollTop = window.pageYOffset
	|| document.documentElement.scrollTop
	|| document.body.scrollTop
	|| 0;
    //var beforeScrollTop = document.body.scrollTop;
    fn = fn || function () {
    };
    class_name = class_name || "" ;
    window.addEventListener("scroll", function (event) {
        event = event || window.event;
        var afterScrollTop = window.pageYOffset
    	|| document.documentElement.scrollTop
    	|| document.body.scrollTop
    	|| 0;
       // var afterScrollTop = document.body.scrollTop;
        delta = afterScrollTop - beforeScrollTop;
        beforeScrollTop = afterScrollTop;
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight == scrollHeight) {  //滚动到底部执行事件
        	 //console.dir(class_name+"_-----bottom");
        	if(class_name == "toolbar"){
        		dynamic.ajaxLoadDyn();
        	}
            return;
        }
        if (scrollTop == 0) {  //滚动到头部部执行事件
//            console.dir("我到头部了")；

            }
        if (Math.abs(delta) < 10) {
            return false;
        }
        fn(delta > 0 ? "down" : "up");
    }, false);
}

common.scrolldirect = function( class_name , hide ,show){
	$("."+class_name).addClass("div_show");
	var upflag=1;
	var  downflag= 1;
	 //scroll滑动,上滑和下滑只执行一次！
	scrollDirect(function (direction) {
	     if (direction == "down") {
	         if (downflag) {
	        	//隐藏
	        	 $("."+class_name).addClass(hide);
	        	 $("."+class_name).removeClass(show);
	             downflag = 0;
	             upflag = 1;
	         }
	     }
	     if (direction == "up") {
	         if (upflag) {
	             //$("."+class_name).slideDown("slow");
	        	 //显示
	        	 $("."+class_name).addClass(show);
	        	 $("."+class_name).removeClass(hide);
	            downflag = 1;
	             upflag = 0;
	         }
	     }
	} , class_name);
}
