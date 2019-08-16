// jQuery Alert Dialogs Plugin
//
// Version 1.1
//
// Cory S.N. LaViska
// A Beautiful Site (http://abeautifulsite.net/)
// 14 May 2009
//
// Visit http://abeautifulsite.net/notebook/87 for more information
//
// Usage:
//		jAlert( message, [title, callback] )
//		jConfirm( message, [title, callback] )
//		jPrompt( message, [value, title, callback] )
// 
// History:
//
//		1.00 - Released (29 December 2008)
//
//		1.01 - Fixed bug where unbinding would destroy all resize events
//
// License:
// 
// This plugin is dual-licensed under the GNU General Public License and the MIT License and
// is copyright 2008 A Beautiful Site, LLC. 
// 
(function($) {
	
	$.alerts = {
		
		// These properties can be read/written by accessing $.alerts.propertyName from your scripts at any time
		
		verticalOffset: -75,                // vertical offset of the dialog from center screen, in pixels
		horizontalOffset: 0,                // horizontal offset of the dialog from center screen, in pixels/
		repositionOnResize: true,           // re-centers the dialog on window resize
		overlayOpacity: .01,                // transparency level of overlay
		overlayColor: '#FFF',               // base color of overlay
		draggable: true,                    // make the dialogs draggable (requires UI Draggables plugin)
		okButton: strBtnText.strOkButton,         // text for the OK button
		confirmButton : strBtnText.strConfirmButton,
		cancelButton: strBtnText.strCancelButton,// text for the Cancel button
		yesButton:strBtnText.strYesButton,
		noButton:strBtnText.strNoButton,
		closeButton:strBtnText.strCloseButton,
		remindAgain:TipText.remindAgain,
		dialogClass: null,                  // if specified, this class will be applied to all dialogs
		
		// Public methods
		
		alert: function(message, title, callback) {
			if( title == null ) title = 'Alert';
			$.alerts._show(title, message, null, 'alert', function(result) {
				if( callback ) callback(result);
			});
		},
		
		confirm: function(message, title, callback,btnStyle,width) {
			if( title == null ) title = 'Confirm';
			$.alerts._show(title, message, null, 'confirm', function(result) {
				if( callback ) callback(result);
			},btnStyle,width);
		},
		
		confirmWithCk: function(message, title, callback,btnStyle) {
			if( title == null ) title = 'Confirm';
			$.alerts._show(title, message, null, 'confirmCk', function(result, isCheck) {
				if( callback ) callback(result, isCheck);
			},btnStyle);
		},
			
		prompt: function(message, value, title, callback) {
			if( title == null ) title = 'Prompt';
			$.alerts._show(title, message, value, 'prompt', function(result) {
				if( callback ) callback(result);
			});
		},
		
		// Private methods
		
		_show: function(title, msg, value, type, callback,btnStyle,width) {
			
			$.alerts._hide();
			$.alerts._overlay('show');
			var jsrespath="/resscmwebsns";
			if(typeof respath!="undefined"){
				jsrespath=respath;
			}
			var defwdit="460";
			if(typeof width!="undefined"){
				defwdit=width;
			}
			$("BODY").append(
					  '<div id="popup_container" style="width: '+defwdit+'px;" >' +
					    '<div class="p_del_title"><span id="popup_title"></span><a id="popup_close" href="javascript:void(0)"><img title="'+$.alerts.closeButton+'" alt="'+$.alerts.closeButton+'" src="'+jsrespath+'/images_v5/mbClose.gif" width="15" height="15"></a></div>' +
					    '<div class="p_del_midd">' +
					      '<div id="popup_message" class="pop_point"></div>' +
					      '<div id="popup_checkbox" style="text-align:right;"></div>' +
						'</div>' +
						'<div id="p_d_bottom" class="p_d_bottom"></div>'+
					  '</div>');
			
			if( $.alerts.dialogClass ) $("#popup_container").addClass($.alerts.dialogClass);
			
			// IE6 Fix
			var pos = ($.browser.msie && parseInt($.browser.version) <= 6 ) ? 'absolute' : 'fixed'; 
			
			$("#popup_container").css({
				position: pos,
				zIndex: 10000005,
				padding: 0,
				margin: 0
			});
			
			
			$("#popup_title").text(title);
			//$("#popup_content").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html( $("#popup_message").text().replace(/\n/g, '<br />') );
			
			$("#popup_container").css({
				minWidth: $("#popup_container").outerWidth(),
				maxWidth: $("#popup_container").outerWidth()
			});
			
			$.alerts._reposition();
			$.alerts._maintainPosition(true);
			
			switch( type ) {
				case 'alert':
					$("#p_d_bottom").html('<a class="uiButton text14" id="popup_ok" title="'+$.alerts.okButton+'" href="javascript:void(0)">'+$.alerts.okButton+'</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
					$("#popup_ok,#popup_close").click( function() {
						$.alerts._hide();
						callback(true);
					});
					setTimeout(function(){
						$("#popup_ok").focus().keypress( function(e) {
							if( e.keyCode == 13 || e.keyCode == 27 ) $("#popup_ok").trigger('click');
						});
					},1000);
					
				break;
				case 'confirm':
					$("#p_d_bottom").html('<a class="uiButton text14" id="popup_ok" title="'+$.alerts.confirmButton+'" href="javascript:void(0)">'+$.alerts.confirmButton+'</a> <a id="popup_cancel" class="uiButton text14 mright10" title="'+$.alerts.cancelButton+'" href="javascript:void(0)">'+$.alerts.cancelButton+'</a>');
					$("#popup_ok").click( function() {
						$.alerts._hide();
						if( callback ) callback(true);
					});
					$("#popup_cancel,#popup_close").click( function() {
						$.alerts._hide();
						if( callback ) callback(false);
					});
					$("#popup_ok").focus();
					$("#popup_ok, #popup_cancel,#popup_close").keypress( function(e) {
						if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
						if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
					});
				break;
				case 'confirmCk':
					$("#popup_checkbox").html('<span><input type="checkbox" style="vertical-align:middle"/><label style="vertical-align:middle"> ' + $.alerts.remindAgain + '</label></span>');
					$("#p_d_bottom").html('<a class="uiButton text14" id="popup_ok" title="'+$.alerts.confirmButton+'" href="javascript:void(0)">'+$.alerts.confirmButton+'</a> <a id="popup_cancel" class="uiButton text14 mright10" title="'+$.alerts.cancelButton+'" href="javascript:void(0)">'+$.alerts.cancelButton+'</a>');
					$("#popup_ok").click( function() {
						var isCheck = $('#popup_checkbox').find('input:checked').length >= 1 ? true : false;
						$.alerts._hide();
						if( callback ) callback(true, isCheck);
					});
					$("#popup_cancel,#popup_close").click( function() {
						$.alerts._hide();
						if( callback ) callback(false, false);
					});
					$("#popup_ok").focus();
					$("#popup_ok, #popup_cancel,#popup_close").keypress( function(e) {
						if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
						if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
					});
					break;
				case 'prompt':
					$("#popup_message").append('<br /><input type="text" size="30" id="popup_prompt" />');
					$("#p_d_bottom").html('<a class="uiButton text14" id="popup_ok" title="'+$.alerts.okButton+'" href="javascript:void(0)">'+$.alerts.okButton+'</a> <a id="popup_cancel" class="uiButton text14 mright10" title="'+$.alerts.cancelButton+'" href="javascript:void(0)">'+$.alerts.cancelButton+'</a>');
					$("#popup_prompt").width( $("#popup_message").width() );
					$("#popup_ok").click( function() {
						var val = $("#popup_prompt").val();
						$.alerts._hide();
						if( callback ) callback( val );
					});
					$("#popup_cancel,#popup_close").click( function() {
						$.alerts._hide();
						if( callback ) callback( null );
					});
					$("#popup_prompt, #popup_ok, #popup_cancel,#popup_close").keypress( function(e) {
						if( e.keyCode == 13 ) $("#popup_ok").trigger('click');
						if( e.keyCode == 27 ) $("#popup_cancel").trigger('click');
					});
					if( value ) $("#popup_prompt").val(value);
					$("#popup_prompt").focus().select();
				break;
			}
			
			// Make draggable
			if( $.alerts.draggable ) {
				try {
					$("#popup_container").draggable({ handle: $("#popup_title") });
					$("#popup_title").css({ cursor: 'move' });
				} catch(e) { /* requires jQuery UI draggables */ }
			}
		},
		
		_hide: function() {
			$("#popup_container").remove();
			$.alerts._overlay('hide');
			$.alerts._maintainPosition(false);
		},
		
		_overlay: function(status) {
			switch( status ) {
				case 'show':
					$.alerts._overlay('hide');
					if($.browser.msie&&($.browser.version == "6.0")&&!$.support.style){
						$("BODY").append('<div id="popup_overlay"><iframe id="popup_overlay_frame" frameborder="0" hspace="0" src="" style="width:100%;height:100%;"/></div>');
					}else{
						$("BODY").append('<div id="popup_overlay"></div>');
					}
					
					$("#popup_overlay").css({
						position: 'absolute',
						zIndex: 10000004,
						top: '0px',
						left: '0px',
						width: '100%',
						height: $(document).height(),
						background: $.alerts.overlayColor,
						opacity: $.alerts.overlayOpacity
					});
				break;
				case 'hide':
					$("#popup_overlay").remove();
				break;
			}
		},
		
		_reposition: function() {
			var top = (($(window).height() / 2) - ($("#popup_container").outerHeight() / 2)) + $.alerts.verticalOffset;
			var left = (($(window).width() / 2) - ($("#popup_container").outerWidth() / 2)) + $.alerts.horizontalOffset;
			if( top < 0 ) top = 0;
			if( left < 0 ) left = 0;
			
			// IE6 fix
			if( $.browser.msie && parseInt($.browser.version) <= 6 ) top = top + $(window).scrollTop();
			
			$("#popup_container").css({
				top: top + 'px',
				left: left + 'px'
			});
			
			$("#popup_overlay").height( $(document).height() );
		},
		
		_maintainPosition: function(status) {
			if( $.alerts.repositionOnResize ) {
				switch(status) {
					case true:
						$(window).bind('resize', $.alerts._reposition);
					break;
					case false:
						$(window).unbind('resize', $.alerts._reposition);
					break;
				}
			}
		}
		
	}
	
	// Shortuct functions
	jAlert = function(message, title, callback) {
		$.alerts.alert(message, title, callback);
	}
	
	jConfirm = function(message, title, callback,btnStyle,width) {
		$.alerts.confirm(message, title, callback,btnStyle,width);
	};
	
	jConfirmWithCk = function(message, title, callback,btnStyle) {
		$.alerts.confirmWithCk(message, title, callback,btnStyle);
	};
		
	jPrompt = function(message, value, title, callback) {
		$.alerts.prompt(message, value, title, callback);
	};
	
})(jQuery);