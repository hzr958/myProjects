(function($) {
	$.fn.capacityFixed = function(options) {
		if(!$(this).offset())
			return;
		var defaultTop = $(this).offset().top;
		var defaultRight = $(this).offset().left;
		var defaultWidth = $(this).width();
		var opts = $.extend( {}, options);
		var obj=this;
		var FixedFun = function(element) {
			var top = defaultTop;
			var right = ($(window).width() - defaultWidth) / 2 + defaultRight;
			$(window).scroll(function() {
				var objHeight=$(obj).height();
				var scrolls = $(this).scrollTop();
				if (scrolls > top) {
					if (window.XMLHttpRequest) {
						element.css( {
							position : "fixed",
							top : 0
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
