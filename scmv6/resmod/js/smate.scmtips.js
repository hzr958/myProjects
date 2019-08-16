/**
 * 
 */
(function($) {
	
	$.smate = $.smate ? $.smate : {};	
	$.smate.scmtips = {
			verticalOffset: -75,                // vertical offset of the dialog from center screen, in pixels
			horizontalOffset: 0,                // horizontal offset of the dialog from center screen, in pixels/
			repositionOnResize: true,           // re-centers the dialog on window resize
			defaultWidth:"300px",
			timeOutObj:null,
			warn: function(message,width,timeOut) {
				$.smate.scmtips.show('warn', message,width, timeOut);
			},
			success: function(message,width,timeOut) {
				$.smate.scmtips.show('success',message,width, timeOut);
			},
			error: function(message,width,timeOut) {
				$.smate.scmtips.show('error',message,width,timeOut);
			},
			show: function(type,msg,width,timeOut) {
				//clear last timeout object
				if($.smate.scmtips.timeOutObj){
					clearTimeout($.smate.scmtips.timeOutObj);
				}
				if(!width){
					width = $.smate.scmtips.defaultWidth;
				}else{
					width = width + "px";
				}
				if(!timeOut){
					timeOut = 3000;
				}
				$.smate.scmtips._hide();
				$("BODY").append('<div id="scmtip_container"><ul><li id="scmtip_content"></li></ul></div>');
				$("BODY").append('<div id="scmtip_overlay"><iframe id="scmtip_overlay_frame" frameborder="0" hspace="0" src="" style="width:100%;height:100%;"/></div>');
				// IE6 Fix
//				var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
				var pos = 'fixed'; 
				$("#scmtip_container").addClass("scmtips_window");
				$("#scmtip_container").css({
					position: pos,
					zIndex: 10000005,
					padding: 0,
					width:width,
					margin: 0
				});
				
				$.smate.scmtips._reposition();
				$.smate.scmtips._maintainPosition(true);
				
				switch( type ) {
					case 'warn':
						$("#scmtip_content").addClass("text_ts");
					break;
					case 'success'||'yes':
						$("#scmtip_content").addClass("text_yes");
					break;
					case 'error':
						$("#scmtip_content").addClass("text_wrong");
					break;
				}
				$("#scmtip_content").text(msg);
				$("#scmtip_content").html( $("#scmtip_content").text().replace(/\n/g, '<br />') );
				
				$("#scmtip_container").css({
					minWidth: $("#scmtip_container").outerWidth(),
					maxWidth: $("#scmtip_container").outerWidth()
				});
				var container = $("#scmtip_container");
				var width = container.width() + 2;
				var height = container.height() + 2;
				$("#scmtip_overlay").css({
					position: pos,
					zIndex: 10000004,
					minWidth: $("#scmtip_container").outerWidth(),
					maxWidth: $("#scmtip_container").outerWidth(),
					width: width + 'px',
					height: height + 'px'
				});
				//hide after 3s
				$.smate.scmtips.timeOutObj = setTimeout(function(){
					$.smate.scmtips._hide();
				},timeOut);
				//hide when click
				$("#scmtip_container").bind("click",function(){
					$.smate.scmtips._hide();
				});
			},
			_hide: function() {
				$("#scmtip_container").remove();
				$("#scmtip_overlay").remove();
				$.smate.scmtips._maintainPosition(false);
			},
			_reposition: function() {
				var top = (($(window).height() / 2) - ($("#scmtip_container").outerHeight() / 2)) + $.smate.scmtips.verticalOffset;
				var left = (($(window).width() / 2) - ($("#scmtip_container").outerWidth() / 2)) + $.smate.scmtips.horizontalOffset;
				if( top < 0 ) top = 0;
				if( left < 0 ) left = 0;
				// IE6 fix
//				if( $.browser.msie && parseInt($.browser.version) <= 6 ) top = top + $(window).scrollTop();
				$("#scmtip_container").css({
					top: top + 'px',
					left: left + 'px'
				});
				$("#scmtip_overlay").css({
					top: top + 'px',
					left: left + 'px'
				});
				
			},
			_maintainPosition: function(status) {
				if( $.smate.scmtips.repositionOnResize ) {
					switch(status) {
						case true:
							$(window).bind('resize', $.smate.scmtips._reposition);
							$(window).bind('scroll', $.smate.scmtips._reposition);
						break;
						case false:
							$(window).unbind('resize', $.smate.scmtips._reposition);
							$(window).unbind('scroll', $.smate.scmtips._reposition);
						break;
					}
				}
			}
	};
	// Shortuct functions
	scmWarn = function(message,width,timeOut) {
		$.smate.scmtips.warn(message,width,timeOut);
	};
	scmSuccess = function(message,width,timeOut) {
		$.smate.scmtips.success(message,width,timeOut);
	};
	scmError = function(message,width,timeOut) {
		$.smate.scmtips.error(message,width,timeOut);
	};
	
	//事件阻止冒泡
	$.Event.getEvent=function getEvent(){
		 if( window.event ){
		        return window.event;
		    }else{
		        var aFunction = function(dArguments, dLevel){
		            if ( !dArguments ) return null;
		            if ( !dLevel ) return null;
		            for ( var i = 0; i < dArguments.length; i++ ){
		                if ( dArguments[i] && dArguments[i].altKey !== undefined ) {
		                    return dArguments[i];
		                }
		            }
		            if ( dArguments && dArguments.callee && dArguments.callee.caller && dArguments.callee.caller.arguments ) {
		                return aFunction(dArguments.callee.caller.arguments, 5-1);
		            }
		        };
		        return aFunction(arguments, 5);
		    }
	};
	$.Event.stopEvent=function getEvent(){
		var e =$.Event.getEvent();
		if(e&&e.stopPropagation){//非IE
			   e.stopPropagation();
			  }else{//IE
			   window.event.cancelBubble=true;
			  }
	};
})(jQuery);