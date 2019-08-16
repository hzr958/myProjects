/**
 * 获取光标位置、光标截取字符串、设置光标位置、光标预选
 * @param $zzx
 * @param underfined
 */

(function($,underfined){
	// 获取光标位置
	$.fn.getCursorPosition = function(){
		if(this.lengh == 0) return -1;
		var input = $(this)[0];
		var posStr = $(this).val()
		if(posStr==null||typeof posStr == "undefined"|| posStr==""){
			 posStr = $(this).text();
		}
		var pos = posStr.length;
		if(typeof(input.selectionStart)!="undefined"){//非IE
			pos = input.selectionStart;
		}else if(document){//IE
			var r = document.getSelection().anchorOffset;
			pos = r;
		}
		return pos;
	}
	//光标截取字符串
	$.fn.getCursorPositionSubStr = function(){
		var preStr  = "";
		var afterStr  = "";
		var totalStr = $.trim($(this).val());
		if(totalStr==null||typeof totalStr == "undefined"|| totalStr==""){
			totalStr=$.trim($(this).text());
		}
		if(totalStr!=null&&typeof totalStr != "undefined"&&totalStr.length>0){
			var currentPosition = $(this).getCursorPosition();
			if(currentPosition>0){
				preStr = totalStr.substring(0,currentPosition);
			}
			if(currentPosition<totalStr.length){
				afterStr = totalStr.substring(currentPosition,totalStr.length);
			}
		}
		return [preStr,afterStr];
	}
	//设置光标位置、光标预选
	$.fn.setMyCursorPosition = function(selectionStart,selectionEnd){
		if ($(this)[0].setSelectionRange) {
			$(this)[0].focus();  
			$(this)[0].setSelectionRange(selectionStart, selectionEnd);  
		 }else if ($(this)[0].createTextRange) {
		    var range = $(this)[0].createTextRange();  
		    range.collapse(true);  
		    range.moveEnd('character', selectionStart);
		    range.moveStart('character', selectionEnd);
		    range.select();  
		  }  
	}
})(jQuery);
