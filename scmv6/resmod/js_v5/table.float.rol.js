(function($) {
	$.fn.capacityFixed = function(options) {
		if (!$(this).offset())
			return;
		var obj = this;
		var defaultTop = $(this).offset().top;
		var defaultRight = $(this).offset().left;
		var defaultWidth = $(this).width();
		var opts = $.extend( {}, options);
		var obj = this;
		//ROL-2404 WSN 特定数量成果列表浮动表头样式问题
		var defaultHeight = $(this).height();
		var addHtml = "<div id='addHtml' style='height:"+defaultHeight+"px'></div>";
		
		var FixedFun = function(element) {

			var top = defaultTop;
			$(window).scroll(function() {
				var docHeight = $(document).height();
				var winHeight = $(window).height();
				var margHeight = docHeight - winHeight;
				var scrolls = $(this).scrollTop();
				//浮动表头时不改变表头宽度_MJG_ROL-1072.
				if (margHeight != scrolls) {
					if (scrolls > top) {
						if (window.XMLHttpRequest) {
							element.css( {
								position : "fixed",
								zIndex : 9999,
								width:defaultWidth,
								top : 0
							});
							
							if($("#addHtml").height() == null){

								$("#mainForm").before(addHtml);
							}
							
						} else {
							element.css( {
								top : scrolls
							});
						}
					} else {
						element.css( {
							position : "static"
						});
						
						$("#addHtml").remove();
					}
				}
			});
		};
		return $(this).each(function() {
			FixedFun($(this));
		});
	};
})(jQuery);
