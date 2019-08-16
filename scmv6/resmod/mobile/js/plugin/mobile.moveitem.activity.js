/**
 * @author wsn
 * 移动端移除列表中记录动画效果
 */
(function($){
	$.moveItem = $.moveItem ? $.moveItem : {};
	$.fn.moveItem = function(options){
		var defaults = {
			obj:$(this),
			fun:function(){},
			timeOut:300,
			objTransform: 'translateX(-100%)',
			nextAllTransform: "",
			objTransition: '0.3s ease-in',
			nextAllTransition: '0.3s',
			needRemove: true
		};
		if (typeof options != "undefined") {
			defaults=$.extend({},defaults, options);
	    }
		defaults.obj.css('transform', defaults.objTransform);
		defaults.obj.css('transition', defaults.objTransition);
		var nextAll = defaults.obj.nextAll();
		if(defaults.nextAllTransform == ""){
			var height = defaults.obj.outerHeight();
			nextAll.css('transform', 'translateY(-'+height+'px)');
		}else{
			nextAll.css('transform', defaults.nextAllTransform);
		}
		nextAll.css('transition', defaults.nextAllTransition);	
		setTimeout(function(){
			if(defaults.needRemove){
				defaults.obj.remove();
			}
			nextAll.css('transform', 'translateY(0px)');
			nextAll.css('transition', '0s');
			defaults.fun();
		}, defaults.timeOut);
	}
	$.moveItemLeft = $.moveItemLeft ? $.moveItemLeft : {};
	$.fn.moveItemLeft = $.fn.moveItem;
	$.moveItemRight = $.moveItemRight ? $.moveItemRight : {};
	$.fn.moveItemRight = function(options){
		var defaults = {
			obj:$(this),
			fun:function(){},
			timeOut:300,
			objTransform: 'translateX(100%)',
			nextAllTransform: "",
			objTransition: '0.3s ease-in',
			nextAllTransition: '0.3s',
			needRemove: true
		};
		if (typeof options != "undefined") {
			defaults=$.extend({},defaults, options);
	    }
		defaults.obj.css('transform', defaults.objTransform);
		defaults.obj.css('transition', defaults.objTransition);
		var nextAll = defaults.obj.nextAll();
		if(defaults.nextAllTransform == ""){
			var height = defaults.obj.outerHeight();
			nextAll.css('transform', 'translateY(-'+height+'px)');
		}else{
			nextAll.css('transform', defaults.nextAllTransform);
		}
		nextAll.css('transition', defaults.nextAllTransition);	
		setTimeout(function(){
			if(defaults.needRemove){
				defaults.obj.remove();
			}
			nextAll.css('transform', 'translateY(0px)');
			nextAll.css('transition', '0s');
			defaults.fun();
		}, defaults.timeOut);
	}
})(jQuery);