/**
*ie6浏览器，在页面最上方显示升级提示
*@author lichangwen
*/
(function($, window) {
	$.fn.browserTips = function(options) {
		var defaults = {
			tipCon_zh : '您当前使用的浏览器是IE6，为了更好的用户体验，<label style="color: rgb(255, 102, 0);">请升级您的浏览器。</label>', // 提示信息
			tipCon_en : 'You are using IE6 currently, for a better experience on ScholarMate,<label style="color: rgb(255, 102, 0);"> Please update your browser.</label>',
				locale: 'zh_CN'
		};
		var opts = $.extend(defaults, options);
		return $(this).each(function() {
			initBrowserTips($(this), opts);
		});
	};
	
	var initBrowserTips = function(obj, options) {
		var browserConf = {};
		var ua = navigator.userAgent.toLowerCase();
		var s;
			(s = ua.match(/msie ([\d.]+)/)) ? browserConf.ie = s[1] : (s = ua
					.match(/firefox\/([\d.]+)/)) ? browserConf.firefox = s[1] : (s = ua
					.match(/chrome\/([\d.]+)/)) ? browserConf.chrome = s[1] : (s = ua
					.match(/opera.([\d.]+)/)) ? browserConf.opera = s[1] : (s = ua
					.match(/version\/([\d.]+).*safari/)) ? browserConf.safari = s[1] : 0;	
					
		var content=options.tipCon_zh;	
		if(options.locale=='en_US') {
			content=options.tipCon_en;
		} 
		if(browserConf.ie=="6.0"){
			var ie6div = "<div class=\"IE6_prompt\"><span class=\"icon_error\">"+content+"</span></div>";
			$("body").html(ie6div+$("body").html());
			//$("<div class=\"IE6_prompt\"><span class=\"icon_error\">"+content+"</span></div>").insertBefore(obj);
		}
	};
})(jQuery, window);