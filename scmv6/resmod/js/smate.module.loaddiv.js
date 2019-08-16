/**
*scm单独模块加载
*urlTypes:{
*"recentlyview":"您最近访问过的机构"
*"pubCooperation":"成果合作者",
*}
*@author zk
*/
(function($, window, undefined) {
	$.fn.loaddiv = function(options) {
		var defaults = {
				ctxpath:"",
				respath:"/resscmwebsns",
				url:"",
				parameters:{},
				type:"",
				loadimg:"/images_v5/loading.gif",
				urlTypes:{
					"recentlyview":"/inspg/module/ajaxrecentlyview",
					"managerinspg":"/inspg/module/ajaxmanagerinspg",
					"similarinspg":"/inspg/module/ajaxsimilarinspg",
					"similarnews":"/inspg/module/ajaxsimilarnews",
					"pubCooperation":"/module/ajaxPubCooperation"
				},
				extendFun:null
		};
		var opts = $.extend(defaults, options);
		return $(this).each(function(type) {	
			$(this).html("<img src='"+opts.respath+opts.loadimg+"' />");
			if (typeof opts.url!= "undefined" && $.trim(opts.url).length>0) {
				defaultLoadDiv($(this),opts);
	        }else{
	        	intiLoadDiv($(this),opts);
	        }				
		});
	};
	
	var defaultLoadDiv = function(obj, url, options) {
		$.ajax({
			url : options.ctxpath+options.url,
			type : 'post',
			data : options.parameters,
			success : function(data) {
				if(data.ajaxSessionTimeOut){
					jConfirm("系统超时或未登录，你要登录吗？", "提示", function(result) {
						if (result){
							location.href = "/scm?service=" + encodeURIComponent(location.href);
						}
					});
					return;
				}
				if(data == ""){//无数据不显示模块
					obj.remove();
				}else{
					obj.html(data);
				}
				
				if(options.extendFun!=null){
					options.extendFun();
				}
			},
			error : function(data) {obj.remove();}
		});
	};
	
	var intiLoadDiv = function(obj, options) {
		$.ajax({
			url : options.ctxpath+options.urlTypes[options.type],
			type : 'post',
			data : options.parameters,
			success : function(data) {
				if(data.ajaxSessionTimeOut){
					jConfirm("系统超时或未登录，你要登录吗？", "提示", function(result) {
						if (result){
							location.href = "/scm?service=" + encodeURIComponent(location.href);
						}
					});
					return;
				}
				if(data == ""){//无数据不显示模块
					obj.remove();
				}else{
					obj.html(data);
				}
				
				if(options.extendFun!=null){
					options.extendFun();
				}
			},
			error : function() {obj.remove();}
		});
	};
})(jQuery, window);