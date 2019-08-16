/**
 * @author zzx
 * 滑动页面
 */
(function($){
	$.doSlide = $.doSlide ? $.doSlide : {};
	$.fn.doSlide = function(options){
		var defaults = {
				objSlide:"",//滑动对象（微信端）
				objLeft:"div[class='dialogs_invitePsn_turnPage prev']",//点击左滑对象
				objRight:"div[class='dialogs_invitePsn_turnPage next']",//点击右滑对象
				PageList:".dialogs_invitePsn_psnList_page",//所有滑动页面
				skipPage:0//直接滑动到第几页
		};
		 if (typeof options != "undefined") {
			 defaults=$.extend({},defaults, options);
	        }
		 var moveLeft = this.find(defaults.objLeft);
	     var moveRight = this.find(defaults.objRight);
	     var objPages = this.find(defaults.PageList);
	     if(typeof objPages != "undefined"&&objPages!=null&&objPages.length!=0){
	     objPages.each(function(index,obj){
	    	 if(index==0){
	    		 moveLeft.hide();
	    		 if(objPages.length==1){
	    			 moveRight.hide();
	    		 }
	    		 $(obj).addClass("active");
	    		 $(obj).removeClass("current");
	    	 }
	    	 $(obj).attr("index",index);
	     });
	     moveLeft.click(function(){//<-----往左
	    	var current = objPages.parent().find(".active");
	    	moveRight.show();
	    	if(current.attr("index")==1){
	    		moveLeft.hide();
	    	}
	    	current.removeClass("active").addClass("next right");
	    	current.prev().removeClass("prev left").addClass("active");
	    });
	     moveRight.click(function(){//往右------>
	    	var current = objPages.parent().find(".active");
	    	moveLeft.show();
	    	if(current.attr("index")==objPages.length-2){
	    		moveRight.hide();
	    	}
	    	current.removeClass("active").addClass("prev left");
	    	current.next().removeClass("next right").addClass("active");
	    });
	     //TODO 滑动事件处理
	   }else{
		   moveLeft.hide();
		   moveRight.hide();
	   }
	}
})(jQuery);
