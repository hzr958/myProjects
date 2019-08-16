(function($) {
	$.fn.capacityFixed = function(options) {
		var defaultTop = $(this).offset().top;
		var defaultRight = $(this).offset().left;
		var defaultWidth = $(this).width();
		var opts = $.extend( {}, options);
		var FixedFun = function(element) {
			var top = defaultTop;
			var right = ($(window).width() - defaultWidth) / 2 + defaultRight;
			$(window).scroll(function() {
				var scrolls = $(this).scrollTop();
				var pubHelpObj=$("#pub_maint_help");
				var shrinkHeight;
				if(pubHelpObj.length>0){
					shrinkHeight=pubHelpObj.height();
				}else{
					shrinkHeight=$("#pub_task_notice").height();
				}
				var marginTop=top+shrinkHeight;
				if (scrolls > marginTop) {
					if (window.XMLHttpRequest) {
						element.css( {
							position : "fixed",
							top : 0-shrinkHeight-15
						});
					} else {
						element.css( {
							top : scrolls
						});
					}
				} else {
					element.css( {
						position : "absolute",
						top : top
					});
				}
			});
		};
		return $(this).each(function() {
			FixedFun($(this));
		});
	};
})(jQuery);
