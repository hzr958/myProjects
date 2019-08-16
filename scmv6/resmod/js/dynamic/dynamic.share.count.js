var resShareUtil;

ResShareUtil = function() {
	this.ajaxBatchLoadShareCount = ResShareUtil.ajaxBatchLoadShareCount;
	this.collectResData = ResShareUtil.collectResData;
	this.showPsnShareDetail = ResShareUtil.showPsnShareDetail;
	this.ajaxLoadShareCount = ResShareUtil.ajaxLoadShareCount;
};

/**
 * 批量获取资源分享次数(resType:资源类别；zeroflag:0是否显示：1：显示；0：不显示).
 */
ResShareUtil.ajaxBatchLoadShareCount = function(resType, zeroflag) {
	var outter = this;
	var resJson = outter.collectResData(resType);
	if (resJson == "[]") {
		return false;
	}
	var jsonParam = "{\"resType\":" + resType + ",\"resDetails\":" + resJson
			+ "}";
	$.ajax( {
		type : "post",
		url : snsctx + "/dynamic/ajaxBatchLoadShareCount",
		data : {
			"jsonParam" : jsonParam
		},
		dataType : "json",
		success : function(data) {
			if (data.result == "success") {
				var rtnJson =  data.rtnJson ;
				for ( var i = 0; i < rtnJson.length; i++) {
					var jsonObj = rtnJson[i];
					var shareLinkObj=$("#shareCountLink_" + resType + "_" + jsonObj.resId);
					var shareLabelObj=$("#shareCountLabel_" + resType + "_" + jsonObj.resId);
					var shareSpanObj=$("#shareCountSpan_" + resType + "_" + jsonObj.resId);
					var isCount= $(shareLabelObj).attr("count");
				   if(typeof ( isCount)=='undefined'){
						$(shareLinkObj).attr("shareId", jsonObj.shareId);
						$(shareLabelObj).text(jsonObj.shareCount);
						if (jsonObj.shareCount > 0) {
							$(shareLabelObj).parent().show();
							$(shareSpanObj).show();
						}
					}else{
						$(shareLabelObj).attr("count",jsonObj.shareCount);
						if (jsonObj.shareCount > 0) {
							$(shareLabelObj).text("("+jsonObj.shareCount+")");
						}else{
							$(shareLabelObj).text("(0)");
						}
					}
				}
				if (zeroflag == 1) {
					$("a.shareCountLink_" + resType).each(function() {
						$(this).show();
					});
				}
			}
		},
		error : function(e) {
		}
	});
};


ResShareUtil.collectResData = function(resType) {
	var json = [];
	$("a.shareCountLink_" + resType).each(function() {
		json.push( {
			"resId" : $(this).attr("resId"),
			"resNode" : $(this).attr("nodeId")
		});
	});
	return JSON.stringify(json);
};

ResShareUtil.showPsnShareDetail = function(obj) {
	var resType = $(obj).attr("resType");
	var resId = $(obj).attr("resId");
	var resNode = $(obj).attr("nodeId");
	var count = $.trim($(obj).find("#shareCountLabel_" + resType + "_" + resId)
			.text());
	if (count == "0") {
		return false;
	}

	$("#hidden-shareLink").attr(
			"alt",
			ctxpath + "/dynamic/ajaxGetPsnShareDetail?resType=" + resType
					+ "&resNode=" + resNode + "&resId=" + resId
					+ "&TB_iframe=true&height=380&width=672");
	$("#hidden-shareLink").attr("title", countShare.title);
	$("#hidden-shareLink").click();
};
