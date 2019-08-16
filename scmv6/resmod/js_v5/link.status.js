(function($) {
	$.fn.disabled = function() {//禁用
		var cloneLink=$(this).clone();
		var linkId=$(this).attr("id");
		cloneLink.attr("id",linkId+"_clone");
		cloneLink.removeAttr("href").removeAttr("onclick");
		cloneLink.attr("disabled","disabled");
		cloneLink.css("color","#ccc");
		$(this).after(cloneLink);
		$(this).hide();
	};
	$.fn.enabled=function(){//启用
		var linkId=$(this).attr("id");
		$("a").remove("#"+linkId+"_clone");
		$(this).show();
	};
})(jQuery);