(function($) {
	$.fn.movetouch = function(options) {
		var startX = 0,startY = 0,fangxiang;
	    var defaults = {
	    		moveDistance:20,
	    		endFunc:null
		};
	    var options = $.extend(defaults, options);
	    this.each(function(){
	    	moveTouchDevice(this);
	    });
	    
		//判断是否支持触摸事件
		function moveTouchDevice(_this) {
		    try {
		    	document.createEvent("TouchEvent");
		    	//绑定事件
		    	_this.addEventListener('touchstart', startTouchFunc, false);
		    	_this.addEventListener('touchmove', moveTouchFunc, false);
		    	_this.addEventListener('touchend', endTouchFunc, false);
		    }catch (e) {
		        alert("不支持TouchEvent事件！" + e.message);
		    }
		    return;
		}
	    
		function startTouchFunc(evt){
			try{ // evt.preventDefault(); //阻止触摸时浏览器的缩放、滚动条滚动等
		        startX = Number(evt.touches[0].pageX); //获取第一个触点页面触点X坐标
		    }catch (e) {
		        alert('touchSatrtFunc：' + e.message);
		    }
		    return;
		}
		
		function moveTouchFunc(evt) {
		    try
		    { //evt.preventDefault(); //阻止触摸时浏览器的缩放、滚动条滚动等
		        var x = Number(evt.touches[0].pageX); //获取第一个触点页面触点X坐标
		        if(x-startX>defaults['moveDistance']){
		        	fangxiang="toRight";
		        }
		        if(x-startX<defaults['moveDistance']){
		        	fangxiang="toLeft";
		        }
		    }catch (e) {
		        alert('touchMoveFunc：' + e.message);
		    }
		    return;
		}
		
		//touch end事件
		function endTouchFunc(evt) {
		    try {//evt.preventDefault(); //阻止触摸时浏览器的缩放、滚动条滚动等
		        if (typeof defaults['endFunc'] != "undefined" && defaults['endFunc']) {
		        	if(fangxiang==null){
		        		fangxiang=="";
		        	}
		        	defaults['endFunc'](fangxiang);
		        }
		        startX = 0,startY = 0,fangxiang=null;
		    }catch (e) {
		        alert('touchEndFunc：' + e.message);
		    }
		    return;
		}
	};
})(jQuery);
