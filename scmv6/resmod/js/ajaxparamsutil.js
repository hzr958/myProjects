var ajaxparamsutil =ajaxparamsutil||{};
//获取请求的参数（成果查询使用）
ajaxparamsutil.getParams = function(obj){
	var params ="{";
	$(obj).find(".paramAttr").each(function(i1,n1){//获取参数名
		var paramName = $(n1).attr("paramName");
		params +=paramName+":\"";
		var paramValue = "";
		$(n1).find(".paramSelected").each(function(i2,n2){//获取参数值
			paramValue = $(n2).attr("paramValue")+","+paramValue;
		});
		params +=paramValue.substring(0,paramValue.length-1)+"\","
	});
	params= params.substring(0,params.length-1)+"}";
	return (new Function("return " + params))();
	//return eval('(' + params + ')');
	//return params;
};
//发送ajax请求
ajaxparamsutil.doAjax = function(url,dataJson,successCallBack,dataType,type,async,errorCallBack,needSession,obj){
	successCallBack = arguments[2] ? arguments[2] : function(){};
	dataType = arguments[3] ? arguments[3] :  "json";
	type  = arguments[4] ? arguments[4] :"post";
	async = arguments[5] ? arguments[5] : false;
	errorCallBack  = arguments[6] ? arguments[6] : function(){/*$.scmtips.show('error',ajaxparamsutili18.i18n_fail);*/};
	needSession = arguments[7]?arguments[7]:true;
	$.ajax({
		url: url,
		async:async,
		dataType:dataType,
		type: type,
		data: dataJson,
		success: function(data){
			var toConfirm=false;
			if("json" == dataType&&data!=null){
				toConfirm=data.ajaxSessionTimeOut=='yes'?true:toConfirm;
			}else{
				toConfirm=data=="{\"ajaxSessionTimeOut\":\"yes\"}"?true:toConfirm;
			}
			if (needSession&&toConfirm) {
				jConfirm(ajaxparamsutili18.i18n_timeout, ajaxparamsutili18.i18n_tipTitle, function(r) {
					if (r) {
						/*top.location.reload(true);*/
						document.location.href=window.location.href;
						return;
					}
				});
			}else{			
				successCallBack(data,dataJson,obj);
			}
		},
		error: function(data){
			if(data.readyState==4&&data.status==200){
				jConfirm(ajaxparamsutili18.i18n_timeout, ajaxparamsutili18.i18n_tipTitle, function(r) {
					if (r) {
						document.location.href=window.location.href;
						return;
					}
				});
			}
			errorCallBack();
		}
	});
};
