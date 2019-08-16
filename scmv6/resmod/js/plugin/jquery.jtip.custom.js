/*
 * JTip
 * By Cody Lindley (http://www.codylindley.com)
 * Under an Attribution, Share Alike License
 * JTip is built on top of the very light weight jquery library.
 */

//on page load (as soon as its ready) call JT_init
$(document).ready(JT_init);

function JT_init(){
	       $("a.jTip")
		   .mouseover(
				function(){
					 JT_show(this.href,this.id,this.name);
				}
			)
		   .mouseout(
				function(){
					 JT_hide(this.href,this.id);
				}
			)
           .click(function(){return false});	
}
function JT_obj_init(obj){
    $(obj)
	   .mouseover(
			function(){
				 JT_show(this.href,this.id,this.name);
			}
		)
	   .mouseout(
			function(){
				 JT_hide(this.href,this.id);
			}
		)
    .click(function(){return false});	
}
function JT_show(url,linkId,title){
	
	JT_hide(url,linkId);
	
	if(title == false)title="";
	var de = document.documentElement;
	var w = self.innerWidth || (de&&de.clientWidth) || document.body.clientWidth;
	var hasArea = w - getAbsoluteLeft(linkId);
	var clickElementy = getAbsoluteTop(linkId) - 15; //set y position
	
	var queryString = url.replace(/^[^\?]+\??/,'');
	var params = parseQuery( queryString );
	if(params['width'] === undefined){params['width'] = 250};
	if(params['link'] !== undefined){
	$('#' + linkId).bind('click',function(){window.location = params['link']});
	$('#' + linkId).css('cursor','pointer');
	}
	//$("body").append("<div id='JT_bg' style='width:"+params['width']*1+"px'></div>");
	if(hasArea>((params['width']*1)+75)){
		$("body").append("<div id='JT' style='width:"+params['width']*1+"px'><div id='JT_arrow_left'></div><div id='JT_close_left'>"+title+"</div><div id='JT_copy'></div></div>");//right side
		var arrowOffset = getElementWidth(linkId) + 11;
		var clickElementx = getAbsoluteLeft(linkId) + arrowOffset; //set x position
		//var JT_bgLeft = clickElementx+3;
	}else{
		//alert($("#JT_bg"));
		$("body").append("<div id='JT' style='width:"+params['width']*1+"px'><div id='JT_arrow_right' style='left:"+((params['width']*1))+"px'></div><div id='JT_close_right'>"+title+"</div><div id='JT_copy'></div></div>");//left side
		var clickElementx = getAbsoluteLeft(linkId) - ((params['width']*1) + 15); //set x position
		//var JT_bgLeft = clickElementx-3;		
	}
	$('#JT').css({left: clickElementx+"px", top: clickElementy+"px"});	
	//$('#JT_bg').show();
	//$('#JT_bg').css({left: JT_bgLeft+"px", top: clickElementy+3+"px"});
	if(url.indexOf('JT_inline') != -1){
		$("#JT_copy").append($('#' + params['inlineId']).children());
	}else if(url.indexOf('JT_iframe') != -1){
		$.ajax({url: url, 
	        type: 'post',
			dataType:'html',
			success:function(data){
				$('#JT_copy').html(data);
			}
		});
	}
	$('#JT').show();
	$('#JT').resize();
	//$('#JT_bg').css("height",$('#JT').height());
	$('#JT').click(function(){
		JT_hide(url,linkId);
	});
	
}
function JT_hide(url,linkId){
	var queryString = url.replace(/^[^\?]+\??/,'');
	var params = parseQuery( queryString );
	
	if(url.indexOf('JT_inline') != -1){
		$('#' + params['inlineId']).append($("#JT_copy").children() );
	}
	$('#JT').remove();
	//$('#JT_bg').remove();
}

function getElementWidth(objectId) {
	x = document.getElementById(objectId);
	return x.offsetWidth;
}

function getAbsoluteLeft(objectId) {
	return $("#"+objectId).offset().left;
	// Get an object left position from the upper left viewport corner
//	var o = document.getElementById(objectId)
//	oLeft = o.offsetLeft            // Get left position from the parent object
//	while(o.offsetParent!=null) {   // Parse the parent hierarchy up to the document element
//		oParent = o.offsetParent    // Get parent object reference
//		oLeft += oParent.offsetLeft // Add parent left position
//		o = oParent
//	}
//	return oLeft
}

function getAbsoluteTop(objectId) {
	// Get an object top position from the upper left viewport corner
	return $("#"+objectId).offset().top;
//	var o = document.getElementById(objectId)
//	var oTop = o.offsetTop            // Get top position from the parent object
//	while(o.offsetParent!=null) { // Parse the parent hierarchy up to the document element
//		var oParent = o.offsetParent  // Get parent object reference
//		oTop += oParent.offsetTop // Add parent top position
//		o = oParent
//	}
//	return oTop
}

function parseQuery ( query ) {
   var Params = new Object ();
   if ( ! query ) return Params; // return empty object
   var Pairs = query.split(/[;&]/);
   for ( var i = 0; i < Pairs.length; i++ ) {
      var KeyVal = Pairs[i].split('=');
      if ( ! KeyVal || KeyVal.length != 2 ) continue;
      var key = unescape( KeyVal[0] );
      var val = unescape( KeyVal[1] );
      val = val.replace(/\+/g, ' ');
      Params[key] = val;
   }
   return Params;
}

function blockEvents(evt) {
              if(evt.target){
              evt.preventDefault();
              }else{
              evt.returnValue = false;
              }
}