/**
 * 输入框水印插件
 * 
 * @author weilong peng
 */
(function($, window) {
	$.fn.divWatermark = function(options) {
		var defaults = {
			tipCon : 'Please enter your content', // 提示信息
			blurClass : false
		// 当输入框失去焦点时提示文本的颜色
		};
		var opts = $.extend(defaults, options);
		return $(this).each(function() {
			initDivWatermark($(this), opts);
		});
	};

	var initDivWatermark = function(obj, options) {
		var countLabelObj=$(obj).parent().find(".count_label");
		
		if (options.blurClass == undefined || options.blurClass == '') {
			if ($(obj).text() == "") {
				$(obj).text(options.tipCon);
			}

			if ($(obj).text() == options.tipCon) {
				$(obj).css('color', '#999');
			} else {
				$(obj).css('color', '#000');
			}
			
			$(countLabelObj).text($(obj).text().length);
			obj.blur(function() {
				if ($(this).text().length == 0) {
					$(this).text(options.tipCon).css('color', '#999');
				}
				$(countLabelObj).text($(this).text().length);
			});
			obj.focus(function() {
				if ($(this).text() == options.tipCon || $(this).text() == "") {
					$(this).text('').css('color', '#000');
				}
				$(countLabelObj).parent().show();
				$(countLabelObj).text($(this).text().length);
			});
		} else {
			if ($(obj).text() == "") {
				$(obj).text(options.tipCon);
			}

			if ($(obj).text() == options.tipCon) {
				$(obj).addClass(options.blurClass);
			} else {
				$(obj).removeClass(options.blurClass);
				$(obj).css('color', '#000');
			}

			obj.blur(function() {
			
				if ($.trim($(this).text()).length == 0) {
					$(this).text(options.tipCon).addClass(options.blurClass);
					$(countLabelObj).text(0);
				}else{
					//$(this).removeClass(options.blurClass);
				}
			});
			obj
					.focus(function() {
						if ($.trim($(this).text()) == options.tipCon
								|| $.trim($(this).text()) == "") {
							$(this).text('').removeClass(options.blurClass);
							$(countLabelObj).text(
									$(this).text().length);
						}
						$(countLabelObj).parent().show();
					});
		}
	};
})(jQuery, window);