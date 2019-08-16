(function($) {
	$.fn.dragtouch = function(options) {
		var startX = 0,startY = 0;
	    var defaults = {
	    		isReset:false,
	    		startFunc:null,
	    		moveLeftFunc:null,
	    		moveRightFunc:null,
	    		moveFunc:null,
	    		endFunc:null
		};
	    var options = $.extend(defaults, options);
	    this.each(function(){
	    	dragTouchDevice(this);
	    });
	    
		//判断是否支持触摸事件
		function dragTouchDevice(_this) {
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
		        startX = Number(evt.touches[0].pageX); //获取第一个触点,页面触点X坐标
		    	//$(this).stop(true, false).css('cursor','move'); 
		    	if (typeof defaults['startFunc'] != "undefined" && defaults['startFunc']) {
			        	defaults['startFunc']();
			    }
		    }catch (e) {
		        alert('touchSatrtFunc：' + e.message);
		    }
		    return;
		}
		
		function moveTouchFunc(evt) {
		    try{ //evt.preventDefault(); //阻止触摸时浏览器的缩放、滚动条滚动等
		       // var touch = evt.touches[0]; //获取第一个触点
		        var x = Number( evt.touches[0].pageX); //页面触点X坐标
		        if(x-startX>20){
		        	if (typeof defaults['moveRightFunc'] != "undefined" && defaults['moveRightFunc']) {
			        	defaults['moveRightFunc']();
		        	}
		        }
		        if(x-startX<20){//往左拽
		        	if (typeof defaults['moveLeftFunc'] != "undefined" && defaults['moveLeftFunc']) {
			        	defaults['moveLeftFunc']();
		        	}
		        }
		        if (typeof defaults['moveFunc'] != "undefined" && defaults['moveFunc']) {
		        	defaults['moveFunc'](x-startX);
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
		        	defaults['endFunc']();
		        }
		        if(defaults["isReset"]){
		        	startX=0,startY=0;
		        }
		    }catch (e) {
		        alert('touchEndFunc：' + e.message);
		    }
		    return;
		}
	};
})(jQuery);
