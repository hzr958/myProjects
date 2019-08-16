/**
*输入框水印插件
*@author weilong peng
*/
(function($, window) {
	$.fn.watermark = function(options) {
		var defaults = {
			tipCon : 'Please enter your content', // 提示信息
			blurClass : false // 当输入框失去焦点时提示文本的颜色
		};
		var opts = $.extend(defaults, options);
		return $(this).each(function() {
			initWatermark($(this), opts);
		});
	};
	
	var initWatermark = function(obj, options) {
		if(options.blurClass == undefined || options.blurClass == '') {
			
			if ($(obj).val() == ""){
				$(obj).val(options.tipCon);
			}
			
			if ($(obj).val() == options.tipCon){
				$(obj).css('color', '#999');
			}else{
				$(obj).css('color', '#000');
			}
			
			obj.blur(function(){
				if ($(this).val().length == 0){
					$(this).val(options.tipCon).css('color', '#999');
				}
			});
			obj.focus(function(){
				if ($(this).val() == options.tipCon || $(this).val()==""){
					$(this).val('').css('color', '#000');
				}
			});
		} else {
			if ($(obj).val() == ""){
				$(obj).val(options.tipCon);
			}
			
			if ($(obj).val() == options.tipCon){
				$(obj).addClass(options.blurClass);
			}else{
				$(obj).removeClass(options.blurClass);
				$(obj).css('color', '#000');
			}
			
			obj.blur(function(){
				if ($(this).val().length == 0){
					$(this).val(options.tipCon).addClass(options.blurClass);
					$(obj).css('color', '#999');
				}
			});
			obj.focus(function(){
				if ($(this).val() == options.tipCon || $(this).val()==""){
					$(this).val('').removeClass(options.blurClass);
					$(obj).css('color', '#000');
				}
			});
		}
	};
})(jQuery, window);