/**
 * @author zym
 * 遮罩插件
 */
(function($){
    if (typeof resmod == "undefined" || resmod == null) {
        alert("遮罩插件缺少资源路径");
        return;
    }
    var __prcWin = null;
    $.proceeding = {
        "show": function(content, elementId){
            __prcWin = new __progressWin(resmod);
            if (typeof elementId != "undefined") {
            	__prcWin.loadingElement(elementId, content);
            } else {
            	__prcWin.openWin(content);
            	}
            
        },
        "hide": function(){
				if(__prcWin!=null){
					 __prcWin.closeWin();
				}
        }
    };
    
    var __progressWin = function(resmod){
    	
    
        this.bgObj = null;
        this.msgObj = null;
        
        this.openWin = function(content){
            var bHeight = $(document).height();
			var cHeight = $(window).height();
			var _scrollTop = $(document).scrollTop();
            var bWidth = $(window).width();
            var div = $('<div style="position: absolute;left: 0px;top: 0px;width:' + bWidth + 'px;height:'+ bHeight + 'px;filter: Alpha(Opacity=30);opacity: 0.3;background-color: #000000;z-index: 109991"><iframe style="position:absolute;top:0;left:0;height:100%;width:100%;border:0;"></div>');
            //var iframe = $('<iframe style="border: 0;position: absolute;left: 0px;top: 0px;width: '+bWidth +' px;height: '+bHeight+'px;z-index: 9991;overflow: hidden"></iframe>');
            var msg_div = $('<div style="position:absolute;font:12px \'宋体\';top:' + (_scrollTop+cHeight / 2) + 'px;left:' + (bWidth / 2 - 20) +'px;text-align:center;z-index:109992; padding:10px 10px; border:1px solid #999; background-color:#fff;"></div>');
            var img_div = $('<div style="margin-right:10px;float:left;">' + '<img src="' + resmod + '/images/loading.gif" width="16px" height="16px" align="absmiddle" style="border:none;"/></div>');
            var wait_div = $('<div style="float:left; line-height:16px;"></div>');
			   var tip = typeof locale != "undefined" && 'zh_CN' == locale ? "加载中....." : "Loading.....";
            wait_div.html((typeof content == "undefined" || content == "") ? tip : content);
            
           // div.append(iframe);
            msg_div.append(img_div).append(wait_div);
            $("body").append(div).append(msg_div);
            this.bgObj = div;
            this.msgObj = msg_div;
        };
        
        
      this.loadingElement = function(elementId, content) {
    	   this.bgObj = null;
         this.msgObj = null;
         
    	   var element = $("#"+elementId) || $(elementId);
    		var left = $(element).offset().left;
    		var top = $(element).offset().top;
    		var width = $(element).outerWidth(); // 包括padding和border的宽度
    		var height = $(element).outerHeight();
    		var bgBox = $("<div style='z-index:900; position:absolute; filter:Alpha(Opacity=30); opacity: 0.3; background-color:#000000;'></div>");
    		var msgBox = $("<div style='background-color:#ffffff; font:12px \"宋体\"; z-index:901; position:absolute; border:medium none; padding:10px; border:1px solid #999;'></div>");
    		var imgBox = $('<div style="float:left;"><img src="' + resmod + '/images_v5/plugin/proceeding/scm_loading.gif" width="16px" height="16px" align="absmiddle" style="border:none;"/></div>');
    		var conBox = $('<div style="float:left; margin-left:10px; line-height:16px;"></div>');
    		var tip = typeof locale != "undefined" && 'zh_CN' == locale ? "加载中....." : "Loading.....";
    		conBox.html(typeof content == "undefined" || content == "" ? tip : content);
    		$(document.body).append(bgBox);
    		$(document.body).append(msgBox.append(imgBox).append(conBox).append('<div style="clear:both;"></div>'));
    		
    		var msgBoxWidth = imgBox.outerWidth(true) + conBox.outerWidth(true);
    		var msgBoxHeight = imgBox.outerHeight(true);
    		msgBox.css({"width" : msgBoxWidth, "height" : msgBoxHeight, "left" : left + (width - msgBoxWidth) / 2, "top" : top + (height - msgBoxHeight) / 2 });
    		bgBox.css({"width" : width, "height" : height, "left" : left, "top" : top});
    		
    		this.bgObj = bgBox;
    		this.msgObj = msgBox;
        }
        
        this.closeWin = function(){
            if (this.bgObj && this.msgObj) {
                this.bgObj.remove();
                this.msgObj.remove();
                this.bgObj = null;
                this.msgObj = null;
            }
        };
    }
})(jQuery);


